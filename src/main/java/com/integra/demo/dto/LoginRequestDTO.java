package com.integra.demo.dto;

public record LoginRequestDTO(
        String email,
        String senha
) {}