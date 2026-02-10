package br.com.autoflex.dto;

public record TokenResponse(
        String accessToken,
        String tokenType,
        Long expiresInSeconds
) {}
