/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites.gui;

import com.tstamborski.Util;
import com.tstamborski.masterofsprites.MasterofSprites;
import com.tstamborski.masterofsprites.Settings;
import com.tstamborski.masterofsprites.AsmCodeStream;
import com.tstamborski.masterofsprites.DialogManager;
import com.tstamborski.masterofsprites.History;
import com.tstamborski.masterofsprites.MenuManager;
import com.tstamborski.masterofsprites.Selection;
import com.tstamborski.masterofsprites.model.SpriteProject;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author Tobiasz
 */
public class MainWindow extends JFrame {
    private static final int HISTORY_SIZE = 10;

    private static int instanceCounter = 0;
    
    private SpriteProject project;
    private History history;
    private File file;
    private Timer timer;
    private final Settings settings;
    
    private final MenuManager menuManager;
    private final DialogManager dialogManager;

    private final JTabbedPane centralPane;
    private final MemoryPanel memoryPanel;
    private final PreviewPanel previewPanel;
    private final EditorPanel editorPanel;
    private final StatusBar statusBar;

    public MainWindow() {
        setIconImage(new ImageIcon(getClass().getResource("icons/commodore-puppet48.png")).getImage());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instanceCounter++;

        centralPane = new JTabbedPane();
        memoryPanel = new MemoryPanel();
        previewPanel = new PreviewPanel();
        centralPane.add("Memory", memoryPanel);
        centralPane.add("Preview", previewPanel);
        editorPanel = new EditorPanel();
        statusBar = new StatusBar();
        
        add(centralPane, BorderLayout.CENTER);
        add(editorPanel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
        
        menuManager = new MenuManager();
        setJMenuBar(menuManager.createMenu());
        dialogManager = new DialogManager();

        settings = new Settings();
        loadSettings();
        newFile();
        
        enableEvents(WindowEvent.WINDOW_EVENT_MASK);
        wireUp();
    }
    
    private void wireUp() {
        timer = new Timer(1000, e->{
            project.getWorkTime().advance(1);
            statusBar.showTime(project.getWorkTime());
        });
        timer.start();
        
        memoryPanel.getMemoryView().addSelectionListener((se)->{
            editorPanel.setSelection(se.getSelection());
            previewPanel.setSelection(se.getSelection());
            
            menuManager.enableBySelection(se.getSelection());
        });
        memoryPanel.getMemoryView().addActionListener((ae)->{
            editorPanel.reload();
            previewPanel.reload();
            pushHistory();
        });
        memoryPanel.getMemoryView().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                
                statusBar.showContextInfo("");
                statusBar.showHint("");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                
                if (!memoryPanel.getMemoryView().isEnabled())
                    return;
                
                statusBar.showHint(
                        "Hold CTRL or SHIFT to make multiple selection; ALT for block selection;");
            }
        });
        memoryPanel.getMemoryView().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!memoryPanel.getMemoryView().isEnabled())
                    return;
                
                statusBar.showContextInfo(String.format("Sprite #%d",
                        memoryPanel.getMemoryView().getIndexAt(e.getX(), e.getY())));
            }
        });
        
        editorPanel.addActionListener(ae -> {
            memoryPanel.getMemoryView().refresh();
            previewPanel.reload();
            setSaved(false);
        });
        editorPanel.addPreviewListener(pe -> {
            previewPanel.reload();
            setSaved(false);
        });
        editorPanel.getSpriteEditor().addActionListener(ae -> {
            memoryPanel.getMemoryView().refreshSelection();
            previewPanel.reload();
            pushHistory();
        });
        editorPanel.getSpriteEditor().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                
                statusBar.showContextInfo("");
                statusBar.showHint("");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                
                if (!editorPanel.getSpriteEditor().isEnabled())
                    return;
                
                statusBar.showHint("CTRL+LCLICK to fill the shape; RCLICK to erase; MWHEEL to change color;");
            }
        });
        editorPanel.getSpriteEditor().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                SpriteEditor editor = editorPanel.getSpriteEditor();
                
                if (!editor.isEnabled())
                    return;
                
                statusBar.showContextInfo(String.format("x: %d y: %d",
                        editor.getSpriteX(e.getX()), editor.getSpriteY(e.getY())));
            }
        });
        
        menuManager.wireMenu(this);
    }
    
    private void loadSettings() {
        settings.loadFileChooser(dialogManager.getProjectDialog(), Settings.PROJECT_FILE_DLG);
        settings.loadFileChooser(dialogManager.getPrgDialog(), Settings.PRG_FILE_DLG);
        settings.loadFileChooser(dialogManager.getRawDialog(), Settings.RAW_FILE_DLG);
        settings.loadFileChooser(dialogManager.getBitmapDialog(), Settings.BITMAP_FILE_DLG);
        settings.loadFileChooser(dialogManager.getAsmDialog(), Settings.ASM_FILE_DLG);
        
        settings.loadAsmSyntaxDialog(dialogManager.getAsmSyntaxDialog());
        settings.loadGhostSkinningDialog(dialogManager.getGhostDialog());
        editorPanel.getSpriteEditor().setGhostSkinning(dialogManager.getGhostDialog().getGhostSkinning());
        
        settings.loadSpriteEditor(editorPanel.getSpriteEditor());
        menuManager.setEditorGrid(editorPanel.getSpriteEditor().isGrid());
    }
    
    private void saveSettings() {
        settings.saveFileChooser(dialogManager.getProjectDialog(), Settings.PROJECT_FILE_DLG);
        settings.saveFileChooser(dialogManager.getPrgDialog(), Settings.PRG_FILE_DLG);
        settings.saveFileChooser(dialogManager.getRawDialog(), Settings.RAW_FILE_DLG);
        settings.saveFileChooser(dialogManager.getBitmapDialog(), Settings.BITMAP_FILE_DLG);
        settings.saveFileChooser(dialogManager.getAsmDialog(), Settings.ASM_FILE_DLG);
        
        settings.saveAsmSyntaxDialog(dialogManager.getAsmSyntaxDialog());
        settings.saveGhostSkinningDialog(dialogManager.getGhostDialog());
        
        settings.saveSpriteEditor(editorPanel.getSpriteEditor());
    }
    
    private void reloadProject() {
        memoryPanel.setProject(project);
        editorPanel.setProject(project);
        previewPanel.setProject(project);
        
        menuManager.enableBySelection(getSelection());
    }
    
    private void updateTitlebar() {
        String savedStr = history.isSaved() ? "" : " ~ ";
        
        if (file != null)
            setTitle(MasterofSprites.PROGRAM_NAME + " -- " + file.getName() + savedStr);
        else
            setTitle(MasterofSprites.PROGRAM_NAME + " -- Untitled" + savedStr);
    }
    
    public final void newFile() {
        project = SpriteProject.getNewProject(64, false);
        
        newHistory();
        file = null;
        reloadProject();
        updateTitlebar();
    }

    public void openFile() {
        if (dialogManager.getProjectDialog().showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            openFile(dialogManager.getProjectDialog().getSelectedFile());
    }
    
    public void openFile(File f) {
        InputStream istream;
        ObjectInputStream oistream;
        
        try {
            istream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            Util.showError(this, e.getMessage());
            return;
        }

        try {
            oistream = new ObjectInputStream(istream);
            project = (SpriteProject)oistream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Util.showError(this, e.getMessage());
            return;
        }

        file = f;
        newHistory();
        reloadProject();
        setSaved(true);

        try {
            oistream.close();
            istream.close();
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
        }
    }
    
    public void loadExample(String name) {
        InputStream istream;
        ObjectInputStream oistream;
        
        istream = getClass().getResourceAsStream("examples/" + name);

        try {
            oistream = new ObjectInputStream(istream);
            project = (SpriteProject)oistream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Util.showError(this, e.getMessage());
            return;
        }

        file = null;
        project.getWorkTime().reset();
        newHistory();
        reloadProject();
        setSaved(true);
        //updateTitlebar();

        try {
            oistream.close();
            istream.close();
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
        }
    }
    
    public boolean saveFile() {
        if (file == null)
            return saveAsFile();
        
        OutputStream ostream;
        ObjectOutputStream oostream;
        
        try {
            ostream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Util.showError(this, e.getMessage());
            return true;
        }
            
        try {
            oostream = new ObjectOutputStream(ostream);
            oostream.writeObject(project);
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
            return true;
        }
        
        setSaved(true);
        
        try {
            oostream.close();
            ostream.close();
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
        }
        
        return true;
    }
    
    public boolean saveAsFile() {
        OutputStream ostream;
        ObjectOutputStream oostream;
        File myProjectFile;
        
        if (dialogManager.getProjectDialog().showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            myProjectFile = Util.addExtension(
                    dialogManager.getProjectDialog().getSelectedFile(),
                    ((FileNameExtensionFilter)dialogManager.getProjectDialog().getFileFilter()).getExtensions());
            
            try {
                ostream = new FileOutputStream(myProjectFile);
            } catch (FileNotFoundException e) {
                Util.showError(this, e.getMessage());
                return true;
            }
            
            try {
                oostream = new ObjectOutputStream(ostream);
                oostream.writeObject(project);
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
                return true;
            }
            
            setSaved(true);
            file = myProjectFile;
            updateTitlebar();
            
            try {
                oostream.close();
                ostream.close();
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    public void importPRGFile() {
        InputStream istream;

        if (dialogManager.getPrgDialog().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                istream = new FileInputStream(dialogManager.getPrgDialog().getSelectedFile());
            } catch (FileNotFoundException e) {
                Util.showError(this, e.getMessage());
                return;
            }
            try {
                project = SpriteProject.importPRGData(istream);
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
                return;
            }
            
            newHistory();
            file = null;
            reloadProject();
            updateTitlebar();
            
            try {
                istream.close();
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
        }
    }

    public void importRawData() {
        InputStream istream;

        if (dialogManager.getRawDialog().showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                istream = new FileInputStream(dialogManager.getRawDialog().getSelectedFile());
            } catch (FileNotFoundException e) {
                Util.showError(this, e.getMessage());
                return;
            }
            try {
                project = SpriteProject.importRAWData(istream);
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
                return;
            }
            
            newHistory();
            file = null;
            reloadProject();
            updateTitlebar();
            
            try {
                istream.close();
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
        }
    }
    
    public void exportPRGFile() {
        dialogManager.getAddressDialog().setAddress(project.getDefaultAddress());
        
        if (dialogManager.getAddressDialog().showDialog(this)) {
            short addr = (short)dialogManager.getAddressDialog().getAddress();
            OutputStream ostream;
            File myPRGFile;
            
            project.setDefaultAddress(addr);

            if (dialogManager.getPrgDialog().showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                myPRGFile = Util.addExtension(
                        dialogManager.getPrgDialog().getSelectedFile(),
                        ((FileNameExtensionFilter)dialogManager.getPrgDialog().getFileFilter()).getExtensions());
                
                if (showOverwriteDialog(myPRGFile) == false)
                    return;
                
                try {
                    ostream = new FileOutputStream(myPRGFile);
                    project.exportToPRGData(ostream, addr);
                }
                catch (FileNotFoundException e) {
                    Util.showError(this, e.getMessage());
                    return;
                }
                catch (IOException e) {
                    Util.showError(this, e.getMessage());
                    return;
                }
                
                try {
                    ostream.close();
                } catch (IOException e) {
                    Util.showError(this, e.getMessage());
                }
            }
        }
    }
    
    public void exportAsmCode() {
        if (dialogManager.getAsmSyntaxDialog().showDialog(this)) {
            OutputStream ostream;
            File myAsmFile;

            if (dialogManager.getAsmDialog().showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                myAsmFile = Util.addExtension(
                        dialogManager.getAsmDialog().getSelectedFile(),
                        ((FileNameExtensionFilter)dialogManager.getAsmDialog().getFileFilter()).getExtensions());
                
                if (showOverwriteDialog(myAsmFile) == false)
                    return;
                
                try {
                    ostream = new FileOutputStream(myAsmFile);
                    try (AsmCodeStream asm = new AsmCodeStream(ostream, dialogManager.getAsmSyntaxDialog().getAsmSyntax())) {
                        asm.printSpriteProject(project);
                        asm.close();
                    }
                }
                catch (FileNotFoundException e) {
                    Util.showError(this, e.getMessage());
                    return;
                }
                
                try {
                    ostream.close();
                } catch (IOException e) {
                    Util.showError(this, e.getMessage());
                }
            }
        }
    }
    
    public void exportRawData() {
        OutputStream ostream;

        if (dialogManager.getRawDialog().showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (showOverwriteDialog(dialogManager.getRawDialog().getSelectedFile()) == false)
                return;
            
            try {
                ostream = new FileOutputStream(dialogManager.getRawDialog().getSelectedFile());
                project.exportToRAWData(ostream);
            }
            catch (FileNotFoundException e) {
                Util.showError(this, e.getMessage());
                return;
            }
            catch (IOException e) {
                Util.showError(this, e.getMessage());
                return;
            }
            
            try {
                ostream.close();
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
        }
    }

    public void exportBitmap() {
        File bitmapFile;
        FileNameExtensionFilter filter;
        String extensions[];

        if (dialogManager.getBitmapDialog().showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (showOverwriteDialog(dialogManager.getBitmapDialog().getSelectedFile()) == false)
                return;
            
            bitmapFile = dialogManager.getBitmapDialog().getSelectedFile();
            filter = (FileNameExtensionFilter) dialogManager.getBitmapDialog().getFileFilter();
            extensions = filter.getExtensions();

            bitmapFile = Util.addExtension(bitmapFile, extensions);

            try {
                switch (extensions[0]) {
                    case "png":
                        ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_ARGB), extensions[0], bitmapFile);
                        break;
                    case "jpg":
                        ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_RGB), extensions[0], bitmapFile);
                        break;
                    case "bmp":
                        ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_BGR), extensions[0], bitmapFile);
                        break;
                    default:
                        ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_ARGB), "png", bitmapFile);
                        break;
                }
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
        }
    }

    public boolean showOverwriteDialog(File file) {
        if (file.exists()) {
            return JOptionPane.showConfirmDialog(this, "Do you want to overwrite?", "File Exist!", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION;
        }
        else {
            return true;
        }
    }
    
    public boolean showUnsavedDialog() {
        int answer = JOptionPane.showConfirmDialog(this, 
                "Do you want to save before proceeding?", 
                "Unsaved work!", JOptionPane.YES_NO_CANCEL_OPTION);
        
        switch (answer) {
            case JOptionPane.YES_OPTION:
                return saveFile();
            case JOptionPane.NO_OPTION:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            saveSettings();
            if (!isSaved() && !showUnsavedDialog())
                return;
        }
        
        if (e.getID() == WindowEvent.WINDOW_CLOSED) {
            instanceCounter--;
            if (instanceCounter <= 0)
                System.exit(0);
        }
        
        super.processWindowEvent(e);
    }
    
    public void setSaved(boolean b) {
        history.setSaved(b);
        updateTitlebar();
    }
    
    public boolean isSaved() {
        return history.isSaved();
    }
    
    public Selection getSelection() {
        return memoryPanel.getMemoryView().getSelection();
    }
    
    public MemoryPanel getMemoryPanel() {
        return memoryPanel;
    }
    
    public PreviewPanel getPreviewPanel() {
        return previewPanel;
    }
    
    public EditorPanel getEditorPanel() {
        return editorPanel;
    }
    
    public JTabbedPane getTabbedPane() {
        return centralPane;
    }
    
    public DialogManager getDialogManager() {
        return dialogManager;
    }
    
    public void showAboutDialog() {
        dialogManager.getAboutDialog().showDialog(this);
    }
    
    public void showManualDialog() {
        dialogManager.getManDialog().showDialog(this);
    }

    private void newHistory() {
        history = new History(project, HISTORY_SIZE);
        history.setSaved(true);
        
        menuManager.enableByHistory(history);
        updateTitlebar();
    }
    
    private void pushHistory() {
        history.push(project);
        menuManager.enableByHistory(history);
        updateTitlebar();
    }
    
    public void undo() {
        history.undo(project);
        editorPanel.reload();
        memoryPanel.reload();
        previewPanel.reload();
        
        menuManager.enableByHistory(history);
        updateTitlebar();
    }
    
    public void redo() {
        history.redo(project);
        editorPanel.reload();
        memoryPanel.reload();
        previewPanel.reload();
        
        menuManager.enableByHistory(history);
        updateTitlebar();
    }
}
