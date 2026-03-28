package com.example.stylohub.application.dto;

public record AuthTokenDTO(
        String accessToken,
        String tokenType,
        long expiresIn,
        String username,
        String email
) {
    public AuthTokenDTO(String accessToken, long expiresIn, String username, String email) {
        this(accessToken, "Bearer", expiresIn, username, email);
    }
}
