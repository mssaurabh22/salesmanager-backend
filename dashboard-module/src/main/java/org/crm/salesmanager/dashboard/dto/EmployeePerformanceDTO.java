package org.crm.salesmanager.dashboard.dto;

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
    private long activitiesToday;
}
