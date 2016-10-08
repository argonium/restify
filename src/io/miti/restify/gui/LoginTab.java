package io.miti.restify.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class LoginTab extends JPanel
{
  private static final long serialVersionUID = 1L;
  
  private JSplitPane sp = null;

  public LoginTab() {
    super(new BorderLayout());
    buildPage();
  }
  
  private void buildPage() {
    
    final JPanel topPanel = getTopPanel();
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(new JButton("bottom"), BorderLayout.CENTER);
    sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(topPanel), new JScrollPane(bottomPanel));
    add(sp, BorderLayout.CENTER);
  }
  
  private JPanel getTopPanel() {
    JPanel topPanel = new JPanel(new GridBagLayout());
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    topPanel.add(new JLabel("URL:"), c);
    
    c.gridx = 1;
    c.gridwidth = 4;
    JTextField tfUrl = new JTextField(32);
    topPanel.add(tfUrl, c);
    
    c.gridx = 5;
    c.gridwidth = 1;
    JCheckBox cbAutoSignIn = new JCheckBox("Auto Sign-In");
    topPanel.add(cbAutoSignIn, c);
    
    c.gridy = 1;
    c.gridx = 0;
    c.gridwidth = 1;
    topPanel.add(new JLabel("User:"), c);
    
    c.gridx = 1;
    JTextField tfUser = new JTextField(8);
    topPanel.add(tfUser, c);
    
    c.gridx = 3;
    topPanel.add(new JLabel("Password:"), c);
    
    c.gridx = 4;
    JTextField tfPassword = new JTextField(8);
    topPanel.add(tfPassword, c);
    
    c.gridx = 5;
    JButton btnSignIn = new JButton("Sign In");
    topPanel.add(btnSignIn, c);
    
    c.gridy = 2;
    c.gridx = 0;
    topPanel.add(new JLabel("Field:"), c);
    
    c.gridx = 1;
    topPanel.add(new JLabel("session_cookie"), c);
    
    c.gridx = 3;
    topPanel.add(new JLabel("Cookie:"), c);
    
    c.gridx = 4;
    JTextField tfCookie = new JTextField(8);
    topPanel.add(tfCookie, c);

    c.gridx = 5;
    JButton btnSignOut = new JButton("Sign Out");
    topPanel.add(btnSignOut, c);
    
    return topPanel;
  }

  /**
   * Move the divider bar to the center of the screen.
   */
  public void setDivider() {
    sp.setDividerLocation(0.5);
  }
}
