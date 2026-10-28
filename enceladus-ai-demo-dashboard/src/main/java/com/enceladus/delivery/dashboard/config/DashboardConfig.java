package com.enceladus.delivery.dashboard.config;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@Configuration
@EnableAsync
@EnableScheduling
@Push(PushMode.AUTOMATIC)
@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
public class DashboardConfig implements AppShellConfigurator {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @EventListener(ApplicationStartedEvent.class)
    public void process() {
        System.out.println("[dashboard] Dashboard backend started");
    }

}
