package org.crm.salesmanager.report.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.activity.repository.ActivityRepository;
import org.crm.salesmanager.followup.enums.FollowUpStatus;
import org.crm.salesmanager.followup.repository.FollowUpRepository;
import org.crm.salesmanager.followup.service.FollowUpService;
import org.crm.salesmanager.lead.enums.LeadStage;
import org.crm.salesmanager.lead.repository.LeadRepository;
import org.crm.salesmanager.report.dto.EmployeePerformanceDTO;
import org.crm.salesmanager.report.dto.FollowUpComplianceDTO;
import org.crm.salesmanager.report.dto.LeadConversionDTO;
import org.crm.salesmanager.report.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final LeadRepository leadRepository;
    private final ActivityRepository activityRepository;
    private final FollowUpRepository followUpRepository;
    private final FollowUpService followUpService;

    @Override
    public List<EmployeePerformanceDTO> getEmployeePerformance() {
        log.info("Generating employee performance report");

        Map<Long, Long> activityCountsByEmployee = new HashMap<>();
        activityRepository.findEmployeeActivityCounts()
                .forEach(result -> activityCountsByEmployee.put(result.getEmployeeId(), result.getActivityCount()));

        Map<Long, EmployeePerformanceDTO> performanceByEmployee = new HashMap<>();

        leadRepository.findEmployeeLeadPerformance(LeadStage.ORDER)
                .stream()
                .map(result -> EmployeePerformanceDTO.builder()
                        .employeeId(result.getEmployeeId())
                        .leadsHandled(result.getLeadsHandled())
                        .activitiesCount(activityCountsByEmployee.getOrDefault(result.getEmployeeId(), 0L))
                        .conversionRate(percentage(result.getClosedLeads(), result.getLeadsHandled()))
                        .build())
                .forEach(result -> performanceByEmployee.put(result.getEmployeeId(), result));

        Set<Long> activityOnlyEmployees = new HashSet<>(activityCountsByEmployee.keySet());
        activityOnlyEmployees.removeAll(performanceByEmployee.keySet());

        activityOnlyEmployees.forEach(employeeId -> performanceByEmployee.put(employeeId, EmployeePerformanceDTO.builder()
                .employeeId(employeeId)
                .leadsHandled(0)
                .activitiesCount(activityCountsByEmployee.getOrDefault(employeeId, 0L))
                .conversionRate(0)
                .build()));

        return performanceByEmployee.values()
                .stream()
                .toList();
    }

    @Override
    public LeadConversionDTO getLeadConversion() {
        log.info("Generating lead conversion report");

        long totalLeads = leadRepository.count();
        long closedLeads = leadRepository.countByStage(LeadStage.ORDER);
        long lostLeads = leadRepository.countByStage(LeadStage.LOST);

        return LeadConversionDTO.builder()
                .totalLeads(totalLeads)
                .closedLeads(closedLeads)
                .lostLeads(lostLeads)
                .conversionRate(percentage(closedLeads, totalLeads))
                .build();
    }

    @Override
    public FollowUpComplianceDTO getFollowUpCompliance() {
        log.info("Generating follow-up compliance report");

        followUpService.markOverdueFollowUpsAsMissed();

        long completedFollowUps = followUpRepository.countByStatus(FollowUpStatus.COMPLETED);
        long missedFollowUps = followUpRepository.countByStatus(FollowUpStatus.MISSED);

        return FollowUpComplianceDTO.builder()
                .completedFollowUps(completedFollowUps)
                .missedFollowUps(missedFollowUps)
                .complianceRate(percentage(completedFollowUps, completedFollowUps + missedFollowUps))
                .build();
    }

    private double percentage(long numerator, long denominator) {
        if (denominator == 0) {
            return 0;
        }

        return (numerator * 100.0) / denominator;
    }
}
