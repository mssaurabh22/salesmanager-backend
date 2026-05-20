package org.crm.salesmanager.lead.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ReassignLeadDTO {

    @NotEmpty(message = "Lead ids are required")
    private List<Long> leadIds;

    @NotNull(message = "Assigned user id is required")
    private Long assignedTo;
}
