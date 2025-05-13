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

import com.tstamborski.AbstractInputDialog;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class ExportPRGDialog extends AbstractInputDialog {
    private final JLabel startAddressLabel;
    private final AddressSpinner startAddressSpinner;
    
    public ExportPRGDialog(JFrame parent) {
        super(parent);
        
        startAddressSpinner = new AddressSpinner();
        startAddressLabel = new JLabel("Start Address:  ");
        startAddressLabel.setDisplayedMnemonic(KeyEvent.VK_A);
        startAddressLabel.setDisplayedMnemonicIndex(6);
        startAddressLabel.setLabelFor(startAddressSpinner);
        
        getCentralPanel().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), BorderFactory.createEmptyBorder(4,4,4,4)
        ));
        getCentralPanel().setLayout(new GridBagLayout());
        getCentralPanel().add(startAddressLabel);
        getCentralPanel().add(startAddressSpinner);
        
        setTitle("Export to PRG File...");
        pack();
    }
    
    public void setAddress(int address) {
        startAddressSpinner.setValue(address);
    }
    
    public int getAddress() {
        return (Integer)startAddressSpinner.getValue();
    }
}
