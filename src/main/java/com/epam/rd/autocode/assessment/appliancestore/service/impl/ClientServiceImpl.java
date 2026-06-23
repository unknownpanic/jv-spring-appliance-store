package com.epam.rd.autocode.assessment.appliancestore.service.impl;

import com.epam.rd.autocode.assessment.appliancestore.mapper.ClientMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Client;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.UpdateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDto> getAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional
    public ClientResponseDto create(CreateClientRequestDto requestDto) {
        if (clientRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
        Client client = clientMapper.toModel(requestDto);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public ClientResponseDto updateById(Long id, UpdateClientRequestDto requestDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        clientRepository.findByEmail(requestDto.getEmail())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Email is already in use");
                });

        clientMapper.updateClient(client, requestDto);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        client.setDeleted(true);
        clientRepository.save(client);
    }
}
