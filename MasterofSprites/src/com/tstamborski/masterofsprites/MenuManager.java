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

import com.tstamborski.masterofsprites.gui.MainMenu;
import com.tstamborski.masterofsprites.gui.MainWindow;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class MenuManager {
    MainMenu menu;
    
    public MenuManager() {
    }
    
    public JMenuBar createMenu() {
        menu = new MainMenu();
        return menu;
    }
        
    public void wireMenu(MainWindow wnd) {
        menu.fileMenu.newMenuItem.addActionListener((ae) -> {
            if (wnd.isSaved() || wnd.showUnsavedDialog())
                wnd.newFile();
        });
        menu.fileMenu.openMenuItem.addActionListener((ae) -> {
            if (wnd.isSaved() || wnd.showUnsavedDialog())
                wnd.openFile();
        });
        menu.fileMenu.saveMenuItem.addActionListener((ae) -> {
            wnd.saveFile();
        });
        menu.fileMenu.saveAsMenuItem.addActionListener((ae) -> {
            wnd.saveAsFile();
        });
        menu.fileMenu.importPRGMenuItem.addActionListener((ae) -> {
            if (wnd.isSaved() || wnd.showUnsavedDialog())
                wnd.importPRGFile();
        });
        menu.fileMenu.importRawMenuItem.addActionListener((ae) -> {
            if (wnd.isSaved() || wnd.showUnsavedDialog())
                wnd.importRawData();
        });
        int itCount = menu.fileMenu.examplesMenu.getItemCount();
        for (int i = 0; i < itCount; i++) {
            JMenuItem item = menu.fileMenu.examplesMenu.getItem(i);
            item.addActionListener(ae -> {
                if (wnd.isSaved() || wnd.showUnsavedDialog())
                    wnd.loadExample(item.getText());
            });
        }
        menu.fileMenu.exitMenuItem.addActionListener(
                (ae) -> {
                    wnd.dispatchEvent(new WindowEvent(wnd, WindowEvent.WINDOW_CLOSING));
                }
        );
        menu.fileMenu.exportPRGMenuItem.addActionListener((ae)->{wnd.exportPRGFile();});
        menu.fileMenu.exportAsmMenuItem.addActionListener((ae)->{wnd.exportAsmCode();});
        menu.fileMenu.exportRawMenuItem.addActionListener((ae)->{wnd.exportRawData();});
        menu.fileMenu.exportBitmapMenuItem.addActionListener((ae) -> {
            wnd.exportBitmap();
        });

        menu.editMenu.undoMenuItem.addActionListener((ae) -> {
            wnd.undo();
        });
        menu.editMenu.redoMenuItem.addActionListener((ae) -> {
            wnd.redo();
        });
        menu.editMenu.cutMenuItem.addActionListener((ae) -> {
            wnd.getMemoryPanel().getMemoryView().cut();
        });
        menu.editMenu.copyMenuItem.addActionListener((ae) -> {
            wnd.getMemoryPanel().getMemoryView().copy();
        });
        menu.editMenu.pasteMenuItem.addActionListener((ae) -> {
            wnd.getMemoryPanel().getMemoryView().paste();
        });
        menu.editMenu.orPasteMenuItem.addActionListener((ae) -> {
            wnd.getMemoryPanel().getMemoryView().specialPaste((a,b) -> (byte)(a | b));
        });
        menu.editMenu.andPasteMenuItem.addActionListener((ae) -> {
            wnd.getMemoryPanel().getMemoryView().specialPaste((a,b) -> (byte)(a & b));
        });
        menu.editMenu.xorPasteMenuItem.addActionListener((ae) -> {
            wnd.getMemoryPanel().getMemoryView().specialPaste((a,b) -> (byte)(a ^ b));
        });
        menu.editMenu.deleteMenuItem.addActionListener((ae) -> {
            wnd.getMemoryPanel().getMemoryView().delete();
        });
        
        menu.spriteMenu.slideUpMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.slideUp());
        });
        menu.spriteMenu.slideDownMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.slideDown());
        });
        menu.spriteMenu.slideLeftMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.slideLeft());
        });
        menu.spriteMenu.slideRightMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.slideRight());
        });
        menu.spriteMenu.flipHorzMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.flipHorizontally());
        });
        menu.spriteMenu.flipVertMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.flipVertically());
        });
        menu.spriteMenu.reflectLeftMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.reflectLeft2Right());
        });
        menu.spriteMenu.reflectTopMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.reflectTop2Bottom());
        });
        menu.spriteMenu.rotateMenuItem.addActionListener(ae -> {
            if (wnd.getRotationDialog().showDialog())
                wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData(
                        sd -> SpriteRotator.rotate(sd, wnd.getRotationDialog().getRadians())
                );
        });
        menu.spriteMenu.rotate90CWMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData(
                        sd -> SpriteRotator.rotate(sd, Math.toRadians(90.0d))
                );
        });
        menu.spriteMenu.rotate90CCWMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData(
                        sd -> SpriteRotator.rotate(sd, Math.toRadians(-90.0d))
                );
        });
        menu.spriteMenu.negateMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().onCurrentSpriteData((sd)->sd.negate());
        });
        
        menu.selectionMenu.selectAllMenuItem.addActionListener(ae -> 
                wnd.getMemoryPanel().getMemoryView().onSelection(s -> {s.all();})
        );
        menu.selectionMenu.invertMenuItem.addActionListener(ae -> 
                wnd.getMemoryPanel().getMemoryView().onSelection(s -> {s.invert();})
        );
        menu.selectionMenu.selectNoneMenuItem.addActionListener(ae -> 
                wnd.getMemoryPanel().getMemoryView().onSelection(s -> {s.clear();})
        );
        menu.selectionMenu.prevFrameMenuItem.addActionListener(ae -> 
                wnd.getMemoryPanel().getMemoryView().onSelection(s -> {s.shift(-1);})
        );
        menu.selectionMenu.nextFrameMenuItem.addActionListener(ae -> 
                wnd.getMemoryPanel().getMemoryView().onSelection(s -> {s.shift(1);})
        );
        menu.selectionMenu.overlayBackwardMenuItem.addActionListener(ae -> 
                wnd.getMemoryPanel().getMemoryView().onSelection(
                        s -> {s.shift(-s.getSpriteProject().getOverlayDistance());}
                )
        );
        menu.selectionMenu.overlayForwardMenuItem.addActionListener(ae -> 
                wnd.getMemoryPanel().getMemoryView().onSelection(
                        s -> {s.shift(s.getSpriteProject().getOverlayDistance());}
                )
        );
        menu.selectionMenu.slideUpMenuItem.addActionListener(ae ->
                wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(sd -> sd.slideUp())
        );
        menu.selectionMenu.slideDownMenuItem.addActionListener(ae ->
                wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(sd -> sd.slideDown())
        );
        menu.selectionMenu.slideLeftMenuItem.addActionListener(ae ->
                wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(sd -> sd.slideLeft())
        );
        menu.selectionMenu.slideRightMenuItem.addActionListener(ae ->
                wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(sd -> sd.slideRight())
        );
        menu.selectionMenu.flipHorzMenuItem.addActionListener(ae ->
                wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(sd -> sd.flipHorizontally())
        );
        menu.selectionMenu.flipVertMenuItem.addActionListener(ae ->
                wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(sd -> sd.flipVertically())
        );
        menu.selectionMenu.rotateMenuItem.addActionListener(ae -> {
                if (wnd.getRotationDialog().showDialog())
                    wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(
                                sd -> SpriteRotator.rotate(sd, wnd.getRotationDialog().getRadians())
                            );
        });
        menu.selectionMenu.rotate90CWMenuItem.addActionListener(ae -> {
            wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(
                    sd -> SpriteRotator.rotate(sd, Math.toRadians(90.0d))
            );
        });
        menu.selectionMenu.rotate90CCWMenuItem.addActionListener(ae -> {
            wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(
                    sd -> SpriteRotator.rotate(sd, Math.toRadians(-90.0d))
            );
        });
        menu.selectionMenu.negateMenuItem.addActionListener(ae -> 
                wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(sd -> sd.negate())
        );
        menu.selectionMenu.applySpriteColorMenuItem.addActionListener(ae -> {
                if (wnd.getApplyColorDialog().showDialog())
                    wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(
                            sd -> sd.setSpriteC64Color(wnd.getApplyColorDialog().getC64Color())
                    );
        });
        menu.selectionMenu.applyMulticolorMenuItem.addActionListener(ae -> {
                if (wnd.getApplyMulticolorDialog().showDialog())
                    wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(
                            sd -> sd.setMulticolor(wnd.getApplyMulticolorDialog().getValue())
                    );
        });
        menu.selectionMenu.applyOverlayMenuItem.addActionListener(ae -> {
                if (wnd.getApplyOverlayDialog().showDialog())
                    wnd.getMemoryPanel().getMemoryView().onSelectedSpriteData(
                            sd -> sd.setOverlay(wnd.getApplyOverlayDialog().getValue())
                    );
        });
        
        menu.viewMenu.switchTabMenuItem.addActionListener(ae -> {
            wnd.getTabbedPane().setSelectedIndex((wnd.getTabbedPane().getSelectedIndex() + 1) % 2);
            wnd.getTabbedPane().requestFocusInWindow();
        });
        menu.viewMenu.ghostSkinningMenuItem.addActionListener(ae -> {
            if (wnd.getGhostSkinningDialog().showDialog()) {
                wnd.getEditorPanel().getSpriteEditor().setGhostSkinning(wnd.getGhostSkinningDialog().getGhostSkinning());
            }
        });
        menu.viewMenu.editorGridMenuItem.addActionListener(ae -> {
            wnd.getEditorPanel().getSpriteEditor().setGrid(menu.viewMenu.editorGridMenuItem.isSelected());
        });
        menu.viewMenu.runNewWindowMenuItem.addActionListener((ActionEvent ae) -> {
            SwingUtilities.invokeLater(() -> {
                MainWindow new_wnd = new MainWindow();
                new_wnd.setVisible(true);
            });
        });

        menu.helpMenu.manualMenuItem.addActionListener((ae) -> wnd.showManualDialog());
        menu.helpMenu.aboutMenuItem.addActionListener((ae) -> wnd.showAboutDialog());
        
        wnd.getToolkit().getSystemClipboard().addFlavorListener(fe -> 
                menu.editMenu.enableClipboardMenuItems(wnd.getSelection()));
    }
    
    public boolean isEditorGrid() {
        return menu.viewMenu.editorGridMenuItem.isSelected();
    }
    
    public void setEditorGrid(boolean state) {
        menu.viewMenu.editorGridMenuItem.setSelected(state);
    }
    
    public void enableByHistory(History history) {
        menu.editMenu.enableHistoryMenuItems(history);
    }
    
    public void enableBySelection(Selection selection) {
        if (menu == null) 
            return;
        
        menu.editMenu.enableClipboardMenuItems(selection);
        menu.spriteMenu.enableItems(selection);
        menu.selectionMenu.enableItems(selection);
    }
}
