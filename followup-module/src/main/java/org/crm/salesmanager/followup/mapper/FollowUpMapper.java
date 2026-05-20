package org.crm.salesmanager.followup.mapper;

import org.crm.salesmanager.followup.domain.FollowUp;
import org.crm.salesmanager.followup.dto.FollowUpRequestDTO;
import org.crm.salesmanager.followup.dto.FollowUpResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowUpMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    FollowUp toEntity(FollowUpRequestDTO dto);

    FollowUpResponseDTO toDTO(FollowUp followUp);
}
