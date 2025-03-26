package com.example.CasaFinderBackEnd.repository;

import com.example.CasaFinderBackEnd.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    // Recupera tutte le email inviate da un determinato mittente
    List<EmailLog> findBySender(String sender);
}
