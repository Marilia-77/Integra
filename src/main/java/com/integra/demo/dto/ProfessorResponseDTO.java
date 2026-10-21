package com.integra.demo.dto;

public record ProfessorResponseDTO(
        Long idProfessor,
        String nomeProfessor,
        String emailProfessor,
        Long idMateria
) {}