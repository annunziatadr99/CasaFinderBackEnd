package com.example.CasaFinderBackEnd.controller;

import com.example.CasaFinderBackEnd.exception.ResourceNotFoundException;
import com.example.CasaFinderBackEnd.model.Favorite;
import com.example.CasaFinderBackEnd.model.Property;
import com.example.CasaFinderBackEnd.model.User;
import com.example.CasaFinderBackEnd.service.FavoriteService;
import com.example.CasaFinderBackEnd.service.PropertyService;
import com.example.CasaFinderBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Favorite> addFavorite(@RequestBody Long propertyId) {
        try {
            // Recupera l'utente autenticato dal SecurityContext
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : null;

            if (username == null) {
                return ResponseEntity.badRequest().build(); // Se l'utente non è autenticato
            }

            // Recupera l'utente e la proprietà
            User user = userService.getUserByUsername(username);
            Property property = propertyService.getPropertyById(propertyId);

            // Crea un nuovo preferito
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setProperty(property);

            // Salva il preferito
            Favorite savedFavorite = favoriteService.saveFavorite(favorite);
            return ResponseEntity.ok(savedFavorite);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build(); // Errore interno del server
        }
    }

    @GetMapping
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        List<Favorite> favorites = favoriteService.getAllFavorites();
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Favorite> getFavoriteById(@PathVariable Long id) {
        Favorite favorite = favoriteService.getFavoriteById(id);
        return ResponseEntity.ok(favorite);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        try {
            // Controlla se l'entità esiste prima di eliminarla
            if (!favoriteService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Restituisce 404 se non esiste
            }

            favoriteService.deleteFavorite(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Errore 404
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Errore interno del server
        }
    }

}
