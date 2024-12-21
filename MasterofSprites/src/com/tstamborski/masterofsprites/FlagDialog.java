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

import com.tstamborski.AbstractInputDialog;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class FlagDialog extends AbstractInputDialog {
    private final ButtonGroup group;
    private final JRadioButton trueButton, falseButton;
    
    public FlagDialog(JFrame parent, String title) {
        super(parent);
        
        JPanel panel = getCentralPanel();
        trueButton = new JRadioButton("TRUE");
        falseButton = new JRadioButton("FALSE");
        group = new ButtonGroup();
        group.add(trueButton);
        group.add(falseButton);
        trueButton.setSelected(true);
        panel.add(trueButton);
        panel.add(falseButton);
        panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        setTitle(title);
        pack();
    }
    
    public boolean getValue() {
        return trueButton.isSelected();
    }
}
