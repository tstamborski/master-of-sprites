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

import com.tstamborski.masterofsprites.Selection;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SelectionMenu extends JMenu {
    
    public JMenuItem selectAllMenuItem;
    public JMenuItem selectNoneMenuItem;
    public JMenuItem invertMenuItem;
    
    public JMenuItem nextFrameMenuItem;
    public JMenuItem prevFrameMenuItem;
    public JMenuItem overlayForwardMenuItem;
    public JMenuItem overlayBackwardMenuItem;
    
    public JMenuItem slideUpMenuItem, slideDownMenuItem;
    public JMenuItem slideLeftMenuItem, slideRightMenuItem;
    public JMenuItem flipHorzMenuItem;
    public JMenuItem flipVertMenuItem;
    public JMenuItem rotateMenuItem;
    public JMenuItem rotate90CWMenuItem;
    public JMenuItem rotate90CCWMenuItem;
    public JMenuItem negateMenuItem;
    
    public JMenuItem applySpriteColorMenuItem;
    public JMenuItem applyMulticolorMenuItem;
    public JMenuItem applyOverlayMenuItem;
    
    private final JMenu commandsMenu;

    public SelectionMenu() {
        super("Selection");
        
        selectAllMenuItem = new JMenuItem("Select All");
        selectAllMenuItem.setMnemonic(KeyEvent.VK_A);
        selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        selectAllMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/selection16.png")));
        selectNoneMenuItem = new JMenuItem("Select None");
        selectNoneMenuItem.setMnemonic(KeyEvent.VK_E);
        selectNoneMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        selectNoneMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/cross-red-small16.png")));
        invertMenuItem = new JMenuItem("Invert Selection");
        invertMenuItem.setMnemonic(KeyEvent.VK_I);
        invertMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, KeyEvent.CTRL_DOWN_MASK));
        invertMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/invert16.png")));
        
        nextFrameMenuItem = new JMenuItem("Next Frame");
        nextFrameMenuItem.setMnemonic(KeyEvent.VK_N);
        nextFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));
        nextFrameMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/increment16.png")));
        prevFrameMenuItem = new JMenuItem("Previous Frame");
        prevFrameMenuItem.setMnemonic(KeyEvent.VK_P);
        prevFrameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
        prevFrameMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/decrement16.png")));
        overlayForwardMenuItem = new JMenuItem("Overlay Distance Forward");
        overlayForwardMenuItem.setMnemonic(KeyEvent.VK_F);
        overlayForwardMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, KeyEvent.CTRL_DOWN_MASK));
        overlayForwardMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/scroll-right16.png")));
        overlayBackwardMenuItem = new JMenuItem("Overlay Distance Backward");
        overlayBackwardMenuItem.setMnemonic(KeyEvent.VK_B);
        overlayBackwardMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, KeyEvent.CTRL_DOWN_MASK));
        overlayBackwardMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/scroll-left16.png")));
        
        commandsMenu = new JMenu("Commands");
        commandsMenu.setMnemonic(KeyEvent.VK_C);
        
        slideUpMenuItem = new JMenuItem("Slide Up");
        slideUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        slideUpMenuItem.setMnemonic(KeyEvent.VK_U);
        slideUpMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-up16.png")));
        slideDownMenuItem = new JMenuItem("Slide Down");
        slideDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        slideDownMenuItem.setMnemonic(KeyEvent.VK_D);
        slideDownMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-down16.png")));
        slideLeftMenuItem = new JMenuItem("Slide Left");
        slideLeftMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        slideLeftMenuItem.setMnemonic(KeyEvent.VK_L);
        slideLeftMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-left16.png")));
        slideRightMenuItem = new JMenuItem("Slide Right");
        slideRightMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        slideRightMenuItem.setMnemonic(KeyEvent.VK_R);
        slideRightMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-right16.png")));
        flipHorzMenuItem = new JMenuItem("Flip Horizontally");
        flipHorzMenuItem.setMnemonic(KeyEvent.VK_H);
        flipHorzMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        flipHorzMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/flip-horz16.png")));
        flipVertMenuItem = new JMenuItem("Flip Vertically");
        flipVertMenuItem.setMnemonic(KeyEvent.VK_V);
        flipVertMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        flipVertMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/flip-vert16.png")));
        rotateMenuItem = new JMenuItem("Rotate...");
        rotateMenuItem.setMnemonic(KeyEvent.VK_R);
        rotateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        rotate90CWMenuItem = new JMenuItem("Rotate 90° CW");
        rotate90CWMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/clockwise16.png")));
        rotate90CWMenuItem.setMnemonic(KeyEvent.VK_W);
        rotate90CWMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        rotate90CCWMenuItem = new JMenuItem("Rotate 90° CCW");
        rotate90CCWMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/counter-clockwise16.png")));
        rotate90CCWMenuItem.setMnemonic(KeyEvent.VK_C);
        rotate90CCWMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        negateMenuItem = new JMenuItem("Negate");
        negateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        negateMenuItem.setMnemonic(KeyEvent.VK_N);
        negateMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/film16.png")));
        
        commandsMenu.add(slideUpMenuItem);
        commandsMenu.add(slideDownMenuItem);
        commandsMenu.add(slideLeftMenuItem);
        commandsMenu.add(slideRightMenuItem);
        commandsMenu.addSeparator();
        commandsMenu.add(flipHorzMenuItem);
        commandsMenu.add(flipVertMenuItem);
        commandsMenu.addSeparator();
        commandsMenu.add(rotateMenuItem);
        commandsMenu.add(rotate90CWMenuItem);
        commandsMenu.add(rotate90CCWMenuItem);
        commandsMenu.addSeparator();
        commandsMenu.add(negateMenuItem);
        
        applySpriteColorMenuItem = new JMenuItem("Apply Sprite Color...");
        applySpriteColorMenuItem.setMnemonic(KeyEvent.VK_L);
        applySpriteColorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        applySpriteColorMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/palette16.png")));
        applyMulticolorMenuItem = new JMenuItem("Apply Multicolor Mode...");
        applyMulticolorMenuItem.setMnemonic(KeyEvent.VK_M);
        applyMulticolorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        applyMulticolorMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/flag-red16.png")));
        applyOverlayMenuItem = new JMenuItem("Apply Overlay Flag...");
        applyOverlayMenuItem.setMnemonic(KeyEvent.VK_O);
        applyOverlayMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        applyOverlayMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/flag-blue16.png")));
        
        add(selectAllMenuItem);
        add(selectNoneMenuItem);
        add(invertMenuItem);
        addSeparator();
        add(nextFrameMenuItem);
        add(prevFrameMenuItem);
        add(overlayForwardMenuItem);
        add(overlayBackwardMenuItem);
        addSeparator();
        add(commandsMenu);
        addSeparator();
        add(applySpriteColorMenuItem);
        add(applyMulticolorMenuItem);
        add(applyOverlayMenuItem);
    }

    public void enableItems(Selection selection) {
        enableItems(!selection.isEmpty());
    }

    public void enableItems(boolean b) {
        nextFrameMenuItem.setEnabled(b);
        prevFrameMenuItem.setEnabled(b);
        overlayForwardMenuItem.setEnabled(b);
        overlayBackwardMenuItem.setEnabled(b);
        
        commandsMenu.setEnabled(b);
        
        applySpriteColorMenuItem.setEnabled(b);
        applyMulticolorMenuItem.setEnabled(b);
        applyOverlayMenuItem.setEnabled(b);
    }
    
}
