package com.enceladus.delivery.dashboard.service;

import com.enceladus.delivery.dashboard.handler.ReactiveDashboardClientHandler;
import com.enceladus.delivery.demo.commons.model.Version;
import com.enceladus.delivery.demo.commons.model.core.DashboardRequest;
import com.enceladus.delivery.demo.commons.model.core.DashboardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Slf4j
@Service
public class VersionService {
    private static final String PATH_VERSION = "/api/version";
    ReactiveDashboardClientHandler reactiveDashboardClientHandler;
    VersionService(ReactiveDashboardClientHandler reactiveDashboardClientHandler) {
        this.reactiveDashboardClientHandler = reactiveDashboardClientHandler;
    }

    public Mono<DashboardResponse<Version>> retrieveVersion() {
        DashboardRequest<?> versionRequest = new DashboardRequest<>();
        return reactiveDashboardClientHandler.httpGetDashboardRequest(
                        PATH_VERSION,
                        versionRequest, Optional.empty(),
                        new ParameterizedTypeReference<DashboardResponse<Version>>() {}
                ).subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> log.error("ERROR: ", e))
                .onErrorResume(e -> Mono.just(new DashboardResponse<>(Version.builder().version("down!").build(),
                        "ERROR","On error fallback to empty")));
    }
}
