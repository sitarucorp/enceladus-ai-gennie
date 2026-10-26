package com.enceladus.delivery.dashboard.base.ui.view;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("/login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSpacing(true);
        setMargin(true);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setOpened(true);
        loginOverlay.setAction("login"); // Must match Spring Security's login-processing URL
        loginOverlay.setTitle("Application Delivery Unit");
        loginOverlay.setDescription("Please log in to continue");

        loginOverlay.setError(hasLoginError());
        add(loginOverlay);
    }

    private boolean hasLoginError() {
        return VaadinSession.getCurrent().getAttribute("login.error") != null;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

}
