package br.com.autoflex.dto;

public record LoginRequest(
        String username, String password
) {}