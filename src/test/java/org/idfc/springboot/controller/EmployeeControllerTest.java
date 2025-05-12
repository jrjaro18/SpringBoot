package org.idfc.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.idfc.springboot.dto.EmployeeRequestDTO;
import org.idfc.springboot.dto.EmployeeResponseDTO;
import org.idfc.springboot.kafka.KafkaProducerService;
import org.idfc.springboot.model.Employee;
import org.idfc.springboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService service;

    @MockitoBean
    private KafkaProducerService producerService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeRequestDTO requestDto;
    private Employee employee;
    private EmployeeResponseDTO responseDto;

    @BeforeEach
    public void setup() {
        requestDto = new EmployeeRequestDTO("Alice", "Developer", 90000.0, "alice123@gmail.com");
        employee = new Employee("123", "Alice", "Developer","alice123@gmail.com", 90000.0);
        responseDto = new EmployeeResponseDTO("123", "Alice", "Developer", 90000.0, "alice123@gmail.com");
    }

    @Test
    public void testGetAll() throws Exception {
        List<Employee> employees = Arrays.asList(employee);
        Mockito.when(service.getAll()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123"))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    public void testGetById() throws Exception {
        Mockito.when(service.getById("123")).thenReturn(employee);

        mockMvc.perform(get("/api/employees/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    public void testCreate() throws Exception {
        Mockito.when(service.containsEmail(requestDto.getEmail())).thenReturn(false);
        Mockito.when(service.save(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    public void testUpdate() throws Exception {
        Employee updated = new Employee("123", "Updated", "Developer", "alice123@gmail.com", 90000.0);
        EmployeeRequestDTO updatedDto = new EmployeeRequestDTO("Updated", "Developer", 90000.0, "alice123@gmail.com");

        Mockito.when(service.getById("123")).thenReturn(employee);
        Mockito.when(service.save(any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/api/employees/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    public void testDelete() throws Exception {
        Mockito.doNothing().when(service).delete("123");

        mockMvc.perform(delete("/api/employees/123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetByRole() throws Exception {
        List<Employee> employees = Arrays.asList(employee);
        Mockito.when(service.getByRole("Developer")).thenReturn(employees);

        mockMvc.perform(get("/api/employees/role/Developer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("Developer"));
    }
}
