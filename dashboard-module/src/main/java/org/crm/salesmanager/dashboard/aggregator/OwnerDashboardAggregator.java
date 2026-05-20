package org.crm.salesmanager.dashboard.aggregator;

import lombok.RequiredArgsConstructor;
import org.crm.salesmanager.activity.repository.ActivityRepository;
import org.crm.salesmanager.dashboard.dto.EmployeePerformanceDTO;
import org.crm.salesmanager.dashboard.dto.OwnerDashboardDTO;
import org.crm.salesmanager.lead.enums.LeadTemperature;
import org.crm.salesmanager.lead.repository.LeadRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OwnerDashboardAggregator {

    private final LeadRepository leadRepository;
    private final ActivityRepository activityRepository;

    public OwnerDashboardDTO aggregate() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<EmployeePerformanceDTO> employeePerformance = activityRepository.findEmployeeActivityCountsBetween(startOfDay, endOfDay)
                .stream()
                .map(result -> EmployeePerformanceDTO.builder()
                        .employeeId(result.getEmployeeId())
                        .activitiesToday(result.getActivityCount())
                        .build())
                .toList();

        return OwnerDashboardDTO.builder()
                .totalLeads(leadRepository.count())
                .hotLeads(leadRepository.countByTemperature(LeadTemperature.HOT))
                .warmLeads(leadRepository.countByTemperature(LeadTemperature.WARM))
                .coldLeads(leadRepository.countByTemperature(LeadTemperature.COLD))
                .revenueForecast(leadRepository.sumExpectedValue())
                .employeePerformance(employeePerformance)
                .build();
    }
}
