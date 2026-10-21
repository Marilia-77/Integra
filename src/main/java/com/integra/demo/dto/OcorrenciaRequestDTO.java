package com.integra.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OcorrenciaRequestDTO(
        @NotBlank(message = "Título é obrigatório")
        String titulo,

        @NotNull(message = "Data é obrigatória")
        LocalDate data,

        String descricao,

        @NotBlank(message = "Categoria é obrigatória")
        String categoria,

        @NotNull(message = "idProfessor é obrigatório")
        Long idProfessor,

        @NotNull(message = "idAluno é obrigatório")
        Long idAluno
) {}