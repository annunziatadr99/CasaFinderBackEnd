package com.example.CasaFinderBackEnd.service;


import com.example.CasaFinderBackEnd.model.EmailLog;
import com.example.CasaFinderBackEnd.repository.EmailLogRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.Date;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository; // Iniettiamo il repository

    public EmailService(JavaMailSender mailSender, EmailLogRepository emailLogRepository) {
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
    }

    public void sendEmail(String recipient, String subject, String content) {
        try {
            // Invio dell'email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            System.out.println("Email inviata con successo!");

            // Salvataggio nel database
            EmailLog emailLog = new EmailLog();
            emailLog.setRecipient(recipient);
            emailLog.setSubject(subject);
            emailLog.setContent(content);
            emailLog.setSentAt(Date.from(Instant.now()));

            emailLogRepository.save(emailLog); // Salva l'email nel database
            System.out.println("Email registrata nel database!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nell'invio dell'email: " + e.getMessage());
        }
    }
}

