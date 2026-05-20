package org.crm.salesmanager.report.service;

import org.crm.salesmanager.report.dto.EmployeePerformanceDTO;
import org.crm.salesmanager.report.dto.FollowUpComplianceDTO;
import org.crm.salesmanager.report.dto.LeadConversionDTO;

import java.util.List;

public interface ReportService {

    List<EmployeePerformanceDTO> getEmployeePerformance();

    LeadConversionDTO getLeadConversion();

    FollowUpComplianceDTO getFollowUpCompliance();
}
