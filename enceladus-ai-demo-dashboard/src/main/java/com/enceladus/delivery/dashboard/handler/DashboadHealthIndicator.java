package com.enceladus.delivery.dashboard.handler;

import org.springframework.boot.health.autoconfigure.contributor.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnEnabledHealthIndicator(value = "dashboard")
public class DashboadHealthIndicator implements HealthIndicator {

    ReactiveDashboardClientHandler reactiveDashboardClientHandler;

    DashboadHealthIndicator(ReactiveDashboardClientHandler reactiveDashboardClientHandler) {
        this.reactiveDashboardClientHandler = reactiveDashboardClientHandler;
    }

    @Override
    public Health health() {
        return Health.up().withDetails(Map.of("nb.http.calls", ReactiveDashboardClientHandler.NB_HTTP_CALLS,
                "nb.http.calls.failed", ReactiveDashboardClientHandler.NB_HTTP_CALLS_KO)).build();
    }
}
