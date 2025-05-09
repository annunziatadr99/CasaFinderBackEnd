package com.example.CasaFinderBackEnd.controller;

import com.example.CasaFinderBackEnd.model.EmailLog;
import com.example.CasaFinderBackEnd.payload.request.EmailRequest;
import com.example.CasaFinderBackEnd.repository.EmailLogRepository;
import com.example.CasaFinderBackEnd.security.jwt.JwtUtils;
import com.example.CasaFinderBackEnd.service.EmailService;
import com.example.CasaFinderBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailLogRepository emailLogRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    // **Metodo POST**: Creazione di un'email (per la pagina DettaglioAnnuncio)
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest, @RequestHeader("Authorization") String token) {
        try {
            // Rimuove il prefisso "Bearer " dal token
            String jwtToken = token.substring(7);

            // Estrae l'username dal token
            String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

            // Ottieni l'email del mittente
            String senderEmail = userService.getUserByUsername(username).getEmail();

            // Invio dell'email e salvataggio nel database
            emailService.sendEmail(
                    senderEmail, // Mittente
                    emailRequest.getRecipient(), // Destinatario
                    emailRequest.getSubject(), // Oggetto
                    emailRequest.getContent() // Contenuto
            );

            return ResponseEntity.ok("Email inviata con successo!");
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
            return ResponseEntity.status(500).body("Errore durante l'invio dell'email.");
        }
    }

    // **Metodo GET**: Recupero delle email inviate (per la pagina AreaPersonale)
    @GetMapping("/logs")
    public ResponseEntity<?> getEmailLogs(@RequestHeader("Authorization") String token) {
        try {
            // Rimuove il prefisso "Bearer " dal token
            String jwtToken = token.substring(7);

            // Estrae l'username dal token
            String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

            // Ottieni l'email dell'utente autenticato
            String userEmail = userService.getUserByUsername(username).getEmail();

            // Recupera le email inviate dall'utente
            List<EmailLog> userLogs = emailLogRepository.findBySender(userEmail);

            return ResponseEntity.ok(userLogs);
        } catch (Exception e) {
            System.err.println("Errore nel recuperare i log delle email: " + e.getMessage());
            return ResponseEntity.status(500).body("Errore nel recuperare i log delle email.");
        }
    }
}
