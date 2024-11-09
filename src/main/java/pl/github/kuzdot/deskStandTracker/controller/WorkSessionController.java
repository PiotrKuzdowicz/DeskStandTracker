package pl.github.kuzdot.deskStandTracker.controller;

import pl.github.kuzdot.deskStandTracker.model.Time;
import pl.github.kuzdot.deskStandTracker.model.WorkSession;

/**
 * WorkSessionController
 */
public class WorkSessionController {

    private static WorkSessionController instance;

    public static WorkSessionController getInstance() {
        if (instance == null) {
            instance = new WorkSessionController();
        }
        return instance;
    }

    public WorkSessionThread createNewSession(Time options) {
        WorkSession workSession = new WorkSession(options);
        WorkSessionThread woThread = new WorkSessionThread(workSession);
        return woThread;
    }

    public void startSession(WorkSessionThread workSessionThread) {
        workSessionThread.start();
    }
    public void stopSession(WorkSessionThread workSessionThread) {
        workSessionThread.stopRunning();
    }

    public void pauseSession(WorkSessionThread workSessionThread) {
        workSessionThread.pauseThread();
    }

    public void resumeSession(WorkSessionThread workSessionThread) {
        workSessionThread.resumeSession();
    }
    public Boolean isPaused(WorkSessionThread workSessionThread){
        return workSessionThread.isPaused();
    }

    public void changeCurrentPosition(WorkSessionThread workSessionThread) {
        workSessionThread.changeCurrentPosition();
    }
}



