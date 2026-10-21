package com.integra.demo.dto;

public record AlunoRequestDTO(
        String nomeAluno,
        String emailAluno,
        String senhaAluno,
        String diagnosticoAluno,
        Long idTurma
) {}