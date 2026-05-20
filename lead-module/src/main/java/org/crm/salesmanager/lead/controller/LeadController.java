package org.crm.salesmanager.lead.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.lead.dto.LeadFilterDTO;
import org.crm.salesmanager.lead.dto.LeadRequestDTO;
import org.crm.salesmanager.lead.dto.LeadResponseDTO;
import org.crm.salesmanager.lead.dto.ReassignLeadDTO;
import org.crm.salesmanager.lead.dto.UpdateStageDTO;
import org.crm.salesmanager.lead.service.LeadService;
import org.crm.salesmanager.user.dto.UserAuthDTO;
import org.crm.salesmanager.user.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lead", description = "Lead management APIs")
@SecurityRequirement(name = "bearerAuth")
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Create lead", description = "Creates a lead and assigns it to the authenticated user")
    public ResponseEntity<LeadResponseDTO> createLead(@Valid @RequestBody LeadRequestDTO dto,
                                                      @AuthenticationPrincipal UserAuthDTO user) {
        log.info("Create lead request received for user id: {}", user.getId());
        return ResponseEntity.ok(leadService.createLead(dto, user.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Get lead", description = "Fetches a lead by id")
    public ResponseEntity<LeadResponseDTO> getLead(@PathVariable Long id) {
        log.info("Get lead request received for id: {}", id);
        return ResponseEntity.ok(leadService.getLead(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Update lead", description = "Updates editable lead fields")
    public ResponseEntity<LeadResponseDTO> updateLead(@PathVariable Long id,
                                                      @Valid @RequestBody LeadRequestDTO dto) {
        log.info("Update lead request received for id: {}", id);
        return ResponseEntity.ok(leadService.updateLead(id, dto));
    }

    @PutMapping("/{id}/stage")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Update lead stage", description = "Updates lead stage after transition validation")
    public ResponseEntity<LeadResponseDTO> updateStage(@PathVariable Long id,
                                                       @Valid @RequestBody UpdateStageDTO dto) {
        log.info("Update stage request received for lead id: {}", id);
        return ResponseEntity.ok(leadService.updateStage(id, dto));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Search leads", description = "Returns paginated leads using specification-based filters")
    public ResponseEntity<Page<LeadResponseDTO>> searchLeads(@Valid @RequestBody LeadFilterDTO filter,
                                                             @AuthenticationPrincipal UserAuthDTO user) {
        if (user.getRole() == Role.EMPLOYEE) {
            filter.setAssignedTo(user.getId());
        }

        log.info("Lead search request received");
        return ResponseEntity.ok(leadService.getLeads(filter));
    }

    @PutMapping("/reassign")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reassign leads", description = "Reassigns one or more leads to another user")
    public ResponseEntity<List<LeadResponseDTO>> reassignLeads(@Valid @RequestBody ReassignLeadDTO dto,
                                                               @AuthenticationPrincipal UserAuthDTO user) {
        log.info("Lead reassignment request received by user id: {}", user.getId());
        return ResponseEntity.ok(leadService.reassignLeads(dto, user.getId()));
    }
}
