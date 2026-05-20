package org.crm.salesmanager.followup.service;

import org.crm.salesmanager.followup.dto.FollowUpRequestDTO;
import org.crm.salesmanager.followup.dto.FollowUpResponseDTO;

import java.util.List;

public interface FollowUpService {

    FollowUpResponseDTO createFollowUp(FollowUpRequestDTO dto, Long userId);

    FollowUpResponseDTO markCompleted(Long id, Long userId);

    List<FollowUpResponseDTO> getTodayFollowUps(Long userId);

    List<FollowUpResponseDTO> getMissedFollowUps(Long userId);

    void markOverdueFollowUpsAsMissed();

    void suggestNextFollowUp(Long leadId, Long userId, String reason);
}
