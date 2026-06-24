package com.epam.rd.autocode.assessment.appliancestore.service;

import com.epam.rd.autocode.assessment.appliancestore.exception.EntityNotFoundException;
import com.epam.rd.autocode.assessment.appliancestore.exception.RegistrationException;
import com.epam.rd.autocode.assessment.appliancestore.mapper.EmployeeMapper;
import com.epam.rd.autocode.assessment.appliancestore.model.Employee;
import com.epam.rd.autocode.assessment.appliancestore.model.Role;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.CreateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.EmployeeResponseDto;
import com.epam.rd.autocode.assessment.appliancestore.model.dto.employee.UpdateEmployeeRequestDto;
import com.epam.rd.autocode.assessment.appliancestore.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliancestore.repository.RoleRepository;
import com.epam.rd.autocode.assessment.appliancestore.service.impl.EmployeeServiceImpl;
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
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;
    private EmployeeResponseDto employeeResponseDto;
    private Role employeeRole;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setEmail("employee@test.com");
        employee.setPassword("encodedPassword");
        employee.setDeleted(false);

        employeeResponseDto = new EmployeeResponseDto();
        employeeResponseDto.setId(1L);
        employeeResponseDto.setEmail("employee@test.com");

        employeeRole = new Role();
        employeeRole.setId(1L);
        employeeRole.setName(Role.RoleName.EMPLOYEE);
    }

    @Test
    void getAll_ShouldReturnListOfEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(employeeResponseDto);

        List<EmployeeResponseDto> result = employeeService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("employee@test.com", result.get(0).getEmail());
        verify(employeeRepository).findAll();
    }

    @Test
    void getById_ShouldReturnEmployee_WhenExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(employeeResponseDto);

        EmployeeResponseDto result = employeeService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> employeeService.getById(1L));

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeMapper, never()).toDto(any(Employee.class));
    }

    @Test
    void create_ShouldCreateAndReturnEmployee() {
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto();
        requestDto.setEmail("employee@test.com");
        requestDto.setPassword("rawPassword");

        when(employeeRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(employeeMapper.toModel(requestDto)).thenReturn(employee);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName(Role.RoleName.EMPLOYEE)).thenReturn(Optional.of(employeeRole));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeResponseDto);

        EmployeeResponseDto result = employeeService.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Set.of(employeeRole), employee.getRoles());
        assertEquals("encodedPassword", employee.getPassword());
        verify(employeeRepository).save(employee);
    }

    @Test
    void create_ShouldThrowRegistrationException_WhenEmailExists() {
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto();
        requestDto.setEmail("employee@test.com");

        when(employeeRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(employee));

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> employeeService.create(requestDto));

        assertEquals("Email is already in use", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowEntityNotFoundException_WhenRoleNotFound() {
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto();
        requestDto.setEmail("employee@test.com");
        requestDto.setPassword("rawPassword");

        when(employeeRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(employeeMapper.toModel(requestDto)).thenReturn(employee);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName(Role.RoleName.EMPLOYEE)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> employeeService.create(requestDto));

        assertEquals("Role EMPLOYEE not found", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateById_ShouldUpdateAndReturnEmployee() {
        UpdateEmployeeRequestDto requestDto = new UpdateEmployeeRequestDto();
        requestDto.setEmail("new_employee@test.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        doNothing().when(employeeMapper).updateEmployee(employee, requestDto);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeResponseDto);

        EmployeeResponseDto result = employeeService.updateById(1L, requestDto);

        assertNotNull(result);
        verify(employeeMapper).updateEmployee(employee, requestDto);
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateById_ShouldThrowRegistrationException_WhenEmailUsedByOtherEmployee() {
        UpdateEmployeeRequestDto requestDto = new UpdateEmployeeRequestDto();
        requestDto.setEmail("used@test.com");

        Employee otherEmployee = new Employee();
        otherEmployee.setId(2L);
        otherEmployee.setEmail("used@test.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(otherEmployee));

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> employeeService.updateById(1L, requestDto));

        assertEquals("Email is already in use", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateById_ShouldThrowEntityNotFoundException_WhenEmployeeNotFound() {
        UpdateEmployeeRequestDto requestDto = new UpdateEmployeeRequestDto();

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> employeeService.updateById(1L, requestDto));

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldSetDeletedFlagToTrue() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        employeeService.deleteById(1L);

        assertTrue(employee.isDeleted());
        verify(employeeRepository).save(employee);
    }

    @Test
    void deleteById_ShouldThrowException_WhenEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> employeeService.deleteById(1L));

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }
}
