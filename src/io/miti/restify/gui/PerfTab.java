package io.miti.restify.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class PerfTab extends JPanel
{
  private static final long serialVersionUID = 1L;

  public PerfTab() {
    super(new GridLayout(2, 1, 20, 20));
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    buildTopPanel();
    buildBottomPanel();
  }
  
  private void buildBottomPanel() {
    
    JPanel panel = new JPanel(new GridBagLayout());    
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 4;
    c.gridheight = 6;
    c.insets = new Insets(5, 4, 5, 4);
    c.anchor = GridBagConstraints.LINE_START;
    
    JTextArea taOutput = new JTextArea(8, 25);
    taOutput.setEditable(false);
    panel.add(new JScrollPane(taOutput), c);
    
    c.gridwidth = 1;
    c.gridheight = 1;
    c.gridx = 4;
    panel.add(new JLabel("Progress:"), c);
    
    c.gridx = 5;
    JTextField tfProgress = new JTextField(6);
    tfProgress.setEditable(false);
    panel.add(tfProgress, c);
    
    c.gridx = 6;
    panel.add(new JLabel("Failure Rate:"), c);
    
    c.gridx = 7;
    JTextField tfFailure = new JTextField(6);
    tfFailure.setEditable(false);
    panel.add(tfFailure, c);
    
    c.gridy = 1;
    c.gridx = 4;
    panel.add(new JLabel("Min Resp Time:"), c);
    
    c.gridx = 5;
    JTextField tfMinTime = new JTextField(6);
    tfMinTime.setEditable(false);
    panel.add(tfMinTime, c);
    
    c.gridx = 6;
    panel.add(new JLabel("Max Resp Time:"), c);
    
    c.gridx = 7;
    JTextField tfMaxTime = new JTextField(6);
    tfMaxTime.setEditable(false);
    panel.add(tfMaxTime, c);
    
    c.gridy = 2;
    c.gridx = 4;
    panel.add(new JLabel("Worst URL:"), c);
    
    c.gridx = 5;
    c.gridwidth = 3;
    JTextField tfWorstUrl = new JTextField(20);
    tfWorstUrl.setEditable(false);
    panel.add(tfWorstUrl, c);
    
    c.gridy = 3;
    c.gridx = 4;
    c.gridwidth = 1;
    panel.add(new JLabel("Worst Time:"), c);
    
    c.gridx = 5;
    JTextField tfWorstTime = new JTextField(6);
    tfWorstTime.setEditable(false);
    panel.add(tfWorstTime, c);
    
    panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Results"));
    
    add(panel);
  }
  
  private void buildTopPanel() {
    
    JPanel panel = new JPanel(new GridBagLayout());    
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 4;
    c.gridheight = 6;
    c.insets = new Insets(5, 4, 5, 4);
    c.anchor = GridBagConstraints.LINE_START;
    
    JTextArea taUrls = new JTextArea(8, 25);
    panel.add(new JScrollPane(taUrls), c);
    
    c.gridwidth = 1;
    c.gridheight = 1;
    c.gridx = 4;
    panel.add(new JLabel("Min Delay (ms):"), c);
    
    c.gridx = 5;
    JTextField tfMinDelay = new JTextField(6);
    panel.add(tfMinDelay, c);
    
    c.gridx = 6;
    panel.add(new JLabel("Max Delay (ms):"), c);
    
    c.gridx = 7;
    JTextField tfMaxDelay = new JTextField(6);
    panel.add(tfMaxDelay, c);
    
    c.gridy = 1;
    c.gridx = 4;
    panel.add(new JLabel("# Threads:"), c);
    
    c.gridx = 5;
    JTextField tfThreads = new JTextField(6);
    panel.add(tfThreads, c);
    
    c.gridx = 6;
    panel.add(new JLabel("# Runs:"), c);
    
    c.gridx = 7;
    JTextField tfRuns = new JTextField(6);
    panel.add(tfRuns, c);
    
    c.gridy = 2;
    c.gridx = 4;
    c.gridwidth = 2;
    panel.add(new JLabel("Failure Threshold (ms):"), c);
    
    c.gridx = 6;
    c.gridwidth = 1;
    JTextField tfThreshold = new JTextField(6);
    panel.add(tfThreshold, c);
    
    c.gridy = 3;
    c.gridx = 4;
    c.fill = GridBagConstraints.HORIZONTAL;
    JButton btnStart = new JButton("Start");
    panel.add(btnStart, c);
    
    c.gridx = 5;
    JButton btnStop = new JButton("Stop");
    panel.add(btnStop, c);
    
    c.gridx = 6;
    JButton btnReset = new JButton("Reset");
    panel.add(btnReset, c);
    
    c.gridx = 7;
    JButton btnImport = new JButton("Import HAR");
    panel.add(btnImport, c);
    
    panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLACK), "URLs and Settings"));
    
    add(panel);
  }
}
