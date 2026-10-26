package com.enceladus.delivery.dashboard.base.ui.view;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.PermitAll;

@Route("/logout")
@PageTitle("Logged Out")
@PermitAll
public class LogoutView extends VerticalLayout {
    public LogoutView() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        setSpacing(true);


        add(
                new H2("You have been logged out."),
                new RouterLink("Login again", LoginView.class)
        );
    }
}
