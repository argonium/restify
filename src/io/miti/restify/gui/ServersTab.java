package io.miti.restify.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import io.miti.restify.util.ServerCache;
import io.miti.restify.util.Utility;

/**
 * This is the Servers tab class for the UI.
 */
public final class ServersTab
{
  private static final String NO_OPTION = "<None Available>";
  
  private static final ServersTab serversTab;
  
  private JPanel serversPanel;
  
  private JComboBox<String> cbServers;
  private JLabel lblUrl;
  private JButton btnEdit;
  private JButton btnDelete;
  private JButton btnSelect;
  
  static {
    serversTab = new ServersTab();
    serversTab.serversPanel = new JPanel(new GridBagLayout());
    serversTab.buildPage();
  }

  /**
   * Default constructor.
   */
  private ServersTab() {
    super();
  }

  /**
   * Build the UI layout.
   */
  private void buildPage() {
    
    String[] servers = new String[1];
    servers[0] = NO_OPTION;
    cbServers = new JComboBox<>(servers);
    cbServers.addActionListener (new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        updateDisplayedUrl();
      }
    });
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    
    JPanel pListing = new JPanel();
    pListing.add(cbServers);
    btnSelect = new JButton("Select");
    btnSelect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectServer();
      }
    });
    pListing.add(btnSelect);
    serversPanel.add(pListing, c);
    
    JPanel pServer = new JPanel();
    JLabel lblTitle = new JLabel("URL: ");
    lblUrl = new JLabel();
    pServer.add(lblTitle);
    pServer.add(lblUrl);
    c.gridy++;
    serversPanel.add(pServer, c);
    
    final JPanel pButtons = new JPanel();
    final JButton btnAdd = new JButton("Add");
    btnAdd.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addServer();
      }
    });
    
    btnEdit = new JButton("Edit");
    btnEdit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editServer();
      }
    });
    
    btnDelete = new JButton("Delete");
    btnDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteServer();
      }
    });
    
    pButtons.add(btnAdd);
    pButtons.add(btnEdit);
    pButtons.add(btnDelete);
    c.gridy++;
    serversPanel.add(pButtons, c);
    
    updateCombo();
    
    updateButtons();
  }

  /**
   * Enable or disable the buttons based on whether we have any servers in the list.
   */
  private void updateButtons() {
    final boolean hasServers = !(ServerCache.getInstance().getMap().isEmpty());
    btnSelect.setEnabled(hasServers);
    btnEdit.setEnabled(hasServers);
    btnDelete.setEnabled(hasServers);
  }

  /**
   * Select a server from the list.
   */
  private void selectServer() {
    
    // Get the selected server name
    final String selectedName = (String) cbServers.getSelectedItem();
    if ((selectedName == null) || selectedName.equals(NO_OPTION)) {
      return;
    }
    
    // Get the URL for this server name
    String url = ServerCache.getInstance().getServerUrl(selectedName);
    
    // Update the login URL
    LoginTab.getInstance().setUrlText(url);
    PerfTab.setSelectedServer(url);
  }

  /**
   * Add a new server to the list.
   */
  private void addServer() {
    getServerInfo(true);
  }

  /**
   * Get information on a server (new or existing).
   *
   * @param addServer whether we're adding a server
   */
  private void getServerInfo(final boolean addServer) {
    
    // Check the selected server name
    String selectedName = (String) cbServers.getSelectedItem();
    selectedName = (selectedName.equals(NO_OPTION)) ? "" : selectedName;
    
    JTextField tfName = new JTextField(10);
    JTextField tfUrl = new JTextField(10);
    
    if (!addServer) {
      tfName.setText(selectedName);
      tfUrl.setText(ServerCache.getInstance().getServerUrl(selectedName));
    }

    JPanel myPanel = new JPanel(new GridLayout(2, 2));
    myPanel.add(new JLabel("Name:"));
    myPanel.add(tfName);
    myPanel.add(new JLabel("URL:"));
    myPanel.add(tfUrl);

    int result = JOptionPane.showConfirmDialog(serversPanel, myPanel, 
             "Please enter the server data", JOptionPane.OK_CANCEL_OPTION,
             JOptionPane.QUESTION_MESSAGE);
    
    // If the user pressed OK, and neither field is empty, then update the combo box
    if (result == JOptionPane.OK_OPTION) {
      final String url = processUrl(tfUrl.getText());
      if (!Utility.anyAreEmpty(tfName.getText(), url)) {

        // Ensure the URL starts with http:// or https://
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
          JOptionPane.showMessageDialog(serversPanel, "URL must start with http:// or https://",
                  "Error in URL", JOptionPane.ERROR_MESSAGE);
        } else {
          // If editing, remove the map entry by name, in case the user renamed
          // the server
          if (!addServer) {
            ServerCache.getInstance().deleteServer(selectedName);
          }

          // Update the list in the combo box and in the cache
          ServerCache.getInstance().add(tfName.getText(), url);
          updateCombo();
        }
      } else {
        JOptionPane.showMessageDialog(serversPanel,
                "Skipping the save since one or more fields are empty",
                "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * If the user entered a trailing "/" at the end of the URL, remove it.
   *
   * @param inputUrl the input URL
   * @return the cleaned-up version
   */
  private static String processUrl(final String inputUrl) {

    // Check the input
    if (Utility.isStringEmpty(inputUrl)) {
      return null;
    }

    // If it ends with a "/" then strip it off (when we import URLs
    // from a HAR session, we keep any leading "/" after the server
    // name and before the API call)
    return (inputUrl.endsWith("/") ? inputUrl.substring(0, inputUrl.length() - 1).trim() : inputUrl.trim());
  }

  /**
   * Edit a server's information.
   */
  private void editServer() {
    getServerInfo(false);
  }

  /**
   * Delete a server from the list.
   */
  private void deleteServer() {
    final String selectedName = (String) cbServers.getSelectedItem();
    ServerCache.getInstance().deleteServer(selectedName);
    updateCombo();
  }

  /**
   * Update the list in the combo box.
   */
  private void updateCombo() {
    
    // Save the selected index, and remove everything from the combo
    final int selectedIndex = cbServers.getSelectedIndex();
    cbServers.removeAllItems();

    // Add everything from the cache to the combo; if nothing
    // in the cache, add NO_OPTION
    Set<Entry<String, String>> map = ServerCache.getInstance().getMap();
    if (map.isEmpty()) {
      cbServers.addItem(NO_OPTION);
    } else {
      for (Entry<String, String> entry : map) {
        cbServers.addItem(entry.getKey());
      }
    }
    
    // Select the same item (or the previous one, if it was deleted)
    if (map.isEmpty()) {
      cbServers.setSelectedIndex(0);
    } else if (selectedIndex >= map.size()) {
      cbServers.setSelectedIndex(map.size() - 1);
    } else {
      cbServers.setSelectedIndex(selectedIndex);
    }
    
    // Update the buttons
    updateButtons();
  }

  /**
   * Return the one instance of this panel.
   *
   * @return the one instance of this panel
   */
  public static JPanel getPanel() {
    return serversTab.serversPanel;
  }

  /**
   * When a URL in the list is highlighted, show the URL on the panel.
   */
  private void updateDisplayedUrl() {
    final String selectedName = (String) cbServers.getSelectedItem();
    String url = ServerCache.getInstance().getServerUrl(selectedName);
    lblUrl.setText(url);
  }
}
