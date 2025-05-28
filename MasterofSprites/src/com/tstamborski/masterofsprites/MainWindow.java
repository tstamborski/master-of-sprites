/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.AboutDialog;
import com.tstamborski.ManualDialog;
import com.tstamborski.Util;
import com.tstamborski.masterofsprites.model.AsmCodeStream;
import com.tstamborski.masterofsprites.model.History;
import com.tstamborski.masterofsprites.model.SpriteProject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
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
    
    private final Timer timer;

    private final JTabbedPane centralPane;
    private final MemoryPanel memoryPanel;
    private final PreviewPanel previewPanel;
    private final EditorPanel editorPanel;
    private final StatusBar statusBar;
    private MainMenu menu;

    private AboutDialog aboutDialog;
    private ManualDialog manDialog;
    
    private ExportPRGDialog addressDialog;
    private AsmSyntaxDialog asmSyntaxDialog;
    private RotationDialog rotateDialog;
    private C64ColorDialog applyColorDialog;
    private FlagDialog applyMulticolorDialog, applyOverlayDialog;
    private GhostSkinningDialog ghostDialog;

    private JFileChooser prgDialog, rawDialog, bitmapDialog, projectDialog, asmDialog;
    private FileNameExtensionFilter spr_filter, prg_filter, png_filter, jpg_filter, bmp_filter, asm_filter;
    
    private final Settings settings;

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
        
        createMenu();
        add(centralPane, BorderLayout.CENTER);
        add(editorPanel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        createFileDialogs();
        createCustomDialogs();

        enableEvents(WindowEvent.WINDOW_EVENT_MASK);

        newFile();
        
        statusBar.showTime(project.getWorkTime());
        timer = new Timer(1000, e->{
            project.getWorkTime().advance(1);
            statusBar.showTime(project.getWorkTime());
        });
        timer.start();
        
        getToolkit().getSystemClipboard().addFlavorListener(fe -> 
                menu.editMenu.enableClipboardMenuItems(memoryPanel.getMemoryView().getSelection()));
        memoryPanel.getMemoryView().addSelectionListener((se)->{
            editorPanel.setSelection(se.getSelection());
            previewPanel.setSelection(se.getSelection());
            
            menu.editMenu.enableClipboardMenuItems(se.getSelection());
            menu.spriteMenu.enableItems(se.getSelection());
            menu.selectionMenu.enableItems(se.getSelection());
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
                        "Hold CTRL or SHIFT to make multiple selection; CTRL+ALT for block selection;");
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
        
        settings = new Settings();
        loadSettings();
    }
    
    private void loadSettings() {
        settings.loadFileChooser(projectDialog, Settings.PROJECT_FILE_DLG);
        settings.loadFileChooser(prgDialog, Settings.PRG_FILE_DLG);
        settings.loadFileChooser(rawDialog, Settings.RAW_FILE_DLG);
        settings.loadFileChooser(bitmapDialog, Settings.BITMAP_FILE_DLG);
        settings.loadFileChooser(asmDialog, Settings.ASM_FILE_DLG);
        
        settings.loadAsmSyntaxDialog(asmSyntaxDialog);
        settings.loadGhostSkinningDialog(ghostDialog);
        editorPanel.getSpriteEditor().setGhostSkinning(ghostDialog.getGhostSkinning());
        
        settings.loadSpriteEditor(editorPanel.getSpriteEditor());
        menu.viewMenu.editorGridMenuItem.setSelected(
                editorPanel.getSpriteEditor().isGrid()
        );
    }
    
    private void saveSettings() {
        settings.saveFileChooser(projectDialog, Settings.PROJECT_FILE_DLG);
        settings.saveFileChooser(prgDialog, Settings.PRG_FILE_DLG);
        settings.saveFileChooser(rawDialog, Settings.RAW_FILE_DLG);
        settings.saveFileChooser(bitmapDialog, Settings.BITMAP_FILE_DLG);
        settings.saveFileChooser(asmDialog, Settings.ASM_FILE_DLG);
        
        settings.saveAsmSyntaxDialog(asmSyntaxDialog);
        settings.saveGhostSkinningDialog(ghostDialog);
        
        settings.saveSpriteEditor(editorPanel.getSpriteEditor());
    }
    
    private void reloadProject() {
        memoryPanel.setProject(project);
        editorPanel.setProject(project);
        previewPanel.setProject(project);
        
        menu.editMenu.enableClipboardMenuItems(memoryPanel.getMemoryView().getSelection());
        menu.spriteMenu.enableItems(memoryPanel.getMemoryView().getSelection());
        menu.selectionMenu.enableItems(memoryPanel.getMemoryView().getSelection());
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
        if (projectDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            openFile(projectDialog.getSelectedFile());
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
        //updateTitlebar();

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
        
        if (projectDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            myProjectFile = Util.addExtension(
                    projectDialog.getSelectedFile(),
                    ((FileNameExtensionFilter)projectDialog.getFileFilter()).getExtensions());
            
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

        if (prgDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                istream = new FileInputStream(prgDialog.getSelectedFile());
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

        if (rawDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                istream = new FileInputStream(rawDialog.getSelectedFile());
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
        addressDialog.setAddress(project.getDefaultAddress());
        
        if (addressDialog.showDialog()) {
            short addr = (short)addressDialog.getAddress();
            OutputStream ostream;
            File myPRGFile;
            
            project.setDefaultAddress(addr);

            if (prgDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                myPRGFile = Util.addExtension(
                        prgDialog.getSelectedFile(),
                        ((FileNameExtensionFilter)prgDialog.getFileFilter()).getExtensions());
                
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
        if (asmSyntaxDialog.showDialog()) {
            OutputStream ostream;
            File myAsmFile;

            if (asmDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                myAsmFile = Util.addExtension(
                        asmDialog.getSelectedFile(),
                        ((FileNameExtensionFilter)asmDialog.getFileFilter()).getExtensions());
                
                if (showOverwriteDialog(myAsmFile) == false)
                    return;
                
                try {
                    ostream = new FileOutputStream(myAsmFile);
                    try (AsmCodeStream asm = new AsmCodeStream(ostream, asmSyntaxDialog.getAsmSyntax())) {
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

        if (rawDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (showOverwriteDialog(rawDialog.getSelectedFile()) == false)
                return;
            
            try {
                ostream = new FileOutputStream(rawDialog.getSelectedFile());
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

        if (bitmapDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (showOverwriteDialog(bitmapDialog.getSelectedFile()) == false)
                return;
            
            bitmapFile = bitmapDialog.getSelectedFile();
            filter = (FileNameExtensionFilter) bitmapDialog.getFileFilter();
            extensions = filter.getExtensions();

            bitmapFile = Util.addExtension(bitmapFile, extensions);

            try {
                if (extensions[0].equals(png_filter.getExtensions()[0])) {
                    ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_ARGB), extensions[0], bitmapFile);
                } else if (extensions[0].equals(jpg_filter.getExtensions()[0])) {
                    ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_RGB), extensions[0], bitmapFile);
                } else if (extensions[0].equals(bmp_filter.getExtensions()[0])) {
                    ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_BGR), extensions[0], bitmapFile);
                } else {
                    ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_ARGB), "png", bitmapFile);
                }
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
        }
    }

    private void createCustomDialogs() {
        aboutDialog = new AboutDialog(this);
        aboutDialog.setApplicationIcon(new ImageIcon(getClass().getResource("icons/commodore-puppet48.png")));
        aboutDialog.setApplicationName(MasterofSprites.PROGRAM_NAME);
        aboutDialog.setApplicationVersion(MasterofSprites.PROGRAM_VERSION);
        aboutDialog.setApplicationCopyright(MasterofSprites.PROGRAM_COPYRIGHT);
        aboutDialog.setApplicationExtraInfo(
                "<html><i>Master of sprites, I'm pulling your strings<br>Twisting your mind and smashing your dreams<br></i><html>"
        );
        try {
            aboutDialog.setApplicationLicense(getClass().getResourceAsStream("docs/license.txt"));
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
        }
        
        try {
            manDialog = new ManualDialog(this, getClass().getResource("docs/manual.html"));
            manDialog.setIconImage(
                    new ImageIcon(getClass().getResource("icons/handbook16.png")).getImage());
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
        }
        
        addressDialog = new ExportPRGDialog(this);
        addressDialog.setIconImage(
                new ImageIcon(getClass().getResource("icons/commodore16.png")).getImage());
        asmSyntaxDialog = new AsmSyntaxDialog(this);
        asmSyntaxDialog.setIconImage(
                new ImageIcon(getClass().getResource("icons/asm-file16.png")).getImage());
        
        rotateDialog = new RotationDialog(this);
        
        applyColorDialog = new C64ColorDialog(this);
        applyColorDialog.setIconImage(
                new ImageIcon(getClass().getResource("icons/palette16.png")).getImage());
        applyMulticolorDialog = new FlagDialog(this, "Multicolor? ");
        applyMulticolorDialog.setIconImage(
                new ImageIcon(getClass().getResource("icons/flag-red16.png")).getImage());
        applyOverlayDialog = new FlagDialog(this, "Overlay? ");
        applyOverlayDialog.setIconImage(
                new ImageIcon(getClass().getResource("icons/flag-blue16.png")).getImage());
        
        ghostDialog = new GhostSkinningDialog(this);
        ghostDialog.setIconImage(new ImageIcon(getClass().getResource("icons/ghost16.png")).getImage());
    }

    private void createFileDialogs() {
        spr_filter
                = new FileNameExtensionFilter("Master of Sprites Project [.spr]", "spr");
        prg_filter
                = new FileNameExtensionFilter("PRG Files [.prg, .PRG]", "prg", "PRG");
        png_filter
                = new FileNameExtensionFilter("PNG Image [.png]", "png");
        jpg_filter
                = new FileNameExtensionFilter("JPG Image [.jpg, .jpeg]", "jpg", "jpeg");
        bmp_filter
                = new FileNameExtensionFilter("BMP Image [.bmp]", "bmp");
        asm_filter
                = new FileNameExtensionFilter("Assembly Code [.asm, .s]", "asm", "s");

        projectDialog = new JFileChooser();
        projectDialog.setDialogTitle("Choose file...");
        projectDialog.setFileFilter(spr_filter);
        projectDialog.setMultiSelectionEnabled(false);
        
        prgDialog = new JFileChooser();
        prgDialog.setDialogTitle("Choose file...");
        prgDialog.setFileFilter(prg_filter);
        prgDialog.setMultiSelectionEnabled(false);
        
        asmDialog = new JFileChooser();
        asmDialog.setDialogTitle("Choose file...");
        asmDialog.setFileFilter(asm_filter);
        asmDialog.setMultiSelectionEnabled(false);

        rawDialog = new JFileChooser();
        rawDialog.setDialogTitle("Choose file...");
        rawDialog.setMultiSelectionEnabled(false);

        bitmapDialog = new JFileChooser();
        bitmapDialog.setDialogTitle("Choose file...");
        bitmapDialog.addChoosableFileFilter(png_filter);
        bitmapDialog.addChoosableFileFilter(jpg_filter);
        bitmapDialog.addChoosableFileFilter(bmp_filter);
        bitmapDialog.setFileFilter(png_filter);
        bitmapDialog.setMultiSelectionEnabled(false);
    }
    
    protected boolean showOverwriteDialog(File file) {
        if (file.exists()) {
            return JOptionPane.showConfirmDialog(this, "Do you want to overwrite?", "File Exist!", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION;
        }
        else {
            return true;
        }
    }
    
    protected boolean showUnsavedDialog() {
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
            if (!history.isSaved() && !showUnsavedDialog())
                return;
        }
        
        if (e.getID() == WindowEvent.WINDOW_CLOSED) {
            instanceCounter--;
            if (instanceCounter <= 0)
                System.exit(0);
        }
        
        super.processWindowEvent(e);
    }
    
    private void setSaved(boolean b) {
        history.setSaved(b);
        updateTitlebar();
    }

    private void newHistory() {
        history = new History(project, HISTORY_SIZE);
        history.setSaved(true); //zobaczymy (?)
        menu.editMenu.enableHistoryMenuItems(history);
        updateTitlebar();
    }
    
    private void pushHistory() {
        history.push(project);
        menu.editMenu.enableHistoryMenuItems(history);
        updateTitlebar();
    }
    
    public void undo() {
        history.undo(project);
        editorPanel.reload();
        memoryPanel.reload();
        previewPanel.reload();
        
        menu.editMenu.enableHistoryMenuItems(history);
        updateTitlebar();
    }
    
    public void redo() {
        history.redo(project);
        editorPanel.reload();
        memoryPanel.reload();
        previewPanel.reload();
        
        menu.editMenu.enableHistoryMenuItems(history);
        updateTitlebar();
    }
    
    private void createMenu() {
        menu = new MainMenu();
        
        menu.fileMenu.newMenuItem.addActionListener((ae) -> {
            if (history.isSaved() || showUnsavedDialog())
                newFile();
        });
        menu.fileMenu.openMenuItem.addActionListener((ae) -> {
            if (history.isSaved() || showUnsavedDialog())
                openFile();
        });
        menu.fileMenu.saveMenuItem.addActionListener((ae) -> {
            saveFile();
        });
        menu.fileMenu.saveAsMenuItem.addActionListener((ae) -> {
            saveAsFile();
        });
        menu.fileMenu.importPRGMenuItem.addActionListener((ae) -> {
            if (history.isSaved() || showUnsavedDialog())
                importPRGFile();
        });
        menu.fileMenu.importRawMenuItem.addActionListener((ae) -> {
            if (history.isSaved() || showUnsavedDialog())
                importRawData();
        });
        int itCount = menu.fileMenu.examplesMenu.getItemCount();
        for (int i = 0; i < itCount; i++) {
            JMenuItem item = menu.fileMenu.examplesMenu.getItem(i);
            item.addActionListener(ae -> {
                if (history.isSaved() || showUnsavedDialog())
                    loadExample(item.getText());
            });
        }
        menu.fileMenu.exitMenuItem.addActionListener(
                (ae) -> {
                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }
        );
         
        menu.fileMenu.exportPRGMenuItem.addActionListener((ae)->{exportPRGFile();});
        menu.fileMenu.exportAsmMenuItem.addActionListener((ae)->{exportAsmCode();});
        menu.fileMenu.exportRawMenuItem.addActionListener((ae)->{exportRawData();});
        menu.fileMenu.exportBitmapMenuItem.addActionListener((ae) -> {
            exportBitmap();
        });

        menu.editMenu.undoMenuItem.addActionListener((ae) -> {
            undo();
        });
        menu.editMenu.redoMenuItem.addActionListener((ae) -> {
            redo();
        });
        
        
        menu.editMenu.cutMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().cut();
        });
        menu.editMenu.copyMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().copy();
        });
        menu.editMenu.pasteMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().paste();
        });
        
        menu.editMenu.orPasteMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().specialPaste((a,b) -> (byte)(a | b));
        });
        menu.editMenu.andPasteMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().specialPaste((a,b) -> (byte)(a & b));
        });
        menu.editMenu.xorPasteMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().specialPaste((a,b) -> (byte)(a ^ b));
        });
        
        menu.editMenu.deleteMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().delete();
        });
        
        menu.spriteMenu.slideUpMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.slideUp());
        });
        menu.spriteMenu.slideDownMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.slideDown());
        });
        menu.spriteMenu.slideLeftMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.slideLeft());
        });
        menu.spriteMenu.slideRightMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.slideRight());
        });
        
        menu.spriteMenu.flipHorzMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.flipHorizontally());
        });
        menu.spriteMenu.flipVertMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.flipVertically());
        });
        
        menu.spriteMenu.reflectLeftMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.reflectLeft2Right());
        });
        menu.spriteMenu.reflectTopMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.reflectTop2Bottom());
        });
        
        menu.spriteMenu.rotateMenuItem.addActionListener(ae -> {
            if (rotateDialog.showDialog())
                editorPanel.getSpriteEditor().onCurrentSpriteData(
                        sd -> SpriteRotator.rotate(sd, rotateDialog.getRadians())
                );
        });
        menu.spriteMenu.rotate90CWMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData(
                        sd -> SpriteRotator.rotate(sd, Math.toRadians(90.0d))
                );
        });
        menu.spriteMenu.rotate90CCWMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData(
                        sd -> SpriteRotator.rotate(sd, Math.toRadians(-90.0d))
                );
        });
        
        menu.spriteMenu.negateMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().onCurrentSpriteData((sd)->sd.negate());
        });
        
        menu.selectionMenu.selectAllMenuItem.addActionListener(ae -> 
                memoryPanel.getMemoryView().onSelection(s -> {s.all();})
        );
        menu.selectionMenu.invertMenuItem.addActionListener(ae -> 
                memoryPanel.getMemoryView().onSelection(s -> {s.invert();})
        );
        menu.selectionMenu.selectNoneMenuItem.addActionListener(ae -> 
                memoryPanel.getMemoryView().onSelection(s -> {s.clear();})
        );
        
        menu.selectionMenu.prevFrameMenuItem.addActionListener(ae -> 
                memoryPanel.getMemoryView().onSelection(s -> {s.shift(-1);})
        );
        menu.selectionMenu.nextFrameMenuItem.addActionListener(ae -> 
                memoryPanel.getMemoryView().onSelection(s -> {s.shift(1);})
        );
        menu.selectionMenu.overlayBackwardMenuItem.addActionListener(ae -> 
                memoryPanel.getMemoryView().onSelection(
                        s -> {s.shift(-s.getSpriteProject().getOverlayDistance());}
                )
        );
        menu.selectionMenu.overlayForwardMenuItem.addActionListener(ae -> 
                memoryPanel.getMemoryView().onSelection(
                        s -> {s.shift(s.getSpriteProject().getOverlayDistance());}
                )
        );
        
        menu.selectionMenu.flipHorzMenuItem.addActionListener(ae ->
                memoryPanel.getMemoryView().onSelectedSpriteData(sd -> sd.flipHorizontally())
        );
        menu.selectionMenu.flipVertMenuItem.addActionListener(ae ->
                memoryPanel.getMemoryView().onSelectedSpriteData(sd -> sd.flipVertically())
        );
        menu.selectionMenu.rotateMenuItem.addActionListener(ae -> {
                if (rotateDialog.showDialog())
                    memoryPanel.getMemoryView().onSelectedSpriteData(
                                sd -> SpriteRotator.rotate(sd, rotateDialog.getRadians())
                            );
        });
        menu.selectionMenu.rotate90CWMenuItem.addActionListener(ae -> {
            memoryPanel.getMemoryView().onSelectedSpriteData(
                    sd -> SpriteRotator.rotate(sd, Math.toRadians(90.0d))
            );
        });
        menu.selectionMenu.rotate90CCWMenuItem.addActionListener(ae -> {
            memoryPanel.getMemoryView().onSelectedSpriteData(
                    sd -> SpriteRotator.rotate(sd, Math.toRadians(-90.0d))
            );
        });
        
        menu.selectionMenu.applySpriteColorMenuItem.addActionListener(ae -> {
                if (applyColorDialog.showDialog())
                    memoryPanel.getMemoryView().onSelectedSpriteData(
                            sd -> sd.setSpriteC64Color(applyColorDialog.getC64Color())
                    );
        });
        menu.selectionMenu.applyMulticolorMenuItem.addActionListener(ae -> {
                if (applyMulticolorDialog.showDialog())
                    memoryPanel.getMemoryView().onSelectedSpriteData(
                            sd -> sd.setMulticolor(applyMulticolorDialog.getValue())
                    );
        });
        menu.selectionMenu.applyOverlayMenuItem.addActionListener(ae -> {
                if (applyOverlayDialog.showDialog())
                    memoryPanel.getMemoryView().onSelectedSpriteData(
                            sd -> sd.setOverlay(applyOverlayDialog.getValue())
                    );
        });
        
        menu.viewMenu.switchTabMenuItem.addActionListener(ae -> {
            centralPane.setSelectedIndex((centralPane.getSelectedIndex() + 1) % 2);
            centralPane.requestFocusInWindow();
        });
        menu.viewMenu.ghostSkinningMenuItem.addActionListener(ae -> {
            if (ghostDialog.showDialog()) {
                editorPanel.getSpriteEditor().setGhostSkinning(ghostDialog.getGhostSkinning());
            }
        });
        menu.viewMenu.editorGridMenuItem.addActionListener(ae -> {
            editorPanel.getSpriteEditor().setGrid(menu.viewMenu.editorGridMenuItem.isSelected());
        });
        menu.viewMenu.runNewWindowMenuItem.addActionListener((ActionEvent ae) -> {
            SwingUtilities.invokeLater(() -> {
                MainWindow wnd = new MainWindow();
                wnd.setVisible(true);
            });
        });

        menu.helpMenu.manualMenuItem.addActionListener((ae) -> manDialog.setVisible(true));
        menu.helpMenu.aboutMenuItem.addActionListener((ae) -> {
            aboutDialog.setVisible(true);
        });
        
        setJMenuBar(menu);
    }
}
