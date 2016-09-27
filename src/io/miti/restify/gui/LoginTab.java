package io.miti.restify.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class LoginTab extends JPanel
{
  private static final long serialVersionUID = 1L;
  
  private JSplitPane sp = null;

  public LoginTab() {
    super(new BorderLayout());
    buildPage();
  }
  
  private void buildPage() {
    
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(new JButton("top"), BorderLayout.CENTER);
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(new JButton("bottom"), BorderLayout.CENTER);
    sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(topPanel), new JScrollPane(bottomPanel));
    add(sp, BorderLayout.CENTER);
  }
  
  /**
   * Move the divider bar to the center of the screen.
   */
  public void setDivider() {
    sp.setDividerLocation(0.5);
  }
}
