package com.enceladus.delivery.dashboard.observability.ui.view;

import com.enceladus.delivery.dashboard.base.ui.component.MainLayout;
import com.enceladus.delivery.dashboard.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "/observability", layout = MainLayout.class)
@PageTitle("Status Page | Observability")
@PermitAll
public class ObservabilityView extends Main {

    public ObservabilityView() {
        Button topologyBtn = new Button("Show Topology", e -> {
            Notification.show("This displays apps http traffic in a topology way visually", 3000, Notification.Position.MIDDLE);
        });

        Button statusPageBtn = new Button("Status Page", e -> {
            Notification.show("This displays status page of project rss feed", 3000, Notification.Position.MIDDLE);
        });

        // FIXME May wish to display anchors for respective status pages for quicker access or subscribe to a RSS feed.
//        add(new Anchor("https://health.aws.amazon.com/health/status"),
//                new Anchor("https://azure.status.microsoft/en-us/status"),
//                new Anchor("https://status.cloud.google.com/"),
//                new Anchor("https://status.redis.io/"),
//                new Anchor("https://status.twilio.com/"),
//                new Anchor("https://cronitor.io/guides/status-page-examples"));

        add(new ViewToolbar("View Observability | Deployments", ViewToolbar.group(statusPageBtn, topologyBtn)));
    }
}
