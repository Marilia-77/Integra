package com.integra.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integra.demo.entities.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {

}