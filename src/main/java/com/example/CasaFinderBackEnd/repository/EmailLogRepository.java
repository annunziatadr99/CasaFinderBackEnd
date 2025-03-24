package com.example.CasaFinderBackEnd.repository;

import com.example.CasaFinderBackEnd.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    List<EmailLog> findByRecipient(String recipient);
}

