package com.integra.demo.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ocorrencia")
public class Ocorrencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOcorrencia;

	private String titulo;

	private LocalDate data;

	private String descricao;

	private String categoria;

	@ManyToOne
	@JoinColumn(name = "id_professor")
	private Professor professor;

	@ManyToOne
	@JoinColumn(name = "id_aluno")
	private Aluno aluno;

	@JsonIgnore
	@OneToOne(mappedBy = "ocorrencia")
	private RecomendacaoIA recomendacaoIA;

	public Ocorrencia() {
	}

	public Long getIdOcorrencia() {
		return idOcorrencia;
	}

	public void setIdOcorrencia(Long idOcorrencia) {
		this.idOcorrencia = idOcorrencia;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public RecomendacaoIA getRecomendacao() {
		return recomendacaoIA;
	}

	public void setRecomendacao(RecomendacaoIA recomendacaoIA) {
		this.recomendacaoIA = recomendacaoIA;
	}
}