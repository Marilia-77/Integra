package com.integra.demo.dto;

public record NotaRequestDTO(
        double nota1,
        double nota2,
        double nota3,
        Long idAluno,
        Long idMateria
) {}