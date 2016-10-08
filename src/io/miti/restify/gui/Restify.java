package io.miti.restify.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import io.miti.restify.util.Utility;
import io.miti.restify.util.WindowState;

/**
 * This is the main class for the application.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class Restify
{
  /**
   * The name of the properties file.
   */
  public static final String PROPS_FILE_NAME = "restify.prop";
  
  /**
   * The application frame.
   */
  private JFrame frame = null;
  
  /**
   * The status bar.
   */
  private StatusBar statusBar = null;
  
  /**
   * The window state (position and size).
   */
  private WindowState windowState = null;
  
  /**
   * The middle panel.
   */
  private JPanel appPanel = null;
  
  private JTabbedPane tp = null;
  
  private static final Restify app = new Restify();
  
  
  /**
   * Default constructor.
   */
  private Restify()
  {
    super();
  }
  
  public static Restify getApp() {
    return app;
  }
  
  
  /**
   * Create the application's GUI.
   */
  private void createGUI()
  {
    // Load the properties file
    windowState = WindowState.getInstance();
    
    // Set up the frame
    setupFrame();
    
    // Create the empty middle window
    initScreen();
    
    // Set up the status bar
    initStatusBar();
    
    // Display the window.
    frame.pack();
    frame.setVisible(true);
    
    ((LoginTab) tp.getComponentAt(1)).setDivider();
  }
  
  public StatusBar getStatusBar() {
    return statusBar;
  }
  
  
  /**
   * Set up the application frame.
   */
  private void setupFrame()
  {
    // Create and set up the window.
    frame = new JFrame(Utility.getAppName());
    
    // Have the frame call exitApp() whenever it closes
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter()
    {
      /**
       * Close the windows.
       * 
       * @param e the event
       */
      @Override
      public void windowClosing(final WindowEvent e)
      {
        exitApp();
      }
    });
    
    // Set up the size of the frame
    frame.setPreferredSize(windowState.getSize());
    frame.setSize(windowState.getSize());
    
    // Set the position
    if (windowState.shouldCenter())
    {
      frame.setLocationRelativeTo(null);
    }
    else
    {
      frame.setLocation(windowState.getPosition());
    }
  }
  
  
  /**
   * Initialize the main screen (middle window).
   */
  private void initScreen()
  {
    // Build the middle panel
    appPanel = new JPanel(new BorderLayout());
    appPanel.setBackground(Color.WHITE);
    
    // Populate the tabbed panes
    tp = new JTabbedPane();
    tp.add("Servers", new ServersTab());
    tp.add("Login", new LoginTab());
    tp.add("Performance", new PerfTab());
    tp.add("Console", new ConsoleTab());
    appPanel.add(tp, BorderLayout.CENTER);
    
    // Add the panel to the frame
    frame.getContentPane().add(appPanel, BorderLayout.CENTER);
  }
  
  
  /**
   * Initialize the status bar.
   */
  private void initStatusBar()
  {
    // Instantiate the status bar
    // statusBar = new JLabel("Ready");
    statusBar = new StatusBar();
    
    // Add to the content pane
    frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
  }
  
  
  /**
   * Exit the application.
   */
  private void exitApp()
  {
    // Store the window state in the properties file
    windowState.update(frame.getBounds());
    windowState.saveToFile(PROPS_FILE_NAME);
    
    // Close the application by disposing of the frame
    frame.dispose();
  }
  
  
  /**
   * Entry point to the application.
   * 
   * @param args arguments passed to the application
   */
  public static void main(final String[] args)
  {
    // Make the application Mac-compatible
    Utility.makeMacCompatible();
    
    // Load the properties file data
    WindowState.load(PROPS_FILE_NAME);
    
    // Initialize the look and feel to the default for this OS
    Utility.initLookAndFeel();
    
    // Check the version number
    if (!Utility.hasRequiredJVMVersion())
    {
      System.exit(0);
    }
    
    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        // Run the application
        app.createGUI();
      }
    });
  }
}
