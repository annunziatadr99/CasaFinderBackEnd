package com.example.CasaFinderBackEnd.service;

import com.example.CasaFinderBackEnd.model.EmailLog;
import com.example.CasaFinderBackEnd.repository.EmailLogRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository; // Repository già integrato

    public EmailService(JavaMailSender mailSender, EmailLogRepository emailLogRepository) {
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository; // Inizializzazione repository
    }

    public void sendEmail(String sender, String recipient, String subject, String content)
    {
        try {
            // Invio dell'email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);

            // Salvataggio nel database
            EmailLog emailLog = new EmailLog();
            emailLog.setSender(sender);
            emailLog.setRecipient(recipient);
            emailLog.setSubject(subject);
            emailLog.setContent(content);
            emailLog.setSentAt(new Date());

            emailLogRepository.save(emailLog);
        } catch (Exception e) {
            throw new RuntimeException("Errore nell'invio dell'email: " + e.getMessage());
        }
    }

    // **📌 Recupera tutte le email inviate**
    public List<EmailLog> getAllEmails() {
        return emailLogRepository.findAll();
    }
}
