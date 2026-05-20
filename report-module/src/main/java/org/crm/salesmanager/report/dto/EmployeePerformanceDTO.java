package org.crm.salesmanager.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePerformanceDTO {

    private Long employeeId;
    private long leadsHandled;
    private long activitiesCount;
    private double conversionRate;
}
