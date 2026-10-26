package com.enceladus.delivery.dashboard.base.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Route(value = "notifications", layout = MainLayout.class)
@PageTitle("Notifications")
@PermitAll
public class NotificationView extends Main {

    private static final Integer MAX_SIZE_TO_HOLD = 500;
    private static boolean isGreetingDone = Boolean.FALSE;
    private static boolean isDisablePopupNotifications = Boolean.FALSE;
    private MessageList messageList = new MessageList();

    Button disablePopupNotificationsButton = new Button("Disable Popup Notifications", click -> disablePopupNotifications());
    Button enablePopupNotificationsButton = new Button("Enable Popup Notifications",  click -> enablePopupNotifications());

    private static final String[] WORDS = {
            "apple", "banana", "cherry", "date", "elderberry", "fig", "grape",
            "honeydew", "kiwi", "lemon", "mango", "nectarine", "orange", "papaya",
            "quince", "raspberry", "strawberry", "tangerine", "ugli", "vanilla",
            "watermelon", "xigua", "yam", "zucchini"
    };

    public NotificationView() {
        Button clearButton = new Button("Clear", event -> clearNotifications());
        clearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        disablePopupNotificationsButton.setVisible(true);
        enablePopupNotificationsButton.setVisible(false);

        add(new ViewToolbar("Notifications", ViewToolbar.group(clearButton, disablePopupNotificationsButton,
                enablePopupNotificationsButton)));
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setPadding(true);
        messageList = initializeMessageListContent();
        layout.add(messageList);
        add(layout);
    }

    private final static List<MessageListItem> streams = new CopyOnWriteArrayList<>();
    private final Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

    @PostConstruct
    public void init() {
        messageList.setItems(streams);
        messageList.setVisible(true);
        if (isGreetingDone){
            if(isDisablePopupNotifications) {
                enablePopupNotificationsButton.setVisible(true);
                disablePopupNotificationsButton.setVisible(false);
            }
        }else {
            Flux.just("Hello, Girish Mahajan").subscribe(this::addInSink);
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 7000)
    public void addInSink() {
        if (streams.size() > MAX_SIZE_TO_HOLD) {
            streams.removeLast(); // trim if needed
        }
        addInSink(WORDS[new Random().nextInt(WORDS.length)]);
    }

    private void addInSink(String data) {
        sink.tryEmitNext(data);
    }

    private MessageList initializeMessageListContent() {
        messageList.setSizeFull();
        messageList.setVisible(true);
        messageList.setHeight("100%");
        messageList.setWidth("100%");
        var ui = UI.getCurrent();
        sink.asFlux().subscribe(data -> {
            Thread.startVirtualThread(() -> {
                if (ui.isAttached()) {
                    ui.access(() -> {
                        greet();
                        streams.addFirst(getMessageListItemCustom(data)); // Add to top
                        messageList.setItems(streams);
                        messageList.setVisible(true);
                        if (!isDisablePopupNotifications) {
                            Notification.show("Live: " + data, 2000, Notification.Position.BOTTOM_END)
                                    .addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                        }
                    });
                }
            });
        });
        return messageList;
    }

    private void greet() {
        if (!isGreetingDone) {
            Flux.just("Hello, Girish Mahajan").subscribe(this::addInSink);
            isGreetingDone = Boolean.TRUE;
        }
    }

    private MessageListItem getMessageListItemCustom(String notifText) {
        MessageListItem item = new MessageListItem();
        item.setTime(Instant.now());
        item.setUserColorIndex(3);
        item.setUserName("Girish Mahajan");
        item.setText(notifText);
        return item;
    }

    private void disablePopupNotifications() {
        isDisablePopupNotifications = Boolean.TRUE;
        enablePopupNotificationsButton.setVisible(true);
        disablePopupNotificationsButton.setVisible(false);
        Notification.show("Notifications popup disabled!", 2000,
                        Notification.Position.TOP_END)
                .addThemeVariants(NotificationVariant.LUMO_WARNING);
        Flux.just("Notifications popup disabled!").subscribe(this::addInSink);
    }

    private void enablePopupNotifications() {
        isDisablePopupNotifications = Boolean.FALSE;
        disablePopupNotificationsButton.setVisible(true);
        enablePopupNotificationsButton.setVisible(false);
        Notification.show("Notifications popup enabled!", 2000,
                        Notification.Position.TOP_END)
                .addThemeVariants(NotificationVariant.LUMO_WARNING);
    }

    private void clearNotifications() {
        messageList.setVisible(false);
        streams.clear();
        messageList.setItems(streams);
        if(isDisablePopupNotifications){
            Notification.show("Notifications cleared!", 2000,
                            Notification.Position.TOP_END)
                    .addThemeVariants(NotificationVariant.LUMO_WARNING);
        } else {
            Flux.just("Notifications cleared!").subscribe(this::addInSink);
        }
        messageList.setVisible(true);
    }
}
