package org.idfc.springboot.kafka;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.idfc.springboot.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class KafkaConsumerService {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(
            topics = "employee.created",
            groupId = "mail-group"
    )
    public void consume(Employee employee) {
        sendWelcomeEmail(employee);
    }

    @Autowired
    private ITemplateEngine templateEngine;

    private void sendWelcomeEmail(Employee employee) {
        String email = employee.getEmail();
        String name = employee.getName();

        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("role", employee.getRole());

            String htmlContent = templateEngine.process("Welcome-Email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("🎉 Welcome to the Team, " + name + "!");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Welcome email sent to: {}", email);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
        }
    }
}
