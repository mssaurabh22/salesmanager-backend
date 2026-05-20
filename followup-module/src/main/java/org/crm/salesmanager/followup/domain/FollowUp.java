package org.crm.salesmanager.followup.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crm.salesmanager.followup.enums.FollowUpStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "follow_ups")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long leadId;

    private Long assignedTo;

    private LocalDateTime followUpTime;

    @Enumerated(EnumType.STRING)
    private FollowUpStatus status;

    private LocalDateTime createdAt;
}
