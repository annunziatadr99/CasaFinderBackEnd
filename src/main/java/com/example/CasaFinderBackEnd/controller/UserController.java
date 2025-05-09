package com.example.CasaFinderBackEnd.controller;

import com.example.CasaFinderBackEnd.exception.ResourceNotFoundException;
import com.example.CasaFinderBackEnd.model.Favorite;
import com.example.CasaFinderBackEnd.model.Property;
import com.example.CasaFinderBackEnd.model.Role;
import com.example.CasaFinderBackEnd.model.User;
import com.example.CasaFinderBackEnd.payload.request.LoginRequest;
import com.example.CasaFinderBackEnd.payload.request.RegisterRequest;
import com.example.CasaFinderBackEnd.payload.response.JwtResponse;
import com.example.CasaFinderBackEnd.payload.response.MessageResponse;
import com.example.CasaFinderBackEnd.payload.response.UserResponse;
import com.example.CasaFinderBackEnd.security.jwt.JwtUtils;
import com.example.CasaFinderBackEnd.security.services.UserDetailsImpl;
import com.example.CasaFinderBackEnd.service.FavoriteService;
import com.example.CasaFinderBackEnd.service.PropertyService;
import com.example.CasaFinderBackEnd.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Effettua l'autenticazione con i dettagli forniti
            System.out.println("Login attempt for user: " + loginRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // Imposta il contesto di sicurezza
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            // Recupera i dettagli dell'utente autenticato
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userService.getUserById(userDetails.getId()); // Recupera l'utente dal database
            System.out.println("Authentication successful for user: " + userDetails.getUsername());

            // Ritorna il JWT e l'id dell'utente, includendo il ruolo
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    user.getId(), // Restituisce userId
                    user.getUsername(),
                    user.getEmail(),
                    user.getNome(),
                    user.getRole().name() // Aggiunge il ruolo nella risposta
            ));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore nel server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Credenziali non valide o errore server."));
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getNome(), user.getCognome(), user.getUsername(), user.getEmail(), user.getTelefono()));
    }

    @GetMapping("/announcements")
    public ResponseEntity<?> getUserAnnouncements(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Property> userAnnouncements = propertyService.findByUserId(userDetails.getId());
        return ResponseEntity.ok(userAnnouncements);
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> getUserFavorites(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Favorite> favoriteAnnouncements = favoriteService.findByUserId(userDetails.getId());
        return ResponseEntity.ok(favoriteAnnouncements);
    }

    @PostMapping("/registrazione")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

// Crea nuovo account utente
        User user = new User(signUpRequest.getNome(), signUpRequest.getCognome(), signUpRequest.getTelefono(),
                signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail(), Role.USER);


        userService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);

        if (property == null) {
            throw new ResourceNotFoundException("Proprietà non trovata con ID " + id);
        }

        return ResponseEntity.ok(property);
    }

}
