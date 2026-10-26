package com.integra.demo.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTurma;

    private LocalDate ano;
    private Double grau;

    @JsonIgnore
    @OneToMany(mappedBy = "turma")
    private List<Aluno> alunos;

    @ManyToMany
    @JoinTable(
        name = "turma_professores",
        joinColumns = @JoinColumn(name = "id_turma"),
        inverseJoinColumns = @JoinColumn(name = "id_professor")
    )
    @JsonIgnoreProperties("turmas")
    private List<Professor> professores = new ArrayList<>();

    public Turma() {}

    public Long getIdTurma() { return idTurma; }
    public void setIdTurma(Long idTurma) { this.idTurma = idTurma; }

    public LocalDate getAno() { return ano; }
    public void setAno(LocalDate ano) { this.ano = ano; }

    public Double getGrau() { return grau; }
    public void setGrau(Double grau) { this.grau = grau; }

    public List<Aluno> getAlunos() { return alunos; }
    public void setAlunos(List<Aluno> alunos) { this.alunos = alunos; }

    public List<Professor> getProfessores() {
        if (professores == null) professores = new ArrayList<>();
        return professores;
    }
    public void setProfessores(List<Professor> professores) { this.professores = professores; }
}