package com.enceladus.delivery.dashboard.task.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.enceladus.delivery.dashboard.service.TaskDashboardService;
import com.enceladus.delivery.demo.commons.model.Task;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

@Route(value = "task-list", layout = MainLayout.class)
@PageTitle("Task List")
@PermitAll // When security is enabled, allow all authenticated users
public class TaskListView extends Main {

    private final TaskDashboardService taskReactiveClientService;

    final TextField description;
    final DatePicker dueDate;
    final Button createBtn;
    final Button clearBtn;
    final Grid<Task> taskGrid;

    public TaskListView(TaskDashboardService taskReactiveClientService, Clock clock) {
        this.taskReactiveClientService = taskReactiveClientService;

        description = new TextField();
        description.setPlaceholder("What do you want to do?");
        description.setAriaLabel("Task description");
        description.setMaxLength(255);
        description.setMinWidth("20em");

        dueDate = new DatePicker();
        dueDate.setPlaceholder("Due date");
        dueDate.setAriaLabel("Due date");

        createBtn = new Button("Create", event -> createTask());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        clearBtn = new Button("Clear", event -> clearTask());
        clearBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

//        TextField filterTextField = new TextField("Search");
//        filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(clock.getZone())
                .withLocale(getLocale());
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());

        taskGrid = new Grid<>();
        taskGrid.setItems(query -> taskReactiveClientService.list(query) //toSpringPageRequest
                .toFuture().join().getData().stream());
        taskGrid.addColumn(Task::description).setHeader("Description");
        taskGrid.addColumn(task -> Optional.ofNullable(task.dueDate()).map(dateFormatter::format).orElse("Never"))
                .setHeader("Due Date");
        taskGrid.addColumn(task -> dateTimeFormatter.format(task.creationDate())).setHeader("Creation Date");
        taskGrid.setSizeFull();

        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

        add(new ViewToolbar("Task List", ViewToolbar.group(description, dueDate, createBtn, clearBtn)));
        add(taskGrid);
    }

    private void clearTask() {
        var ui = UI.getCurrent();
        taskGrid.setVisible(false);
        //taskGrid.setClassName(LumoUtility.Display.HIDDEN);
    }
    private void createTask() {
        var ui = UI.getCurrent();
        taskReactiveClientService.createTask(description.getValue(), dueDate.getValue())
                .subscribe(response -> {
                    if (response.getStatus().equals("SUCCESS")) {
                        ui.access(() -> {
                            taskGrid.setVisible(false);
                            taskGrid.setItems(query -> taskReactiveClientService.list(query) //toSpringPageRequest
                                    .toFuture().join().getData().stream());
                            taskGrid.getDataProvider().refreshAll();
                            taskGrid.setVisible(true);
                            description.clear();
                            dueDate.clear();
                            Notification.show("Task created successfully (id:" + response.getData().id() + ")",
                                            3000, Notification.Position.BOTTOM_END)
                                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        });
                    } else {
                        ui.access(() -> {
                            Notification.show("Error: " + response.getStatus(), 3000, Notification.Position.BOTTOM_END);
                        });
                    }
                });
    }
}
