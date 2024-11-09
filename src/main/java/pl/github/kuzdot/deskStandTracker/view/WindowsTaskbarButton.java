package pl.github.kuzdot.deskStandTracker.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JButton;

import pl.github.kuzdot.deskStandTracker.view.ui.WindowsPalette;

public class WindowsTaskbarButton extends JButton {
    private int radius;
    private int buttonHeight;
    private int buttonWidth;
    private WindowsPalette color;
    private Color backgroundColor;
    private Color labelColor;
    private Icon icon;

    public WindowsTaskbarButton(Icon icon, String text, WindowsPalette color, int buttonWidth, int buttonHeight) {
        super(icon);
        this.icon = icon;
        this.radius = 10;
        this.icon = icon;
        this.buttonHeight = buttonHeight;
        this.buttonWidth = buttonWidth;
        this.color = color;
        backgroundColor = color.BUTTON;
        labelColor = color.LABEL;
        setText(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        addMouseListener(hoverAdapter);
    }

    public WindowsTaskbarButton(String text, WindowsPalette color, int buttonWidth, int buttonHeight) {
        super(text);
        this.radius = 10;
        this.buttonHeight = buttonHeight;
        this.buttonWidth = buttonWidth;
        this.color = color;
        this.icon = null;
        backgroundColor = color.BUTTON;
        labelColor = color.LABEL;
        setContentAreaFilled(false);
        setFocusPainted(false);
        addMouseListener(hoverAdapter);
    }

    public WindowsTaskbarButton(String text, WindowsPalette color) {
        super(text);
        this.radius = 10;
        this.buttonWidth = 25;
        this.buttonHeight = 25;
        this.icon = null;
        this.color = color;
        backgroundColor = color.BUTTON;
        labelColor = color.LABEL;
        setContentAreaFilled(false);
        setFocusPainted(false);
        addMouseListener(hoverAdapter);
    }

    public void setPalette(WindowsPalette color) {
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Icon icon = getIcon();
        String text = getText();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape rounded = new RoundRectangle2D.Float(0, 0, buttonWidth, buttonHeight, radius, radius);
        g2.setColor(backgroundColor);
        g2.fill(rounded);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        if (icon != null) {
            int iconX = 10;
            int iconY = (buttonHeight - icon.getIconHeight()) / 2;

            if (icon != null) {
                icon.paintIcon(this, g2, iconX, iconY);
            }
            FontMetrics fm = g2.getFontMetrics();
            int textX = iconX + (icon != null ? icon.getIconWidth() + 10 : 0);
            int textY = (buttonHeight + 2 + fm.getAscent()) / 2 - fm.getDescent();
            g2.setColor(labelColor);
            g2.drawString(text, textX, textY);
        } else {
            if (text != "") {
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textX = (buttonWidth - textWidth) / 2;
                int textY = (buttonHeight + 2 + fm.getAscent()) / 2 - fm.getDescent();
                g2.setColor(labelColor);
                g2.drawString(getText(), textX, textY);
            }
        }
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color.BUTTON_BORDER);
        g2.drawRoundRect(0, 0, buttonWidth - 1, buttonHeight - 1, radius, radius);
        g2.dispose();
    }

    MouseAdapter hoverAdapter = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            WindowsTaskbarButton button = (WindowsTaskbarButton) e.getSource();
            backgroundColor = color.BUTTON_HOVER;
            labelColor = color.LABEL;
            button.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            WindowsTaskbarButton button = (WindowsTaskbarButton) e.getSource();
            backgroundColor = color.BUTTON;
            labelColor = color.LABEL;
            button.repaint();
        }
    };
}
