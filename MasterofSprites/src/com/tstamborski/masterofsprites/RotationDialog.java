/*
 * The MIT License
 *
 * Copyright 2024 Tobiasz Stamborski <tstamborski@outlook.com>.
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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class RotationDialog extends JDialog {
    private final JPanel mainPanel;
    private final JPanel centralPanel;
    private final JPanel southPanel;
    
    private final JLabel rotationLabel, degreesLabel;
    private final JSpinner spinner;
    
    private final JButton okButton;
    private final JButton cancelButton;
    
    private boolean acceptedFlag;
    
    public RotationDialog(JFrame parent) {
        acceptedFlag = false;
        
        rotationLabel = new JLabel("Rotation:    ");
        degreesLabel = new JLabel(" ° ");
        spinner = new JSpinner(new SpinnerNumberModel(90, -360, 360, 1));
                
        centralPanel = new JPanel();
        centralPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createBevelBorder(BevelBorder.RAISED), 
                        BorderFactory.createEmptyBorder(16, 16, 16, 16))
        );
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
        centralPanel.add(rotationLabel);
        centralPanel.add(spinner);
        centralPanel.add(degreesLabel);
        
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
        
        setTitle("Rotate...");
        getRootPane().setDefaultButton(okButton);
        setModal(true);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    public double getDegrees() {
        return (Integer)spinner.getValue();
    }
    
    public double getRadians() {
        return Math.toRadians((Integer)spinner.getValue());
    }
    
    public boolean showDialog() {
        acceptedFlag = false;
        setVisible(true);
        return acceptedFlag;
    }
}
