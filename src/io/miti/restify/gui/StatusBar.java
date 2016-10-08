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
    rightMsg = new JLabel("No User", SwingConstants.RIGHT);
    
    add(leftMsg, BorderLayout.WEST);
    add(rightMsg, BorderLayout.EAST);    
    
    // Set the color and border
    setForeground(Color.black);
    setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
              new SoftBevelBorder(SoftBevelBorder.LOWERED)));
  }
  
  public void setServerMsg(final String msg) {
    if ((msg == null) || msg.isEmpty()) {
      leftMsg.setText("No Server");
    } else {
      leftMsg.setText("Server: " + msg);
    }
  }
  
  public void setUserMsg(final String msg) {
    if ((msg == null) || msg.isEmpty()) {
      rightMsg.setText("No User");
    } else {
      rightMsg.setText("User: " + msg);
    }
  }
}
