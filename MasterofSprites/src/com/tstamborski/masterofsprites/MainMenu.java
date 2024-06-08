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
    public SpriteMenu spriteMenu;
    public ViewMenu viewMenu;
    public HelpMenu helpMenu;
    
    public MainMenu() {
        fileMenu = new FileMenu();
        fileMenu.setMnemonic(KeyEvent.VK_F);
        editMenu = new EditMenu();
        editMenu.setMnemonic(KeyEvent.VK_E);
        spriteMenu = new SpriteMenu();
        spriteMenu.setMnemonic(KeyEvent.VK_S);
        viewMenu = new ViewMenu();
        viewMenu.setMnemonic(KeyEvent.VK_V);
        helpMenu = new HelpMenu();
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        add(fileMenu);
        add(editMenu);
        add(spriteMenu);
        add(viewMenu);
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

class SpriteMenu extends JMenu {
    public JMenuItem slideUpMenuItem, slideDownMenuItem, slideLeftMenuItem, slideRightMenuItem;
    public JMenuItem flipHorzMenuItem, flipVertMenuItem;
    public JMenuItem reflectLeftMenuItem, reflectTopMenuItem;
    public JMenuItem rotateMenuItem, rotate90CWMenuItem, rotate90CCWMenuItem;
    public JMenuItem negateMenuItem;
    
    public SpriteMenu() {
        super("Sprite");
        
        slideUpMenuItem = new JMenuItem("Slide Up");
        slideUpMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK)
        );
        slideUpMenuItem.setMnemonic(KeyEvent.VK_U);
        slideUpMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-up16.png")));
        
        slideDownMenuItem = new JMenuItem("Slide Down");
        slideDownMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK)
        );
        slideDownMenuItem.setMnemonic(KeyEvent.VK_D);
        slideDownMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-down16.png")));
        
        slideLeftMenuItem = new JMenuItem("Slide Left");
        slideLeftMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK)
        );
        slideLeftMenuItem.setMnemonic(KeyEvent.VK_L);
        slideLeftMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-left16.png")));
        
        slideRightMenuItem = new JMenuItem("Slide Right");
        slideRightMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK)
        );
        slideRightMenuItem.setMnemonic(KeyEvent.VK_R);
        slideRightMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/dir-right16.png")));
        
        flipHorzMenuItem = new JMenuItem("Flip Horizontally");
        flipHorzMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0)
        );
        flipHorzMenuItem.setMnemonic(KeyEvent.VK_H);
        flipHorzMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/flip-horz16.png")));
        
        flipVertMenuItem = new JMenuItem("Flip Vertically");
        flipVertMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0)
        );
        flipVertMenuItem.setMnemonic(KeyEvent.VK_V);
        flipVertMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/flip-vert16.png")));
        
        reflectLeftMenuItem = new JMenuItem("Reflect Left to Right");
        reflectLeftMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0)
        );
        reflectLeftMenuItem.setMnemonic(KeyEvent.VK_L);
        reflectLeftMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/mirror-horizontal16.png")));
        
        reflectTopMenuItem = new JMenuItem("Reflect Top to Bottom");
        reflectTopMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0)
        );
        reflectTopMenuItem.setMnemonic(KeyEvent.VK_T);
        reflectTopMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/mirror-vertical16.png")));
        
        rotateMenuItem = new JMenuItem("Rotate...");
        rotateMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        rotateMenuItem.setMnemonic(KeyEvent.VK_R);
        
        rotate90CWMenuItem = new JMenuItem("Rotate 90° CW");
        rotate90CWMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, KeyEvent.CTRL_DOWN_MASK)
        );
        rotate90CWMenuItem.setMnemonic(KeyEvent.VK_W);
        rotate90CWMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/clockwise16.png")));
        
        rotate90CCWMenuItem = new JMenuItem("Rotate 90° CCW");
        rotate90CCWMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, KeyEvent.CTRL_DOWN_MASK)
        );
        rotate90CCWMenuItem.setMnemonic(KeyEvent.VK_C);
        rotate90CCWMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/counter-clockwise16.png")));
        
        negateMenuItem = new JMenuItem("Negate");
        negateMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK)
        );
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
    
    public void enable(ArrayList<Integer> selection) {
        enable(!selection.isEmpty());
    }
    
    @Override
    public void enable(boolean b) {
        for (int i = 0; i < getItemCount(); i++) {
            JMenuItem item = getItem(i);
            if (item != null)
                item.setEnabled(b);
        }
    }
}

class ViewMenu extends JMenu {
    public JMenuItem switchTabMenuItem, runNewInstanceMenuItem;
    
    public ViewMenu() {
        super("View");
        
        switchTabMenuItem = new JMenuItem("Switch Memory / Preview");
        switchTabMenuItem.setMnemonic(KeyEvent.VK_S);
        switchTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        
        runNewInstanceMenuItem = new JMenuItem("Run New Instance");
        runNewInstanceMenuItem.setMnemonic(KeyEvent.VK_N);
        runNewInstanceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, 
                KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK));
        runNewInstanceMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/run16.png")));
        
        add(runNewInstanceMenuItem);
        addSeparator();
        add(switchTabMenuItem);
    }
}

class HelpMenu extends JMenu {
    public JMenuItem aboutMenuItem, manualMenuItem;
    
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
