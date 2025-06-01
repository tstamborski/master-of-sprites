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
package com.tstamborski.masterofsprites.gui;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class ViewMenu extends JMenu {
    
    public JMenuItem switchTabMenuItem;
    public JMenuItem runNewWindowMenuItem;
    public JMenuItem ghostSkinningMenuItem;
    public JCheckBoxMenuItem editorGridMenuItem;

    public ViewMenu() {
        super("View");
        switchTabMenuItem = new JMenuItem("Switch Memory / Preview");
        switchTabMenuItem.setMnemonic(KeyEvent.VK_M);
        switchTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        runNewWindowMenuItem = new JMenuItem("Run New Window... ");
        runNewWindowMenuItem.setMnemonic(KeyEvent.VK_N);
        runNewWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        runNewWindowMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/run16.png")));
        editorGridMenuItem = new JCheckBoxMenuItem("Editor Grid", false);
        editorGridMenuItem.setMnemonic(KeyEvent.VK_G);
        editorGridMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK));
        editorGridMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/grid16.png")));
        ghostSkinningMenuItem = new JMenuItem("Set Ghost Skinning... ");
        ghostSkinningMenuItem.setMnemonic(KeyEvent.VK_S);
        ghostSkinningMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK));
        ghostSkinningMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/ghost16.png")));
        add(runNewWindowMenuItem);
        addSeparator();
        add(ghostSkinningMenuItem);
        add(editorGridMenuItem);
        addSeparator();
        add(switchTabMenuItem);
    }
    
}
