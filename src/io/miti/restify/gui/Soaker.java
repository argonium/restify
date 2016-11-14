package io.miti.restify.gui;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import io.miti.restify.model.ThreadSettings;
import io.miti.restify.util.Utility;

public final class Soaker extends Thread {
  
  private int serverNum;
  
  private ThreadSettings ts;
  
  private String cookie;
  
  private boolean haltProcess = false;

  public Soaker(final int serverNum, final ThreadSettings ts, final String cookie) {
    this.serverNum = serverNum;
    this.ts = ts;
    this.cookie = cookie;
  }

  @Override
  public void run() {
    
    // For this thread, iterate over the list of URLs the number of times
    // requested by the user (the number of runs)
    for (int i = 0; i < ts.getNumRuns() && !haltProcess; ++i) {
      
      // Iterate over the list of URLs
      final Iterator<String> urls = ts.getURLs();
      while (urls.hasNext() && !haltProcess) {
        final String url = urls.next();
        
        // TODO Make the call, and compare the execution time against the failure threshold
        final long startTime = System.currentTimeMillis();
        // Make the call
        final long timeDelta = System.currentTimeMillis() - startTime;
        
        // Check the threshold
        if (timeDelta > ((long) (ts.getFailureThreshold() * 1000L))) {
          // TODO Handle the failure (the API call took too long)
        }
        
        // Sleep for some duration (between min and max)
        if (!haltProcess) {
          int sleepDuration = ThreadLocalRandom.current().nextInt((int) (ts.getMinDelay() * 1000), ((int) (ts.getMaxDelay() * 1000)) + 1);
          Utility.sleep(sleepDuration);
        }
      }
    }
  }
  
  /**
   * Tell the thread to stop processing as soon as possible.
   */
  public void haltThread() {
    haltProcess = true;
  }
}
