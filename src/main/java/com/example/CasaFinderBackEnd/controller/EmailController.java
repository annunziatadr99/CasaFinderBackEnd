package com.example.CasaFinderBackEnd.controller;

import com.example.CasaFinderBackEnd.model.EmailLog;
import com.example.CasaFinderBackEnd.payload.request.EmailRequest;
import com.example.CasaFinderBackEnd.repository.EmailLogRepository;
import com.example.CasaFinderBackEnd.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailLogRepository emailLogRepository;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest.getRecipient(), emailRequest.getSubject(), emailRequest.getContent());
        return ResponseEntity.ok("Email inviata con successo!");
    }

    @GetMapping("/logs")
    public ResponseEntity<List<EmailLog>> getEmailLogs() {
        List<EmailLog> logs = emailLogRepository.findAll();
        return ResponseEntity.ok(logs);
    }
}

