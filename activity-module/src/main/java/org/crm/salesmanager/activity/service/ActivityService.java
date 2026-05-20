package org.crm.salesmanager.activity.service;

import org.crm.salesmanager.activity.dto.ActivityRequestDTO;
import org.crm.salesmanager.activity.dto.ActivityResponseDTO;

import java.util.List;

public interface ActivityService {

    ActivityResponseDTO createActivity(ActivityRequestDTO dto, Long employeeId);

    List<ActivityResponseDTO> getActivitiesByLead(Long leadId);

    List<ActivityResponseDTO> getActivitiesByUser(Long userId);
}
