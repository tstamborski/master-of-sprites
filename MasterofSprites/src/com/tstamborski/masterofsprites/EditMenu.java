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

import com.tstamborski.masterofsprites.model.History;
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
public class EditMenu extends JMenu {
    
    public JMenuItem undoMenuItem;
    public JMenuItem redoMenuItem;
    public JMenuItem deleteMenuItem;
    public JMenuItem pasteMenuItem;
    public JMenuItem orPasteMenuItem;
    public JMenuItem andPasteMenuItem;
    public JMenuItem xorPasteMenuItem;
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
        pasteMenuItem.setEnabled(!selection.isEmpty() && getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
        orPasteMenuItem.setEnabled(!selection.isEmpty() && getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
        andPasteMenuItem.setEnabled(!selection.isEmpty() && getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
        xorPasteMenuItem.setEnabled(!selection.isEmpty() && getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
    }

    public void enableHistoryMenuItems(History history) {
        undoMenuItem.setEnabled(history.hasUndo());
        redoMenuItem.setEnabled(history.hasRedo());
    }
    
}
