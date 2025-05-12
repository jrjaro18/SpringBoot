package org.idfc.springboot.service;

import org.idfc.springboot.model.Employee;
import org.idfc.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    public List<Employee> getAll() {
        return repo.findAll();
    }

    public Employee getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found with ID: " + id));
    }

    public Employee save(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee object must not be null");
        }
        return repo.save(employee);
    }

    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Cannot delete. Employee not found with ID: " + id);
        }
        repo.deleteById(id);
    }

    public List<Employee> getByRole(String role) {
        List<Employee> employees = repo.findByRole(role);
        if (employees.isEmpty()) {
            throw new NoSuchElementException("No employees found with role: " + role);
        }
        return employees;
    }

    public boolean containsEmail(String email) {
        List<Employee> employees = repo.findByEmail(email);
        return !employees.isEmpty();
    }
}
