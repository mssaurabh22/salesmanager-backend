package org.crm.salesmanager.activity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.activity.dto.ActivityRequestDTO;
import org.crm.salesmanager.activity.dto.ActivityResponseDTO;
import org.crm.salesmanager.activity.service.ActivityService;
import org.crm.salesmanager.user.dto.UserAuthDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Activity", description = "Lead activity management APIs")
@SecurityRequirement(name = "bearerAuth")
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Create activity", description = "Creates an immutable activity linked to a lead")
    public ResponseEntity<ActivityResponseDTO> createActivity(@Valid @RequestBody ActivityRequestDTO dto,
                                                              @AuthenticationPrincipal UserAuthDTO user) {
        log.info("Create activity request received for user id: {}", user.getId());
        return ResponseEntity.ok(activityService.createActivity(dto, user.getId()));
    }

    @GetMapping("/lead/{leadId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Get lead activities", description = "Returns activities for a lead ordered by newest first")
    public ResponseEntity<List<ActivityResponseDTO>> getActivitiesByLead(@PathVariable Long leadId) {
        log.info("Get activities by lead request received for lead id: {}", leadId);
        return ResponseEntity.ok(activityService.getActivitiesByLead(leadId));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Get user activities", description = "Returns activities created by the authenticated user")
    public ResponseEntity<List<ActivityResponseDTO>> getActivitiesByUser(@AuthenticationPrincipal UserAuthDTO user) {
        log.info("Get activities by user request received for user id: {}", user.getId());
        return ResponseEntity.ok(activityService.getActivitiesByUser(user.getId()));
    }
}
