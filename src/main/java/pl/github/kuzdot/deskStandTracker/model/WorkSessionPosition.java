package pl.github.kuzdot.deskStandTracker.model;

/**
 * WorkSessionPosition
 */
public enum WorkSessionPosition {
    STANDING("Standing"), SITTING("Sitting");

    private String name;

    public String getName() {
        return name;
    }

    WorkSessionPosition(String name) {
        this.name = name;
    }

}



