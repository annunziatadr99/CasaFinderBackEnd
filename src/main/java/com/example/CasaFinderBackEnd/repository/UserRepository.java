package com.example.CasaFinderBackEnd.repository;

import com.example.CasaFinderBackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u") // 🔥 Recupera tutti gli utenti con ID, Nome, Cognome, Email, Password, Telefono, Username, Ruolo
    List<User> findAllUsers();

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsernameAndPassword(String username, String password);

    // 🔥 Metodo aggiunto per verificare se un utente esiste prima di eliminarlo
    boolean existsById(Long id);
}
