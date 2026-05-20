package org.crm.salesmanager.dashboard.service;

import org.crm.salesmanager.dashboard.dto.EmployeeDashboardDTO;
import org.crm.salesmanager.dashboard.dto.OwnerDashboardDTO;

public interface DashboardService {

    EmployeeDashboardDTO getEmployeeDashboard(Long userId);

    OwnerDashboardDTO getOwnerDashboard();
}
