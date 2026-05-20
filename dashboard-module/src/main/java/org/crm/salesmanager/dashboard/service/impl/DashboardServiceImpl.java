package org.crm.salesmanager.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.dashboard.aggregator.EmployeeDashboardAggregator;
import org.crm.salesmanager.dashboard.aggregator.OwnerDashboardAggregator;
import org.crm.salesmanager.dashboard.dto.EmployeeDashboardDTO;
import org.crm.salesmanager.dashboard.dto.OwnerDashboardDTO;
import org.crm.salesmanager.dashboard.service.DashboardService;
import org.crm.salesmanager.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeDashboardAggregator employeeDashboardAggregator;
    private final OwnerDashboardAggregator ownerDashboardAggregator;
    private final UserService userService;

    @Override
    public EmployeeDashboardDTO getEmployeeDashboard(Long userId) {
        log.info("Fetching employee dashboard for user id: {}", userId);
        validateUser(userId);
        return employeeDashboardAggregator.aggregate(userId);
    }

    @Override
    public OwnerDashboardDTO getOwnerDashboard() {
        log.info("Fetching owner dashboard");
        return ownerDashboardAggregator.aggregate();
    }

    private void validateUser(Long userId) {
        if (userId == null) {
            throw new RuntimeException("User id is required");
        }

        userService.getUserForAuthById(userId);
    }
}
