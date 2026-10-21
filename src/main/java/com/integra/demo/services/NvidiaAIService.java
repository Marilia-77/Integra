package com.integra.demo.services;

import com.integra.demo.dto.NvidiaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NvidiaAIService {

    private static final Logger log = LoggerFactory.getLogger(NvidiaAIService.class);

    @Value("${nvidia.api.url}")
    private String apiUrl;

    @Value("${nvidia.api.key}")
    private String apiKey;

    @Value("${nvidia.api.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String gerarRecomendacao(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("NVIDIA_API_KEY não configurada no .env");
        }
        if (!apiKey.startsWith("nvapi-")) {
            throw new IllegalStateException("NVIDIA_API_KEY inválida (deve começar com 'nvapi-')");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            NvidiaDTO.Request requestBody = new NvidiaDTO.Request(
                    model,
                    List.of(new NvidiaDTO.Message("user", prompt))
            );

            HttpEntity<NvidiaDTO.Request> entity = new HttpEntity<>(requestBody, headers);

            NvidiaDTO.Response response = restTemplate.postForObject(
                    apiUrl, entity, NvidiaDTO.Response.class);

            if (response != null
                    && response.choices != null
                    && !response.choices.isEmpty()
                    && response.choices.get(0).message != null) {
                return response.choices.get(0).message.content;
            }
            return "Não foi possível gerar uma recomendação no momento.";

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("NVIDIA retornou {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("NVIDIA " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Erro ao chamar NVIDIA", e);
            throw new RuntimeException("Falha ao chamar NVIDIA: " + e.getMessage(), e);
        }
    }
}