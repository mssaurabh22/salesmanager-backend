package org.crm.salesmanager.lead.domain;

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
import org.crm.salesmanager.lead.enums.LeadStage;
import org.crm.salesmanager.lead.enums.LeadTemperature;

import java.time.LocalDateTime;

@Entity
@Table(name = "leads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String businessName;

    private String contactNumber;

    private String email;

    @Enumerated(EnumType.STRING)
    private LeadStage stage;

    private Integer weightage;

    @Enumerated(EnumType.STRING)
    private LeadTemperature temperature;

    private Double expectedValue;

    private Long assignedTo;

    private Long createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
