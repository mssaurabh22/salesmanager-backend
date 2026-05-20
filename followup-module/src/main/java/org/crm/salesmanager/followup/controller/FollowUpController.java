package org.crm.salesmanager.followup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.followup.dto.FollowUpRequestDTO;
import org.crm.salesmanager.followup.dto.FollowUpResponseDTO;
import org.crm.salesmanager.followup.service.FollowUpService;
import org.crm.salesmanager.user.dto.UserAuthDTO;
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
@RequestMapping("/api/followups")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Follow-Up", description = "Follow-up management APIs")
@SecurityRequirement(name = "bearerAuth")
public class FollowUpController {

    private final FollowUpService followUpService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Create follow-up", description = "Creates a pending follow-up assigned to the authenticated user")
    public ResponseEntity<FollowUpResponseDTO> createFollowUp(@Valid @RequestBody FollowUpRequestDTO dto,
                                                              @AuthenticationPrincipal UserAuthDTO user) {
        log.info("Create follow-up request received for user id: {}", user.getId());
        return ResponseEntity.ok(followUpService.createFollowUp(dto, user.getId()));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Complete follow-up", description = "Marks a follow-up as completed for the authenticated user")
    public ResponseEntity<FollowUpResponseDTO> markCompleted(@PathVariable Long id,
                                                             @AuthenticationPrincipal UserAuthDTO user) {
        log.info("Complete follow-up request received for id: {} and user id: {}", id, user.getId());
        return ResponseEntity.ok(followUpService.markCompleted(id, user.getId()));
    }

    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Get today's follow-ups", description = "Returns pending follow-ups scheduled for today")
    public ResponseEntity<List<FollowUpResponseDTO>> getTodayFollowUps(@AuthenticationPrincipal UserAuthDTO user) {
        log.info("Get today follow-ups request received for user id: {}", user.getId());
        return ResponseEntity.ok(followUpService.getTodayFollowUps(user.getId()));
    }

    @GetMapping("/missed")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Get missed follow-ups", description = "Returns missed follow-ups for the authenticated user")
    public ResponseEntity<List<FollowUpResponseDTO>> getMissedFollowUps(@AuthenticationPrincipal UserAuthDTO user) {
        log.info("Get missed follow-ups request received for user id: {}", user.getId());
        return ResponseEntity.ok(followUpService.getMissedFollowUps(user.getId()));
    }
}
