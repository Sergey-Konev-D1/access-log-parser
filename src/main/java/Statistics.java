import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> linkPages = new HashSet<>();
    private HashSet<String> pageNotFound = new HashSet<>();
    private HashMap<String, Integer> osCounter = new HashMap<>();
    private HashMap<String, Integer> browserCounter = new HashMap<>();
    private int realUserVisit = 0;
    private int errorReq = 0;
    private Set<String> realUserIp = new LinkedHashSet<>();

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

        if (entryLogLine.getResponseCode() == 200){
            linkPages.add(entryLogLine.getPath());
        }

        if (entryLogLine.getResponseCode() == 404){
            pageNotFound.add(entryLogLine.getPath());
        }

        String os = entryLogLine.getUserAgent().getOperationSystem();
        if (os != null && !os.trim().isEmpty()){
            os = os.trim();
        }
        if (osCounter.containsKey(os)){
            int tmpCurrentCountOs = osCounter.get(os);
            osCounter.put(os, tmpCurrentCountOs + 1);
        } else {
            osCounter.put(os, 1);
        }

        String browser = entryLogLine.getUserAgent().getBrowser();
        if (browser != null && !browser.trim().isEmpty()){
            browser = browser.trim();
        }
        if (browserCounter.containsKey(browser)){
            int tmpCerrentCountBrwsr = browserCounter.get(browser);
            browserCounter.put(browser, tmpCerrentCountBrwsr + 1);
        } else {
            browserCounter.put(browser,1);
        }

        boolean isBot = entryLogLine.getUserAgent().getUserAgentString().toLowerCase().contains("bot");

        if(!isBot){
            realUserVisit++;
            realUserIp.add(entryLogLine.getIpAddress());
        }

        if(entryLogLine.getResponseCode() >= 400 && entryLogLine.getResponseCode() <= 505){
            errorReq++;
        }
    }

    public Set<String> getRealUserIp() {
        return realUserIp;
    }

    public HashSet<String> getLinkPages() {
        return linkPages;
    }

    public HashSet<String> getPageNotFound() {
        return pageNotFound;
    }

    public HashMap<String,Double> getOsStatistics(){
        HashMap<String,Double> resOs = new HashMap<>();
        int totalCountOs = 0;

        ArrayList<Integer> countsListOs = new ArrayList<>(osCounter.values());
        for (int i = 0; i < countsListOs.size(); i++){
            totalCountOs +=countsListOs.get(i);
        }
        if (totalCountOs == 0){
            return resOs;
        }

        ArrayList<String> osNames = new ArrayList<>(osCounter.keySet());
        for (int i = 0; i < osNames.size(); i++) {
            String osName = osNames.get(i);
            int count = osCounter.get(osName);
            double fraction =(double) count / totalCountOs;
            resOs.put(osName,fraction);
        }
        return resOs;

    }

    public HashMap<String, Double> getBrowserStatistics() {
        HashMap<String, Double> resBrowser = new HashMap<>();
        int totalCountBrowser = 0;

        ArrayList<Integer> countListBrowser = new ArrayList<>(browserCounter.values());
        for (int i = 0; i < countListBrowser.size(); i++){
            totalCountBrowser += countListBrowser.get(i);
        }
        if (totalCountBrowser == 0){
            return resBrowser;
        }

        ArrayList<String> browserNames = new ArrayList<>(browserCounter.keySet());
        for(int i =0; i < browserNames.size(); i++){
            String browserName = browserNames.get(i);
            int count  = browserCounter.get(browserName);
            double fraction = (double) count / totalCountBrowser;
            resBrowser.put(browserName,fraction);
        }
        return resBrowser;
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null) {
            return 0;
        }
        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        System.out.println(hoursBetween);
        if(hoursBetween == 0){
            return totalTraffic;
        }
        return (double) totalTraffic / hoursBetween;
    }

    public double getAverageNumberOfWebsiteVisitsPerHour(){
        if(minTime == null || maxTime == null){
            return 0;
        }
        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        if(hoursBetween == 0){
            return realUserVisit;
        }
        return (double) realUserVisit / hoursBetween;
    }

    public double  getAverageNumberOfIncorrectRequestsPerHour(){
        if(minTime == null || maxTime == null){
            return 0;
        }
        long hoursBetween = ChronoUnit.HOURS.between(minTime, maxTime);
        if(hoursBetween == 0){
            return errorReq;
        }
        return (double) errorReq / hoursBetween;
    }

    public double getAverageAttendancePerUser(){
        if(realUserIp.isEmpty()){
            return 0;
        }
        return (double) realUserVisit / realUserIp.size();
    }
}
