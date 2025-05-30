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

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class HelpMenu extends JMenu {
    
    public JMenuItem aboutMenuItem;
    public JMenuItem manualMenuItem;

    public HelpMenu() {
        super("Help");
        manualMenuItem = new JMenuItem("Manual...");
        manualMenuItem.setMnemonic(KeyEvent.VK_M);
        manualMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        manualMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/handbook16.png")));
        aboutMenuItem = new JMenuItem("About...");
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        aboutMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/info16.png")));
        add(manualMenuItem);
        addSeparator();
        add(aboutMenuItem);
    }
    
}
