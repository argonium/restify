package io.miti.restify.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
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

  private String sessionCookie;
  
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

  public String getSessionCookie() {
    return sessionCookie;
  }

  public void setSessionCookie(final String sCookie) {
    sessionCookie = sCookie;
  }
}
