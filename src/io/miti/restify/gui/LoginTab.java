package io.miti.restify.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.miti.restify.util.Utility;

public class LoginTab extends JPanel
{
  private static final long serialVersionUID = 1L;
  
  private JSplitPane sp = null;
  
  private static String sessionCookie = null;

  private JTextField tfUrl;

  private JCheckBox cbAutoSignIn;

  private JTextField tfUser;

  private JTextField tfPassword;

  private JTextField tfCookie;

  public LoginTab() {
    super(new BorderLayout());
    buildPage();
  }
  
  private void buildPage() {
    
    final JPanel topPanel = getTopPanel();
    final JPanel bottomPanel = getBottomPanel();
    sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(topPanel), new JScrollPane(bottomPanel));
    add(sp, BorderLayout.CENTER);
  }
  
  private JPanel getBottomPanel() {
    
    JTabbedPane tp = new JTabbedPane();
    tp.add("Request", getRequestPanel());
    tp.add("Response", getResponsePanel());
    
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(tp, BorderLayout.CENTER);
    return bottomPanel;
  }
  
  private JPanel getRequestPanel() {
    // TODO
    JPanel request = new JPanel();
    return request;
  }
  
  private JPanel getResponsePanel() {
    // TODO
    JPanel response = new JPanel();
    return response;
  }
  
  private JPanel getTopPanel() {
    JPanel topPanel = new JPanel(new GridBagLayout());
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(2, 2, 2, 2);
    c.anchor = GridBagConstraints.LINE_START;
    topPanel.add(new JLabel("URL:"), c);
    
    c.gridx = 1;
    c.gridwidth = 4;
    tfUrl = new JTextField(32);
    topPanel.add(tfUrl, c);
    
    c.gridx = 5;
    c.gridwidth = 1;
    cbAutoSignIn = new JCheckBox("Auto Sign-In");
    topPanel.add(cbAutoSignIn, c);
    
    c.gridy = 1;
    c.gridx = 0;
    c.gridwidth = 1;
    topPanel.add(new JLabel("User:"), c);
    
    c.gridx = 1;
    tfUser = new JTextField(8);
    topPanel.add(tfUser, c);
    
    c.gridx = 3;
    topPanel.add(new JLabel("Password:"), c);
    
    c.gridx = 4;
    tfPassword = new JPasswordField(8);
    topPanel.add(tfPassword, c);
    
    c.gridx = 5;
    JButton btnSignIn = new JButton("Sign In");
    btnSignIn.setMnemonic(KeyEvent.VK_S);
    btnSignIn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        signIn();
      }
    });
    topPanel.add(btnSignIn, c);
    
    c.gridy = 2;
    c.gridx = 0;
    topPanel.add(new JLabel("Field:"), c);
    
    c.gridx = 1;
    topPanel.add(new JLabel("session_cookie"), c);
    
    c.gridx = 3;
    topPanel.add(new JLabel("Cookie:"), c);
    
    c.gridx = 4;
    tfCookie = new JTextField(17);
    topPanel.add(tfCookie, c);

    c.gridx = 5;
    JButton btnSignOut = new JButton("Sign Out");
    btnSignOut.setMnemonic(KeyEvent.VK_O);
    btnSignOut.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        signOut();
      }
    });
    topPanel.add(btnSignOut, c);
    
    return topPanel;
  }
  
  private void signOut() {
    sessionCookie = null;
    updateStatusBar();
  }
  
  private void signIn() {
    
    // Get the inputs
    final String url = tfUrl.getText();
    final String user = tfUser.getText();
    final String pw = tfPassword.getText();
    if (Utility.anyAreEmpty(url, user, pw)) {
      JOptionPane.showMessageDialog(this, "The URL, user and password must be provided", "Error", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    // Log the user in
    loginUser(url, user, pw);
    
    // Update the session cookie field
    tfCookie.setText((sessionCookie == null) ? "" : sessionCookie);
    
    // Update the status bar
    updateStatusBar();    
  }
  
  private void loginUser(String url, String username, String password) {

    sessionCookie = null;
    try {
      HttpResponse<String> response = Unirest.post(url)
          .field("username", username)
          .field("password", password).asString();
      
      final int status = response.getStatus();
      if (status != 200) {
        JOptionPane.showMessageDialog(this, "Login failed", "Error", JOptionPane.WARNING_MESSAGE);
        return;
      } else {
        JOptionPane.showMessageDialog(this, "Login successful");
      }
      
      // Get the response headers so we can find the Set-Cookie value
      // (used to authenticate later calls to the server)
      final Headers headers = response.getHeaders();
      
      List<String> cookies = headers.get("Set-Cookie");
      if (cookies != null) {
        final String cookie = cookies.get(0);
        // System.out.println("Set cookie is " + cookie);
        String[] split = cookie.split(";");
        for (String child : split) {
          if (child.startsWith("session_cookie=")) {
            sessionCookie = child.substring(15);
            break;
          }
        }
      }
    } catch (UnirestException e) {
      e.printStackTrace();
    }
  }
  
  private void updateStatusBar() {
    final String serverName = (sessionCookie == null) ? null : tfUrl.getText();
    final String userName = (sessionCookie == null) ? null : tfUser.getText();
    Restify.getApp().getStatusBar().setServerMsg(serverName);
    Restify.getApp().getStatusBar().setUserMsg(userName);
  }
  
  public static String getSessionCookie() {
    return sessionCookie;
  }
  
  public static boolean isLoggedIn() {
    return (sessionCookie == null);
  }

  /**
   * Move the divider bar to the center of the screen.
   */
  public void setDivider() {
    sp.setDividerLocation(0.5);
  }
}
