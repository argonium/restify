package io.miti.restify.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Encapsulate the thread settings passed from PerfTab to Soaker, and
 * in the callback from Soaker to PerfTab (to update the progress).
 *
 * @author mike
 */
public final class ThreadSettings
{
  private float minDelay;
  
  private float maxDelay;
  
  private int numThreads;
  
  private int numRuns;
  
  private float failureThreshold;
  
  private final List<String> urls = new ArrayList<>(20);
  
  private String selectedServer;

  private int failCount;

  private int completedCount;

  private long minResponseTime = Long.MAX_VALUE;

  private long maxResponseTime = Long.MIN_VALUE;

  /**
   * Default constructor.
   */
  public ThreadSettings() {
    super();
  }

  public float getMinDelay() {
    return minDelay;
  }

  public void setMinDelay(float minDelay) {
    this.minDelay = minDelay;
  }

  public float getMaxDelay() {
    return maxDelay;
  }

  public void setMaxDelay(float maxDelay) {
    this.maxDelay = maxDelay;
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

  public float getFailureThreshold() {
    return failureThreshold;
  }

  public void setFailureThreshold(float failureThreshold) {
    this.failureThreshold = failureThreshold;
  }
  
  public void addURL(final String url) {
    if ((url != null) && !url.trim().isEmpty()) {
      urls.add(url);
    }
  }
  
  public int getURLCount() {
    return urls.size();
  }
  
  public Iterator<String> getURLs() {
    return urls.iterator();
  }
  
  public void setSelectedServer(final String pServer) {
    selectedServer = pServer;
  }
  
  public String getSelectedServer() {
    return selectedServer;
  }

  public int getFailCount() {
    return failCount;
  }

  public void incrementFailCount() {
    failCount++;
  }

  public int getCompletedCount() {
    return completedCount;
  }

  public void incrementCompletedCount() {
    completedCount++;
  }

  public long getMinResponseTime() {
    return minResponseTime;
  }

  public void updateMinResponseTime(final long lCurrTime) {
    minResponseTime = Math.min(minResponseTime, lCurrTime);
  }

  public long getMaxResponseTime() {
    return maxResponseTime;
  }

  public void updateMaxResponseTime(final long lCurrTime) {
    maxResponseTime = Math.max(maxResponseTime, lCurrTime);
  }
}
