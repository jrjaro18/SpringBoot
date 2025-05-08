package org.idfc.springboot.repository;

import org.idfc.springboot.model.Employee;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        Employee emp = new Employee("1", "Amit", "Backend", 30000.99);
        repository.save(emp);

        var result = repository.findById("1");
        assertTrue(result.isPresent());
        assertEquals("Amit", result.get().getName());
    }

    @Test
    void testFindByRole() {
        repository.save(new Employee("2", "Neha", "DevOps",30000.99));
        repository.save(new Employee("3", "Rahul", "DevOps", 30000.99));
        repository.save(new Employee("4", "Meena", "QA", 30000.99));

        List<Employee> devOps = repository.findByRole("DevOps");
        assertEquals(2, devOps.size());
    }

    @Test
    void testDeleteById() {
        Employee emp = new Employee("5", "Sonal", "HR", 30000.99);
        repository.save(emp);
        repository.deleteById("5");

        assertFalse(repository.findById("5").isPresent());
    }

    @Test
    void testFindAll() {
        repository.save(new Employee("6", "Ravi", "Frontend", 30000.99));
        repository.save(new Employee("7", "Kiran", "Frontend", 30000.99));

        List<Employee> all = repository.findAll();
        assertTrue(all.size() >= 2);
    }
}
