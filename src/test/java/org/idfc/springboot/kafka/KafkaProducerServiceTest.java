package org.idfc.springboot.kafka;

import org.idfc.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    private KafkaTemplate<String, Employee> kafkaTemplate;
    private KafkaProducerService producerService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producerService = new KafkaProducerService();

        var kafkaTemplateField = KafkaProducerService.class.getDeclaredFields()[0];
        kafkaTemplateField.setAccessible(true);
        try {
            kafkaTemplateField.set(producerService, kafkaTemplate);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSendEmployeeMessage() {
        Employee employee = new Employee();
        employee.setName("Rohan Jaiswal");
        employee.setEmail("jrjaro2004@gmail.com");
        employee.setRole("Developer");

        producerService.send(employee);

        verify(kafkaTemplate, times(1)).send("employee.created", employee);
    }
}
