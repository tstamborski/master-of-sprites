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
package com.tstamborski.masterofsprites.gui;

import com.tstamborski.AbstractInputDialog;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class RotationDialog extends AbstractInputDialog {
    private final JLabel rotationLabel, degreesLabel;
    private final JSpinner spinner;
    
    public RotationDialog(JFrame parent) {
        super(parent);
        
        rotationLabel = new JLabel("Rotation:    ");
        degreesLabel = new JLabel(" ° ");
        spinner = new JSpinner(new SpinnerNumberModel(90, -360, 360, 1));
        
        getCentralPanel().setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), 
                        BorderFactory.createEmptyBorder(16, 16, 16, 16))
        );
        getCentralPanel().setLayout(new BoxLayout(getCentralPanel(), BoxLayout.X_AXIS));
        getCentralPanel().add(rotationLabel);
        getCentralPanel().add(spinner);
        getCentralPanel().add(degreesLabel);
        
        setTitle("Rotate...");
        pack();
    }
    
    public double getDegrees() {
        return (Integer)spinner.getValue();
    }
    
    public double getRadians() {
        return Math.toRadians((Integer)spinner.getValue());
    }
}
