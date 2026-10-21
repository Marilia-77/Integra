package com.integra.demo.dto;

public record FrequenciaResponseDTO(
        Long idFrequencia,
        double porcentagem,
        Long idAluno,
        Long idMateria
) {}