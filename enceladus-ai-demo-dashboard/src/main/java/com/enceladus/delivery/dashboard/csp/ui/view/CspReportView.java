package com.enceladus.delivery.dashboard.csp.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "/csp-reports", layout = MainLayout.class)
@PageTitle("CSP Reports")
@PermitAll
public class CspReportView extends Main {
    public CspReportView() {
        Button cspReportBtn = new Button("Show Live reports", e -> {
            Notification.show("This displays live violations of Content Security Policies for the project", 3000, Notification.Position.MIDDLE);
        });
        add(new ViewToolbar("Live CSP Reports", cspReportBtn));
    }
}
