package org.crm.salesmanager.activity.mapper;

import org.crm.salesmanager.activity.domain.Activity;
import org.crm.salesmanager.activity.dto.ActivityRequestDTO;
import org.crm.salesmanager.activity.dto.ActivityResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Activity toEntity(ActivityRequestDTO dto);

    ActivityResponseDTO toDTO(Activity activity);
}
