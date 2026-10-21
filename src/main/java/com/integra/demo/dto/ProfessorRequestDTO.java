package com.integra.demo.dto;

public record ProfessorRequestDTO(
        String nomeProfessor,
        String emailProfessor,
        String senhaProfessor,
        Long idMateria
) {}