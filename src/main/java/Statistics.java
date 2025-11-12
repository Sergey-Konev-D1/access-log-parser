import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> linkPages = new HashSet<>();
    private HashMap<String, Integer> osCounter = new HashMap<>();

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

        if(entryLogLine.getResponseCode() == 200){
            linkPages.add(entryLogLine.getPath());
        }

        String os = entryLogLine.getUserAgent().getOperationSystem();
        if(os != null && !os.trim().isEmpty()){
            os = os.trim();
        }
        if(osCounter.containsKey(os)){
            int tmpCurrentCount = osCounter.get(os);
            osCounter.put(os, tmpCurrentCount + 1);
        } else {
            osCounter.put(os, 1);
        }
    }

    public HashSet<String> getLinkPages() {
        return linkPages;
    }

    public HashMap<String,Double> getOsStatistics(){
        HashMap<String,Double> res = new HashMap<>();
        int totalCountOs = 0;

        ArrayList<Integer> countsList = new ArrayList<>(osCounter.values());
        for (int i = 0; i < countsList.size(); i++){
            totalCountOs +=countsList.get(i);
        }
        if (totalCountOs == 0){
            return res;
        }

        ArrayList<String> osNames = new ArrayList<>(osCounter.keySet());
        for(int i = 0; i < osNames.size(); i++) {
            String osName = osNames.get(i);
            int count = osCounter.get(osName);
            double fraction =(double) count / totalCountOs;
            res.put(osName,fraction);
        }
        return res;

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
