package com.integra.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfessorRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nomeProfessor,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String emailProfessor,

        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
        String senhaProfessor,

        Long idMateria
) {}