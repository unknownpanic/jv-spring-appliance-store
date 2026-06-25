package com.epam.rd.autocode.assessment.appliancestore.service;

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
import com.epam.rd.autocode.assessment.appliancestore.service.impl.ClientServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ClientServiceImpl clientService;
    private Client client;
    private ClientResponseDto clientResponseDto;
    private Role clientRole;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setEmail("test@email.com");
        client.setPassword("encodedPassword");
        client.setDeleted(false);

        clientResponseDto = new ClientResponseDto();
        clientResponseDto.setId(1L);
        clientResponseDto.setEmail("test@email.com");

        clientRole = new Role();
        clientRole.setId(1L);
        clientRole.setName(Role.RoleName.CLIENT);
    }

    @Test
    void getAll_ShouldReturnListOfClients() {
        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientResponseDto);

        List<ClientResponseDto> result = clientService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@email.com", result.get(0).getEmail());
        verify(clientRepository).findAll();
    }

    @Test
    void getById_ShouldReturnClient_WhenExists() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientResponseDto);

        ClientResponseDto result = clientService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(clientRepository).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clientService.getById(1L));

        assertEquals("Client not found", exception.getMessage());
        verify(clientMapper, never()).toDto(any(Client.class));
    }

    @Test
    void create_ShouldCreateAndReturnClient() {
        CreateClientRequestDto requestDto = new CreateClientRequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("rawPassword");

        when(clientRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(clientMapper.toModel(requestDto)).thenReturn(client);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName(Role.RoleName.CLIENT)).thenReturn(Optional.of(clientRole));
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientResponseDto);

        ClientResponseDto result = clientService.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Set.of(clientRole), client.getRoles());
        assertEquals("encodedPassword", client.getPassword());
        verify(clientRepository).save(client);
    }

    @Test
    void create_ShouldThrowRegistrationException_WhenEmailExists() {
        CreateClientRequestDto requestDto = new CreateClientRequestDto();
        requestDto.setEmail("test@email.com");

        when(clientRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(client));

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> clientService.create(requestDto));

        assertEquals("Email is already in use", exception.getMessage());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowEntityNotFoundException_WhenRoleNotFound() {
        CreateClientRequestDto requestDto = new CreateClientRequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("rawPassword");

        when(clientRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(clientMapper.toModel(requestDto)).thenReturn(client);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName(Role.RoleName.CLIENT)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clientService.create(requestDto));

        assertEquals("Role CLIENT not found", exception.getMessage());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateById_ShouldUpdateAndReturnClient() {
        UpdateClientRequestDto requestDto = new UpdateClientRequestDto();
        requestDto.setEmail("new@email.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        doNothing().when(clientMapper).updateClient(client, requestDto);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientResponseDto);

        ClientResponseDto result = clientService.updateById(1L, requestDto);

        assertNotNull(result);
        verify(clientMapper).updateClient(client, requestDto);
        verify(clientRepository).save(client);
    }

    @Test
    void updateById_ShouldThrowRegistrationException_WhenEmailUsedByOtherClient() {
        UpdateClientRequestDto requestDto = new UpdateClientRequestDto();
        requestDto.setEmail("used@email.com");

        Client otherClient = new Client();
        otherClient.setId(2L);
        otherClient.setEmail("used@email.com");

        when(userRepository.existsByEmail("used@email.com")).thenReturn(true);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> clientService.updateById(1L, requestDto));

        assertEquals("Email is already in use", exception.getMessage());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateById_ShouldThrowEntityNotFoundException_WhenClientNotFound() {
        UpdateClientRequestDto requestDto = new UpdateClientRequestDto();

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clientService.updateById(1L, requestDto));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldSetDeletedFlagToTrue() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        clientService.deleteById(1L);

        assertTrue(client.isDeleted());
        verify(clientRepository).save(client);
    }

    @Test
    void deleteById_ShouldThrowException_WhenClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clientService.deleteById(1L));

        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, never()).save(any());
    }
}
