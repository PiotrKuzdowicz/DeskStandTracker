package pl.github.kuzdot.deskStandTracker.view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;

/**
 * DeskStandTrackerTryIcon
 */
public class DeskStandTrackerTryIcon {

    private static DeskStandTrackerTryIcon sIcon;
    private TrayIcon tIcon;

    private DeskStandTrackerTryIcon() {
        try {
            tIcon = createTrayIcon();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static DeskStandTrackerTryIcon getInstance() {
        if (sIcon == null) {
            sIcon = new DeskStandTrackerTryIcon();
        }
        return sIcon;
    }

    private static TrayIcon createTrayIcon() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = IconProvider.getIcon();
          PopupMenu popupMenu = new PopupMenu();
        
        MenuItem exitItem = new MenuItem("Zamknij");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });
        
        popupMenu.add(exitItem);
        TrayIcon trayIcon = new TrayIcon(image, "Desk Stand Tracker" , popupMenu);
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
        return trayIcon;
    }

    public TrayIcon getTIcon() {
        return tIcon;
    }
}



