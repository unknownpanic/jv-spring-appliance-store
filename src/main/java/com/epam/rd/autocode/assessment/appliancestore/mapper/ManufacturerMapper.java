package com.epam.rd.autocode.assessment.appliancestore.mapper;

import com.epam.rd.autocode.assessment.appliancestore.config.MapperConfig;
import com.epam.rd.autocode.assessment.appliancestore.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.CreateManufacturerRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.manufacturer.ManufacturerResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ManufacturerMapper {
    ManufacturerResponseDto toDto(Manufacturer manufacturer);

    @Mapping(target = "id", ignore = true)
    Manufacturer toModel(CreateManufacturerRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    void updateManufacturer(
            @MappingTarget Manufacturer manufacturer,
            CreateManufacturerRequestDto requestDto);
}
