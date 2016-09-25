package io.miti.restify.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import io.miti.restify.util.Utility;

public class ConsoleTab extends JPanel
{
  private static final long serialVersionUID = 1L;
  
  /** The logged events. */
  private JTextArea taOutput = null;
  
  /** The button to clear the page. */
  private JButton btnClear = null;

  /**
   * Default constructor.
   */
  public ConsoleTab() {
    super(new BorderLayout(20, 10));
    setupPanel();
  }
  
  /**
   * Add components to the panel.
   */
  public void setupPanel() {
    
    taOutput = new JTextArea();
    JScrollPane spOutput = new JScrollPane(taOutput);
    
    btnClear = new JButton("Clear");
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(btnClear, BorderLayout.WEST);
    
    add(spOutput, BorderLayout.CENTER);
    add(topPanel, BorderLayout.NORTH);
  }
  
  /**
   * Add an event to the text control.
   * 
   * @param threadNumber the thread number
   * @param runNumber the run number
   * @param event the event description
   */
  public synchronized void addEvent(final int threadNumber, final int runNumber,
                                    final String event) {
    
    // Get the current date/time
    final String date = Utility.getDateTimeString();
    
    // TODO Make this thread-safe
    String line = String.format("%d,%d,%s,%s\n", threadNumber, runNumber, date, event);
    taOutput.append(line);
  }
}
