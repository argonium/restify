package io.miti.restify.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import io.miti.restify.util.Utility;

public final class ConsoleTab
{
  private static final ConsoleTab consoleTab;
  
  private JPanel consolePanel;
  
  /** The logged events. */
  private JTextArea taOutput = null;

  static {
    consoleTab = new ConsoleTab();
    consoleTab.consolePanel = new JPanel(new BorderLayout(20, 10));
    consoleTab.setupPanel();
  }
  
  /**
   * Default constructor.
   */
  private ConsoleTab() {
    super();
  }
  
  /**
   * Add components to the panel.
   */
  private void setupPanel() {
    
    taOutput = new JTextArea();
    JScrollPane spOutput = new JScrollPane(taOutput);
    
    final JButton btnClear = new JButton("Clear");
    btnClear.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        consoleTab.taOutput.setText("");
      }
    });
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(btnClear, BorderLayout.WEST);
    
    consolePanel.add(spOutput, BorderLayout.CENTER);
    consolePanel.add(topPanel, BorderLayout.NORTH);
  }
  
  public static JPanel getPanel() {
    return consoleTab.consolePanel;
  }
  
  /**
   * Add an event to the text control.
   * 
   * @param threadNumber the thread number
   * @param runNumber the run number
   * @param event the event description
   */
  public static synchronized void addEvent(final int threadNumber, final int runNumber,
                                           final String event) {
    
    // Get the current date/time, and build the string to add to the main control
    final String date = Utility.getDateTimeString();
    final String line = String.format("%s: Thread %d @ Run %d: %s\n", date, threadNumber, runNumber, event.trim());

    // This method can get called from either the EDT or another
    // thread, so handle both cases
    if (SwingUtilities.isEventDispatchThread()) {
      // We're on the EDT, so just add the line to the component
      consoleTab.taOutput.append(line);
    } else {
      // Not on the EDT, so set the text on the EDT
      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          @Override
          public void run() {
            consoleTab.taOutput.append(line);
          }
        });
      } catch (Exception ex) {
        System.err.println("Exception adding event to console tab: " + ex.getMessage());
      }
    }
  }
}
