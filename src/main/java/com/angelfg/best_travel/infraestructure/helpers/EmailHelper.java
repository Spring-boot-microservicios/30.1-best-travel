package com.angelfg.best_travel.infraestructure.helpers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
@Slf4j
public class EmailHelper {

    private final Path TEMPLATE_PATH = Paths.get("src/main/resources/email/email_template.html");

    private final JavaMailSender mailSender;

    public void sendMail(String to, String name, String product) {
        // SimpleMessage => texto en duro
        MimeMessage message = mailSender.createMimeMessage();
        String htmlContent = this.readHTMLTemplate(name, product);

        try {
            message.setFrom(new InternetAddress("angelfgdeveloper@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setContent(htmlContent, MediaType.TEXT_HTML_VALUE);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("Error to send mail", e);
        }

    }

    private String readHTMLTemplate(String name, String product) {

        try (Stream<String> lines = Files.lines(TEMPLATE_PATH)) {
            String html = lines.collect(Collectors.joining()); // va a concatenar

            return html.replace("{name}", name).replace("{product}", product);
        } catch (IOException e) {
            log.error("Cant read html template", e);
            throw new RuntimeException();
        }

    }

}
