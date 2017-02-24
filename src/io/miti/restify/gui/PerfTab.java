package io.miti.restify.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import io.miti.restify.model.ThreadSettings;
import io.miti.restify.util.Utility;
import io.miti.restify.util.WindowState;

/**
 * The is the Performance tab class for the UI.
 */
public final class PerfTab
{
  private static final PerfTab perfTab;
  
  private JPanel perfPanel;
  
  // Output fields
  private JTextArea taOutput;
  private JTextField tfProgress;
  private JTextField tfFailure;
  private JTextField tfMinTime;
  private JTextField tfMaxTime;
  private JTextField tfWorstUrl;
  
  // Input fields
  private JTextArea taUrls;
  private JTextField tfMinDelay;
  private JTextField tfMaxDelay;
  private JTextField tfThreads;
  private JTextField tfRuns;
  private JTextField tfThreshold;

  /** Record the amount of elapsed time for the worst-performing URL */
  private long worstUrlTime;
  
  // The soaker threads
  private final List<Soaker> soakers = new ArrayList<>();
  
  private static String selectedServer;

  static {
    perfTab = new PerfTab();
    perfTab.perfPanel = new JPanel(new GridLayout(2, 1, 20, 20));
    perfTab.perfPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    perfTab.buildTopPanel();
    perfTab.buildBottomPanel();
    perfTab.resetFields();
    perfTab.loadStoredURLs();
  }
  
  private PerfTab() {
    super();
  }

  private void buildBottomPanel() {
    
    JPanel panel = new JPanel(new GridBagLayout());    
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 4;
    c.gridheight = 6;
    c.insets = new Insets(5, 4, 5, 4);
    c.anchor = GridBagConstraints.LINE_START;
    
    taOutput = new JTextArea(8, 25);
    taOutput.setEditable(false);
    panel.add(new JScrollPane(taOutput), c);
    
    c.gridwidth = 1;
    c.gridheight = 1;
    c.gridx = 4;
    panel.add(new JLabel("Progress:"), c);
    
    c.gridx = 5;
    tfProgress = new JTextField(6);
    tfProgress.setEditable(false);
    panel.add(tfProgress, c);
    
    c.gridx = 6;
    panel.add(new JLabel("Failure Rate:"), c);
    
    c.gridx = 7;
    tfFailure = new JTextField(6);
    tfFailure.setEditable(false);
    panel.add(tfFailure, c);
    
    c.gridy = 1;
    c.gridx = 4;
    panel.add(new JLabel("Min Resp Time:"), c);
    
    c.gridx = 5;
    tfMinTime = new JTextField(6);
    tfMinTime.setEditable(false);
    panel.add(tfMinTime, c);
    
    c.gridx = 6;
    panel.add(new JLabel("Max Resp Time:"), c);
    
    c.gridx = 7;
    tfMaxTime = new JTextField(6);
    tfMaxTime.setEditable(false);
    panel.add(tfMaxTime, c);
    
    c.gridy = 2;
    c.gridx = 4;
    panel.add(new JLabel("Worst URL:"), c);
    
    c.gridx = 5;
    c.gridwidth = 3;
    tfWorstUrl = new JTextField(20);
    tfWorstUrl.setEditable(false);
    panel.add(tfWorstUrl, c);
    
    panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Results"));
    
    perfPanel.add(panel);
  }
  
