package com.integra.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integra.demo.entities.Aluno;
import com.integra.demo.entities.Turma;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByEmailAlunoAndSenhaAluno(String emailAluno, String senhaAluno);

    List<Aluno> findByTurma(Turma turma);
}