package com.example.CasaFinderBackEnd.service;

import com.example.CasaFinderBackEnd.model.EmailLog;
import com.example.CasaFinderBackEnd.repository.EmailLogRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String sender, String recipient, String subject, String content, EmailLogRepository emailLogRepository) {
        try {
            // Invio dell'email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender); // Mittente
            helper.setTo(recipient); // Destinatario
            helper.setSubject(subject); // Oggetto
            helper.setText(content, true); // Contenuto HTML o testo

            mailSender.send(message);

            // Salvataggio nel database
            EmailLog emailLog = new EmailLog();
            emailLog.setSender(sender); // Mittente
            emailLog.setRecipient(recipient); // Destinatario
            emailLog.setSubject(subject); // Oggetto
            emailLog.setContent(content); // Contenuto
            emailLog.setSentAt(new Date()); // Data e ora

            emailLogRepository.save(emailLog); // Salva nel database
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'invio dell'email: " + e.getMessage());
        }
    }
}
