package com.example.CasaFinderBackEnd.controller;

import com.example.CasaFinderBackEnd.enumerated.PropertyType;
import com.example.CasaFinderBackEnd.model.Property;
import com.example.CasaFinderBackEnd.model.User;
import com.example.CasaFinderBackEnd.service.CloudinaryService;
import com.example.CasaFinderBackEnd.service.PropertyService;
import com.example.CasaFinderBackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserService userService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Property> createProperty(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("userId") Long userId,
            @RequestParam("titolo") String titolo,
            @RequestParam("prezzo") Double prezzo,
            @RequestParam("tipo") PropertyType tipo,
            @RequestParam("indirizzo") String indirizzo,
            @RequestParam("descrizione") String descrizione,
            @RequestParam("superficie") Double superficie,
            @RequestParam("numeroBagni") int numeroBagni,
            @RequestParam("numeroBalconi") int numeroBalconi,
            @RequestParam("zona") String zona
    ) throws IOException {
        try {
            User user = userService.getUserById(userId);
            Property property = new Property();
            property.setUser(user);
            property.setTitolo(titolo);
            property.setPrezzo(prezzo);
            property.setTipo(tipo);
            property.setIndirizzo(indirizzo);
            property.setDescrizione(descrizione);
            property.setSuperficie(superficie);
            property.setNumeroBagni(numeroBagni);
            property.setNumeroBalconi(numeroBalconi);
            property.setZona(zona);

            // Upload multiple images
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                String imageUrl = cloudinaryService.uploadImage(file);
                imageUrls.add(imageUrl);
            }
            property.setImageUrls(imageUrls);

            // Manteniamo compatibilità con imageUrl
            if (!imageUrls.isEmpty()) {
                property.setImageUrl(imageUrls.get(0));
            }

            Property savedProperty = propertyService.saveProperty(property);
            return ResponseEntity.ok(savedProperty);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping
    public ResponseEntity<Page<Property>> getAllProperties(Pageable pageable) {
        Page<Property> properties = propertyService.getAllProperties(pageable);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(
            @PathVariable Long id,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "titolo", required = false) String titolo,
            @RequestParam(value = "prezzo", required = false) Double prezzo,
            @RequestParam(value = "tipo", required = false) PropertyType tipo,
            @RequestParam(value = "indirizzo", required = false) String indirizzo,
            @RequestParam(value = "descrizione", required = false) String descrizione,
            @RequestParam(value = "superficie", required = false) Double superficie,
            @RequestParam(value = "numeroBagni", required = false) Integer numeroBagni,
            @RequestParam(value = "numeroBalconi", required = false) Integer numeroBalconi,
            @RequestParam(value = "zona", required = false) String zona // Parametro opzionale
    ) throws IOException {
        // Recupera l'annuncio esistente dal database
        Property property = propertyService.getPropertyById(id);

        // Aggiorna solo i campi forniti nella richiesta
        if (titolo != null) {
            property.setTitolo(titolo);
        }
        if (prezzo != null) {
            property.setPrezzo(prezzo);
        }
        if (tipo != null) {
            property.setTipo(tipo);
        }
        if (indirizzo != null) {
            property.setIndirizzo(indirizzo);
        }
        if (descrizione != null) {
            property.setDescrizione(descrizione);
        }
        if (superficie != null) {
            property.setSuperficie(superficie);
        }
        if (numeroBagni != null) {
            property.setNumeroBagni(numeroBagni);
        }
        if (numeroBalconi != null) {
            property.setNumeroBalconi(numeroBalconi);
        }
        if (zona != null) {
            property.setZona(zona);
        }

        // Gestione dell'upload multiplo di immagini
        if (files != null && !files.isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                String imageUrl = cloudinaryService.uploadImage(file);
                imageUrls.add(imageUrl);
            }
            property.setImageUrls(imageUrls);

            if (!imageUrls.isEmpty()) {
                property.setImageUrl(imageUrls.get(0)); // Mantiene la retrocompatibilità
            }
        }

        // Salva le modifiche
        Property updatedProperty = propertyService.saveProperty(property);

        return ResponseEntity.ok(updatedProperty);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint per ricerca combinata
    @GetMapping("/search")
    public ResponseEntity<List<Property>> searchProperties(
            @RequestParam(required = false) Double prezzo,
            @RequestParam(required = false) String titolo,
            @RequestParam(required = false) PropertyType tipo,
            @RequestParam(required = false) String indirizzo,
            @RequestParam(required = false) String descrizione,
            @RequestParam(required = false) String zona
    ) {
        List<Property> properties = propertyService.searchProperties(prezzo, titolo, tipo, indirizzo, descrizione, zona);
        return ResponseEntity.ok(properties);
    }

}
