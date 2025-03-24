package com.example.CasaFinderBackEnd.service;


import com.example.CasaFinderBackEnd.enumerated.PropertyType;
import com.example.CasaFinderBackEnd.exception.ResourceNotFoundException;
import com.example.CasaFinderBackEnd.model.Property;
import com.example.CasaFinderBackEnd.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> findByUserId(Long userId) {
        return propertyRepository.findByUserId(userId);
    }
    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Page<Property> getAllProperties(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));
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
        property.setImageUrl(propertyDetails.getImageUrl());
        return propertyRepository.save(property);
    }


    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    public List<Property> searchProperties(Double prezzo, String titolo, PropertyType tipo, String indirizzo, String descrizione, String zona) {
        if (prezzo != null) {
            return propertyRepository.findByPrezzo(prezzo);
        }
        if (titolo != null) {
            return propertyRepository.findByTitoloContainingIgnoreCase(titolo);
        }
        if (tipo != null) {
            return propertyRepository.findByTipo(tipo);
        }
        if (indirizzo != null) {
            return propertyRepository.findByIndirizzoContainingIgnoreCase(indirizzo);
        }
        if (descrizione != null) {
            return propertyRepository.findByDescrizioneContainingIgnoreCase(descrizione);
        }
        if (zona != null) { // Filtro per zona
            return propertyRepository.findByZonaContainingIgnoreCase(zona);
        }
        return propertyRepository.findAll();
    }

}
