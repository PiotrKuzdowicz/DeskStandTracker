package pl.github.kuzdot.deskStandTracker.controller;


import pl.github.kuzdot.deskStandTracker.model.Notification;
import pl.github.kuzdot.deskStandTracker.model.NotificationEvent;
import pl.github.kuzdot.deskStandTracker.view.DeskStandTrackerTryIcon;

/**
 * NotificationEventHandler
 */
public class NotificationEventHandler implements NotificationEvent {

    NotificationManager nm = new NotificationManager(DeskStandTrackerTryIcon.getInstance().getTIcon());

    @Override
    public void handle(Notification notification) {
        try {
            nm.displayTray(notification.title, notification.message, notification.messageType);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}



