package org.crm.salesmanager.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.dashboard.dto.EmployeeDashboardDTO;
import org.crm.salesmanager.dashboard.dto.OwnerDashboardDTO;
import org.crm.salesmanager.dashboard.service.DashboardService;
import org.crm.salesmanager.user.dto.UserAuthDTO;
import org.crm.salesmanager.user.entity.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "Dashboard insight APIs")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/employee")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "Get employee dashboard", description = "Returns today's work summary for the authenticated employee")
    public ResponseEntity<EmployeeDashboardDTO> getEmployeeDashboard(@AuthenticationPrincipal UserAuthDTO user) {
        log.info("Employee dashboard request received for user id: {}", user.getId());
        return ResponseEntity.ok(dashboardService.getEmployeeDashboard(user.getId()));
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get owner dashboard", description = "Returns aggregate business metrics for admin users")
    public ResponseEntity<OwnerDashboardDTO> getOwnerDashboard(@AuthenticationPrincipal UserAuthDTO user) {
        log.info("Owner dashboard request received for user id: {}", user.getId());

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only admin users can access owner dashboard");
        }

        return ResponseEntity.ok(dashboardService.getOwnerDashboard());
    }
}
