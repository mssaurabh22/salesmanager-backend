package org.crm.salesmanager.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.report.dto.EmployeePerformanceDTO;
import org.crm.salesmanager.report.dto.FollowUpComplianceDTO;
import org.crm.salesmanager.report.dto.LeadConversionDTO;
import org.crm.salesmanager.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reports", description = "Business reporting and analytics APIs")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/employee-performance")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Employee performance report", description = "Returns leads handled, activity count, and conversion rate by employee")
    public ResponseEntity<List<EmployeePerformanceDTO>> getEmployeePerformance() {
        log.info("Employee performance report request received");
        return ResponseEntity.ok(reportService.getEmployeePerformance());
    }

    @GetMapping("/lead-conversion")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lead conversion report", description = "Returns total, closed, lost, and conversion-rate metrics")
    public ResponseEntity<LeadConversionDTO> getLeadConversion() {
        log.info("Lead conversion report request received");
        return ResponseEntity.ok(reportService.getLeadConversion());
    }

    @GetMapping("/followup-compliance")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Follow-up compliance report", description = "Returns completed vs missed follow-up metrics")
    public ResponseEntity<FollowUpComplianceDTO> getFollowUpCompliance() {
        log.info("Follow-up compliance report request received");
        return ResponseEntity.ok(reportService.getFollowUpCompliance());
    }
}
