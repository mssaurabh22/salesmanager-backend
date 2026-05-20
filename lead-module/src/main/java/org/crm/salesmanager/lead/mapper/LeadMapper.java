package org.crm.salesmanager.lead.mapper;

import org.crm.salesmanager.lead.domain.Lead;
import org.crm.salesmanager.lead.dto.LeadRequestDTO;
import org.crm.salesmanager.lead.dto.LeadResponseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LeadMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stage", ignore = true)
    @Mapping(target = "weightage", ignore = true)
    @Mapping(target = "temperature", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Lead toEntity(LeadRequestDTO dto);

    LeadResponseDTO toDTO(Lead lead);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stage", ignore = true)
    @Mapping(target = "weightage", ignore = true)
    @Mapping(target = "temperature", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateLeadFromDto(LeadRequestDTO dto, @MappingTarget Lead lead);
}
