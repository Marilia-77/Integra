package com.integra.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "E-mail é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha
) {}