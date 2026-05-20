package org.crm.salesmanager.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadConversionDTO {

    private long totalLeads;
    private long closedLeads;
    private long lostLeads;
    private double conversionRate;
}
