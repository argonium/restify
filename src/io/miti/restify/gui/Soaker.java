package io.miti.restify.gui;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import io.miti.restify.model.ThreadSettings;
import io.miti.restify.util.ResponseCodeCache;
import io.miti.restify.util.Utility;

/**
 * This class represents a single thread used for the performance
 * test (called by PerfTab).
 */
public final class Soaker extends Thread {
  
  private final int serverNum;
  
  private final ThreadSettings ts;

  private boolean haltProcess = false;

  public Soaker(final int serverNum, final ThreadSettings ts) {
    super("Soaker-" + serverNum);
    this.serverNum = serverNum;
    this.ts = ts;
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

        // Sleep for some duration (between min and max)
        if (!haltProcess) {
          final int sleepDuration = getSleepDuration(ts.getMinDelay(), ts.getMaxDelay());
          if (sleepDuration > 0) {
            Utility.sleep(sleepDuration);
          }
        }

        // Check if the execution has been stopped
        if (haltProcess) {
          break;
        }

        // Get the next URL to call
        final String url = urls.next();
        
        // Record the start time, so we can later compute the amount of elapsed time
        final long startTime = System.currentTimeMillis();

        // Make the call, and check for an error
        final boolean result = executeUrl(server + url, i + 1);

        // Record the amount of elapsed time
        final long timeDelta = System.currentTimeMillis() - startTime;

        // If there was an error, stop the test
        haltProcess = haltProcess || !result;

        // Log the run
        PerfTab.updateProgress(ts, url, timeDelta, serverNum, i + 1);
      }
    }
  }

  /**
   * Get the amount of time to sleep.
   *
   * @param minDelay the minimum amount of time to sleep (in seconds)
   * @param maxDelay the maximum amount of time to sleep (in seconds)
   * @return the amount of time to sleep (in milliseconds)
   */
  private static int getSleepDuration(final float minDelay, final float maxDelay) {

    // Convert the input parameters from seconds to milliseconds
    final int min = (int) (minDelay * 1000.0f);
    final int max = (int) (maxDelay * 1000.0f);

    // Check for zero, or equal values
    if ((max == 0) || (min == max)) {
      return max;
    }

    // Return a random number in the inclusive range (add 1 to max since nextInt is exclusive
    // on the maximum value parameter)
    return ThreadLocalRandom.current().nextInt(min, max + 1);
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

    // If the hostname ends with a "/", return everything in the string before it
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
      // Make the URL call (Unirest stores the cookie from the earlier login call)
      final HttpResponse<String> response = Unirest.get(url).asString();

      // Check the response status
      final int status = response.getStatus();

      // Log the event
      final String event = String.format("Status for %s is %d: %s", url, status,
              ResponseCodeCache.getCache().getResponseMessage(status));
      ConsoleTab.addEvent(serverNum, runIndex, event);

      // Check the status (if it's not a 2xx code, there was some failure)
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
