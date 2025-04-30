package com.example.CasaFinderBackEnd.service;

import com.example.CasaFinderBackEnd.enumerated.PropertyType;
import com.example.CasaFinderBackEnd.exception.ResourceNotFoundException;
import com.example.CasaFinderBackEnd.model.Property;
import com.example.CasaFinderBackEnd.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> findByUserId(Long userId) {
        return propertyRepository.findByUserId(userId);
    }

    public Property saveProperty(Property property) {
        // Se imageUrls è null, inizializzarlo con un array vuoto per evitare errori
        if (property.getImageUrls() == null) {
            property.setImageUrls(new ArrayList<>());
        }

        // Se imageUrl è presente ma imageUrls è vuoto, aggiungerlo a imageUrls
        if (property.getImageUrl() != null && property.getImageUrls().isEmpty()) {
            property.setImageUrls(List.of(property.getImageUrl()));
        }

        // Aggiorniamo anche imageUrl per retrocompatibilità
        if (!property.getImageUrls().isEmpty()) {
            property.setImageUrl(property.getImageUrls().get(0)); // Usa la prima immagine
        }

        return propertyRepository.save(property);
    }


    public Page<Property> getAllProperties(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    public Property getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));

        // Se la property ha solo imageUrl, convertiamola in un array per imageUrls
        if (property.getImageUrls() == null || property.getImageUrls().isEmpty()) {
            property.setImageUrls(List.of(property.getImageUrl()));
        }

        return property;
    }

    public Property updateProperty(Long id, Property propertyDetails) {
        Property property = getPropertyById(id);
        property.setTitolo(propertyDetails.getTitolo());
        property.setPrezzo(propertyDetails.getPrezzo());
        property.setTipo(propertyDetails.getTipo());
        property.setIndirizzo(propertyDetails.getIndirizzo());
        property.setDescrizione(propertyDetails.getDescrizione());
        property.setSuperficie(propertyDetails.getSuperficie());
        property.setNumeroBagni(propertyDetails.getNumeroBagni());
        property.setNumeroBalconi(propertyDetails.getNumeroBalconi());
        property.setZona(propertyDetails.getZona());

        // Aggiorniamo le immagini
        property.setImageUrls(propertyDetails.getImageUrls());

        if (property.getImageUrls() != null && !property.getImageUrls().isEmpty()) {
            property.setImageUrl(property.getImageUrls().get(0)); // Manteniamo retrocompatibilità
        }

        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    // Metodo per la ricerca combinata
    public List<Property> searchProperties(Double prezzo, String titolo, PropertyType tipo, String indirizzo, String descrizione, String zona) {
        List<Property> allProperties = propertyRepository.findAll();

        return allProperties.stream()
                .filter(p -> prezzo == null || p.getPrezzo() <= prezzo)
                .filter(p -> titolo == null || p.getTitolo().toLowerCase().contains(titolo.toLowerCase()))
                .filter(p -> tipo == null || p.getTipo().equals(tipo))
                .filter(p -> indirizzo == null || p.getIndirizzo().toLowerCase().contains(indirizzo.toLowerCase()))
                .filter(p -> descrizione == null || p.getDescrizione().toLowerCase().contains(descrizione.toLowerCase()))
                .filter(p -> zona == null || p.getZona().toLowerCase().contains(zona.toLowerCase()))
                .collect(Collectors.toList());
    }
}
