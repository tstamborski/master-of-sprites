
package com.tstamborski;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class AboutDialog extends JDialog {
    
    protected final JTabbedPane tabs;
    private final JPanel aboutPanel, lowerPanel;
    private final JScrollPane licensePane;
    
    protected final JLabel iconLabel, appNameLabel, versionLabel, copyrightLabel, extraInfoLabel;
    protected final JTextArea licenseArea;
    protected final JButton okButton;
    
    public AboutDialog() {
        GroupLayout layout;
        
        iconLabel = new JLabel();
        appNameLabel = new JLabel();
        versionLabel = new JLabel();
        copyrightLabel = new JLabel();
        extraInfoLabel = new JLabel();
        
        licenseArea = new JTextArea();
        licenseArea.setEditable(false);
        licenseArea.setBackground(Color.white);
        
        aboutPanel = new JPanel();
        layout = new GroupLayout(aboutPanel);
        aboutPanel.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup().
                addComponent(iconLabel).addGap(16).addGroup(layout.createParallelGroup().
                        addComponent(appNameLabel).addComponent(versionLabel).addComponent(copyrightLabel).
                        addComponent(extraInfoLabel)));
        layout.setVerticalGroup(layout.createParallelGroup().addComponent(iconLabel).
                addGroup(layout.createSequentialGroup().addComponent(appNameLabel).addGap(16).
                        addComponent(versionLabel).addGap(16).
                        addComponent(copyrightLabel).addGap(32).addComponent(extraInfoLabel)));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        licensePane = new JScrollPane();
        licensePane.setViewportView(licenseArea);
        
        okButton = new JButton("OK");
        okButton.addActionListener((ae)->{
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout());
        lowerPanel.add(okButton);
        
        tabs = new JTabbedPane();
        tabs.add("About", aboutPanel);
        tabs.add("License", licensePane);
        
        add(tabs);
        add(lowerPanel, BorderLayout.SOUTH);
        
        setTitle("About...");
        setSize(400, 300);
        setResizable(false);
        setModal(true);
        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        enableEvents(WindowEvent.WINDOW_EVENT_MASK | ActionEvent.ACTION_EVENT_MASK);
    }
    
    public void showDialog(Component parent) {
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
    public void setApplicationIcon(ImageIcon i) {
        this.setIconImage(i.getImage());
        iconLabel.setIcon(i);
    }
    
    public void setApplicationName(String n) {
        appNameLabel.setText(n);
    }
    
    public void setApplicationVersion(String v) {
        versionLabel.setText(v);
    }
    
    public void setApplicationCopyright(String v) {
        copyrightLabel.setText(v);
    }
    
    public void setApplicationExtraInfo(String v) {
        extraInfoLabel.setText(v);
    }
    
    public void setApplicationLicense(String v) {
        licenseArea.setText(v);
    }
    
    public void setApplicationLicense(URL url) throws IOException {
        licenseArea.read(new BufferedReader(new FileReader(new File(url.getPath()))), null);
    }
    
}
