package com.example.CasaFinderBackEnd.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String telefono;

    public UserResponse(Long id, String nome, String cognome, String username, String email, String telefono) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.telefono = telefono;
    }
}


