package org.crm.salesmanager.followup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crm.salesmanager.followup.enums.FollowUpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpResponseDTO {

    private Long id;
    private Long leadId;
    private LocalDateTime followUpTime;
    private FollowUpStatus status;
}
