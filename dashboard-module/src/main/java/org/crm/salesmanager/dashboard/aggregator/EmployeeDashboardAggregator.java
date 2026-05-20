package org.crm.salesmanager.dashboard.aggregator;

import lombok.RequiredArgsConstructor;
import org.crm.salesmanager.activity.repository.ActivityRepository;
import org.crm.salesmanager.dashboard.dto.EmployeeDashboardDTO;
import org.crm.salesmanager.followup.enums.FollowUpStatus;
import org.crm.salesmanager.followup.repository.FollowUpRepository;
import org.crm.salesmanager.followup.service.FollowUpService;
import org.crm.salesmanager.lead.repository.LeadRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class EmployeeDashboardAggregator {

    private final FollowUpRepository followUpRepository;
    private final FollowUpService followUpService;
    private final LeadRepository leadRepository;
    private final ActivityRepository activityRepository;

    public EmployeeDashboardDTO aggregate(Long userId) {
        followUpService.markOverdueFollowUpsAsMissed();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return EmployeeDashboardDTO.builder()
                .todayFollowUps(followUpRepository.countByAssignedToAndFollowUpTimeBetweenAndStatus(
                        userId,
                        startOfDay,
                        endOfDay,
                        FollowUpStatus.PENDING
                ))
                .missedFollowUps(followUpRepository.countByAssignedToAndStatus(userId, FollowUpStatus.MISSED))
                .totalLeads(leadRepository.countByAssignedTo(userId))
                .activitiesToday(activityRepository.countByEmployeeIdAndCreatedAtBetween(userId, startOfDay, endOfDay))
                .build();
    }
}
