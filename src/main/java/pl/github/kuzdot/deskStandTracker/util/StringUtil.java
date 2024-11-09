package pl.github.kuzdot.deskStandTracker.util;

import java.time.Duration;

/**
 * StringUtil
 */
public class StringUtil {

    
    public static String formatMillis(long millis) {
        Duration duration = Duration.ofMillis(millis);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }
}



