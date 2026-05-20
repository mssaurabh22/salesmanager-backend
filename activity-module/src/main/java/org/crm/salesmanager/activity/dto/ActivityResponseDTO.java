package org.crm.salesmanager.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crm.salesmanager.activity.enums.ActivityType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponseDTO {

    private Long id;
    private Long leadId;
    private ActivityType type;
    private String notes;
    private LocalDateTime createdAt;
}
