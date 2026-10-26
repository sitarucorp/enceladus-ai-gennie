package com.enceladus.delivery.dashboard.base.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.enceladus.delivery.dashboard.service.HealthCheckService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.Builder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Route(value = "/health", layout = MainLayout.class)
@PageTitle("Health check")
@PermitAll
public class HealthView extends Main {

    private final HealthCheckService healthCheckService;

    VerticalLayout verticalLayout;

    public HealthView(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
        add(new ViewToolbar("Application Health"), ViewToolbar.group(
                new Button("Refresh", VaadinIcon.REFRESH.create(),
                        click -> refreshView())));
        verticalLayout = new VerticalLayout();
        fetchHealthStatus();
    }

    private void refreshView() {
        fetchHealthStatus();
    }

    private void fetchHealthStatus() {
        var ui = UI.getCurrent();
        healthCheckService.checkHealth().subscribe(response -> {
            ui.access(() -> {
                if (Objects.nonNull(response)) {
                    displayHealthStatus(response);
                } else {
                    Notification.show("Unable to show health", 2000,
                                    Notification.Position.TOP_END)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
        });
    }

    private void displayHealthStatus(Map<String, Object> healthStatus) {
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setSizeFull();
        verticalLayout.setPadding(true);
        verticalLayout.setHeight("100%");
        verticalLayout.add(new H3("Status: "+ (String) healthStatus.get("status")));
        List<HorizontalLayout> table = new LinkedList<>();

        Map<String, Object> components = (Map<String, Object>) healthStatus.get("components");
        components.forEach((componentName, comp) -> {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Map<String, Object> health = (Map<String, Object>) comp;
            String componentStatus = (String) health.get("status");
            horizontalLayout.add(new H4(componentName+" -- "+ componentStatus));
            if (health.containsKey("details")) {
                Grid<HealthComponent> grid = new Grid<>();
                grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
                grid.addColumn(HealthComponent::name).setHeader("Name");
                grid.addColumn(HealthComponent::value).setHeader("Value");
                List<HealthComponent> detailsHealth = ((Map<String, Object>) health.get("details")).keySet().stream().map( key  -> {
                    return HealthComponent.builder().name(key).value(String.valueOf (((Map<?, ?>) health.get("details")).get(key))).build();
                }).toList();
                grid.setItems(detailsHealth);
                horizontalLayout.add(grid);
                table.add(horizontalLayout);
                verticalLayout.add(horizontalLayout);
            }
        });
        Grid<HorizontalLayout> horizontalLayoutGrid = new Grid<>();
        horizontalLayoutGrid.setSizeFull();
        horizontalLayoutGrid.getStyle().setPadding("10px");
        horizontalLayoutGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        horizontalLayoutGrid.setItems(table);

//        verticalLayout.add(horizontalLayoutGrid);
        verticalLayout.setVisible(true);
        add(verticalLayout);
    }

    @Builder public static record HealthComponent (String name, String value){}
}
