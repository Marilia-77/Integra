package com.integra.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integra.demo.entities.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByEmailProfessorAndSenhaProfessor(String email, String senha);

    @EntityGraph(attributePaths = "materia")
    Optional<Professor> findByEmailProfessor(String email);
}