package org.crm.salesmanager.activity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.common.event.LeadEngagementEvent;
import org.crm.salesmanager.activity.domain.Activity;
import org.crm.salesmanager.activity.dto.ActivityRequestDTO;
import org.crm.salesmanager.activity.dto.ActivityResponseDTO;
import org.crm.salesmanager.activity.mapper.ActivityMapper;
import org.crm.salesmanager.activity.repository.ActivityRepository;
import org.crm.salesmanager.activity.service.ActivityService;
import org.crm.salesmanager.activity.validator.ActivityValidator;
import org.crm.salesmanager.lead.service.LeadService;
import org.crm.salesmanager.timeline.enums.TimelineAction;
import org.crm.salesmanager.timeline.service.TimelineService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final ActivityValidator activityValidator;
    private final LeadService leadService;
    private final TimelineService timelineService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ActivityResponseDTO createActivity(ActivityRequestDTO dto, Long employeeId) {
        log.info("Creating activity for lead id: {} by employee id: {}", dto.getLeadId(), employeeId);

        activityValidator.validateCreateRequest(dto, employeeId);

        Activity activity = activityMapper.toEntity(dto);
        activity.setEmployeeId(employeeId);
        activity.setCreatedAt(LocalDateTime.now());

        Activity savedActivity = activityRepository.save(activity);
        leadService.recordActivityImpact(savedActivity.getLeadId(), employeeId);
        timelineService.recordEvent(
                savedActivity.getLeadId(),
                employeeId,
                TimelineAction.ACTIVITY_CREATED,
                "Activity created: " + savedActivity.getType()
        );
        eventPublisher.publishEvent(new LeadEngagementEvent(savedActivity.getLeadId(), employeeId, "Activity created: " + savedActivity.getType()));
        return activityMapper.toDTO(savedActivity);
    }

    @Override
    public List<ActivityResponseDTO> getActivitiesByLead(Long leadId) {
        log.info("Fetching activities for lead id: {}", leadId);

        activityValidator.validateLead(leadId);

        return activityRepository.findByLeadIdOrderByCreatedAtDesc(leadId)
                .stream()
                .map(activityMapper::toDTO)
                .toList();
    }

    @Override
    public List<ActivityResponseDTO> getActivitiesByUser(Long userId) {
        log.info("Fetching activities for user id: {}", userId);

        activityValidator.validateEmployee(userId);

        return activityRepository.findByEmployeeId(userId)
                .stream()
                .sorted(Comparator.comparing(Activity::getCreatedAt).reversed())
                .map(activityMapper::toDTO)
                .toList();
    }
}
