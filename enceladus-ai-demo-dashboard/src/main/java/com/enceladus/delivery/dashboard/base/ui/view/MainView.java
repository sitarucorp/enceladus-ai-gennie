package com.enceladus.delivery.dashboard.base.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.enceladus.delivery.dashboard.service.VersionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route(value = "", layout = MainLayout.class)
@PermitAll // When security is enabled, allow all authenticated users
@PageTitle("Delivery Console | Home")
public final class MainView extends Main {

    VersionService versionService;

    private final List<MessageListItem> streams = new CopyOnWriteArrayList<>();
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    MainView(VersionService versionService) {
        this.versionService = versionService;
        add(new ViewToolbar("Main"));
        addClassName(LumoUtility.Padding.MEDIUM);
        add(new Div("Please select a view from the menu on the left."));
        showVersion();
    }


    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }

    public void showVersion() {
        var ui = UI.getCurrent();
        versionService.retrieveVersion()
                .subscribe(response -> {

                    ui.access(() -> {
                        if ("SUCCESS".equals(response.getStatus())) {
                            Notification.show("Dashboard Backend : " + response.getData().version(),
                                            2000, Notification.Position.TOP_STRETCH)
                                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        } else {
                            Notification.show("Dashboard Backend : " + response.getData().version(),
                                            2000, Notification.Position.TOP_STRETCH)
                                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    });
                });
    }
}
