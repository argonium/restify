package io.miti.restify.util;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Utility methods.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class Utility
{
  /**
   * The line separator for this OS.
   */
  private static String lineSep = null;
  
  /**
   * Default constructor.
   */
  private Utility()
  {
    super();
  }
  
  
  /**
   * Return the application name.
   * 
   * @return the application name
   */
  public static String getAppName()
  {
    return "Restify";
  }
  
  
  /**
   * Return the line separator for this OS.
   * 
   * @return the line separator for this OS
   */
  public static String getLineSeparator()
  {
    // See if it's been initialized
    if (lineSep == null)
    {
      lineSep = System.getProperty("line.separator");
    }
    
    return lineSep;
  }

  
  /**
   * Sleep for the specified number of milliseconds.
   * 
   * @param time the number of milliseconds to sleep
   */
  public static void sleep(final long time)
  {
    try
    {
      Thread.sleep(time);
    }
    catch (InterruptedException e)
    {
      Logger.error(e);
    }
  }
  
  /**
   * Convert a string into an integer.
   * 
   * @param sInput the input string
   * @param defaultValue the default value
   * @param emptyValue the value to return for an empty string
   * @return the value as an integer
   */
  public static int getStringAsInteger(final String sInput,
                                       final int defaultValue,
                                       final int emptyValue)
  {
    // This is the variable that gets returned
    int value = defaultValue;
    
    // Check the input
    if (sInput == null)
    {
      return emptyValue;
    }
    
    // Trim the string
    final String inStr = sInput.trim();
    if (inStr.length() < 1)
    {
      // The string is empty
      return emptyValue;
    }
    
    // Convert the number
    try
    {
      value = Integer.parseInt(inStr);
    }
    catch (NumberFormatException nfe)
    {
      value = defaultValue;
    }
    
    // Return the value
    return value;
  }
  
  
  /**
   * Convert a string into a floating point number.
   * 
   * @param sInput the input string
   * @param defaultValue the default value
   * @param emptyValue the value to return for an empty string
   * @return the value as a float
   */
  public static float getStringAsFloat(final String sInput,
                                       final float defaultValue,
                                       final float emptyValue)
  {
    // This is the variable that gets returned
    float fValue = defaultValue;
    
    // Check the input
    if (sInput == null)
    {
      return emptyValue;
    }
    
    // Trim the string
    final String inStr = sInput.trim();
    if (inStr.length() < 1)
    {
      // The string is empty
      return emptyValue;
    }
    
    // Convert the number
    try
    {
      fValue = Float.parseFloat(inStr);
    }
    catch (NumberFormatException nfe)
    {
      fValue = defaultValue;
    }
    
    // Return the value
    return fValue;
  }

  
  /**
   * Return whether the string is null or has no length.
   * 
   * @param msg the input string
   * @return whether the string is null or has no length
   */
  public static boolean isStringEmpty(final String msg)
  {
    return ((msg == null) || (msg.length() == 0));
  }
  
  
  /**
   * Make the application compatible with Apple Macs.
   */
  public static void makeMacCompatible()
  {
    // Set the system properties that a Mac uses
    System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
    System.setProperty("apple.awt.brushMetalLook", "true");
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("apple.awt.showGrowBox", "true");
    System.setProperty("com.apple.mrj.application.apple.menu.about.name",
                       getAppName());
  }
  
  
  /**
   * Initialize the application's Look And Feel with the default
   * for this OS.
   */
  public static void initLookAndFeel()
  {
    // Use the default look and feel
    try
    {
      javax.swing.UIManager.setLookAndFeel(
        javax.swing.UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      Logger.error("Exception: " + e.getMessage());
    }
  }
  
  
  /**
   * Center the application on the screen.
   * 
   * @param comp the component to center on the screen
   */
  public static void centerOnScreen(final java.awt.Component comp)
  {
    // Get the size of the screen
    Dimension screenDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    
    // Determine the new location of the window
    int x = (screenDim.width - comp.getSize().width) / 2;
    int y = (screenDim.height - comp.getSize().height) / 2;
    
    // Move the window
    comp.setLocation(x, y);
  }
  
  /**
   * Store the properties object to the filename.
   * 
   * @param filename name of the output file
   * @param props the properties to store
   */
  public static void storeProperties(final String filename,
                                     final Properties props)
  {
    // Write the properties to a file
    FileOutputStream outStream = null;
    try
    {
      // Open the output stream
      outStream = new FileOutputStream(filename);
      
      // Save the properties
      props.store(outStream, "Properties file");
      
      // Close the stream
      outStream.close();
      outStream = null;
    }
    catch (FileNotFoundException fnfe)
    {
      Logger.error("File not found: " + fnfe.getMessage());
    }
    catch (IOException ioe)
    {
      Logger.error("IOException: " + ioe.getMessage());
    }
    finally
    {
      if (outStream != null)
      {
        try
        {
          outStream.close();
        }
        catch (IOException ioe)
        {
          Logger.error("IOException: " + ioe.getMessage());
        }
        
        outStream = null;
      }
    }
  }
  
  
  /**
   * Load the properties object.
   * 
   * @param filename the input file name
   * @return the loaded properties
   */
  public static Properties getProperties(final String filename)
  {
    // The object that gets returned
    Properties props = null;
    
    InputStream propStream = null;
    try
    {
      // Open the input stream as a file
      propStream = new FileInputStream(filename);
      
      // Load the input stream
      props = new Properties();
      props.load(propStream);

      // Close the stream
      propStream.close();
      propStream = null;
    }
    catch (IOException ioe)
    {
      props = null;
    }
    finally
    {
      // Make sure we close the stream
      if (propStream != null)
      {
        try
        {
          propStream.close();
        }
        catch (IOException e)
        {
          Logger.error(e.getMessage());
        }
        
        propStream = null;
      }
    }
    
    // Return the properties
    return props;
  }
  
  /**
   * Format the date and time as a string, using a standard format.
   * 
   * @return the date as a string
   */
  public static String getDateTimeString()
  {
    // Declare our formatter
    java.text.SimpleDateFormat formatter = new
      java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    
    // Return the date/time as a string
    return formatter.format(new Date());
  }

  /**
   * Return whether any of the input strings are empty or null.
   *
   * @param words the array of words to check
   * @return true if any of the words are empty or null
   */
  public static boolean anyAreEmpty(String... words) {
    
    for (String word : words) {
      if ((word == null) || word.isEmpty()) {
        return true;
      }
    }
    
    return false;
  }
}
