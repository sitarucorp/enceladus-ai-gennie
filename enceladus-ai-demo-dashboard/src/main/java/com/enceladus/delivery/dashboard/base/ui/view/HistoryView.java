package com.enceladus.delivery.dashboard.base.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "history", layout = MainLayout.class)
@PageTitle("View History")
@PermitAll
public class HistoryView extends Main {
    public HistoryView() {
        add(new ViewToolbar("View History"));
    }
}
