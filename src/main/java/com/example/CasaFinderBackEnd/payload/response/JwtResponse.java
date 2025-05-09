package com.example.CasaFinderBackEnd.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String nome;
    private String role; // Aggiunto il campo ruolo

    public JwtResponse(String accessToken, Long id, String username, String email, String nome, String role) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.role = role;
    }
}
