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

public class ServersTab extends JPanel
{
  private static final String NO_OPTION = "<None Available>";
  
  private static final long serialVersionUID = 1L;
  private JComboBox<String> cbServers;
  private JLabel lblUrl;
  private JButton btnEdit;
  private JButton btnDelete;
  private JButton btnAdd;
  private JButton btnSelect;

  public ServersTab() {
    super(new GridBagLayout());
    buildPage();
  }
  
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
    add(pListing, c);
    
    JPanel pServer = new JPanel();
    JLabel lblTitle = new JLabel("URL: ");
    lblUrl = new JLabel();
    pServer.add(lblTitle);
    pServer.add(lblUrl);
    c.gridy++;
    add(pServer, c);
    
    JPanel pButtons = new JPanel();
    btnAdd = new JButton("Add");
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
    add(pButtons, c);
    
    updateCombo();
    
    updateButtons();
  }
  
  private void updateButtons() {
    final boolean hasServers = !(ServerCache.getInstance().getMap().isEmpty());
    btnSelect.setEnabled(hasServers);
    btnEdit.setEnabled(hasServers);
    btnDelete.setEnabled(hasServers);
  }
  
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

  private void addServer() {
    getServerInfo(true);
  }
  
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

    int result = JOptionPane.showConfirmDialog(this, myPanel, 
             "Please enter the server data", JOptionPane.OK_CANCEL_OPTION,
             JOptionPane.QUESTION_MESSAGE);
    
    // If the user pressed OK, and neither field is empty, then update the combo box
    if (result == JOptionPane.OK_OPTION) {      
      if (!Utility.anyAreEmpty(tfName.getText(), tfUrl.getText())) {
        
        // If editing, remove the map entry by name, in case the user renamed
        // the server
        if (!addServer) {
          ServerCache.getInstance().deleteServer(selectedName);
        }
        
        ServerCache.getInstance().add(tfName.getText(), tfUrl.getText());
        updateCombo();
      }
    }
  }

  private void editServer() {
    getServerInfo(false);
  }

  private void deleteServer() {
    final String selectedName = (String) cbServers.getSelectedItem();
    ServerCache.getInstance().deleteServer(selectedName);
    updateCombo();
  }
  
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

  private void updateDisplayedUrl() {
    final String selectedName = (String) cbServers.getSelectedItem();
    String url = ServerCache.getInstance().getServerUrl(selectedName);
    lblUrl.setText(url);
  }
}
