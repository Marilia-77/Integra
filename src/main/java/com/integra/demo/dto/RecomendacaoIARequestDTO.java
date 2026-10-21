package com.integra.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecomendacaoIARequestDTO(
        @NotBlank(message = "Texto é obrigatório")
        String texto,

        @NotNull(message = "idOcorrencia é obrigatório")
        Long idOcorrencia
) {}