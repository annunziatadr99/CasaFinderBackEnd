package com.example.CasaFinderBackEnd.controller;

import com.example.CasaFinderBackEnd.enumerated.PropertyType;
import com.example.CasaFinderBackEnd.model.User;
import com.example.CasaFinderBackEnd.model.Property;
import com.example.CasaFinderBackEnd.model.EmailLog;
import com.example.CasaFinderBackEnd.model.Role;
import com.example.CasaFinderBackEnd.payload.request.RegisterRequest;
import com.example.CasaFinderBackEnd.payload.response.MessageResponse;
import com.example.CasaFinderBackEnd.security.jwt.JwtUtils;
import com.example.CasaFinderBackEnd.service.UserService;
import com.example.CasaFinderBackEnd.service.PropertyService;
import com.example.CasaFinderBackEnd.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    // 🔥 Registrazione di un nuovo ADMIN, accessibile solo agli ADMIN esistenti
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // 🔥 Creazione esplicita del ruolo ADMIN
        User adminUser = new User(signUpRequest.getNome(), signUpRequest.getCognome(), signUpRequest.getTelefono(),
                signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail(), Role.ADMIN);

        userService.saveUser(adminUser);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    // 🔥 Recupero di tutti gli utenti, accessibile solo agli ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String token) {
        System.out.println("Token ricevuto: " + token);
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Errore nel recuperare gli utenti: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel recuperare gli utenti.");
        }
    }

    // 🔥 Eliminazione di un utente, accessibile solo agli ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!userService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore: Utente non trovato!");
            }

            userService.deleteUser(id);
            return ResponseEntity.ok("Utente eliminato con successo.");
        } catch (Exception e) {
            System.err.println("Errore durante l'eliminazione dell'utente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'utente.");
        }
    }

    // 🔥 Recupero di tutti gli annunci, accessibile solo agli ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/properties")
    public ResponseEntity<?> getAllProperties(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        try {
            List<Property> properties = propertyService.getAllProperties(PageRequest.of(page, size)).getContent();
            return ResponseEntity.ok(properties);
        } catch (Exception e) {
            System.err.println("Errore nel recuperare gli annunci: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel recuperare gli annunci.");
        }
    }

    // 🔥 Eliminazione di un annuncio, accessibile solo agli ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/properties/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        try {
            propertyService.deleteProperty(id);
            return ResponseEntity.ok("Annuncio eliminato con successo.");
        } catch (Exception e) {
            System.err.println("Errore durante l'eliminazione dell'annuncio: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione dell'annuncio.");
        }
    }

    // 🔥 Recupero di tutte le email, accessibile solo agli ADMIN
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/emails")
    public ResponseEntity<?> getAllEmails() {
        try {
            List<EmailLog> emails = emailService.getAllEmails();
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            System.err.println("Errore nel recuperare le email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nel recuperare le email.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/properties/{id}")
    public ResponseEntity<?> updatePropertyByAdmin(
            @PathVariable Long id,
            @RequestParam(value = "titolo", required = false) String titolo,
            @RequestParam(value = "prezzo", required = false) Double prezzo,
            @RequestParam(value = "tipo", required = false) PropertyType tipo,
            @RequestParam(value = "indirizzo", required = false) String indirizzo,
            @RequestParam(value = "zona", required = false) String zona,
            @RequestParam(value = "descrizione", required = false) String descrizione,
            @RequestParam(value = "superficie", required = false) Double superficie,
            @RequestParam(value = "numeroBagni", required = false) Integer numeroBagni,
            @RequestParam(value = "numeroBalconi", required = false) Integer numeroBalconi
    ) {
        try {
            System.out.println("Modifica annuncio ricevuta per ID: " + id);

            Property existingProperty = propertyService.getPropertyById(id);
            if (existingProperty == null) {
                System.err.println("Errore: Annuncio non trovato!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Errore: Annuncio non trovato!"));
            }

            System.out.println("Annuncio trovato: " + existingProperty.getTitolo());

            // 🔥 Verifica che la property sia effettivamente recuperata prima di modificarla
            System.out.println("Prima dell'aggiornamento: " + existingProperty);

            // 🔥 Aggiorniamo solo i campi forniti nella richiesta
            if (titolo != null) existingProperty.setTitolo(titolo);
            if (prezzo != null) existingProperty.setPrezzo(prezzo);
            if (tipo != null) existingProperty.setTipo(tipo);
            if (indirizzo != null) existingProperty.setIndirizzo(indirizzo);
            if (zona != null) existingProperty.setZona(zona);
            if (descrizione != null) existingProperty.setDescrizione(descrizione);
            if (superficie != null) existingProperty.setSuperficie(superficie);
            if (numeroBagni != null) existingProperty.setNumeroBagni(numeroBagni);
            if (numeroBalconi != null) existingProperty.setNumeroBalconi(numeroBalconi);

            // 🔥 Evitare `UnsupportedOperationException`
            if (existingProperty.getImageUrls() == null) {
                existingProperty.setImageUrls(new ArrayList<>());
            }

            System.out.println("Dopo l'aggiornamento: " + existingProperty);

            propertyService.saveProperty(existingProperty);

            System.out.println("Annuncio salvato con successo!");
            return ResponseEntity.ok(Map.of("message", "Annuncio modificato con successo!"));
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento dell'annuncio: " + e.getMessage());
            e.printStackTrace(); // 🔥 Stampa il log completo dell'errore
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Errore durante la modifica dell'annuncio."));
        }

    }
    }


