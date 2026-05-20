package org.crm.salesmanager.followup.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowUpRequestDTO {

    @NotNull(message = "Lead id is required")
    private Long leadId;

    @NotNull(message = "Follow-up time is required")
    private LocalDateTime followUpTime;
}
