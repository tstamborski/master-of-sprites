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
import com.tstamborski.masterofsprites.model.AsmCodeStream;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class AsmSyntaxDialog extends AbstractInputDialog {
    private final ButtonGroup btnGroup;
    private final JRadioButton kickassRadioButton;
    private final JRadioButton acmeRadioButton;
    private final JRadioButton tmpxRadioButton;
    
    public AsmSyntaxDialog(JFrame parent) {
        super(parent);
        
        kickassRadioButton = new JRadioButton("KickAss ");
        acmeRadioButton = new JRadioButton("ACME ");
        tmpxRadioButton = new JRadioButton("TMPx ");
        btnGroup = new ButtonGroup();
        btnGroup.add(kickassRadioButton);
        btnGroup.add(acmeRadioButton);
        btnGroup.add(tmpxRadioButton);
        kickassRadioButton.setSelected(true);
                
        getCentralPanel().setBorder(BorderFactory.createTitledBorder("Syntax: "));
        getCentralPanel().setLayout(new BoxLayout(getCentralPanel(), BoxLayout.Y_AXIS));
        getCentralPanel().add(kickassRadioButton);
        getCentralPanel().add(acmeRadioButton);
        getCentralPanel().add(tmpxRadioButton);
        
        setTitle("Export to Assembly Code...");
        pack();
    }
    
    public int getAsmSyntax() {
        if (tmpxRadioButton.isSelected())
            return AsmCodeStream.TMPX_SYNTAX;
        else if (acmeRadioButton.isSelected())
            return AsmCodeStream.ACME_SYNTAX;
        else
            return AsmCodeStream.KICKASS_SYNTAX;
    }
}
