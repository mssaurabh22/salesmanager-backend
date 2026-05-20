package org.crm.salesmanager.timeline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crm.salesmanager.timeline.enums.TimelineAction;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineEventDTO {

    private Long id;
    private Long leadId;
    private Long userId;
    private TimelineAction action;
    private String description;
    private LocalDateTime timestamp;
}
