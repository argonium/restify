package io.miti.restify.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsulate a snapshot of a performance run.
 *
 * Created by mike on 2/24/17.
 */
public final class Snapshot {

    static class Result {

        private int threadNumber;

        private int runNumber;

        private String url;

        private long runTime;

        public int getThreadNumber() {
            return threadNumber;
        }

        public void setThreadNumber(int threadNumber) {
            this.threadNumber = threadNumber;
        }

        public int getRunNumber() {
            return runNumber;
        }

        public void setRunNumber(int runNumber) {
            this.runNumber = runNumber;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getRunTime() {
            return runTime;
        }

        public void setRunTime(long runTime) {
            this.runTime = runTime;
        }
    }

    private String runDate;

    private int numThreads;

    private int numRuns;

    private int urlCount;

    private List<String> urls = new ArrayList<>(10);

    private List<Result> results;

    private String worstUrl;

    private long worstUrlTime;

    private int failCount;

    private long minResponseTime = Long.MAX_VALUE;

    private long maxResponseTime = Long.MIN_VALUE;

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getNumRuns() {
        return numRuns;
    }

    public void setNumRuns(int numRuns) {
        this.numRuns = numRuns;
    }

    public int getUrlCount() {
        return urlCount;
    }

    public void setUrlCount(int urlCount) {
        this.urlCount = urlCount;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setUrls(final Iterator<String> pUrls) {

        while (pUrls.hasNext()) {
            urls.add(pUrls.next());
        }
    }

    public void clearUrls() {
        urls.clear();
    }

    public void addUrl(final String url) {
        urls.add(url);
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void clearResults() {
        results.clear();
    }

    public void addResult(final int thread, final int run, final String url, final long runTime) {
        final Result result = new Result();
        result.setThreadNumber(thread);
        result.setRunNumber(run);
        result.setUrl(url);
        result.setRunTime(runTime);
        results.add(result);
    }

    public String getWorstUrl() {
        return worstUrl;
    }

    public void setWorstUrl(String worstUrl) {
        this.worstUrl = worstUrl;
    }

    public long getWorstUrlTime() {
        return worstUrlTime;
    }

    public void setWorstUrlTime(long worstUrlTime) {
        this.worstUrlTime = worstUrlTime;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public void incrementFailCount() {
        failCount++;
    }

    public long getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(long minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public void updateMinResponseTime(final long lCurrTime) {
        minResponseTime = Math.min(minResponseTime, lCurrTime);
    }

    public long getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(long maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public void updateMaxResponseTime(final long lCurrTime) {
        maxResponseTime = Math.max(maxResponseTime, lCurrTime);
    }
}
