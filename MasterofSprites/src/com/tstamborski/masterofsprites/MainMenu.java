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

import com.tstamborski.masterofsprites.model.History;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
class MainMenu extends JMenuBar {
    public FileMenu fileMenu;
    public EditMenu editMenu;
    public HelpMenu helpMenu;
    
    public MainMenu() {
        fileMenu = new FileMenu();
        editMenu = new EditMenu();
        helpMenu = new HelpMenu();
        
        add(fileMenu);
        add(editMenu);
        add(helpMenu);
    }
}

class FileMenu extends JMenu {
    public JMenuItem newMenuItem;
    public JMenuItem openMenuItem, saveMenuItem, saveAsMenuItem;
    public JMenuItem exportBitmapMenuItem;
    public JMenuItem exitMenuItem;
    public JMenuItem exportPRGMenuItem;
    public JMenuItem exportAsmMenuItem;
    public JMenuItem importPRGMenuItem;
    public JMenuItem exportRawMenuItem;
    public JMenuItem importRawMenuItem;
    
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

        setMnemonic(KeyEvent.VK_F);
        add(newMenuItem);
        add(openMenuItem);
        add(saveMenuItem);
        add(saveAsMenuItem);
        addSeparator();
        add(importPRGMenuItem);
        add(importRawMenuItem);
        add(exportMenu);
        addSeparator();
        add(exitMenuItem);
    }
}

class EditMenu extends JMenu {
    public JMenuItem undoMenuItem, redoMenuItem;
    public JMenuItem deleteMenuItem;
    public JMenuItem pasteMenuItem;
    public JMenuItem orPasteMenuItem, andPasteMenuItem, xorPasteMenuItem;
    public JMenuItem cutMenuItem;
    public JMenuItem copyMenuItem;
    
    public EditMenu() {
        super("Edit");
        
        undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/undo16.png")));
        undoMenuItem.setMnemonic(KeyEvent.VK_U);
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        
        redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/redo16.png")));
        redoMenuItem.setMnemonic(KeyEvent.VK_R);
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        
        
        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/cut16.png")));
        cutMenuItem.setMnemonic(KeyEvent.VK_T);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/copy16.png")));
        copyMenuItem.setMnemonic(KeyEvent.VK_Y);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        
        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/paste16.png")));
        pasteMenuItem.setMnemonic(KeyEvent.VK_P);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        
        orPasteMenuItem = new JMenuItem("OR Paste");
        orPasteMenuItem.setMnemonic(KeyEvent.VK_O);
        
        andPasteMenuItem = new JMenuItem("AND Paste");
        andPasteMenuItem.setMnemonic(KeyEvent.VK_A);
        
        xorPasteMenuItem = new JMenuItem("XOR Paste");
        xorPasteMenuItem.setMnemonic(KeyEvent.VK_X);
        
        deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/bin16.png")));
        deleteMenuItem.setMnemonic(KeyEvent.VK_D);
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        
        setMnemonic(KeyEvent.VK_E);
        add(undoMenuItem);
        add(redoMenuItem);
        addSeparator();
        add(cutMenuItem);
        add(copyMenuItem);
        add(pasteMenuItem);
        addSeparator();
        add(orPasteMenuItem);
        add(andPasteMenuItem);
        add(xorPasteMenuItem);
        addSeparator();
        add(deleteMenuItem);
    }
    
    public void enableClipboardMenuItems(ArrayList<Integer> selection) {
        deleteMenuItem.setEnabled(!selection.isEmpty());
        cutMenuItem.setEnabled(!selection.isEmpty());
        copyMenuItem.setEnabled(!selection.isEmpty());
        
        pasteMenuItem.setEnabled(!selection.isEmpty() && 
                getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
        orPasteMenuItem.setEnabled(!selection.isEmpty() && 
                getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
        andPasteMenuItem.setEnabled(!selection.isEmpty() && 
                getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
        xorPasteMenuItem.setEnabled(!selection.isEmpty() && 
                getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
    }
    
    public void enableHistoryMenuItems(History history) {
        undoMenuItem.setEnabled(history.hasUndo());
        redoMenuItem.setEnabled(history.hasRedo());
    }
}

class HelpMenu extends JMenu {
    public JMenuItem aboutMenuItem;
    
    public HelpMenu() {
        super("Help");
        
        aboutMenuItem = new JMenuItem("About... ");
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        aboutMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/info16.png")));
        
        setMnemonic(KeyEvent.VK_H);
        add(aboutMenuItem);
    }
}
