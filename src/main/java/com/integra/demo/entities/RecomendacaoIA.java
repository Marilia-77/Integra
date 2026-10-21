package com.integra.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recomendacao_ia")
public class RecomendacaoIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecomendacaoIA;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @OneToOne
    @JoinColumn(name = "id_ocorrencia")
    private Ocorrencia ocorrencia;

    public RecomendacaoIA() {}

    public RecomendacaoIA(Long idRecomendacaoIA, String texto, Ocorrencia ocorrencia) {
        this.idRecomendacaoIA = idRecomendacaoIA;
        this.texto = texto;
        this.ocorrencia = ocorrencia;
    }

    public Long getIdRecomendacaoIA() { return idRecomendacaoIA; }
    public void setIdRecomendacaoIA(Long idRecomendacaoIA) { this.idRecomendacaoIA = idRecomendacaoIA; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Ocorrencia getOcorrencia() { return ocorrencia; }
    public void setOcorrencia(Ocorrencia ocorrencia) { this.ocorrencia = ocorrencia; }
}