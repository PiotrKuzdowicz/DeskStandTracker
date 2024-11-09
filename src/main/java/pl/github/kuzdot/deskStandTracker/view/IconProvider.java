package pl.github.kuzdot.deskStandTracker.view;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

/**
 * IconProvider
 */
public class IconProvider {

    public static Image getIcon() {
        URL url = IconProvider.class
                .getClassLoader()
                .getResource("icon.png");
        Image image = Toolkit.getDefaultToolkit().createImage(url);
        return image;
    }
}



