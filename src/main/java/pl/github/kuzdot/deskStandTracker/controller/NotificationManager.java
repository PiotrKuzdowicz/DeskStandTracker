package pl.github.kuzdot.deskStandTracker.controller;

import java.awt.AWTException;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

/**
 * NotificationManager
 */
public class NotificationManager {

    TrayIcon trayIcon;

    public NotificationManager(TrayIcon trayIcon) {
		this.trayIcon = trayIcon;
	}


	public void displayTray(String title, String message, MessageType messageType)
            throws AWTException {
        this.trayIcon.displayMessage(title, message, messageType);
    }

}



