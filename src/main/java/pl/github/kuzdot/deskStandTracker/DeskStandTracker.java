package pl.github.kuzdot.deskStandTracker;

/**
 * DeskStandTracker
 */

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.event.MouseAdapter;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import pl.github.kuzdot.deskStandTracker.model.Time;
import pl.github.kuzdot.deskStandTracker.view.ClockWindow;
import pl.github.kuzdot.deskStandTracker.view.DeskStandTrackerTryIcon;
import pl.github.kuzdot.deskStandTracker.view.ui.Dark;
import pl.github.kuzdot.deskStandTracker.view.ui.WindowsPalette;

public class DeskStandTracker {

    public static void main(String[] args) throws AWTException {
        IconFontSwing.register(FontAwesome.getIconFont());
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        Time time = new Time(15, 20);
        WindowsPalette color = new Dark();
        if (SystemTray.isSupported()) {
            ClockWindow clockWindow = ClockWindow.getInstance(time, color);
            DeskStandTrackerTryIcon.getInstance().getTIcon().addMouseListener(new MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        if (clockWindow.getFrame() == null) {
                            clockWindow.show();
                        } else {
                            clockWindow.slideIn(clockWindow.getFrame());
                        }
                    }
                };
            });
            return;
        } else {
            System.err.println("System tray not supported!");
            System.exit(-1);
        }
    }
}



