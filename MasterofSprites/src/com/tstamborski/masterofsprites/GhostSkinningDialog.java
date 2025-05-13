/*
 * The MIT License
 *
 * Copyright 2025 Tobiasz Stamborski <tstamborski@outlook.com>.
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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class GhostSkinningDialog extends AbstractInputDialog {
    ButtonGroup buttonGroup;
    JRadioButton noSkinningBtn, memSkinningBtn, selSkinningBtn, ovSkinningBtn;
    
    public GhostSkinningDialog(JFrame parent) {
        super(parent);
        
        noSkinningBtn = new JRadioButton("0. No ghost skinning.");
        noSkinningBtn.setMnemonic('0');
        memSkinningBtn = new JRadioButton("1. Previous in memory skinning.");
        memSkinningBtn.setMnemonic('1');
        selSkinningBtn = new JRadioButton("2. Previous in selection skinning.");
        selSkinningBtn.setMnemonic('2');
        ovSkinningBtn = new JRadioButton("3. Overlay stack skinning.");
        ovSkinningBtn.setMnemonic('3');
        buttonGroup = new ButtonGroup();
        buttonGroup.add(noSkinningBtn);
        buttonGroup.add(memSkinningBtn);
        buttonGroup.add(selSkinningBtn);
        buttonGroup.add(ovSkinningBtn);
        ovSkinningBtn.setSelected(true);
        
        JPanel panel = getCentralPanel();
        panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(noSkinningBtn);
        panel.add(memSkinningBtn);
        panel.add(selSkinningBtn);
        panel.add(ovSkinningBtn);
        
        setTitle("Set Ghost Skinning... ");
        pack();
    }
    
    public GhostSkinning getGhostSkinning() {
        return null;
    }
}
