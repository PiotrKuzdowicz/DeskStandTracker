package pl.github.kuzdot.deskStandTracker.model;

import java.io.Serializable;

import pl.github.kuzdot.deskStandTracker.util.StringUtil;

/**
 * WorkSessionTime
 */
public class WorkSessionTime implements Serializable {

    private long id;
    private long time;
    private WorkSessionPosition position;

    public WorkSessionTime(long id, long time, WorkSessionPosition position) {
        this.id = id;
        this.time = time;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Lp: "+id+", Position: "+position.getName()+", Time: "+StringUtil.formatMillis(time)+"\n";
    }

}



