package com.example.CasaFinderBackEnd.service;

import com.example.CasaFinderBackEnd.exception.ResourceNotFoundException;
import com.example.CasaFinderBackEnd.model.Role;
import com.example.CasaFinderBackEnd.model.User;
import com.example.CasaFinderBackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        } else {
            try {
                user.setRole(Role.valueOf(user.getRole().toString())); // 🔥 Converte la stringa in ENUM correttamente
            } catch (IllegalArgumentException e) {
                System.err.println("Errore: Ruolo non valido! " + e.getMessage());
                user.setRole(Role.USER); // Se il ruolo non è valido, assegniamo USER
            }
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setNome(userDetails.getNome());
        user.setCognome(userDetails.getCognome());
        user.setTelefono(userDetails.getTelefono());
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }
        return userRepository.save(user);
    }

    // 🔥 Metodo migliorato per eliminare un utente e gestire eventuali errori
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Errore: Utente con ID " + id + " non trovato!");
        }

        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username and password"));
    }
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

}
