package com.integra.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integra.demo.entities.Frequencia;

@Repository
public interface FrequenciaRepository extends JpaRepository<Frequencia, Long> {

    // Navega pela propriedade 'aluno' até o 'idAluno'
    List<Frequencia> findByAlunoIdAluno(Long idAluno);

    // Navega por 'aluno.idAluno' e 'materia.idMateria'
    Optional<Frequencia> findByAlunoIdAlunoAndMateriaIdMateria(Long idAluno, Long idMateria);

    @Query("SELECT f FROM Frequencia f WHERE f.porcentagem < :porcentagem")
    List<Frequencia> findByPorcentagemLessThan(@Param("porcentagem") Double porcentagem);
}