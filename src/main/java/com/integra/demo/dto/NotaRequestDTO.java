package com.integra.demo.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record NotaRequestDTO(
        @DecimalMin(value = "0.0", message = "Nota mínima é 0")
        @DecimalMax(value = "10.0", message = "Nota máxima é 10")
        double nota1,

        @DecimalMin("0.0") @DecimalMax("10.0")
        double nota2,

        @DecimalMin("0.0") @DecimalMax("10.0")
        double nota3,

        @NotNull(message = "idAluno é obrigatório")
        Long idAluno,

        @NotNull(message = "idMateria é obrigatório")
        Long idMateria
) {}