package org.idfc.springboot.repository;

import org.idfc.springboot.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    List<Employee> findByRole(String role);
    List<Employee> findByEmail(String department);
}
