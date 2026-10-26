package com.enceladus.delivery.dashboard.handler;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnEnabledHealthIndicator(value = "dashboard.enabled") // management.health.dashboard.enabled ? // FIXME
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

    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }
}