  private void buildTopPanel() {
    
    JPanel panel = new JPanel(new GridBagLayout());    
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 4;
    c.gridheight = 6;
    c.insets = new Insets(5, 4, 5, 4);
    c.anchor = GridBagConstraints.LINE_START;
    
    taUrls = new JTextArea(8, 25);
    panel.add(new JScrollPane(taUrls), c);
    
    c.gridwidth = 1;
    c.gridheight = 1;
    c.gridx = 4;
    panel.add(new JLabel("Min Delay (sec):"), c);
    
    c.gridx = 5;
    tfMinDelay = new JTextField(4);
    panel.add(tfMinDelay, c);
    
    c.gridx = 6;
    panel.add(new JLabel("Max Delay (sec):"), c);
    
    c.gridx = 7;
    tfMaxDelay = new JTextField(4);
    panel.add(tfMaxDelay, c);
    
    c.gridy = 1;
    c.gridx = 4;
    panel.add(new JLabel("# Threads:"), c);
    
    c.gridx = 5;
    tfThreads = new JTextField(4);
    panel.add(tfThreads, c);
    
    c.gridx = 6;
    panel.add(new JLabel("# Runs:"), c);
    
    c.gridx = 7;
    tfRuns = new JTextField(4);
    panel.add(tfRuns, c);
    
    c.gridy = 2;
    c.gridx = 4;
    c.gridwidth = 2;
    panel.add(new JLabel("Failure Threshold (sec):"), c);
    
    c.gridx = 6;
    c.gridwidth = 1;
    tfThreshold = new JTextField(6);
    panel.add(tfThreshold, c);
    
    c.gridy = 3;
    c.gridx = 4;
    c.fill = GridBagConstraints.HORIZONTAL;
    JButton btnStart = new JButton("Start");
    btnStart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        startRun();
      }
    });
    panel.add(btnStart, c);
    
    c.gridx = 5;
    JButton btnStop = new JButton("Stop");
    btnStop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        stopRun();
      }
    });
    panel.add(btnStop, c);
    
    c.gridx = 6;
    JButton btnReset = new JButton("Reset");
    btnReset.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        resetRun();
      }
    });
    panel.add(btnReset, c);
    
    c.gridx = 7;
    JButton btnImport = new JButton("Import HAR");
    btnImport.setToolTipText("Import HAR data from the clipboard");
    btnImport.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        importHAR();
      }
    });
    panel.add(btnImport, c);
    
    panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK),
        "URLs and Settings"));
    
    perfPanel.add(panel);
  }

  /**
   * Import API calls from a HAR session (e.g., from Chrome's "Network" tab in
   * the Developer Tools.
   */
  private void importHAR() {
    
    // Get the raw URLs from the clipboard
    final List<String> urls = getURLsFromHar();
    if (urls.isEmpty()) {
      JOptionPane.showMessageDialog(perfPanel, "Error occurred during HAR import from the clipboard", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    // Filter out any URLs that don't start with the selected server's URL
    filterURLsFromHAR(urls);
    
    // Populate taUrls with the list of found URLs
    if (!urls.isEmpty()) {
      StringBuilder target = new StringBuilder(100);
      for (String url : urls) {
        target.append(url.trim()).append('\n');
      }
      
      taUrls.setText(target.toString().trim());
      taUrls.setCaretPosition(0);
    } else {
      JOptionPane.showMessageDialog(perfPanel, "No matching URLs found in the HAR data", "Warning", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  /**
   * Only include URLs (in the HAR) if they start with the selected server URL.
   *
   * @param urls the list of URLs to filter
   */
  private void filterURLsFromHAR(final List<String> urls) {
    
    // This holds the modified URLs we want to keep
    List<String> targets = new ArrayList<>(20);
    
    // Ensure a server is selected first
    if (selectedServer != null) {
      // Iterate over the URLs
      for (String url : urls) {
        
        // If this URL starts with the selected server, continue processing
        if (url.toLowerCase().startsWith(selectedServer.toLowerCase())) {
          
          // Save everything after selectedServer
          String target = url.substring(selectedServer.length());
          
          // The length must be at least 2 (possibly including "/")
          if (target.length() > 1) {
            targets.add(target.startsWith("/") ? target : ("/" + target));
          }
        }
      }
    }
    
    // Copy the targets to the URLs field
    urls.clear();
    for (String target : targets) {
      urls.add(target);
    }
  }

  /**
   * Retrieve the URLs from the HAR (in the clipboard).
   *
   * @return the list of URLs found in the HAR session
   */
  private List<String> getURLsFromHar() {
    
    // Import any HAR data from the clip board
    final List<String> urls = new ArrayList<>(20);
    try {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      if (clipboard != null) {
        Transferable contents = clipboard.getContents(null);
        if (contents != null) {
          String text = (String) contents.getTransferData(DataFlavor.stringFlavor);
          if ((text != null) && !text.isEmpty()) {
            
            JSONObject jsonObject = new JSONObject(text);
            JSONObject log = jsonObject.getJSONObject("log");
            if (log != null) {
              JSONArray entries = log.getJSONArray("entries");
              if (entries != null) {
                final int numEntries = entries.length();
                for (int i = 0; i < numEntries; ++i) {
                  JSONObject entry = entries.getJSONObject(i);
                  JSONObject req = entry.getJSONObject("request");
                  if (req != null) {
                    String url = (String) req.get("url");
                    if (url != null) {
                      if (includeUrl(url)) {
                        urls.add(url);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    } catch (Exception ex) {
      System.err.println("Exception during HAR import: " + ex.getMessage());
      urls.clear();
    }
    
    return urls;
  }

  /**
   * Determine if a URL should be kept (exclude some common URL-types that we don't care
   * about, such as images and CSS).
   *
   * @param url the URL to check
   * @return whether we should keep the URL
   */
  private static boolean includeUrl(final String url) {
    if ((url == null) || url.trim().isEmpty()) {
      return false;
    } else if (url.endsWith(".html") || url.endsWith(".png") || url.endsWith(".js") ||
        url.endsWith(".jpeg") || url.endsWith(".jpg") || url.endsWith(".css") ||
        url.contains("/js/") || url.contains("/css/")) {
      return false;
    }
    
    return true;
  }

  /**
   * Run the threads to check performance of the selected server.
   */
  private void startRun() {
    
    // Clear the list of soakers
    soakers.clear();
    
    // Check the input parameters
    ThreadSettings ts;
    try {
      ts = getThreadSettings();
    } catch (Exception ex) {
      ts = null;
      JOptionPane.showMessageDialog(perfPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // If an error occurred, exit
    if (ts == null) {
      return;
    }

    // Get the session cookie for the soaker's API calls
    final String cookie = getSessionCookie();
    if (cookie == null) {
      // TODO Authentication can be optional
      // An error occurred.  A message should have been shown to the user, so return.
      return;
    }

    // Create the list of soakers
    final int numThreads = ts.getNumThreads();
    for (int i = 0; i < numThreads; ++i) {
      soakers.add(new Soaker(i + 1, ts));
    }
    
    // Start the list of threads
    soakers.forEach(Thread::start);
  }

  /**
   * Get a valid session cookie.
   *
   * @return the session cookie for the API calls
   */
  private String getSessionCookie() {

    // Check for a cookie already generated (by manual login)
    if (LoginTab.isLoggedIn()) {
      // We have a cookie already, so use that
      return LoginTab.getSessionCookie();
    }

    // The user is not logged in, so check if the user wants to automatically login
    if (LoginTab.autoSignInChecked()) {
      // Log in now
      if (LoginTab.getInstance().signIn()) {
        // Successful login, so return the session cookie
        return LoginTab.getSessionCookie();
      }

      // An error message should have already been shown by the signin() method
      // JOptionPane.showMessageDialog(perfPanel, "Automatic login failed.  Please log in first.", "Error", JOptionPane.ERROR_MESSAGE);
      return null;
    } else {

      // The user has auto-login disabled, and has not manually logged in.  Assume
      // this means the user does not need to log into the server before calling
      // URLs for that server.  Return something other than null (null === error).
      // JOptionPane.showMessageDialog(perfPanel, "You are not signed in, and auto-login is unchecked.\nPlease log in first.", "Error", JOptionPane.ERROR_MESSAGE);
      return "";
    }
  }
  
  /**
   * Signal that a single run completed.
   * 
   * @param ts the thread settings
   * @param url the current URL from the run
   * @param runTime the run time (in ms) for the URL
   */
  public static synchronized void updateProgress(final ThreadSettings ts, final String url, final long runTime,
                                                 final int serverNum, final int runNum) {

    // Update the stateful data
    ts.incrementCompletedCount();
    ts.updateMinResponseTime(runTime);
    ts.updateMaxResponseTime(runTime);

    // Check the failure threshold
    if (runTime > ((long) (ts.getFailureThreshold() * 1000L))) {
      // Handle the failure (the API call took too long)
      ts.incrementFailCount();
    }

    // Check if the runtime is the worst
    if (runTime > perfTab.worstUrlTime) {
      perfTab.worstUrlTime = runTime;
      perfTab.tfWorstUrl.setText(url);
    }

    // Update the output fields (bottom half of the panel)
    final int totalCount = ts.getNumRuns() * ts.getNumThreads() * ts.getURLCount();
    final double progress = 100.0 * (double) ts.getCompletedCount() / (double) totalCount;
    final double failCount = 100.0 * (double) ts.getFailCount() / (double) ts.getCompletedCount();
    perfTab.tfProgress.setText(Utility.toString(progress, 2) + "%");
    perfTab.tfFailure.setText(Utility.toString(failCount, 2) + "%");
    perfTab.tfMinTime.setText(Long.toString(ts.getMinResponseTime()));
    perfTab.tfMaxTime.setText(Long.toString(ts.getMaxResponseTime()));

    // Add the info to the output control - taOutput
    String result = String.format("%d,%d,%s,%d\n", serverNum, runNum, url, runTime);
    perfTab.addOutputEvent(result);
  }


  /**
   * Add an event to the text control.
   *
   * @param event the event description
   */
  private void addOutputEvent(final String event) {

    // This method can get called from either the EDT or another
    // thread, so handle both cases
    if (SwingUtilities.isEventDispatchThread()) {
      // We're on the EDT, so just add the line to the component
      taOutput.append(event);
    } else {
      // Not on the EDT, so set the text on the EDT
      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          @Override
          public void run() {
            taOutput.append(event);
          }
        });
      } catch (Exception ex) {
        System.err.println("Exception adding event to perf tab: " + ex.getMessage());
      }
    }
  }


  /**
   * Save the thread settings from the options in the UI.
   *
   * @return the settings for the soaker threads
   * @throws Exception thrown for bad input
   */
  private ThreadSettings getThreadSettings() throws Exception {
    
    // Populate our object with the data from the input section of the screen
    final ThreadSettings ts = new ThreadSettings();
    ts.setMinDelay(Utility.getStringAsFloat(tfMinDelay.getText(), -1.0f, -1.0f));
    ts.setMaxDelay(Utility.getStringAsFloat(tfMaxDelay.getText(), -1.0f, -1.0f));
    ts.setNumThreads(Utility.getStringAsInteger(tfThreads.getText(), -1, -1));
    ts.setNumRuns(Utility.getStringAsInteger(tfRuns.getText(), -1, -1));
    ts.setFailureThreshold(Utility.getStringAsFloat(tfThreshold.getText(), -1.0f, -1.0f));
    
    // Add the URLs
    final String urls = taUrls.getText();
    if (urls != null) {
      String[] split = urls.trim().split("\n");
      if (split.length > 0) {
        for (String url : split) {
          String line = url.trim();
          // If it starts with a ; then treat it as a comment
          if ((line.length() > 1) && !line.startsWith(";")) {
            ts.addURL(line);
          }
        }
      }
    }
    
    // Set the selected server
    ts.setSelectedServer(selectedServer);
    
    // Check for errors
    if (ts.getMinDelay() < 0.0f) {
      throw new Exception("The minimum delay must be a non-negative floating point number");
    } else if (ts.getMaxDelay() < ts.getMinDelay()) {
      throw new Exception("The maximum delay must be greater than or equal to the minimum delay");
    } else if (ts.getNumThreads() < 1) {
      throw new Exception("The number of threads must be a positive integer");
    } else if (ts.getNumRuns() < 1) {
      throw new Exception("The number of runs must be a positive integer");
    } else if (ts.getFailureThreshold() < 0) {
      throw new Exception("The failure threshold must be a non-negative floating point number");
    } else if (ts.getURLCount() == 0) {
      throw new Exception("No URLs were specified");
    } else if ((selectedServer == null) || selectedServer.isEmpty()) {
      throw new Exception("No server is selected");
    }
    
    // If we reach this point, the input looks good
    return ts;
  }
  
  /**
   * Set a flag for any running threads to stop processing.
   */
  private void stopRun() {
    // Set the halt-process flag for each thread
    for (Soaker t : soakers) {
      t.haltThread();
    }
  }
  
  /**
   * Reset the input and output fields for the page.
   */
  private void resetRun() {
    resetFields();
  }
  
  public static void setSelectedServer(final String url) {
    selectedServer = url;
  }
  
  private void resetFields() {
    // Clear the output
    taOutput.setText("");
    tfProgress.setText("");
    tfFailure.setText("");
    tfMinTime.setText("");
    tfMaxTime.setText("");
    tfWorstUrl.setText("");
    
    // Set the default input values
    tfMinDelay.setText("2");
    tfMaxDelay.setText("5");
    tfThreads.setText("10");
    tfRuns.setText("5");
    tfThreshold.setText("10");

    worstUrlTime = 0L;
  }
  
  public static JPanel getPanel() {
    return perfTab.perfPanel;
  }
  
  public static String getURLText() {
    return perfTab.taUrls.getText();
  }

  /**
   * Load the list of URLs from the properties file.
   */
  private void loadStoredURLs() {
    
    // Get any saved URL text from the properties file
    String urlText = WindowState.getInstance().getURLs();
    if ((urlText != null) && !urlText.isEmpty()) {
      // Text was found, so save it now
      taUrls.setText(urlText);
    }
  }
}
