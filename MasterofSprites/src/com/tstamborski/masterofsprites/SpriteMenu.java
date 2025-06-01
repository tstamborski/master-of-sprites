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
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpriteMenu extends JMenu {
    
    public JMenuItem slideUpMenuItem;
    public JMenuItem slideDownMenuItem;
    public JMenuItem slideLeftMenuItem;
    public JMenuItem slideRightMenuItem;
    public JMenuItem flipHorzMenuItem;
    public JMenuItem flipVertMenuItem;
    public JMenuItem reflectLeftMenuItem;
    public JMenuItem reflectTopMenuItem;
    public JMenuItem rotateMenuItem;
    public JMenuItem rotate90CWMenuItem;
    public JMenuItem rotate90CCWMenuItem;
    public JMenuItem negateMenuItem;

    public SpriteMenu() {
        super("Sprite");
        
        slideUpMenuItem = new JMenuItem("Slide Up");
        slideUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK));
        slideUpMenuItem.setMnemonic(KeyEvent.VK_U);
        slideUpMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-up16.png")));
        slideDownMenuItem = new JMenuItem("Slide Down");
        slideDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK));
        slideDownMenuItem.setMnemonic(KeyEvent.VK_D);
        slideDownMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-down16.png")));
        slideLeftMenuItem = new JMenuItem("Slide Left");
        slideLeftMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK));
        slideLeftMenuItem.setMnemonic(KeyEvent.VK_L);
        slideLeftMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-left16.png")));
        slideRightMenuItem = new JMenuItem("Slide Right");
        slideRightMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK));
        slideRightMenuItem.setMnemonic(KeyEvent.VK_R);
        slideRightMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-right16.png")));
        
        flipHorzMenuItem = new JMenuItem("Flip Horizontally");
        flipHorzMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        flipHorzMenuItem.setMnemonic(KeyEvent.VK_H);
        flipHorzMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/flip-horz16.png")));
        flipVertMenuItem = new JMenuItem("Flip Vertically");
        flipVertMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        flipVertMenuItem.setMnemonic(KeyEvent.VK_V);
        flipVertMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/flip-vert16.png")));
        
        reflectLeftMenuItem = new JMenuItem("Reflect Left to Right");
        reflectLeftMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        reflectLeftMenuItem.setMnemonic(KeyEvent.VK_L);
        reflectLeftMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/mirror-horizontal16.png")));
        reflectTopMenuItem = new JMenuItem("Reflect Top to Bottom");
        reflectTopMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        reflectTopMenuItem.setMnemonic(KeyEvent.VK_T);
        reflectTopMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/mirror-vertical16.png")));
        
        rotateMenuItem = new JMenuItem("Rotate...");
        rotateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.CTRL_DOWN_MASK));
        rotateMenuItem.setMnemonic(KeyEvent.VK_R);
        rotate90CWMenuItem = new JMenuItem("Rotate 90° CW");
        rotate90CWMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, KeyEvent.CTRL_DOWN_MASK));
        rotate90CWMenuItem.setMnemonic(KeyEvent.VK_W);
        rotate90CWMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/clockwise16.png")));
        rotate90CCWMenuItem = new JMenuItem("Rotate 90° CCW");
        rotate90CCWMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, KeyEvent.CTRL_DOWN_MASK));
        rotate90CCWMenuItem.setMnemonic(KeyEvent.VK_C);
        rotate90CCWMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/counter-clockwise16.png")));
        
        negateMenuItem = new JMenuItem("Negate");
        negateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK));
        negateMenuItem.setMnemonic(KeyEvent.VK_N);
        negateMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/film16.png")));
        
        add(slideUpMenuItem);
        add(slideDownMenuItem);
        add(slideLeftMenuItem);
        add(slideRightMenuItem);
        addSeparator();
        add(flipHorzMenuItem);
        add(flipVertMenuItem);
        addSeparator();
        add(reflectLeftMenuItem);
        add(reflectTopMenuItem);
        addSeparator();
        add(rotateMenuItem);
        add(rotate90CWMenuItem);
        add(rotate90CCWMenuItem);
        addSeparator();
        add(negateMenuItem);
    }

    public void enableItems(Selection selection) {
        enableItems(!selection.isEmpty());
    }

    public void enableItems(boolean b) {
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null) {
                item.setEnabled(b);
            }
        }
    }
    
}
