package pl.github.kuzdot.deskStandTracker.model;

import java.awt.TrayIcon.MessageType;
import java.io.Serializable;

/**
 * Notification
 */
public class Notification implements Serializable {

    public String title;
    public String message;
    public MessageType messageType;

    public Notification(String title, String message, MessageType messageType) {
        this.title = title;
        this.message = message;
        this.messageType = messageType;
    }

}



