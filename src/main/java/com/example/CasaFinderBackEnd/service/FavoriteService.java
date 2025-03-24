package com.example.CasaFinderBackEnd.service;


import com.example.CasaFinderBackEnd.exception.ResourceNotFoundException;
import com.example.CasaFinderBackEnd.model.Favorite;
import com.example.CasaFinderBackEnd.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    public Favorite getFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found with id " + id));
    }

    public boolean existsById(Long id) {
        return favoriteRepository.existsById(id); // Verifica l'esistenza dell'ID
    }

    public void deleteFavorite(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Favorite not found with id " + id);
        }
        favoriteRepository.deleteById(id);
    }
}

