package com.epam.rd.autocode.assessment.appliancestore.mapper;

import com.epam.rd.autocode.assessment.appliancestore.config.MapperConfig;
import com.epam.rd.autocode.assessment.appliancestore.model.Order;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.order.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderRowMapper.class)
public interface OrderMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "orderRowSet", target = "rows")
    OrderResponseDto toDto(Order order);
}
