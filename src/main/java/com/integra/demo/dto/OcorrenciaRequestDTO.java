package com.integra.demo.dto;

import java.time.LocalDate;

public record OcorrenciaRequestDTO(
        String titulo,
        LocalDate data,
        String descricao,
        String categoria,
        Long idProfessor,
        Long idAluno
) {}