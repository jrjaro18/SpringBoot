package org.idfc.springboot.kafka;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.idfc.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ITemplateEngine templateEngine;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Mock
    private MimeMessage mimeMessage;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setName("Rohan Jaiswal");
        employee.setRole("developer");
        employee.setEmail("jrjaro2004@gmail.com");
    }

    @Test
    void testSendWelcomeEmail_success() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(templateEngine.process(eq("Welcome-Email"), any(Context.class)))
                .thenReturn("<html>Mock HTML</html>");

        kafkaConsumerService.consume(employee);

        verify(mailSender).send(any(MimeMessage.class));
    }
}