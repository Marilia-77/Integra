package com.integra.demo.dto;

public record NotaResponseDTO(
        Long idNota,
        double nota1,
        double nota2,
        double nota3,
        double media,
        Long idAluno,
        Long idMateria
) {}