package org.crm.salesmanager.lead.service;

import org.crm.salesmanager.lead.dto.LeadFilterDTO;
import org.crm.salesmanager.lead.dto.LeadRequestDTO;
import org.crm.salesmanager.lead.dto.LeadResponseDTO;
import org.crm.salesmanager.lead.dto.ReassignLeadDTO;
import org.crm.salesmanager.lead.dto.UpdateStageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LeadService {

    LeadResponseDTO createLead(LeadRequestDTO dto, Long userId);

    LeadResponseDTO updateLead(Long id, LeadRequestDTO dto);

    LeadResponseDTO updateStage(Long id, UpdateStageDTO dto);

    List<LeadResponseDTO> reassignLeads(ReassignLeadDTO dto, Long adminUserId);

    LeadResponseDTO recordActivityImpact(Long leadId, Long userId);

    LeadResponseDTO getLead(Long id);

    Page<LeadResponseDTO> getLeads(LeadFilterDTO filter);
}
