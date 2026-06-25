package com.epam.rd.autocode.assessment.appliancestore.service.impl;

import com.epam.rd.autocode.assessment.appliancestore.exception.EntityNotFoundException;
import com.epam.rd.autocode.assessment.appliancestore.exception.RegistrationException;
import com.epam.rd.autocode.assessment.appliancestore.mapper.ClientMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Client;
import com.epam.rd.autocode.assessment.appliancestore.model.Role;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.ClientResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.CreateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.client.UpdateClientRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.RoleRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.UserRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.ClientService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDto> getAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto getByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        return clientMapper.toDto(client);
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
            throw new RegistrationException("Email is already in use");
        }
        Client client = clientMapper.toModel(requestDto);
        client.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role clientRole = roleRepository.findByName(Role.RoleName.CLIENT)
                .orElseThrow(() -> new EntityNotFoundException("Role CLIENT not found"));
        client.setRoles(Set.of(clientRole));

        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public ClientResponseDto updateById(Long id, UpdateClientRequestDto requestDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (!client.getEmail().equals(requestDto.getEmail())
                && userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Email is already in use");
        }

        clientMapper.updateClient(client, requestDto);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public ClientResponseDto updateByEmail(String currentEmail, UpdateClientRequestDto requestDto) {
        Client client = clientRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (!client.getEmail().equals(requestDto.getEmail())
                && userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Email is already in use");
        }

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
