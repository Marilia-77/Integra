package com.integra.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank @Email(message = "E-mail inválido")
        String email,

        @NotBlank @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String senha,

        String role
) {}