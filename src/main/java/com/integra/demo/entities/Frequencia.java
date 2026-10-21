package com.integra.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "frequencia")
public class Frequencia {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idFrequencia;

	    private double porcentagem;

	    @ManyToOne
	    @JoinColumn(name = "id_aluno")
	    private Aluno aluno;

	    @ManyToOne
	    @JoinColumn(name = "id_materia")
	    private Materia materia;

	    public Frequencia() {
	    	
	    }
		public Frequencia(Long idFrequencia, double porcentagem, Aluno aluno, Materia materia) {
			super();
			this.idFrequencia = idFrequencia;
			this.porcentagem = porcentagem;
			this.aluno = aluno;
			this.materia = materia;
		}

		public Long getIdFrequencia() {
			return idFrequencia;
		}

		public void setIdFrequencia(Long idFrequencia) {
			this.idFrequencia = idFrequencia;
		}

		public double getPorcentagem() {
			return porcentagem;
		}

		public void setPorcentagem(double porcentagem) {
			this.porcentagem = porcentagem;
		}

		public Aluno getAluno() {
			return aluno;
		}

		public void setAluno(Aluno aluno) {
			this.aluno = aluno;
		}

		public Materia getMateria() {
			return materia;
		}

		public void setMateria(Materia materia) {
			this.materia = materia;
		}

	    

}
