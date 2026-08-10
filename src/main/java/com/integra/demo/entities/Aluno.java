package com.integra.demo.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long idAluno;
	  
	  private String nomeAluno;

    @Column(unique = true, nullable = false)
	  private String emailAluno;
	  
	  private String senhaAluno;
	  
	  private String diagnosticoAluno;
	  
	   @ManyToOne
	    @JoinColumn(name = "id_turma")
	    private Turma turma;

	    @OneToMany(mappedBy = "aluno")
	    private List<Nota> notas;

	    @OneToMany(mappedBy = "aluno")
	    private List<Frequencia> frequencias;

	    @OneToMany(mappedBy = "aluno")
	    private List<Ocorrencia> ocorrencias;

	 
	  public Aluno(Long idAluno, String nomeAluno, String emailAluno, String senhaAluno, String diagnosticoAluno) {
		super();
		this.idAluno = idAluno;
		this.nomeAluno = nomeAluno;
		this.emailAluno = emailAluno;
		this.senhaAluno = senhaAluno;
		this.diagnosticoAluno = diagnosticoAluno;
	  }



	  public Long getIdAluno() {
		  return idAluno;
	  }



	  public void setIdAluno(Long idAluno) {
		  this.idAluno = idAluno;
	  }



	  public String getNomeAluno() {
		  return nomeAluno;
	  }



	  public void setNomeAluno(String nomeAluno) {
		  this.nomeAluno = nomeAluno;
	  }



	  public String getEmailAluno() {
		  return emailAluno;
	  }



	  public void setEmailAluno(String emailAluno) {
		  this.emailAluno = emailAluno;
	  }



	  public String getSenhaAluno() {
		  return senhaAluno;
	  }



	  public void setSenhaAluno(String senhaAluno) {
		  this.senhaAluno = senhaAluno;
	  }



	  public String getDiagnosticoAluno() {
		  return diagnosticoAluno;
	  }



	  public void setDiagnosticoAluno(String diagnosticoAluno) {
		  this.diagnosticoAluno = diagnosticoAluno;
	  }
	  
	  
	
}
