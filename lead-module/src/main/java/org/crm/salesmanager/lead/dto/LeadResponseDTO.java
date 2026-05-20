package org.crm.salesmanager.lead.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crm.salesmanager.lead.enums.LeadStage;
import org.crm.salesmanager.lead.enums.LeadTemperature;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponseDTO {

    private Long id;
    private String customerName;
    private String businessName;
    private LeadStage stage;
    private Integer weightage;
    private LeadTemperature temperature;
    private Double expectedValue;
}
