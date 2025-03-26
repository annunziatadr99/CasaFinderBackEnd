package com.example.CasaFinderBackEnd.repository;


import com.example.CasaFinderBackEnd.enumerated.PropertyType;
import com.example.CasaFinderBackEnd.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByUserId(Long userId);

    List<Property> findByTitoloContainingIgnoreCase(String titolo);

    List<Property> findByPrezzo(double prezzo);

    List<Property> findByTipo(PropertyType tipo);

    List<Property> findByIndirizzoContainingIgnoreCase(String indirizzo);

    List<Property> findByDescrizioneContainingIgnoreCase(String descrizione);

    List<Property> findByZonaContainingIgnoreCase(String zona);

}
