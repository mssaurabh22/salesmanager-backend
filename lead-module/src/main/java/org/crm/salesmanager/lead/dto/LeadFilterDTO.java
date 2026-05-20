package org.crm.salesmanager.lead.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.crm.salesmanager.lead.enums.LeadStage;
import org.crm.salesmanager.lead.enums.LeadTemperature;

@Data
public class LeadFilterDTO {

    private LeadTemperature temperature;
    private LeadStage stage;
    private Long assignedTo;
    private Double minValue;
    private Double maxValue;
    private String search;

    @Min(value = 0, message = "Page must be greater than or equal to 0")
    private Integer page = 0;

    @Min(value = 1, message = "Size must be greater than 0")
    private Integer size = 10;

    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
}
