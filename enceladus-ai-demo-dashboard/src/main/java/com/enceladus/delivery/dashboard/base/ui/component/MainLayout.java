package com.enceladus.delivery.dashboard.base.ui.component;

import com.enceladus.delivery.dashboard.base.ui.view.*;
import com.enceladus.delivery.dashboard.csp.ui.view.CspReportView;
import com.enceladus.delivery.dashboard.observability.ui.view.ObservabilityView;
import com.enceladus.delivery.dashboard.task.ui.view.TaskListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import jakarta.annotation.security.PermitAll;

import static com.vaadin.flow.theme.lumo.LumoUtility.*;

@PermitAll // When security is enabled, allow all authenticated users
public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createHeader(), createRootSideNav(),  new Scroller(createOptionsSideNav()), createClientsSideNav(), createUserMenu());
        setDrawerOpened(true);
    }

    private Div createHeader() {
        // TODO Replace with real application logo and name
        var appLogo = VaadinIcon.COFFEE.create();
        appLogo.addClassNames(TextColor.PRIMARY, IconSize.LARGE);

        var appName = new Span("Intelligence Console");
        appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

        var header = new Div(appLogo, appName);
        header.addClassNames(Display.FLEX, Padding.MEDIUM, Gap.MEDIUM, AlignItems.CENTER);
        return header;
    }

    private SideNav createRootSideNav() {
        var nav = new SideNav();
        nav.addItem(new SideNavItem("Home", MainView.class, VaadinIcon.HOME.create()));
        nav.addItem(new SideNavItem("Tasks", TaskListView.class, VaadinIcon.CHART_3D.create()));
        nav.addItem(new SideNavItem("CSP Reports", CspReportView.class, VaadinIcon.CHART_GRID.create()));
        nav.addItem(new SideNavItem("Observability", ObservabilityView.class, VaadinIcon.ABACUS.create()));
        return nav;
    }

    private SideNav createOptionsSideNav() {
        var nav = new SideNav();
        nav.addClassNames(Margin.Horizontal.LARGE, Padding.MEDIUM);

        nav.setLabel("Options");
        nav.setCollapsible(true);
        nav.setExpanded(false);
        nav.setVisible(true);

        nav.addItem(new SideNavItem("Activity", NotificationView.class, VaadinIcon.INBOX.create()));
        nav.addItem(new SideNavItem("History", HistoryView.class, VaadinIcon.ARCHIVE.create()));
        nav.addItem(new SideNavItem("Health", HealthView.class, VaadinIcon.HEALTH_CARD.create()));
        nav.addItem(new SideNavItem("Version", VersionView.class, VaadinIcon.DASHBOARD.create()));

        return nav;
    }

    private SideNav createClientsSideNav() {
        var nav = new SideNav();
        nav.addClassNames(Margin.Horizontal.LARGE, Padding.MEDIUM);

        nav.setLabel("Projects");
        nav.setCollapsible(true);
        nav.setExpanded(false);
        nav.setVisible(true);

        nav.addItem(new SideNavItem("PROJ C3", NotificationView.class, VaadinIcon.TASKS.create()));
        nav.addItem(new SideNavItem("PROJ X3", NotificationView.class, VaadinIcon.TASKS.create()));

        return nav;
    }

    private Component createUserMenu() {
        // TODO Replace with real user information and actions
        var avatar = new Avatar("Girish Mahajan");
        avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
        avatar.addClassNames(Margin.Right.SMALL);
        avatar.setColorIndex(5);

        var userMenu = new MenuBar();
        userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        userMenu.addClassNames(Margin.MEDIUM);

        var userMenuItem = userMenu.addItem(avatar);
        userMenuItem.add("Girish Mahajan");

        userMenuItem.getSubMenu().addItem("Profile", e -> {
            UI.getCurrent().navigate(ProfileView.class);
        });
        userMenuItem.getSubMenu().addItem("Preferences", e -> {
            UI.getCurrent().navigate(SettingsView.class);
        });
//        userMenuItem.getSubMenu().addItem("Notifications", e -> {
//            UI.getCurrent().navigate("/notifications");
//        });
//        userMenuItem.getSubMenu().addItem("History", e -> {
//            UI.getCurrent().navigate("/history");
//        });
        userMenuItem.getSubMenu().addItem("Feedback", e -> {
            UI.getCurrent().navigate(FeedbackView.class);
        });
        userMenuItem.getSubMenu().addItem("Logout", e -> {
            Dialog dialog = new Dialog();
            dialog.setWidth("320px");
            dialog.setHeight("140px");
            dialog.setHeaderTitle("Do you want to logout?");
            dialog.setCloseOnEsc(true);

            Button button = new Button("Logout");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.setEnabled(true);
            dialog.add(button);
            button.addClickListener(f -> {
                setDrawerOpened(false);
                UI.getCurrent().access(dialog::close);
                UI.getCurrent().navigate(LogoutView.class);
            });

            UI.getCurrent().access(dialog::open);
        });
        return userMenu;
    }
}
