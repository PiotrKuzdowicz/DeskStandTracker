package pl.github.kuzdot.deskStandTracker.model;

/**
 * Time
 */
public class Time {

    private int standingTime;
    private int sittingTime;

    public Time(int standingTime, int sittingTime) {
        this.standingTime = standingTime;
        this.sittingTime = sittingTime;
    }

    public int getStandingTime() {
        return standingTime;
    }

    public void setStandingTime(int standingTime) {
        this.standingTime = standingTime;
    }

    public int getSittingTime() {
        return sittingTime;
    }

    public void setSittingTime(int sittingTime) {
        this.sittingTime = sittingTime;
    }

}



