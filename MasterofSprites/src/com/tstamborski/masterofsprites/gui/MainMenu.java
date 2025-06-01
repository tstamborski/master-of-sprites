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

import java.awt.event.KeyEvent;
import javax.swing.JMenuBar;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class MainMenu extends JMenuBar {
    public FileMenu fileMenu;
    public EditMenu editMenu;
    public SpriteMenu spriteMenu;
    public SelectionMenu selectionMenu;
    public ViewMenu viewMenu;
    public HelpMenu helpMenu;
    
    public MainMenu() {
        fileMenu = new FileMenu();
        fileMenu.setMnemonic(KeyEvent.VK_F);
        editMenu = new EditMenu();
        editMenu.setMnemonic(KeyEvent.VK_E);
        spriteMenu = new SpriteMenu();
        spriteMenu.setMnemonic(KeyEvent.VK_S);
        selectionMenu = new SelectionMenu();
        selectionMenu.setMnemonic(KeyEvent.VK_L);
        viewMenu = new ViewMenu();
        viewMenu.setMnemonic(KeyEvent.VK_V);
        helpMenu = new HelpMenu();
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        add(fileMenu);
        add(editMenu);
        add(spriteMenu);
        add(selectionMenu);
        add(viewMenu);
        add(helpMenu);
    }
}
