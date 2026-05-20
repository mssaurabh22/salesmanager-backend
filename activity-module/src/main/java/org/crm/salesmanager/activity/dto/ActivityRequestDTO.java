package org.crm.salesmanager.activity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.crm.salesmanager.activity.enums.ActivityType;

@Data
public class ActivityRequestDTO {

    @NotNull(message = "Lead id is required")
    private Long leadId;

    @NotNull(message = "Activity type is required")
    private ActivityType type;

    private String notes;

    private Double gpsLat;

    private Double gpsLong;
}
