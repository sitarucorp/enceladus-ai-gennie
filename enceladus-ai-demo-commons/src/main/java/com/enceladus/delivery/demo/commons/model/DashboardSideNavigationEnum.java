package com.enceladus.delivery.demo.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Align side navigation views together
 * For Dashboard
 */
@Getter
@AllArgsConstructor
public enum DashboardSideNavigationEnum {

    DashboardMain("Home", "/", "vaadin:coffee"),
    DashboardNotification("Notifications", "/notifications", "dropdown"),
    DashboardProfile("User Profile", "/profile", "lumo:user"),
    DashboardHistory("History", "/history", "calendar-clock"),
    DashboardPreferences("Settings", "/preferences", "cogs"),
    DashboardFeedback("Give Feedback", "/feedback", "cogs"),
    DashboardVersion("Version", "/version", "info");

    final String icon;
    final String title;
    final String path;
}
