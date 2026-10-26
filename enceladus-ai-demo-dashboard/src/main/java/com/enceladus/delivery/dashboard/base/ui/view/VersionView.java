package com.enceladus.delivery.dashboard.base.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.enceladus.delivery.dashboard.service.VersionService;
import com.enceladus.delivery.demo.commons.model.Version;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.Builder;

import java.util.List;

@Route(value = "version", layout = MainLayout.class)
@PageTitle("Version")
@PermitAll
public class VersionView extends Main {

    @Builder static record Detail(String name, String value) { }

    VerticalLayout verticalLayout = new VerticalLayout();
    Grid<Version> history = new Grid<>(Version.class, false);
    Dialog dialog = new Dialog("Version Information");
    VersionService versionService;

    public VersionView(VersionService versionService) {
        this.versionService = versionService;
        dialog.getFooter().add(new Button("Close", click -> dialog.close()));
        history.setSizeFull();
        history.setEnabled(true);
        history.setVisible(true);
        history.setMultiSort(true);
        history.addItemClickListener(click -> showDialog(click.getItem()));

        history.addColumn(Version::time).setHeader("Build Time");
        history.addColumn(Version::version).setHeader("Version");
        history.addColumn(Version::apiPathVersion).setHeader("API Path");
        history.addColumn(Version::artifact).setHeader("ArtifactId");
        history.addColumn(Version::group).setHeader("Group");
        history.addColumn(Version::name).setHeader("Project");
        history.getStyle().setPadding("2px 2px 2px 2px");

        setHeightFull();
        add(new ViewToolbar("Version History"));
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);


        verticalLayout.add(history);
        verticalLayout.add(dialog);
        add(verticalLayout);
        var ui = UI.getCurrent();
        versionService.retrieveVersion().subscribe(response -> {
            ui.access(() -> {
                if ("SUCCESS".equals(response.getStatus())) {
                    refreshGrid(response.getData());
                    showDialog(response.getData());
                } else {
                    Notification.show("Backend down!", 2000, Notification.Position.TOP_END).addThemeVariants(NotificationVariant.LUMO_WARNING);
                }
            });
        });
    }

    private void showDialog(Version version) {
        dialog.setVisible(true);
        dialog.setHeaderTitle("Version Information");
        dialog.setWidth("650px");
        dialog.setHeight("450px");
        dialog.setCloseOnEsc(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.add(getVersionLayout(version));
        dialog.addThemeVariants(DialogVariant.MATERIAL_NO_PADDING);
        dialog.open();
    }

    private void refreshGrid(Version dto) {
        history.setItems(List.of(dto));
        history.getDataProvider().refreshAll();
    }

    private Component getVersionLayout(Version dto) {
        VerticalLayout layout = new VerticalLayout();
        Grid<Detail> infoGrid = new Grid<>(Detail.class, false);

        layout.setSizeFull();
        layout.setSpacing(true);

        infoGrid.addColumn(Detail::name).setHeader("Legend");
        infoGrid.addColumn(Detail::value).setHeader("Value");
        infoGrid.getStyle().setBoxShadow("10px");
        infoGrid.getStyle().setPadding("2px 2px 2px 2px");
        infoGrid.addClassNames(Style.FontWeight.LIGHTER.name(), Style.AlignItems.CENTER.name());
        List<Detail> details = List.of(Detail.builder().name("Name").value(dto.name()).build(), Detail.builder().name("API Path Version").value(dto.apiPathVersion()).build(), Detail.builder().name("Group").value(dto.group()).build(), Detail.builder().name("ArtifactId").value(dto.artifact()).build(), Detail.builder().name("Version").value(dto.version()).build(), Detail.builder().name("Build Time").value(dto.time().toString()).build());
        infoGrid.setVisible(true);
        infoGrid.setItems(details);
        layout.add(infoGrid);
        return layout;
    }
}