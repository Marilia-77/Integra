package com.integra.demo.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record FrequenciaRequestDTO(
        @DecimalMin(value = "0.0", message = "Frequência mínima é 0")
        @DecimalMax(value = "100.0", message = "Frequência máxima é 100")
        double porcentagem,

        @NotNull(message = "idAluno é obrigatório")
        Long idAluno,

        @NotNull(message = "idMateria é obrigatório")
        Long idMateria
) {}