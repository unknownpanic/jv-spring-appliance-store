package com.epam.rd.autocode.assessment.appliancestore.mapper;

import com.epam.rd.autocode.assessment.appliancestore.config.MapperConfig;
import com.epam.rd.autocode.assessment.appliancestore.model.Appliance;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.ApplianceResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.appliance.CreateApplianceRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ApplianceMapper {
    @Mapping(source = "manufacturer.id", target = "manufacturerId")
    ApplianceResponseDto toDto(Appliance appliance);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    Appliance toModel(CreateApplianceRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    void updateAppliance(
            @MappingTarget Appliance appliance,
            CreateApplianceRequestDto requestDto);
}
