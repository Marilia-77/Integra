package com.integra.demo.dto;

public record UsuarioResponseDTO(
        Long idUsuario,
        String email,
        String role
) {}