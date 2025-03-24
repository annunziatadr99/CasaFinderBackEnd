package com.example.CasaFinderBackEnd.service;


import com.example.CasaFinderBackEnd.exception.ResourceNotFoundException;
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
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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

    public void deleteUser(Long id) {
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
}
