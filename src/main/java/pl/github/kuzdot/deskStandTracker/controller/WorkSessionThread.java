package pl.github.kuzdot.deskStandTracker.controller;

import java.awt.TrayIcon.MessageType;

import pl.github.kuzdot.deskStandTracker.model.Notification;
import pl.github.kuzdot.deskStandTracker.model.WorkSession;
import pl.github.kuzdot.deskStandTracker.model.WorkSessionPosition;
import pl.github.kuzdot.deskStandTracker.util.StringUtil;

/**
 * WorkSessionThread
 */
public class WorkSessionThread extends Thread {

    private final WorkSession workSession;
    private volatile boolean isRunning = true;
    private volatile boolean isPaused = false;
    private volatile long lastChecking;
    private volatile boolean firstRun = true;

    public WorkSessionThread(WorkSession workSession) {
        this.workSession = workSession;
    }

    @Override
    public void run() {
        if (firstRun) {
            workSession.startCounting();
            firstRun = false;
        }
        lastChecking = System.currentTimeMillis();
        while (isRunning) {
            if (!isPaused) {
                workSession.calculateWindow();
            }
            synchronized (this) {
                while (isPaused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if (System.currentTimeMillis() - lastChecking >= 900) {
                checkState(System.currentTimeMillis());
                lastChecking = System.currentTimeMillis();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkState(long currentTime) {
        System.out.println("--------");
        System.out.println(workSession.getCurrentPosition());
        if (WorkSessionPosition.SITTING.equals(workSession.getCurrentPosition())) {
            sittingLogic(currentTime);
        } else if (WorkSessionPosition.STANDING.equals(workSession.getCurrentPosition())) {
            standingLogic(currentTime);
        } else {
            EventProvider.getInstance().execute(
                    new Notification("ERROR", "Error in program - cannot check current state", MessageType.ERROR));
            isRunning = false;
        }

    }

    private void standingLogic(long currentTime) {
        standingLogic(currentTime, Boolean.FALSE);
    }

    private void standingLogic(long currentTime, Boolean force) {
        int interval = workSession.getTime().getStandingTime();
        long intervalMilis = interval * 60000;
        Long startMilis = workSession.getLastPositionTime();
        Long pauseMilis = workSession.getPauseTime();
        Long diffMilis = currentTime - startMilis;
        if (pauseMilis != null) {
            diffMilis = diffMilis - pauseMilis;
        }
        System.out.println("Interval - " + intervalMilis);
        System.out.println("DifMilis - " + diffMilis);
        if (diffMilis >= intervalMilis || force) {
            workSession.changePosition(currentTime, diffMilis);
            EventProvider.getInstance().execute(
                    new Notification("INFO", "Last session time: " + StringUtil.formatMillis(diffMilis) + " Please sit down", MessageType.INFO));
        }
    }

    private void sittingLogic(long currentTime) {
        sittingLogic(currentTime, Boolean.FALSE);
    }

    private void sittingLogic(long currentTime, Boolean force) {
        int interval = workSession.getTime().getSittingTime();
        long intervalMilis = interval * 60000;
        Long startMilis = workSession.getLastPositionTime();
        Long pauseMilis = workSession.getPauseTime();
        Long diffMilis = currentTime - startMilis;
        if (pauseMilis != null) {
            diffMilis = diffMilis - pauseMilis;
        }
        System.out.println("Interval - " + intervalMilis);
        System.out.println("StartMilis - " + startMilis);
        System.out.println("DifMilis - " + diffMilis);
        if (diffMilis >= intervalMilis || force) {
            workSession.changePosition(currentTime, diffMilis);
            EventProvider.getInstance().execute(
                    new Notification("INFO", "Last session time: " + StringUtil.formatMillis(diffMilis) + " Please stand up", MessageType.INFO));
        }
    }

    public void stopRunning() {
        workSession.stopCounting();
        isRunning = false;
        resumeThread();
    }

    public synchronized void pauseThread() {
        workSession.pauseCounting();
        isPaused = true;
    }

    public synchronized void resumeThread() {
        isPaused = false;
        notify();
    }

    public Boolean isPaused() {
        return isPaused;
    }

    public void resumeSession() {
        workSession.resumeCounting();
        resumeThread();
    }

    public void changeCurrentPosition() {
        long currentTime = System.currentTimeMillis();
        if (WorkSessionPosition.SITTING.equals(workSession.getCurrentPosition())) {
            sittingLogic(currentTime,true);
        } else if (WorkSessionPosition.STANDING.equals(workSession.getCurrentPosition())) {
            standingLogic(currentTime,true);
        } else {
            EventProvider.getInstance().execute(
                    new Notification("ERROR", "Error in program - cannot check current state", MessageType.ERROR));
            isRunning = false;
        }
    }

}



