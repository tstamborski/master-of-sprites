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

import com.tstamborski.masterofsprites.model.AsmCodeStream;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class AsmSyntaxDialog extends JDialog {
    private final JPanel mainPanel;
    private final JPanel centralPanel;
    private final JPanel southPanel;
    
    private final ButtonGroup btnGroup;
    private final JRadioButton kickassRadioButton;
    private final JRadioButton acmeRadioButton;
    private final JRadioButton tmpxRadioButton;
    
    private final JButton okButton;
    private final JButton cancelButton;
    
    private boolean acceptedFlag;
    
    public AsmSyntaxDialog(JFrame parent) {
        acceptedFlag = false;
        
        kickassRadioButton = new JRadioButton("KickAss ");
        acmeRadioButton = new JRadioButton("ACME ");
        tmpxRadioButton = new JRadioButton("TMPx ");
        btnGroup = new ButtonGroup();
        btnGroup.add(kickassRadioButton);
        btnGroup.add(acmeRadioButton);
        btnGroup.add(tmpxRadioButton);
        kickassRadioButton.setSelected(true);
                
        centralPanel = new JPanel();
        centralPanel.setBorder(BorderFactory.createTitledBorder("Syntax: "));
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.add(kickassRadioButton);
        centralPanel.add(acmeRadioButton);
        centralPanel.add(tmpxRadioButton);
        
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
        
        setTitle("Export to Assembly Code...");
        getRootPane().setDefaultButton(okButton);
        setModal(true);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    public int getAsmSyntax() {
        if (tmpxRadioButton.isSelected())
            return AsmCodeStream.TMPX_SYNTAX;
        else if (acmeRadioButton.isSelected())
            return AsmCodeStream.ACME_SYNTAX;
        else
            return AsmCodeStream.KICKASS_SYNTAX;
    }
    
    public boolean showDialog() {
        acceptedFlag = false;
        setVisible(true);
        return acceptedFlag;
    }
}
