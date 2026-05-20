package org.crm.salesmanager.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDashboardDTO {

    private long todayFollowUps;
    private long missedFollowUps;
    private long totalLeads;
    private long activitiesToday;
}
