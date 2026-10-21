package com.integra.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integra.demo.entities.Nota;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {

	List<Nota> findByAlunoIdAluno(Long idAluno);

	Optional<Nota> findByAlunoIdAlunoAndMateriaIdMateria(Long idAluno, Long idMateria);

	List<Nota> findByMediaLessThan(Double media);
}