package org.idfc.springboot.controller;

import org.idfc.springboot.dto.EmployeeRequestDTO;
import org.idfc.springboot.dto.EmployeeResponseDTO;
import org.idfc.springboot.kafka.KafkaProducerService;
import org.idfc.springboot.model.Employee;
import org.idfc.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private KafkaProducerService producerService;

    @GetMapping
    public List<EmployeeResponseDTO> getAll() {
        return service.getAll().stream()
                .map(e -> new EmployeeResponseDTO(e.getId(), e.getName(), e.getRole(), e.getSalary(), e.getEmail()))
                .toList();
    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO getById(@PathVariable String id) {
        Employee e = service.getById(id);
        return new EmployeeResponseDTO(e.getId(), e.getName(), e.getRole(), e.getSalary(), e.getEmail());
    }


    @PostMapping
    public EmployeeResponseDTO create(@RequestBody EmployeeRequestDTO dto) {
        Employee e = new Employee();
        e.setName(dto.getName());
        e.setRole(dto.getRole());
        e.setSalary(dto.getSalary());
        e.setEmail(dto.getEmail());

        Employee saved = service.save(e);
        producerService.send(saved);
        return new EmployeeResponseDTO(saved.getId(), saved.getName(), saved.getRole(), saved.getSalary(), saved.getEmail());
    }


    @PutMapping("/{id}")
    public EmployeeResponseDTO update(@PathVariable String id, @RequestBody EmployeeRequestDTO dto) {
        Employee e = service.getById(id);

        e.setId(id);
        e.setName(dto.getName());
        e.setRole(dto.getRole());
        e.setSalary(dto.getSalary());
        Employee saved = service.save(e);
        return new EmployeeResponseDTO(saved.getId(), saved.getName(), saved.getRole(), saved.getSalary(), e.getEmail());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/role/{role}")
    public List<EmployeeResponseDTO> getByRole(@PathVariable String role) {
        return service.getByRole(role).stream().map(e -> new EmployeeResponseDTO(e.getId(), e.getName(), e.getRole(), e.getSalary(), e.getEmail())).toList();
    }
}
