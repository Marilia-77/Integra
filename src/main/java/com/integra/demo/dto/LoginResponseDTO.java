package com.integra.demo.dto;

public record LoginResponseDTO(
        String token,
        String email,
        String role
) {}