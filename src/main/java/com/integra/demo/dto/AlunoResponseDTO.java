package com.integra.demo.dto;

public record AlunoResponseDTO(
        Long idAluno,
        String nomeAluno,
        String emailAluno,
        String diagnosticoAluno,
        Long idTurma
) {}