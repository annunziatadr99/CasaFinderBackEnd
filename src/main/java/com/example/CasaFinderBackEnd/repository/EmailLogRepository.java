package com.example.CasaFinderBackEnd.repository;

import com.example.CasaFinderBackEnd.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    // Recupera tutte le email inviate da un determinato mittente
    List<EmailLog> findBySender(String sender);

    // Recupera tutte le email presenti nel sistema
    List<EmailLog> findAll();
}
