package com.enceladus.delivery.dashboard.config;

import com.enceladus.delivery.demo.commons.model.core.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Configuration
public class WebBackendTransportConfig {

    @Value("${app.backend.url:http://localhost/}")        // FIXME
    private String APP_BACKEND_URL;

    @Value("${app.backend.tenant:UHJhYW5WYWF5dQo=}")        // FIXME
    private String BACKEND_TENANT_NAME;

    @Bean
    WebClient webClient() {
        return WebClient.builder().baseUrl(APP_BACKEND_URL)
                .defaultHeaders(headers -> {
                    headers.add(Constants.API_KEY_HEADER_NAME, Constants.DASHBOARD_API_KEY);
                    headers.add(Constants.API_KEY_TENANT_NAME, BACKEND_TENANT_NAME);
                })
                .filter(addDynamicHeaders())
                .build();
    }

    private ExchangeFilterFunction addDynamicHeaders() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            ClientRequest newRequest = ClientRequest.from(clientRequest)
                    .headers(headers -> {
                        headers.set(Constants.REQUEST_ID_HEADER_NAME, UUID.randomUUID().toString());
                    }).build();
            return Mono.just(newRequest);
        });
    }
}
