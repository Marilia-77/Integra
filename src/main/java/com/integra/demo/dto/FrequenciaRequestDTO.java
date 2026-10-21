package com.integra.demo.dto;

public record FrequenciaRequestDTO(
        double porcentagem,
        Long idAluno,
        Long idMateria
) {}