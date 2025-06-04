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

import com.tstamborski.masterofsprites.gui.StatusBar;
import com.tstamborski.masterofsprites.model.Time;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class StatusBarManager {
    StatusBar bar;
    
    public StatusBarManager() {
    }
    
    public StatusBar createStatusBar() {
        bar = new StatusBar();
        return bar;
    }
    
    public void showStatusTime(Time t) {
        bar.showTime(t);
    }
    
    public void addStatusMessageHolder(StatusMessageHolder component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                
                bar.showContextInfo("");
                bar.showHint("");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                
                if (!component.isEnabled())
                    return;
                
                bar.showHint(component.getStatusHint());
            }
        });
        
        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!component.isEnabled())
                    return;
                
                bar.showContextInfo(component.getStatusMessage(e.getX(), e.getY()));
            }
        });
    }
}
