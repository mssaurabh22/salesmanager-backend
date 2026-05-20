package org.crm.salesmanager.timeline.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.timeline.dto.TimelineEventDTO;
import org.crm.salesmanager.timeline.service.TimelineService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Timeline", description = "Lead journey and audit trail APIs")
@SecurityRequirement(name = "bearerAuth")
public class TimelineController {

    private final TimelineService timelineService;

    @GetMapping("/lead/{leadId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Get lead timeline", description = "Returns the full lead journey sorted by newest event first")
    public ResponseEntity<List<TimelineEventDTO>> getLeadTimeline(@PathVariable Long leadId) {
        log.info("Lead timeline request received for lead id: {}", leadId);
        return ResponseEntity.ok(timelineService.getLeadTimeline(leadId));
    }
}
