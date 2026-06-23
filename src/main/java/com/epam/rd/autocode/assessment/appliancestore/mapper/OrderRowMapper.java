package com.epam.rd.autocode.assessment.appliancestore.mapper;

import com.epam.rd.autocode.assessment.appliancestore.config.MapperConfig;
import com.epam.rd.autocode.assessment.appliancestore.model.OrderRow;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderRowResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderRowMapper {
    @Mapping(source = "appliance.id", target = "applianceId")
    OrderRowResponseDto toDto(OrderRow orderRow);
}
