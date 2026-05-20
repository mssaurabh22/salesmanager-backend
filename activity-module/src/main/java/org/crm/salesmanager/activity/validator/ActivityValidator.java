package org.crm.salesmanager.activity.validator;

import lombok.RequiredArgsConstructor;
import org.crm.salesmanager.activity.dto.ActivityRequestDTO;
import org.crm.salesmanager.lead.service.LeadService;
import org.crm.salesmanager.user.service.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivityValidator {

    private final LeadService leadService;
    private final UserService userService;

    public void validateCreateRequest(ActivityRequestDTO dto, Long employeeId) {
        if (dto.getLeadId() == null) {
            throw new RuntimeException("Lead id is required");
        }

        if (dto.getType() == null) {
            throw new RuntimeException("Activity type is required");
        }

        if (employeeId == null) {
            throw new RuntimeException("Activity must have an employee");
        }

        leadService.getLead(dto.getLeadId());
        userService.getUserForAuthById(employeeId);
    }

    public void validateLead(Long leadId) {
        if (leadId == null) {
            throw new RuntimeException("Lead id is required");
        }

        leadService.getLead(leadId);
    }

    public void validateEmployee(Long employeeId) {
        if (employeeId == null) {
            throw new RuntimeException("User id is required");
        }

        userService.getUserForAuthById(employeeId);
    }
}
