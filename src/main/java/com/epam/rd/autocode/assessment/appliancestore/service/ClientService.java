package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.UpdateClientRequestDto;
import java.util.List;

public interface ClientService {
    List<ClientResponseDto> getAll();

    ClientResponseDto getByEmail(String name);

    ClientResponseDto getById(Long id);

    ClientResponseDto create(CreateClientRequestDto requestDto);

    ClientResponseDto updateById(Long id, UpdateClientRequestDto requestDto);

    ClientResponseDto updateByEmail(String name, UpdateClientRequestDto requestDto);

    void deleteById(Long id);
}
