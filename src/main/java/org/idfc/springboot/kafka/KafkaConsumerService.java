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

@Slf4j
@Service
public class KafkaConsumerService {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "employee.created", groupId = "mail-group")
    public void consume(Employee employee) {
        sendWelcomeEmail(employee);
    }

    private void sendWelcomeEmail(Employee employee) {
        String email = employee.getEmail();
        String name = employee.getName();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("🎉 Welcome to the Team, " + name + "!");

            String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                    <h2 style="color: #2c3e50;">Welcome aboard, %s! 👋</h2>
                    <p style="font-size: 16px; color: #34495e;">
                        We're thrilled to have you join us as a <strong>%s</strong> at our company.
                        Your journey with us starts now, and we're excited to see the great things you'll accomplish.
                    </p>
                    <p style="font-size: 16px; color: #34495e;">
                        If you have any questions or need help settling in, don’t hesitate to reach out.
                    </p>
                    <br>
                    <p style="font-size: 16px; color: #34495e;">Cheers,</p>
                    <p style="font-size: 16px; color: #34495e;"><strong>The Team</strong></p>
                </div>
            </body>
            </html>
        """.formatted(name, employee.getRole());

            helper.setText(htmlContent, true);
            mailSender.send(message);

            log.info("Welcome email sent to: {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
        }
    }
}
