package io.miti.restify.gui;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.miti.restify.model.ThreadSettings;
import io.miti.restify.util.Utility;

public final class Soaker extends Thread {
  
  private final int serverNum;
  
  private final ThreadSettings ts;

  private final String cookie;

  private boolean haltProcess = false;

  public Soaker(final int serverNum, final ThreadSettings ts, final String cookie) {
    super("Soaker-" + serverNum);
    this.serverNum = serverNum;
    this.ts = ts;
    this.cookie = cookie;
  }

  /**
   * Run one thread.
   */
  @Override
  public void run() {
    
    // For this thread, iterate over the list of URLs the number of times
    // requested by the user (the number of runs)
    final String server = normalizeServer(ts.getSelectedServer());
    if (Utility.isStringEmpty(server)) {
      ConsoleTab.addEvent(serverNum, 0, "No selected server.  Stopping.");
      return;
    }

    for (int i = 0; i < ts.getNumRuns() && !haltProcess; ++i) {
      
      // Iterate over the list of URLs
      final Iterator<String> urls = ts.getURLs();
      while (urls.hasNext() && !haltProcess) {
        final String url = urls.next();
        
        // Make the call, and compare the execution time against the failure threshold
        final long startTime = System.currentTimeMillis();

        // Make the call, and check for an error
        final boolean result = executeUrl(server + url, i + 1);

        // Record the amount of elapsed time
        final long timeDelta = System.currentTimeMillis() - startTime;

        // If there was an error, stop the test
        haltProcess = !result;

        // Log the run
        PerfTab.updateProgress(ts, url, timeDelta);
        
        // Sleep for some duration (between min and max)
        if (!haltProcess) {
          int sleepDuration = ThreadLocalRandom.current().nextInt((int) (ts.getMinDelay() * 1000), ((int) (ts.getMaxDelay() * 1000)) + 1);
          Utility.sleep(sleepDuration);
        }
      }
    }
  }

  /**
   * Ensure the server name does not end with a "/", since that's assumed
   * to be the first character of any API call.
   *
   * @param hostname the selected server (should be of the format http://xyz
   * @return the hostname with any trailing "/" removed
   */
  private String normalizeServer(final String hostname) {

    // If the hostname is null or empty, return null (error)
    if (Utility.isStringEmpty(hostname)) {
      return null;
    }

    // If the hostname neds with a "/", return everything in the string before it
    if (hostname.endsWith("/")) {
      return hostname.substring(0, hostname.length() - 1);
    }

    // The hostname should be fine
    return hostname;
  }

  /**
   * Call the URL endpoint.
   *
   * @param url the URL to call
   * @param runIndex the (1-based) index of the current run number
   * @return whether the call was successful or not
   */
  private boolean executeUrl(final String url, final int runIndex) {

    boolean result = true;
    try {
      // Make the URL call
      final HttpResponse<String> response = Unirest.get(url).asString();

      // Check the response status
      final int status = response.getStatus();

      // Log the event
      final String event = String.format("Status for %s is %d: %s", url, status, response.getStatusText());
      ConsoleTab.addEvent(serverNum, runIndex, event);

      // Check the status
      result = ((status >= 200) && (status < 300));
    } catch (Exception ue) {

      // Log the exception to the Console tab
      final String event = String.format("Exception for %s: %s", url, ue.getMessage());
      ConsoleTab.addEvent(serverNum, runIndex, event);
      result = false;
    }

    return result;
  }
  
  /**
   * Tell the thread to stop processing as soon as possible.
   */
  public void haltThread() {
    haltProcess = true;
  }
}
