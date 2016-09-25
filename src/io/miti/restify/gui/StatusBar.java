package io.miti.restify.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

public final class StatusBar extends JPanel {

  private static final long serialVersionUID = 1L;
  
  private JLabel leftMsg = null;
  
  private JLabel rightMsg = null;

  /**
   * Default constructor.
   */
  public StatusBar() {
    super(new BorderLayout());
    buildPanel();
  }
  
  /**
   * Build the panel.
   */
  private void buildPanel() {
    
    leftMsg = new JLabel("No Server");
    rightMsg = new JLabel("User: n/a", SwingConstants.RIGHT);
    
    add(leftMsg, BorderLayout.WEST);
    add(rightMsg, BorderLayout.EAST);    
    
    // Set the color and border
    setForeground(Color.black);
    setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
              new SoftBevelBorder(SoftBevelBorder.LOWERED)));
  }
  
  public void setServerMsg(final String msg) {
    leftMsg.setText(msg);
  }
  
  public void setUserMsg(final String msg) {
    rightMsg.setText(msg);
  }
}
