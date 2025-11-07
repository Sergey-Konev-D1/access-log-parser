import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    public void addEntry(LogEntry entryLogLine) {
        totalTraffic += entryLogLine.getResponseSize();

        LocalDateTime time = entryLogLine.getDateTime();
        if (minTime == null || time.isBefore(minTime)) {
            minTime = time;
        }
        if (maxTime == null || time.isAfter(maxTime)) {
            maxTime = time;
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }
        long hoursBetween = ChronoUnit.HOURS.between(maxTime, minTime);
        if(hoursBetween == 0){
            return totalTraffic;
        }
        return (double) totalTraffic / hoursBetween;
    }
}
