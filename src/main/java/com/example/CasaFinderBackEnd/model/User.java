package com.example.CasaFinderBackEnd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String nome;

    @NotNull
    @Size(min = 2, max = 50)
    private String cognome;

    @NotNull
    @Size(min = 10, max = 15)
    @Column(unique = true)
    private String telefono;

    @NotNull
    @Size(min = 4, max = 20)
    @Column(unique = true)
    private String username;

    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(String nome, String cognome, String telefono, String username, String password, String email, Role role) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
