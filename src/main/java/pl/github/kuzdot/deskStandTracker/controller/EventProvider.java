package pl.github.kuzdot.deskStandTracker.controller;
import java.util.ArrayList;
import java.util.List;

import pl.github.kuzdot.deskStandTracker.model.Notification;
import pl.github.kuzdot.deskStandTracker.model.NotificationEvent;

/**
 * EventProvider
 */
public class EventProvider {

    private List<NotificationEvent> notificationEvents = new ArrayList<>();
    private static EventProvider eProvider;

    public static EventProvider getInstance() {
        if (eProvider == null) {
            eProvider = new EventProvider();
        }
        return eProvider;
    }

    private EventProvider() {
        registerHandler(new NotificationEventHandler());
    }

    private void registerHandler(NotificationEvent notificationEvent) {
        notificationEvents.add(notificationEvent);
    }

    public void execute(Notification notification) {
        notificationEvents.forEach(event -> {
            event.handle(notification);
        });
    }

}



