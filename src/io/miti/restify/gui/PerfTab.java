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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import io.miti.restify.model.ThreadSettings;
import io.miti.restify.util.Utility;
import io.miti.restify.util.WindowState;

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
  
  protected void importHAR() {
    
    // Get the raw URLs from the clipboard
    final List<String> urls = getURLsFromHar();
    if (urls.isEmpty()) {
      JOptionPane.showMessageDialog(perfPanel, "Error occurred during HAR import from the clipboard", "Error", JOptionPane.ERROR_MESSAGE);;
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
  
  private void filterURLsFromHAR(List<String> urls) {
    
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
            if (jsonObject != null) {
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
      }
    } catch (Exception ex) {
      System.err.println("Exception during HAR import: " + ex.getMessage());
      urls.clear();
    }
    
    return urls;
  }

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

  private void startRun() {
    
    ThreadSettings ts;
    try {
      ts = getThreadSettings();
    } catch (Exception ex) {
      ts = null;
      JOptionPane.showMessageDialog(perfPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    if (ts == null) {
      return;
    }
    
    // TODO
  }
  
  /**
   * Signal that a single run completed.
   * 
   * @param ts the thread settings
   * @param url the worst-performing URL from the run
   * @param runTime the run time (in ms) for the URL
   */
  public static void updateProgress(ThreadSettings ts, String url, long runTime) {
    // TODO
  }
  
  private ThreadSettings getThreadSettings() throws Exception {
    
    // Populate our object with the data from the input section of the screen
    ThreadSettings ts = new ThreadSettings();
    ts.setMinDelay(Utility.getStringAsFloat(tfMinDelay.getText(), -1.0f, -1.0f));
    ts.setMaxDelay(Utility.getStringAsFloat(tfMaxDelay.getText(), -1.0f, -1.0f));
    ts.setNumThreads(Utility.getStringAsInteger(tfThreads.getText(), -1, -1));
    ts.setNumRuns(Utility.getStringAsInteger(tfRuns.getText(), -1, -1));
    ts.setFailureThreshold(Utility.getStringAsFloat(tfFailure.getText(), -1.0f, -1.0f));
    
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
    }
    
    // If we reach this point, the input looks good
    return ts;
  }
  
  private void stopRun() {
    // TODO
  }
  
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
  }
  
  public static JPanel getPanel() {
    return perfTab.perfPanel;
  }
  
  public static String getURLText() {
    return perfTab.taUrls.getText();
  }
  
  private void loadStoredURLs() {
    
    // Get any saved URL text from the properties file
    String urlText = WindowState.getInstance().getURLs();
    if ((urlText != null) && !urlText.isEmpty()) {
      // Text was found, so save it now
      taUrls.setText(urlText);
    }
  }
}
