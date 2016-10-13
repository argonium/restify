package io.miti.restify.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import io.miti.restify.util.Utility;

public class LoginTab
{
  private static LoginTab loginTab;
  
  private JPanel loginPanel = null;
  
  private JSplitPane sp = null;
  
  private static String sessionCookie = null;

  private JTextField tfUrl;

  private JCheckBox cbAutoSignIn;

  private JTextField tfUser;

  private JTextField tfPassword;

  private JTextField tfCookie;
  
  private JTextArea taRequest;
  
  private JTextArea taResponse;
  
  private static final String usernameField = "username";
  
  private static final String passwordField = "password";
  
  private static final String LOGIN_URL = "login";
  
  static {
    loginTab = new LoginTab();
    loginTab.loginPanel = new JPanel(new BorderLayout());
    loginTab.buildPage();
  }

  private LoginTab() {
    super();
  }
  
  public static LoginTab getInstance() {
    return loginTab;
  }
  
  private void buildPage() {
    
    final JPanel topPanel = getTopPanel();
    final JPanel bottomPanel = getBottomPanel();
    sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(topPanel), new JScrollPane(bottomPanel));
    loginPanel.add(sp, BorderLayout.CENTER);
  }
  
  private JPanel getBottomPanel() {
    
    final JTabbedPane tp = new JTabbedPane();
    tp.add("Request", getRequestPanel());
    tp.add("Response", getResponsePanel());
    
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(tp, BorderLayout.CENTER);
    return bottomPanel;
  }
  
  private JPanel getRequestPanel() {
    
    JPanel request = new JPanel(new BorderLayout());
    taRequest = new JTextArea();
    taRequest.setWrapStyleWord(false);
    taRequest.setLineWrap(true);
    request.add(new JScrollPane(taRequest), BorderLayout.CENTER);
    return request;
  }
  
  private JPanel getResponsePanel() {
    
    JPanel response = new JPanel(new BorderLayout());
    taResponse = new JTextArea();
    taResponse.setWrapStyleWord(false);
    taResponse.setLineWrap(true);
    response.add(new JScrollPane(taResponse), BorderLayout.CENTER);
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
    tfUrl = new JTextField(23);
    topPanel.add(tfUrl, c);
    
    c.gridx = 5;
    c.gridwidth = 1;
    cbAutoSignIn = new JCheckBox("Auto Sign-In");
    cbAutoSignIn.setSelected(true);
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
    tfCookie = new JTextField(8);
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
      JOptionPane.showMessageDialog(loginPanel, "The URL, user and password must be provided", "Error", JOptionPane.WARNING_MESSAGE);
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
    taRequest.setText("");
    taResponse.setText("");
    try {
      HttpResponse<String> response = Unirest.post(url)
          .field(usernameField, username)
          .field(passwordField, password).asString();
      
      // Show the request and response fields
      taRequest.setText(getURLRequest(url, username, password.length()));
      taRequest.setCaretPosition(0);
      taResponse.setText(getURLResponse(response));
      taResponse.setCaretPosition(0);
      
      // Check the response status
      final int status = response.getStatus();
      if (status != 200) {
        JOptionPane.showMessageDialog(loginPanel, "Login failed", "Error", JOptionPane.WARNING_MESSAGE);
        return;
      }
      
      // JOptionPane.showMessageDialog(loginPanel, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
      
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
      // Show an error message
      JOptionPane.showMessageDialog(loginPanel, "Error: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
    }
  }
  
  private static String getURLResponse(HttpResponse<String> response) {
    
    StringBuilder sb = new StringBuilder();
    
    // Build the response text
    sb.append(Integer.toString(response.getStatus()))
      .append(" ").append(response.getStatusText())
      .append("\n\n");
    
    Headers headers = response.getHeaders();
    if ((headers == null) || headers.isEmpty()) {
      sb.append("<NO HEADER>");
    } else {
      for (Entry<String, List<String>> entry : headers.entrySet()) {
        for (String value : entry.getValue()) {
          sb.append(entry.getKey()).append(": ").append(value).append("\n");
        }
      }
    }
    
    return sb.toString();
  }

  private String getURLRequest(String url, String username, int length) {
    
    StringBuilder sb = new StringBuilder();
    
    // Build the URL and post body
    sb.append("POST ").append(url).append('\n')
      .append(usernameField).append("=").append(username)
      .append("&").append(passwordField).append("=");
    
    // Hide the password
    for (int i = 0; i < length; ++i) {
      sb.append("*");
    }
    
    return sb.toString();
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
  
  public JPanel getPanel() {
    return loginPanel;
  }

  /**
   * Move the divider bar to the center of the screen.
   */
  public void setDivider() {
    sp.setDividerLocation(0.5);
  }

  public void setUrlText(String url) {
    
    if ((url == null) || url.trim().isEmpty()) {
      tfUrl.setText("");
    } else {
      final String name = url.trim();
      StringBuilder sb = new StringBuilder(100);
      sb.append(name);
      if (!name.endsWith("/")) {
        sb.append("/");
      }
      
      // Add the endpoint for login
      sb.append(LOGIN_URL);
      
      tfUrl.setText(sb.toString());
    }
  }
}
