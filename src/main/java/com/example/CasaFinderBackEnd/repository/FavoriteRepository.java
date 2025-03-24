package com.example.CasaFinderBackEnd.repository;

import com.example.CasaFinderBackEnd.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);

    List<Favorite> findByPropertyId(Long propertyId);
}
