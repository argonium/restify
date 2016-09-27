package io.miti.restify.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ServersTab extends JPanel
{
  private static final long serialVersionUID = 1L;

  public ServersTab() {
    super(new GridBagLayout());
    buildPage();
  }
  
  private void buildPage() {
    
    String[] servers = new String[3];
    servers[0] = "Local";
    servers[1] = "Staging";
    servers[2] = "Productioin";
    JComboBox cbServers = new JComboBox(servers);
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    add(cbServers, c);
    
    JPanel pServer = new JPanel();
    JLabel lblName = new JLabel("Server: ");
    JLabel lblNameValue = new JLabel("localhost");
    pServer.add(lblName);
    pServer.add(lblNameValue);
    c.gridy++;
    add(pServer, c);
    
    JPanel pPort = new JPanel();
    JLabel lblPort = new JLabel("Port: ");
    JLabel lblPortValue = new JLabel("80");
    pPort.add(lblPort);
    pPort.add(lblPortValue);
    c.gridy++;
    add(pPort, c);
    
    JPanel pButtons = new JPanel();
    JButton btnAdd = new JButton("Add");
    JButton btnEdit = new JButton("Edit");
    JButton btnDelete = new JButton("Delete");
    pButtons.add(btnAdd);
    pButtons.add(btnEdit);
    pButtons.add(btnDelete);
    c.gridy++;
    add(pButtons, c);
  }
}
