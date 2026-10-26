package com.integra.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integra.demo.entities.RecomendacaoIA;

@Repository
public interface RecomendacaoIARepository extends JpaRepository<RecomendacaoIA, Long> {
    
    @Query("SELECT r FROM RecomendacaoIA r WHERE r.ocorrencia.idOcorrencia = :idOcorrencia")
    Optional<RecomendacaoIA> buscarPorIdOcorrencia(@Param("idOcorrencia") Long idOcorrencia);
}