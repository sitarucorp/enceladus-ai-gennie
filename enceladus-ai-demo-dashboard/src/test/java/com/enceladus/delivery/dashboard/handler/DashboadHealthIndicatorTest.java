package com.enceladus.delivery.dashboard.handler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DashboadHealthIndicatorTest {

    @Test
    void reportsHealthyStatusAndRequestCounters() {
        ReactiveDashboardClientHandler.NB_HTTP_CALLS = 5;
        ReactiveDashboardClientHandler.NB_HTTP_CALLS_KO = 2;
        DashboadHealthIndicator indicator = new DashboadHealthIndicator(null);

        var health = indicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals(5, health.getDetails().get("nb.http.calls"));
        assertEquals(2, health.getDetails().get("nb.http.calls.failed"));
    }
}
