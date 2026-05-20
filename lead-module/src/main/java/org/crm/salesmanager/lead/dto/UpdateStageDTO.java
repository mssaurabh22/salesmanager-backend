package org.crm.salesmanager.lead.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.crm.salesmanager.lead.enums.LeadStage;

@Data
public class UpdateStageDTO {

    @NotNull(message = "Stage is required")
    private LeadStage stage;
}
