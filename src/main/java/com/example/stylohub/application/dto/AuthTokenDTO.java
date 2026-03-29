package com.example.stylohub.application.dto;

public record AuthTokenDTO(
        String accessToken,
        String tokenType,
        long expiresIn,
        String userId,
        String username,
        String email
) {
    public AuthTokenDTO(String accessToken, long expiresIn, String userId, String username, String email) {
        this(accessToken, "Bearer", expiresIn, userId, username, email);
    }
}
