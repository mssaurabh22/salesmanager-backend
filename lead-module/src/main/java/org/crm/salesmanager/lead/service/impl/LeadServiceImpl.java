package org.crm.salesmanager.lead.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.common.event.LeadEngagementEvent;
import org.crm.salesmanager.lead.domain.Lead;
import org.crm.salesmanager.lead.dto.LeadFilterDTO;
import org.crm.salesmanager.lead.dto.LeadRequestDTO;
import org.crm.salesmanager.lead.dto.LeadResponseDTO;
import org.crm.salesmanager.lead.dto.ReassignLeadDTO;
import org.crm.salesmanager.lead.dto.UpdateStageDTO;
import org.crm.salesmanager.lead.enums.LeadStage;
import org.crm.salesmanager.lead.mapper.LeadMapper;
import org.crm.salesmanager.lead.repository.LeadRepository;
import org.crm.salesmanager.lead.service.LeadService;
import org.crm.salesmanager.lead.specification.LeadSpecification;
import org.crm.salesmanager.lead.strategy.WeightageStrategy;
import org.crm.salesmanager.lead.validator.LeadStageValidator;
import org.crm.salesmanager.timeline.enums.TimelineAction;
import org.crm.salesmanager.timeline.service.TimelineService;
import org.crm.salesmanager.user.dto.UserAuthDTO;
import org.crm.salesmanager.user.entity.Role;
import org.crm.salesmanager.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadServiceImpl implements LeadService {

    private static final int DEFAULT_WEIGHTAGE = 10;

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;
    private final UserService userService;
    private final WeightageStrategy weightageStrategy;
    private final LeadStageValidator leadStageValidator;
    private final TimelineService timelineService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public LeadResponseDTO createLead(LeadRequestDTO dto, Long userId) {
        log.info("Creating lead for user id: {}", userId);

        userService.getUserForAuthById(userId);
        validateDuplicateLead(dto);

        Lead lead = leadMapper.toEntity(dto);
        lead.setStage(LeadStage.SUSPECT);
        lead.setWeightage(DEFAULT_WEIGHTAGE);
        lead.setTemperature(weightageStrategy.resolveTemperature(DEFAULT_WEIGHTAGE));
        lead.setAssignedTo(userId);
        lead.setCreatedBy(userId);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());

        validateLeadState(lead);
        Lead savedLead = leadRepository.save(lead);
        timelineService.recordEvent(savedLead.getId(), userId, TimelineAction.LEAD_CREATED, "Lead created");
        return leadMapper.toDTO(savedLead);
    }

    @Override
    public LeadResponseDTO updateLead(Long id, LeadRequestDTO dto) {
        log.info("Updating lead with id: {}", id);

        Lead lead = getLeadEntity(id);
        leadMapper.updateLeadFromDto(dto, lead);
        lead.setUpdatedAt(LocalDateTime.now());
        lead.setTemperature(weightageStrategy.resolveTemperature(lead.getWeightage()));

        validateLeadState(lead);
        Lead savedLead = leadRepository.save(lead);
        timelineService.recordEvent(savedLead.getId(), savedLead.getAssignedTo(), TimelineAction.LEAD_UPDATED, "Lead details updated");
        return leadMapper.toDTO(savedLead);
    }

    @Override
    public LeadResponseDTO updateStage(Long id, UpdateStageDTO dto) {
        log.info("Updating stage for lead id: {}", id);

        Lead lead = getLeadEntity(id);
        LeadStage oldStage = lead.getStage();
        leadStageValidator.validateTransition(lead.getStage(), dto.getStage());
        lead.setStage(dto.getStage());
        lead.setUpdatedAt(LocalDateTime.now());

        validateLeadState(lead);
        Lead savedLead = leadRepository.save(lead);
        timelineService.recordEvent(
                savedLead.getId(),
                savedLead.getAssignedTo(),
                TimelineAction.STAGE_CHANGED,
                "Stage changed from " + oldStage + " to " + savedLead.getStage()
        );
        eventPublisher.publishEvent(new LeadEngagementEvent(savedLead.getId(), savedLead.getAssignedTo(), "Stage changed to " + savedLead.getStage()));
        return leadMapper.toDTO(savedLead);
    }

    @Override
    public List<LeadResponseDTO> reassignLeads(ReassignLeadDTO dto, Long adminUserId) {
        log.info("Reassigning leads {} to user id: {}", dto.getLeadIds(), dto.getAssignedTo());

        UserAuthDTO admin = userService.getUserForAuthById(adminUserId);
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admin users can reassign leads");
        }

        userService.getUserForAuthById(dto.getAssignedTo());

        return dto.getLeadIds()
                .stream()
                .map(leadId -> reassignLead(leadId, dto.getAssignedTo(), adminUserId))
                .toList();
    }

    @Override
    public LeadResponseDTO recordActivityImpact(Long leadId, Long userId) {
        Lead lead = getLeadEntity(leadId);
        lead.setWeightage(weightageStrategy.applyActivityImpact(lead.getWeightage()));
        lead.setTemperature(weightageStrategy.resolveTemperature(lead.getWeightage()));
        lead.setUpdatedAt(LocalDateTime.now());

        Lead savedLead = leadRepository.save(lead);
        timelineService.recordEvent(
                savedLead.getId(),
                userId,
                TimelineAction.LEAD_UPDATED,
                "Lead weightage recalculated after activity"
        );
        return leadMapper.toDTO(savedLead);
    }

    @Override
    public LeadResponseDTO getLead(Long id) {
        log.info("Fetching lead with id: {}", id);
        return leadMapper.toDTO(getLeadEntity(id));
    }

    @Override
    public Page<LeadResponseDTO> getLeads(LeadFilterDTO filter) {
        log.info("Searching leads with supplied filters");

        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(resolveDirection(filter.getSortDirection()), resolveSortBy(filter.getSortBy()))
        );

        Specification<Lead> specification = Specification.where(LeadSpecification.hasTemperature(filter.getTemperature()))
                .and(LeadSpecification.hasStage(filter.getStage()))
                .and(LeadSpecification.assignedTo(filter.getAssignedTo()))
                .and(LeadSpecification.valueBetween(filter.getMinValue(), filter.getMaxValue()))
                .and(LeadSpecification.search(filter.getSearch()));

        return leadRepository.findAll(specification, pageable)
                .map(leadMapper::toDTO);
    }

    private Lead getLeadEntity(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));
    }

    private LeadResponseDTO reassignLead(Long leadId, Long assignedTo, Long adminUserId) {
        Lead lead = getLeadEntity(leadId);
        Long previousAssignee = lead.getAssignedTo();
        lead.setAssignedTo(assignedTo);
        lead.setUpdatedAt(LocalDateTime.now());

        Lead savedLead = leadRepository.save(lead);
        timelineService.recordEvent(
                savedLead.getId(),
                adminUserId,
                TimelineAction.LEAD_REASSIGNED,
                "Lead reassigned from user " + previousAssignee + " to user " + assignedTo
        );
        return leadMapper.toDTO(savedLead);
    }

    private void validateDuplicateLead(LeadRequestDTO dto) {
        if (dto.getContactNumber() != null && leadRepository.existsByContactNumber(dto.getContactNumber())) {
            throw new RuntimeException("Duplicate lead found with contact number: " + dto.getContactNumber());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank() && leadRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Duplicate lead found with email: " + dto.getEmail());
        }
    }

    private void validateLeadState(Lead lead) {
        if (lead.getStage() == null) {
            throw new RuntimeException("Lead stage is required");
        }

        if (lead.getAssignedTo() == null) {
            throw new RuntimeException("Lead must have an assigned user");
        }

        userService.getUserForAuthById(lead.getAssignedTo());
    }

    private Sort.Direction resolveDirection(String sortDirection) {
        return "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    private String resolveSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "createdAt";
        }

        return switch (sortBy) {
            case "customerName", "businessName", "expectedValue", "createdAt", "updatedAt", "stage", "temperature" -> sortBy;
            default -> "createdAt";
        };
    }
}
