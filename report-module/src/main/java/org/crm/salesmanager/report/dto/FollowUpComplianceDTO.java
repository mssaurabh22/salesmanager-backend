package org.crm.salesmanager.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpComplianceDTO {

    private long completedFollowUps;
    private long missedFollowUps;
    private double complianceRate;
}
