package com.epam.rd.autocode.assessment.appliancestore.mapper;

import com.epam.rd.autocode.assessment.appliancestore.config.MapperConfig;
import com.epam.rd.autocode.assessment.appliancestore.model.Client;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.UpdateClientRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ClientMapper {
    ClientResponseDto toDto(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Client toModel(CreateClientRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateClient(@MappingTarget Client client, UpdateClientRequestDto requestDto);
}
