package com.integra.demo.dto;

public record UsuarioRequestDTO(
        String email,
        String senha,
        String role
) {}