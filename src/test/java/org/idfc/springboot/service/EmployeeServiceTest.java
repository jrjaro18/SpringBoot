package org.idfc.springboot.service;

import org.idfc.springboot.model.Employee;
import org.idfc.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Employee> mockList = List.of(new Employee("1", "John", "Manager", 30000.99));
        when(employeeRepository.findAll()).thenReturn(mockList);

        List<Employee> result = employeeService.getAll();
        assertEquals(1, result.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetById_Exists() {
        Employee employee = new Employee("1", "Jane", "Developer", 30000.99);
        when(employeeRepository.findById("1")).thenReturn(Optional.of(employee));

        Employee result = employeeService.getById("1");
        assertEquals("Jane", result.getName());
    }

    @Test
    void testGetById_NotExists() {
        when(employeeRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> employeeService.getById("99"));
    }

    @Test
    void testSave_ValidEmployee() {
        Employee employee = new Employee("2", "Raj", "QA", 30000.99);
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee saved = employeeService.save(employee);
        assertEquals("Raj", saved.getName());
        verify(employeeRepository).save(employee);
    }

    @Test
    void testSave_NullEmployee() {
        assertThrows(IllegalArgumentException.class, () -> employeeService.save(null));
    }

    @Test
    void testDelete_Exists() {
        when(employeeRepository.existsById("1")).thenReturn(true);
        doNothing().when(employeeRepository).deleteById("1");

        employeeService.delete("1");
        verify(employeeRepository).deleteById("1");
    }

    @Test
    void testDelete_NotExists() {
        when(employeeRepository.existsById("100")).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> employeeService.delete("100"));
    }

    @Test
    void testGetByRole_Found() {
        List<Employee> list = List.of(new Employee("3", "Ali", "DevOps", 30000.99));
        when(employeeRepository.findByRole("DevOps")).thenReturn(list);

        List<Employee> result = employeeService.getByRole("DevOps");
        assertEquals(1, result.size());
    }

    @Test
    void testGetByRole_NotFound() {
        when(employeeRepository.findByRole("Intern")).thenReturn(Collections.emptyList());
        assertThrows(NoSuchElementException.class, () -> employeeService.getByRole("Intern"));
    }
}
