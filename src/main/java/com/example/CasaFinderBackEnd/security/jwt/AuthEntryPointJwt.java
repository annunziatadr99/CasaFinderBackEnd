package com.example.CasaFinderBackEnd.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        System.err.println("Unauthorized error: " + authException.getMessage());

        // 🔥 Gestione specifica per gli ADMIN
        if (request.getRequestURI().startsWith("/api/admin")) {
            System.err.println("Tentativo di accesso non autorizzato agli endpoint ADMIN.");
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
