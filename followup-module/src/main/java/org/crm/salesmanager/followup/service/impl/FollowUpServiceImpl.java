package org.crm.salesmanager.followup.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.followup.domain.FollowUp;
import org.crm.salesmanager.followup.dto.FollowUpRequestDTO;
import org.crm.salesmanager.followup.dto.FollowUpResponseDTO;
import org.crm.salesmanager.followup.enums.FollowUpStatus;
import org.crm.salesmanager.followup.mapper.FollowUpMapper;
import org.crm.salesmanager.followup.repository.FollowUpRepository;
import org.crm.salesmanager.followup.service.FollowUpService;
import org.crm.salesmanager.lead.service.LeadService;
import org.crm.salesmanager.timeline.enums.TimelineAction;
import org.crm.salesmanager.timeline.service.TimelineService;
import org.crm.salesmanager.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowUpServiceImpl implements FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final FollowUpMapper followUpMapper;
    private final LeadService leadService;
    private final UserService userService;
    private final TimelineService timelineService;

    @Override
    public FollowUpResponseDTO createFollowUp(FollowUpRequestDTO dto, Long userId) {
        log.info("Creating follow-up for lead id: {} and user id: {}", dto.getLeadId(), userId);

        validateCreation(dto, userId);

        FollowUp followUp = followUpMapper.toEntity(dto);
        followUp.setAssignedTo(userId);
        followUp.setStatus(FollowUpStatus.PENDING);
        followUp.setCreatedAt(LocalDateTime.now());

        FollowUp savedFollowUp = followUpRepository.save(followUp);
        timelineService.recordEvent(savedFollowUp.getLeadId(), userId, TimelineAction.FOLLOW_UP_CREATED, "Follow-up created");
        return followUpMapper.toDTO(savedFollowUp);
    }

    @Override
    public FollowUpResponseDTO markCompleted(Long id, Long userId) {
        log.info("Marking follow-up as completed for id: {} and user id: {}", id, userId);

        validateUser(userId);

        FollowUp followUp = followUpRepository.findByIdAndAssignedTo(id, userId)
                .orElseThrow(() -> new RuntimeException("Follow-up not found with id: " + id));

        if (followUp.getStatus() == FollowUpStatus.COMPLETED) {
            return followUpMapper.toDTO(followUp);
        }

        followUp.setStatus(FollowUpStatus.COMPLETED);
        FollowUp savedFollowUp = followUpRepository.save(followUp);
        timelineService.recordEvent(savedFollowUp.getLeadId(), userId, TimelineAction.FOLLOW_UP_COMPLETED, "Follow-up completed");
        return followUpMapper.toDTO(savedFollowUp);
    }

    @Override
    public List<FollowUpResponseDTO> getTodayFollowUps(Long userId) {
        log.info("Fetching today's follow-ups for user id: {}", userId);

        validateUser(userId);

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return followUpRepository.findByAssignedToAndFollowUpTimeBetweenAndStatusOrderByFollowUpTimeAsc(
                        userId,
                        startOfDay,
                        endOfDay,
                        FollowUpStatus.PENDING
                )
                .stream()
                .map(followUpMapper::toDTO)
                .toList();
    }

    @Override
    public List<FollowUpResponseDTO> getMissedFollowUps(Long userId) {
        log.info("Fetching missed follow-ups for user id: {}", userId);

        validateUser(userId);
        markOverdueFollowUpsAsMissed();

        return followUpRepository.findByAssignedToAndStatusOrderByFollowUpTimeAsc(userId, FollowUpStatus.MISSED)
                .stream()
                .map(followUpMapper::toDTO)
                .toList();
    }

    @Override
    public void markOverdueFollowUpsAsMissed() {
        LocalDateTime now = LocalDateTime.now();
        List<FollowUp> overdueFollowUps = followUpRepository.findByFollowUpTimeBeforeAndStatus(now, FollowUpStatus.PENDING);

        if (overdueFollowUps.isEmpty()) {
            return;
        }

        overdueFollowUps.forEach(followUp -> {
            followUp.setStatus(FollowUpStatus.MISSED);
            timelineService.recordEvent(followUp.getLeadId(), followUp.getAssignedTo(), TimelineAction.FOLLOW_UP_MISSED, "Follow-up missed");
        });
        followUpRepository.saveAll(overdueFollowUps);

        log.info("Marked {} overdue follow-ups as missed", overdueFollowUps.size());
    }

    @Override
    public void suggestNextFollowUp(Long leadId, Long userId, String reason) {
        if (leadId == null || userId == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean hasFutureFollowUp = followUpRepository.existsByLeadIdAndStatusAndFollowUpTimeAfter(
                leadId,
                FollowUpStatus.PENDING,
                now
        );

        if (hasFutureFollowUp) {
            return;
        }

        FollowUp followUp = FollowUp.builder()
                .leadId(leadId)
                .assignedTo(userId)
                .followUpTime(now.plusHours(48))
                .status(FollowUpStatus.PENDING)
                .createdAt(now)
                .build();

        FollowUp savedFollowUp = followUpRepository.save(followUp);
        timelineService.recordEvent(
                leadId,
                userId,
                TimelineAction.FOLLOW_UP_CREATED,
                "Suggested next follow-up in 48 hours after " + reason
        );
        log.info("Suggested follow-up id: {} for lead id: {}", savedFollowUp.getId(), leadId);
    }

    private void validateCreation(FollowUpRequestDTO dto, Long userId) {
        if (dto.getLeadId() == null) {
            throw new RuntimeException("Lead id is required");
        }

        if (dto.getFollowUpTime() == null) {
            throw new RuntimeException("Follow-up time is required");
        }

        validateUser(userId);
        leadService.getLead(dto.getLeadId());
    }

    private void validateUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User id is required");
        }

        userService.getUserForAuthById(userId);
    }
}
