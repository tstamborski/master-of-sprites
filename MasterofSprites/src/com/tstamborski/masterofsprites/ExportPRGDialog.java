/*
 * The MIT License
 *
 * Copyright 2023 Tobiasz Stamborski <tstamborski@outlook.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tstamborski.masterofsprites;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class ExportPRGDialog extends JDialog {
    private final JPanel mainPanel;
    private final JPanel centralPanel;
    private final JPanel southPanel;
    private final JLabel startAddressLabel;
    private final AddressSpinner startAddressSpinner;
    private final JButton okButton;
    private final JButton cancelButton;
    
    private boolean acceptedFlag;
    
    public ExportPRGDialog(JFrame parent) {
        acceptedFlag = false;
                
        centralPanel = new JPanel();
        startAddressSpinner = new AddressSpinner();
        startAddressLabel = new JLabel("Start Address:  ");
        startAddressLabel.setDisplayedMnemonic(KeyEvent.VK_A);
        startAddressLabel.setDisplayedMnemonicIndex(6);
        startAddressLabel.setLabelFor(startAddressSpinner);
        centralPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(), BorderFactory.createEmptyBorder(4,4,4,4)
        ));
        centralPanel.setLayout(new GridBagLayout());
        centralPanel.add(startAddressLabel);
        centralPanel.add(startAddressSpinner);
        
        southPanel = new JPanel();
        okButton = new JButton("OK");
        okButton.addActionListener((ae)->{acceptedFlag = true; setVisible(false);});
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((ae)->{acceptedFlag = false; setVisible(false);});
        southPanel.setLayout(new FlowLayout());
        southPanel.add(okButton);
        southPanel.add(cancelButton);
        
        mainPanel = new JPanel();
        mainPanel.add(centralPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        
        add(mainPanel);
        
        setTitle("Export to PRG file...");
        getRootPane().setDefaultButton(okButton);
        setModal(true);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    public void setAddress(int address) {
        startAddressSpinner.setValue(address);
    }
    
    public int getAddress() {
        return (Integer)startAddressSpinner.getValue();
    }
    
    public boolean showDialog() {
        acceptedFlag = false;
        setVisible(true);
        return acceptedFlag;
    }
    
}
