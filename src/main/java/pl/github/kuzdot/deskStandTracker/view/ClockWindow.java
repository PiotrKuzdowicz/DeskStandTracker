package pl.github.kuzdot.deskStandTracker.view;

/**
 * ClockWindow
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.net.URL;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import jiconfont.IconCode;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import pl.github.kuzdot.deskStandTracker.controller.WorkSessionController;
import pl.github.kuzdot.deskStandTracker.controller.WorkSessionThread;
import pl.github.kuzdot.deskStandTracker.model.Time;
import pl.github.kuzdot.deskStandTracker.view.ui.WindowsPalette;

public class ClockWindow {

    JButton play;
    JButton pause;
    JButton stop;
    JButton changePosition;
    Time time;
    JFrame frame;
    JLabel standingTimeLabel;
    JLabel sittingTimeLabel;
    public JLabel currenPositionLabel;
    public int currenPositionItemSize = 65;
    public WindowsPalette color;
    int panelHeight = 375;
    int panelWidhth = 300;
    int panelMargin = 25;
    int margin = 10;
    WorkSessionThread workSessionThread;
    public JLabel currentSessionLabel;
    private static ClockWindow clockWindow;
    private int windowPositionX;

    public static ClockWindow getInstance() {
        assert clockWindow != null : "This should never happend";
        return clockWindow;
    }

    public static ClockWindow getInstance(Time time, WindowsPalette color) {
        if (clockWindow == null) {
            clockWindow = new ClockWindow(time, color);
        }
        return clockWindow;
    }

    private ClockWindow(Time time, WindowsPalette color) {
        this.time = time;
        this.color = color;
    }

    public void show() {
        frame = addFrame();
        JPanel panel = addMainPanel();
        JPanel headerPanel = addHeaderPanel();
        JPanel bodyPanel = addBodyPanel();
        // Header
        addLabel(headerPanel, "Standing Clock", (label) -> {
            label.setHorizontalAlignment(JLabel.CENTER);
        });
        // Body
        addBody(bodyPanel);
        panel.add(headerPanel);
        panel.add(bodyPanel);
        frame.add(panel);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Insets screenInsets = toolkit.getScreenInsets(frame.getGraphicsConfiguration());

        int taskbarHeight = screenInsets.bottom;
        int x = screenSize.width;
        int y = screenSize.height - frame.getHeight() - taskbarHeight;
        windowPositionX = x - 315;
        frame.setLocation(windowPositionX, y - margin);
        slideIn(frame);
    }

    private void addBody(JPanel bodyPanel) {
        addCurrentStateRow(bodyPanel);
        addTitleRow(bodyPanel, "Options");
        currentSessionLabel = addTitleRow(bodyPanel, "-");
        addStandingRow(bodyPanel);
        addSittingRow(bodyPanel);
        addChangePositionRow(bodyPanel);
        addActionRow(bodyPanel);
    }

    private void addCurrentStateRow(JPanel bodyPanel) {
        int width = panelWidhth - panelMargin;
        int heidht = 100;

        JPanel firstBodyRow = addPanel((panelModifier) -> {
            panelModifier.setBorder(new EmptyBorder(10, 0, 10, 0));
            panelModifier.setPreferredSize(new Dimension(width, heidht));
            panelModifier.setMaximumSize(new Dimension(width, heidht));
            panelModifier.setMinimumSize(new Dimension(width, heidht));
            panelModifier.setLayout(new FlowLayout());
        });

        Icon icon = IconFontSwing.buildIcon(FontAwesome.STOP, currenPositionItemSize, color.LABEL);
        currenPositionLabel = new JLabel(icon);
        firstBodyRow.add(currenPositionLabel);
        bodyPanel.add(firstBodyRow);
    }

    private void addChangePositionRow(JPanel bodyPanel) {
        int width = panelWidhth - panelMargin;
        int heidht = 45;

        JPanel row = addPanel((panelModifier) -> {
            panelModifier.setBorder(new EmptyBorder(10, 0, 0, 0));
            panelModifier.setPreferredSize(new Dimension(width, heidht));
            panelModifier.setMaximumSize(new Dimension(width, heidht));
            panelModifier.setMinimumSize(new Dimension(width, heidht));
            panelModifier.setLayout(new FlowLayout());
        });
        changePosition = createActionButton(GoogleMaterialDesignIcons.SWAP_VERT, "", 35);
        changePosition.setToolTipText("Change current position");
        changePosition.addActionListener(event -> changePositionAction(event));
        changePosition.setEnabled(false);
        row.add(changePosition);
        bodyPanel.add(row);

    }

    private void changePositionAction(ActionEvent event) {
        WorkSessionController.getInstance().changeCurrentPosition(workSessionThread);
        ;
    }

    private void addActionRow(JPanel bodyPanel) {
        int width = panelWidhth - panelMargin;
        int heidht = 45;

        JPanel firstBodyRow = addPanel((panelModifier) -> {
            panelModifier.setBorder(new EmptyBorder(10, 0, 0, 0));
            panelModifier.setPreferredSize(new Dimension(width, heidht));
            panelModifier.setMaximumSize(new Dimension(width, heidht));
            panelModifier.setMinimumSize(new Dimension(width, heidht));
            panelModifier.setLayout(new FlowLayout());
        });
        play = createActionButton(FontAwesome.PLAY, "Play");
        pause = createActionButton(FontAwesome.PAUSE, "Pause");
        pause.setEnabled(false);
        stop = createActionButton(FontAwesome.STOP, "Stop");
        stop.setEnabled(false);

        play.addActionListener(action -> playAction(action));
        pause.addActionListener(action -> pauseAction(action));
        stop.addActionListener(action -> stopAction(action));

        firstBodyRow.add(play);
        firstBodyRow.add(pause);
        firstBodyRow.add(stop);
        bodyPanel.add(firstBodyRow);

    }

    private void stopAction(ActionEvent event) {
        WorkSessionController.getInstance().stopSession(workSessionThread);
        play.setEnabled(Boolean.TRUE);
        stop.setEnabled(Boolean.FALSE);
        pause.setEnabled(Boolean.FALSE);
        changePosition.setEnabled(Boolean.FALSE);
        changePosition.repaint();
        play.repaint();
        stop.repaint();
        pause.repaint();
    }

    private void playAction(ActionEvent event) {
        workSessionThread = WorkSessionController.getInstance().createNewSession(time);
        workSessionThread.start();
        stop.setEnabled(Boolean.TRUE);
        play.setEnabled(Boolean.FALSE);
        pause.setEnabled(Boolean.TRUE);
        changePosition.setEnabled(Boolean.TRUE);
        changePosition.repaint();
        pause.repaint();
        play.repaint();
        stop.repaint();
    }

    private void pauseAction(ActionEvent event) {
        Icon icon;
        if (WorkSessionController.getInstance().isPaused(workSessionThread)) {
            WorkSessionController.getInstance().resumeSession(workSessionThread);
            icon = IconFontSwing.buildIcon(FontAwesome.PAUSE, 15, color.LABEL);
            pause.setText("Pause");
            stop.setEnabled(Boolean.TRUE);
            stop.repaint();
            changePosition.setEnabled(Boolean.TRUE);
            changePosition.repaint();
        } else {
            WorkSessionController.getInstance().pauseSession(workSessionThread);
            icon = IconFontSwing.buildIcon(FontAwesome.PLAY, 15, color.LABEL);
            pause.setText("Resume");
            stop.setEnabled(Boolean.FALSE);
            stop.repaint();
            changePosition.setEnabled(Boolean.FALSE);
            changePosition.repaint();
        }
        pause.setIcon(icon);
        pause.repaint();
    }

    private JButton createActionButton(IconCode iconCode, String text, int width) {
        Icon icon = IconFontSwing.buildIcon(iconCode, 15, color.LABEL);
        return addActionButton(icon, text, width);

    }

    private JButton createActionButton(IconCode iconCode, String text) {
        Icon icon = IconFontSwing.buildIcon(iconCode, 15, color.LABEL);
        return addActionButton(icon, text);

    }

    private JLabel addTitleRow(JPanel bodyPanel, String text) {

        JPanel jPanel = addPanel((panelModifier) -> {
            panelModifier.setBorder(new EmptyBorder(0, 0, 10, 0));
        });
        JLabel label = addLabel(text);
        jPanel.add(label);
        bodyPanel.add(jPanel);
        return label;

    }

    private JButton addActionButton(final Icon icon, final String text, int width) {
        WindowsTaskbarButton button = new WindowsTaskbarButton(icon, text, color, width, 25);
        button.setPreferredSize(new Dimension(width, 25));
        button.setMaximumSize(new Dimension(width, 25));
        button.setMinimumSize(new Dimension(width, 25));
        return button;
    }

    private JButton addActionButton(final Icon icon, final String text) {
        int width = 85;
        return addActionButton(icon, text, width);

    }

    private void addSittingRow(JPanel bodyPanel) {

        JPanel firstBodyRow = addPanel((panelModifier) -> {
        });
        JLabel label = addLabel("Sitting time", (jlabel) -> {
            jlabel.setHorizontalAlignment(JLabel.LEFT);
        });
        label.setPreferredSize(new Dimension(100, 30));
        label.setMaximumSize(new Dimension(100, 30));
        label.setMinimumSize(new Dimension(100, 30));
        firstBodyRow.add(label);

        JButton minus = addSittingButton("-", ActionType.DECREASE, time);
        minus.setPreferredSize(new Dimension(30, 30));
        minus.setMaximumSize(new Dimension(30, 30));
        minus.setMinimumSize(new Dimension(30, 30));
        firstBodyRow.add(minus);

        sittingTimeLabel = addLabel(time.getSittingTime(), " min");
        sittingTimeLabel.setPreferredSize(new Dimension(70, 30));
        sittingTimeLabel.setMaximumSize(new Dimension(70, 30));
        sittingTimeLabel.setMinimumSize(new Dimension(70, 30));
        firstBodyRow.add(sittingTimeLabel);

        JButton plus = addSittingButton("+", ActionType.INCREASE, time);
        plus.setPreferredSize(new Dimension(30, 30));
        plus.setMaximumSize(new Dimension(30, 30));
        plus.setMinimumSize(new Dimension(30, 30));
        firstBodyRow.add(plus);

        bodyPanel.add(firstBodyRow);

    }

    private void addStandingRow(JPanel bodyPanel) {

        JPanel firstBodyRow = addPanel((panelModifier) -> {
        });
        JLabel label = addLabel("Standing time", (jlabel) -> {
            jlabel.setHorizontalAlignment(JLabel.LEFT);
        });
        label.setPreferredSize(new Dimension(100, 30));
        label.setMaximumSize(new Dimension(100, 30));
        label.setMinimumSize(new Dimension(100, 30));
        firstBodyRow.add(label);

        JButton minus = addStandingButton("-", ActionType.DECREASE, time);
        minus.setPreferredSize(new Dimension(30, 30));
        minus.setMaximumSize(new Dimension(30, 30));
        minus.setMinimumSize(new Dimension(30, 30));
        firstBodyRow.add(minus);

        standingTimeLabel = addLabel(time.getStandingTime(), " min");
        standingTimeLabel.setPreferredSize(new Dimension(70, 30));
        standingTimeLabel.setMaximumSize(new Dimension(70, 30));
        standingTimeLabel.setMinimumSize(new Dimension(70, 30));
        firstBodyRow.add(standingTimeLabel);

        JButton plus = addStandingButton("+", ActionType.INCREASE, time);
        plus.setPreferredSize(new Dimension(30, 30));
        plus.setMaximumSize(new Dimension(30, 30));
        plus.setMinimumSize(new Dimension(30, 30));
        firstBodyRow.add(plus);

        bodyPanel.add(firstBodyRow);

    }

    private JPanel addPanel(Consumer<JPanel> modifier) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        modifier.accept(panel);
        return panel;
    }

    private JPanel addHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBackground(Color.ORANGE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }

    private JPanel addBodyPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBackground(Color.GREEN);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setSize(new Dimension(panelWidhth, 180));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }

    private JLabel addLabel(int number, String text) {
        JLabel label = new JLabel("<html><b>" + number + "</b>" + text + "</html>", JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(color.LABEL);
        return label;
    }

    private JFrame addFrame() {
        JFrame frame = new JFrame("Timer Selector");
        URL iconUrl = this.getClass().getResource("/icon.png");
        Image icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
        frame.setIconImage(icon);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("Standing Clock");
        frame.setSize(panelWidhth, panelHeight);
        frame.setUndecorated(true);
        frame.setBackground(color.FRAME_BACKGROUND);
        frame.setLayout(new GridLayout(1, 1));
        frame.setAlwaysOnTop(true);
        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                slideOut(frame);
            }
        });
        return frame;
    }

    public void slideOut(JFrame frame) {
        int targetX = windowPositionX + frame.getWidth();
        frame.setLocation(windowPositionX, frame.getY());

        Timer timer = new Timer(3, e -> {
            int currentX = frame.getX();
            if (currentX < targetX) {
                frame.setLocation(currentX + 50, frame.getY());
            } else {
                ((Timer) e.getSource()).stop();
                frame.setVisible(false);
            }
        });
        timer.start();
        frame.setAlwaysOnTop(false);
    }

    public void slideIn(JFrame frame) {
        frame.setVisible(true);
        int targetX = windowPositionX;
        frame.setLocation(targetX + frame.getWidth(), frame.getY());

        Timer timer = new Timer(3, e -> {
            int currentX = frame.getX();
            if (currentX > targetX) {
                frame.setLocation(currentX - 50, frame.getY());
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
        frame.setAlwaysOnTop(true);
    }

    private JLabel addLabel(final String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(color.LABEL);
        return label;
    }

    private JLabel addLabel(final String text, Consumer<JLabel> modifier) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(color.LABEL);
        label.setSize(new Dimension(panelWidhth, 13));
        modifier.accept(label);
        return label;
    }

    private JLabel addLabel(final JPanel panel, final String text, Consumer<JLabel> modifier) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(color.LABEL);
        modifier.accept(label);
        panel.add(label);
        return label;
    }

    private JButton addStandingButton(final String text, final ActionType actionType, Time value) {
        WindowsTaskbarButton button = new WindowsTaskbarButton(text, color);
        button.addActionListener(action -> standingAction(actionType, value));
        return button;
    }

    private JButton addSittingButton(final String text, final ActionType actionType, Time value) {
        WindowsTaskbarButton button = new WindowsTaskbarButton(text, color);
        button.addActionListener(action -> sittingAction(actionType, value));
        return button;
    }

    private void standingAction(final ActionType actionType, Time value) {
        if (ActionType.INCREASE.equals(actionType)) {
            value.setStandingTime(value.getStandingTime() + 1);
        } else if (ActionType.DECREASE.equals(actionType)) {
            value.setStandingTime(value.getStandingTime() - 1);
            if (value.getStandingTime() <= 0) {
                value.setStandingTime(0);
            }
        }
        standingTimeLabel.setText(value.getStandingTime() + " min");
        standingTimeLabel.repaint();
    }

    private void sittingAction(final ActionType actionType, Time value) {
        if (ActionType.INCREASE.equals(actionType)) {
            value.setSittingTime(value.getSittingTime() + 5);
        } else if (ActionType.DECREASE.equals(actionType)) {
            value.setSittingTime(value.getSittingTime() - 5);
            if (value.getSittingTime() <= 0) {
                value.setSittingTime(0);
            }
        }
        sittingTimeLabel.setText(value.getSittingTime() + " min");
        sittingTimeLabel.repaint();
    }

    private JPanel addMainPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(color.PANEL_COLOR);
                g2d.fillRoundRect(0, 0, panelWidhth, panelHeight, 30, 30);

                g2d.setColor(color.PANEL_BORDER);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, panelWidhth - 1, panelHeight - 1, 30, 30);

                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    public JFrame getFrame() {
        return this.frame;
    }
}
