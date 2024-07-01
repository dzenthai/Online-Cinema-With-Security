package org.online.cinema.security.service.subscription;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class SubscriptionEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    public void sendSubscriptionEmail(String to, String subscriptionLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "Subscription Confirmation";
        String content = buildEmailContent(subscriptionLink);

        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(content, true);

        log.info("Sending an email: email={}", to);
        mailSender.send(message);
    }

    private String buildEmailContent(String subscriptionLink) {
        Context context = new Context();
        context.setVariable("subscriptionLink", subscriptionLink);

        return templateEngine.process("subscription-email", context);
    }
}
