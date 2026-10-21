package com.integra.demo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integra.demo.entities.Ocorrencia;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

	List<Ocorrencia> findByAlunoIdAluno(Long idAluno);

	List<Ocorrencia> findByProfessorIdProfessor(Long idProfessor);

	List<Ocorrencia> findByCategoria(String categoria);

	List<Ocorrencia> findByDataBetween(LocalDate inicio, LocalDate fim);
}