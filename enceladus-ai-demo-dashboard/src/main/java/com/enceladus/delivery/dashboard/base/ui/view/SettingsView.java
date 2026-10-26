package com.enceladus.delivery.dashboard.base.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "preferences", layout = MainLayout.class)
@PageTitle("User Settings")
@PermitAll
public class SettingsView extends Main {
    public SettingsView() {
        add(new ViewToolbar("Edit Settings"));
    }
}
