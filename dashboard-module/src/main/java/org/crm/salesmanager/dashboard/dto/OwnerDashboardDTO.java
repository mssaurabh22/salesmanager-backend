package org.crm.salesmanager.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDashboardDTO {

    private long totalLeads;
    private long hotLeads;
    private long warmLeads;
    private long coldLeads;
    private double revenueForecast;
    private List<EmployeePerformanceDTO> employeePerformance;
}
