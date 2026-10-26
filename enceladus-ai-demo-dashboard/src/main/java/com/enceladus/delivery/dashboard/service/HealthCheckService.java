package com.enceladus.delivery.dashboard.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class HealthCheckService {

    WebClient webClient;
    HealthCheckService(WebClient webClient){
        this.webClient = webClient;
    }

    public Mono<Map<String, Object>> checkHealth() {
        return webClient.get()
                .uri("/howru/health")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> { log.error("[http.get] Unable to retrieve data " + e.getMessage());})
                .onErrorResume(e -> Mono.just(new HashMap<>()));
    }
}
