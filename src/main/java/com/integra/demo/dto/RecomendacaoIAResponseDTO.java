package com.integra.demo.dto;

public record RecomendacaoIAResponseDTO(
        Long idRecomendacaoIA,
        String texto,
        Long idOcorrencia
) {}