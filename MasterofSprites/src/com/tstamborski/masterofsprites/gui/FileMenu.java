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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class FileMenu extends JMenu {
    
    public JMenuItem newMenuItem;
    public JMenuItem openMenuItem;
    public JMenuItem saveMenuItem;
    public JMenuItem saveAsMenuItem;
    public JMenuItem exportBitmapMenuItem;
    public JMenuItem exitMenuItem;
    public JMenuItem exportPRGMenuItem;
    public JMenuItem exportAsmMenuItem;
    public JMenuItem importPRGMenuItem;
    public JMenuItem exportRawMenuItem;
    public JMenuItem importRawMenuItem;
    public final JMenu examplesMenu;
    private final JMenu exportMenu;

    public FileMenu() {
        super("File");
        newMenuItem = new JMenuItem("New");
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/newfile16.png")));
        openMenuItem = new JMenuItem("Open...");
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        openMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/openfile16.png")));
        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/savefile16.png")));
        saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        saveAsMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/savefile16.png")));
        importPRGMenuItem = new JMenuItem("Import PRG file...");
        importPRGMenuItem.setMnemonic(KeyEvent.VK_I);
        importPRGMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        importPRGMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/commodore16.png")));
        importRawMenuItem = new JMenuItem("Import Raw Data...");
        importRawMenuItem.setMnemonic(KeyEvent.VK_R);
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        exitMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/exit16.png")));
        exportPRGMenuItem = new JMenuItem("As PRG File...");
        exportPRGMenuItem.setMnemonic(KeyEvent.VK_P);
        exportPRGMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        exportPRGMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/commodore16.png")));
        exportAsmMenuItem = new JMenuItem("As Assembly Code...");
        exportAsmMenuItem.setMnemonic(KeyEvent.VK_A);
        exportAsmMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/asm-file16.png")));
        exportRawMenuItem = new JMenuItem("As Raw Data...");
        exportRawMenuItem.setMnemonic(KeyEvent.VK_R);
        exportBitmapMenuItem = new JMenuItem("As Bitmap...");
        exportBitmapMenuItem.setMnemonic(KeyEvent.VK_B);
        exportBitmapMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/picture16.png")));
        exportMenu = new JMenu("Export");
        exportMenu.setMnemonic(KeyEvent.VK_E);
        exportMenu.add(exportPRGMenuItem);
        exportMenu.add(exportAsmMenuItem);
        exportMenu.add(exportBitmapMenuItem);
        exportMenu.add(exportRawMenuItem);
        examplesMenu = new JMenu("Examples");
        examplesMenu.setMnemonic(KeyEvent.VK_M);
        //class loader w javie **8** nie obsluguje
        //zeby przejrzec caly pakiet - trzeba na piechote
        examplesMenu.add(new JMenuItem("Gorilla.spr"));
        examplesMenu.add(new JMenuItem("Halloween.spr"));
        examplesMenu.add(new JMenuItem("Pirate.spr"));
        examplesMenu.add(new JMenuItem("Symbols.spr"));
        examplesMenu.add(new JMenuItem("Various.spr"));
        add(newMenuItem);
        add(openMenuItem);
        add(saveMenuItem);
        add(saveAsMenuItem);
        addSeparator();
        add(importPRGMenuItem);
        add(importRawMenuItem);
        add(exportMenu);
        addSeparator();
        add(examplesMenu);
        addSeparator();
        add(exitMenuItem);
    }
    
}
