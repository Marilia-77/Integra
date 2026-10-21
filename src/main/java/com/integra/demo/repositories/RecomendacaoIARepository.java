package com.integra.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integra.demo.entities.RecomendacaoIA;
@Repository
public interface RecomendacaoIARepository extends JpaRepository<RecomendacaoIA, Long> {

}
