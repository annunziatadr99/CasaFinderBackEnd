package com.example.CasaFinderBackEnd.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "email_logs")
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipient; // Email destinatario

    @Column(nullable = false)
    private String subject; // Oggetto dell'email

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // Contenuto dell'email

    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt; // Data e ora di invio
}
