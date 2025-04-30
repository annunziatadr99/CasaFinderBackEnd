package com.example.CasaFinderBackEnd.model;

import com.example.CasaFinderBackEnd.enumerated.PropertyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Entity
@Table(name = "properties")
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 100)
    private String titolo;

    @NotNull
    private double prezzo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PropertyType tipo;

    @NotNull
    @Size(min = 10, max = 100)
    private String indirizzo;

    @NotNull
    @Size(min = 10, max = 5000)
    private String descrizione;

    // Manteniamo imageUrl come opzionale per la retrocompatibilità
    @Column(nullable = true)
    private String imageUrl;

    // Nuova colonna obbligatoria per gestire più immagini
    @NotNull
    @ElementCollection
    private List<String> imageUrls;

    @NotNull
    private double superficie;

    @NotNull
    private int numeroBagni;

    @NotNull
    private int numeroBalconi;

    @NotNull
    @Size(min = 3, max = 50)
    private String zona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
