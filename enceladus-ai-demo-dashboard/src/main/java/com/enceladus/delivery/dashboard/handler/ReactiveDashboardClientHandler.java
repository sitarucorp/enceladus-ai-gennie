package com.enceladus.delivery.dashboard.handler;

import com.enceladus.delivery.demo.commons.model.core.DashboardRequest;
import com.enceladus.delivery.demo.commons.model.core.DashboardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class ReactiveDashboardClientHandler {
    WebClient webClient;
    public static Integer NB_HTTP_CALLS = 0;
    public static Integer NB_HTTP_CALLS_KO = 0;
    //oops

    ReactiveDashboardClientHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T, R> Mono<DashboardResponse<R>> httpGetDashboardRequest(
            String path,
            DashboardRequest<T> request,
            Optional<Map<String, String>> optionalQueryParams,
            ParameterizedTypeReference<DashboardResponse<R>> responseType) {
        NB_HTTP_CALLS++;
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    optionalQueryParams.ifPresent(params ->
                            params.forEach(uriBuilder::queryParam)
                    );
                    return uriBuilder.build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    NB_HTTP_CALLS_KO++;
                    log.error("[http.get] Unable to retrieve data " + e.getMessage());
                });
    }

    public <T, R> Mono<DashboardResponse<R>> httpPostDashboardRequest(
            String path,
            DashboardRequest<T> request,
            Optional<Map<String, String>> optionalQueryParams,
            ParameterizedTypeReference<DashboardResponse<R>> responseType) {
        NB_HTTP_CALLS++;

        return webClient.post()
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    optionalQueryParams.ifPresent(params ->
                            params.forEach(uriBuilder::queryParam)
                    );
                    return uriBuilder.build();
                })
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(responseType)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    NB_HTTP_CALLS_KO++;
                    log.error("[http.post] Unable to retrieve data " + e.getMessage());
                });
    }
}
