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

import com.tstamborski.masterofsprites.model.Time;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.*;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class StatusBar extends JPanel {
    private final JLabel tipLabel, timeLabel;
    
    public StatusBar() {
        tipLabel = new JLabel();
        tipLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        timeLabel = new JLabel();
        timeLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        timeLabel.setPreferredSize(new Dimension(100,20));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        
        setLayout(new BorderLayout());
        add(tipLabel, BorderLayout.CENTER);
        add(timeLabel, BorderLayout.EAST);
    }
    
    public void showTip(String tip) {
        tipLabel.setText(tip);
    }
    
    public void showTime(Time time) {
        timeLabel.setText(time.toString());
    }
}
