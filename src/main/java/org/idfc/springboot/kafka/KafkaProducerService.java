package org.idfc.springboot.kafka;

import org.idfc.springboot.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, Employee> kafkaTemplate;

    public void send(Employee employee) {
        kafkaTemplate.send("employee.created", employee);
    }
}