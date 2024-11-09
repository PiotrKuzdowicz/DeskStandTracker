package pl.github.kuzdot.deskStandTracker.model;

import java.awt.TrayIcon.MessageType;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;

import jiconfont.IconCode;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;
import pl.github.kuzdot.deskStandTracker.controller.EventProvider;
import pl.github.kuzdot.deskStandTracker.view.ClockWindow;

/**
 * WorkSession
 */
public class WorkSession implements Serializable {

    private Time time;
    private Long startTime;
    private Long stopTime;
    private Long pauseTime;
    private Long startPauseTime;
    private Long stopPauseTime;
    private Long lastPositionTime;
    private Boolean isCounting;
    private Boolean pause;
    private List<WorkSessionTime> standingTime;
    private List<WorkSessionTime> sittingTime;
    private WorkSessionPosition currentPosition;

    public WorkSession(Time time) {
        this.time = time;
        this.pause = false;
        standingTime = new ArrayList<>();
        sittingTime = new ArrayList<>();
        isCounting = false;
        pauseTime = 0L;
    }

    public Time getTime() {
        return time;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public Boolean getIsCounting() {
        return isCounting;
    }

    public List<WorkSessionTime> getStandingTime() {
        return standingTime;
    }

    public List<WorkSessionTime> getSittingTime() {
        return sittingTime;
    }

    public void startCounting() {
        this.isCounting = true;
        if (startTime == null) {
            startTime = System.currentTimeMillis();
            lastPositionTime = System.currentTimeMillis();
            currentPosition = WorkSessionPosition.STANDING;
            pauseTime = 0L;
            EventProvider.getInstance()
                    .execute(new Notification("Start new session", "Please Stand up", MessageType.INFO));
            changePositionIcon(FontAwesome.MALE);
        }
    }

    public void resumeCounting() {
        this.isCounting = true;
        this.pause = false;
        stopPauseTime = System.currentTimeMillis();
        if (stopPauseTime != null) {
            pauseTime = pauseTime + (stopPauseTime - startPauseTime);
        }
        EventProvider.getInstance()
                .execute(new Notification("Pause session", "Session is started again", MessageType.INFO));
        if (WorkSessionPosition.SITTING.equals(currentPosition)) {
            changePositionIcon(GoogleMaterialDesignIcons.AIRLINE_SEAT_RECLINE_NORMAL);
        } else if (WorkSessionPosition.STANDING.equals(currentPosition)) {
            changePositionIcon(FontAwesome.MALE);
        } else {
            EventProvider.getInstance().execute(
                    new Notification("ERROR", "Error in program - cannot check current state", MessageType.ERROR));
        }
    }

    public void pauseCounting() {
        this.isCounting = false;
        this.pause = true;
        startPauseTime = (System.currentTimeMillis());
        EventProvider.getInstance().execute(new Notification("Pause session", "Session is paused", MessageType.INFO));
        changePositionIcon(FontAwesome.PAUSE);
    }

    public void stopCounting() {
        this.isCounting = false;
        stopTime = (System.currentTimeMillis());
        changePositionIcon(FontAwesome.STOP);
        List<WorkSessionTime> workSession = new ArrayList<>();
        workSession.addAll(standingTime);
        workSession.addAll(sittingTime);
        workSession.sort(new Comparator<WorkSessionTime>() {
            @Override
            public int compare(WorkSessionTime o1, WorkSessionTime o2) {
                if (o1.getId() > o2.getId()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        EventProvider.getInstance().execute(new Notification("Stop session",
                workSession.toString().replace("[", "").replace("]", "").replace(",", ""), MessageType.INFO));
    }

    public WorkSessionPosition getCurrentPosition() {
        return currentPosition;
    }

    public Long getLastPositionTime() {
        return lastPositionTime;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = stopTime;
    }

    public void setPauseTime(Long pauseTime) {
        this.pauseTime = pauseTime;
    }

    public void setLastPositionTime(Long lastPositionTime) {
        this.lastPositionTime = lastPositionTime;
    }

    public void setIsCounting(Boolean isCounting) {
        this.isCounting = isCounting;
    }

    public void setStandingTime(List<WorkSessionTime> standingTime) {
        this.standingTime = standingTime;
    }

    public void setSittingTime(List<WorkSessionTime> sittingTime) {
        this.sittingTime = sittingTime;
    }

    public void setCurrentPosition(WorkSessionPosition currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void changePosition(long milis, long time) {
        if (WorkSessionPosition.SITTING.equals(currentPosition)) {
            currentPosition = WorkSessionPosition.STANDING;
            sittingTime.add(new WorkSessionTime(sittingTime.size() + standingTime.size() + 1, time,
                    WorkSessionPosition.SITTING));
            changePositionIcon(FontAwesome.MALE);
        } else if (WorkSessionPosition.STANDING.equals(currentPosition)) {
            currentPosition = WorkSessionPosition.SITTING;
            standingTime.add(new WorkSessionTime(standingTime.size() + standingTime.size() + 1, time,
                    WorkSessionPosition.STANDING));
            changePositionIcon(GoogleMaterialDesignIcons.AIRLINE_SEAT_RECLINE_NORMAL);
        } else {
            EventProvider.getInstance().execute(
                    new Notification("ERROR", "Error in program - cannot check current state", MessageType.ERROR));
        }
        lastPositionTime = milis;
    }

    private void changePositionIcon(IconCode faIcon) {
        ClockWindow clockWindow = ClockWindow.getInstance();
        Icon icon = IconFontSwing.buildIcon(faIcon, clockWindow.currenPositionItemSize, clockWindow.color.LABEL);
        clockWindow.currenPositionLabel.setIcon(icon);
        clockWindow.currenPositionLabel.repaint();
    }

    public void calculateWindow() {
        ClockWindow clockWindow = ClockWindow.getInstance();
        Long currentTime = System.currentTimeMillis() - startTime;
        currentTime = currentTime - pauseTime;
        clockWindow.currentSessionLabel.setText(formatMillis(currentTime));
    }

    private String formatMillis(long millis) {
        Duration duration = Duration.ofMillis(millis);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}
