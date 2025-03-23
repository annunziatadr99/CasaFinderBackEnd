package com.example.CasaFinderBackEnd.model;


import com.example.CasaFinderBackEnd.enumerated.PropertyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Size(min = 10, max = 500)
    private String descrizione;

    @NotNull
    private String imageUrl;

    @NotNull
    private double superficie;

    @NotNull
    private int numeroBagni;

    @NotNull
    private int numeroBalconi;

    @NotNull
    @Size(min = 3, max = 50)
    private String zona;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER per caricare automaticamente l'utente
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
