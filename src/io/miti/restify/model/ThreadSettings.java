package io.miti.restify.model;

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
}
