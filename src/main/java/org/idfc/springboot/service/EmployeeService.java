package org.idfc.springboot.service;

import org.idfc.springboot.model.Employee;
import org.idfc.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;
    public List<Employee> getAll() {
        return repo.findAll();
    }

    public Employee getById(String id) {
        return repo.findById(id).orElse(null);
    }

    public Employee save(Employee employee) {
        return repo.save(employee);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public List<Employee> getByRole(String role) {
        return repo.findByRole(role);
    }
}
