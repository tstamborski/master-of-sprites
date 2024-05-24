/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.AboutDialog;
import com.tstamborski.Util;
import com.tstamborski.masterofsprites.model.SpriteProject;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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

    private SpriteProject project;
    private File file;
    
    private final Timer timer;

    private final JTabbedPane centralPane;
    private final MemoryPanel memoryPanel;
    private final EditorPanel editorPanel;
    private final StatusBar statusBar;
    private JMenuBar menu;

    private AboutDialog aboutDialog;
    private ExportPRGDialog addressDialog;

    private JFileChooser prgDialog, rawDialog, bitmapDialog, projectDialog;
    private FileNameExtensionFilter spr_filter, prg_filter, png_filter, jpg_filter, bmp_filter;
    
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem, saveMenuItem, saveAsMenuItem;
    private JMenuItem exportBitmapMenuItem;
    private JMenu fileMenu;
    private JMenuItem exitMenuItem;
    private JMenuItem exportPRGMenuItem;
    private JMenu editMenu;
    private JMenuItem deleteMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenuItem pasteMenuItem;
    private JMenuItem importPRGMenuItem;
    private JMenuItem cutMenuItem;
    private JMenu exportMenu;
    private JMenuItem exportRawMenuItem;
    private JMenu helpMenu;
    private JMenuItem importRawMenuItem;
    private JMenuItem copyMenuItem;

    public MainWindow() {
        setIconImage(new ImageIcon(getClass().getResource("icons/commodore-tool32.png")).getImage());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        centralPane = new JTabbedPane();
        memoryPanel = new MemoryPanel();
        centralPane.add("Memory", memoryPanel);
        editorPanel = new EditorPanel();

        statusBar = new StatusBar();
        
        createMenu();

        setJMenuBar(menu);
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
        
        memoryPanel.getMemoryView().addSelectionListener((se)->editorPanel.setSelection(se.getSelection()));
        memoryPanel.getMemoryView().addActionListener((ae)->{
            editorPanel.getSpriteEditor().refresh();
            editorPanel.setSelection(memoryPanel.getMemoryView().getSelection());
        });
        memoryPanel.getMemoryView().addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                if (!memoryPanel.getMemoryView().isEnabled())
                    return;
                
                statusBar.showContextInfo(String.format("Sprite #%d",
                        memoryPanel.getMemoryView().getIndexAt(e.getX(), e.getY())));
                statusBar.showHint("Hold CTRL or SHIFT to make multiple selection;");
            }
        });
        
        editorPanel.addActionListener(ae -> memoryPanel.getMemoryView().refresh());
        editorPanel.getSpriteEditor().addActionListener(ae -> memoryPanel.getMemoryView().refreshSelection());
        editorPanel.getSpriteEditor().addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                SpriteEditor editor = editorPanel.getSpriteEditor();
                
                if (!editor.isEnabled())
                    return;
                
                statusBar.showContextInfo(String.format("x: %d y: %d",
                        editor.getSpriteX(e.getX()), editor.getSpriteY(e.getY())));
                statusBar.showHint("CTRL+LCLICK to fill the shape; RCLICK to erase;");
            }
        });
    }

    private void reloadProject() {
        memoryPanel.getMemoryView().setProject(project);
        editorPanel.setProject(project);
    }
    
    private void updateTitlebar() {
        if (file != null)
            setTitle(MasterofSprites.PROGRAM_NAME + " -- " + file.getName());
        else
            setTitle(MasterofSprites.PROGRAM_NAME + " -- New File");
    }
    
    public final void newFile() {
        project = SpriteProject.getNewProject(64, false);
        file = null;
        
        reloadProject();
        updateTitlebar();
    }

    public void openFile() {
        InputStream istream;
        ObjectInputStream oistream;
        
        if (projectDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                istream = new FileInputStream(projectDialog.getSelectedFile());
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
            
            file = projectDialog.getSelectedFile();
            reloadProject();
            updateTitlebar();
            
            try {
                oistream.close();
                istream.close();
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
        }
    }
    
    public void saveFile() {
        if (file == null) {
            saveAsFile();
            return;
        }
        
        OutputStream ostream;
        ObjectOutputStream oostream;
        
        try {
            ostream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Util.showError(this, e.getMessage());
            return;
        }
            
        try {
            oostream = new ObjectOutputStream(ostream);
            oostream.writeObject(project);
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
            return;
        }
        
        try {
            oostream.close();
            ostream.close();
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
        }
    }
    
    public void saveAsFile() {
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
                return;
            }
            
            try {
                oostream = new ObjectOutputStream(ostream);
                oostream.writeObject(project);
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
                return;
            }
            
            file = myProjectFile;
            updateTitlebar();
            
            try {
                oostream.close();
                ostream.close();
            } catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
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
        aboutDialog.setApplicationIcon(new ImageIcon(getClass().getResource("icons/commodore-tool32.png")));
        aboutDialog.setApplicationName(MasterofSprites.PROGRAM_NAME);
        aboutDialog.setApplicationVersion(MasterofSprites.PROGRAM_VERSION);
        aboutDialog.setApplicationCopyright(MasterofSprites.PROGRAM_COPYRIGHT);
        aboutDialog.setApplicationExtraInfo(
                "<html><i>Master of sprites, I'm pulling your strings<br>Twisting your mind and smashing your dreams<br></i><html>"
        );
        try {
            aboutDialog.setApplicationLicense(getClass().getResourceAsStream("LICENSE"));
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
        }
        
        addressDialog = new ExportPRGDialog(this);
        addressDialog.setIconImage(new ImageIcon(getClass().getResource("icons/commodore16.png")).getImage());
    }

    private void createFileDialogs() {
        spr_filter
                = new FileNameExtensionFilter("Master of Sprites Project", "spr");
        prg_filter
                = new FileNameExtensionFilter("PRG Files", "prg");
        png_filter
                = new FileNameExtensionFilter("PNG Image", "png");
        jpg_filter
                = new FileNameExtensionFilter("JPG Image", "jpg", "jpeg");
        bmp_filter
                = new FileNameExtensionFilter("BMP Image", "bmp");

        projectDialog = new JFileChooser();
        projectDialog.setDialogTitle("Choose file...");
        projectDialog.setFileFilter(spr_filter);
        projectDialog.setMultiSelectionEnabled(false);
        
        prgDialog = new JFileChooser();
        prgDialog.setDialogTitle("Choose file...");
        prgDialog.setFileFilter(prg_filter);
        prgDialog.setMultiSelectionEnabled(false);

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
                "You have unsaved work!\nDo you want to save before proceeding?", 
                "Unsaved work!", JOptionPane.YES_NO_CANCEL_OPTION);
        
        switch (answer) {
            case JOptionPane.YES_OPTION:
                saveFile();
                return true;
            case JOptionPane.NO_OPTION:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e); //To change body of generated methods, choose Tools | Templates.
    }

    private void createMenu() {
        newMenuItem = new JMenuItem("New");
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/newfile16.png")));
        newMenuItem.addActionListener((ae) -> {
            newFile();
        });

        openMenuItem = new JMenuItem("Open...");
        openMenuItem.setMnemonic(KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        openMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/openfile16.png")));
        openMenuItem.addActionListener((ae) -> {
            openFile();
        });
        
        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/savefile16.png")));
        saveMenuItem.addActionListener((ae) -> {
            saveFile();
        });
        
        saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        saveAsMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/savefile16.png")));
        saveAsMenuItem.addActionListener((ae) -> {
            saveAsFile();
        });
        
        importPRGMenuItem = new JMenuItem("Import PRG file...");
        importPRGMenuItem.setMnemonic(KeyEvent.VK_I);
        importPRGMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        importPRGMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/commodore16.png")));
        importPRGMenuItem.addActionListener((ae) -> {
            importPRGFile();
        });

        importRawMenuItem = new JMenuItem("Import raw data...");
        importRawMenuItem.setMnemonic(KeyEvent.VK_R);
        importRawMenuItem.addActionListener((ae) -> {
            importRawData();
        });

        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        exitMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/exit16.png")));
        exitMenuItem.addActionListener(
                (ae) -> {
                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }
        );

        exportPRGMenuItem = new JMenuItem("As PRG file...");
        exportPRGMenuItem.setMnemonic(KeyEvent.VK_P);
        exportPRGMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        exportPRGMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/commodore16.png")));
        exportPRGMenuItem.addActionListener((ae)->{exportPRGFile();});

        exportRawMenuItem = new JMenuItem("As raw data...");
        exportRawMenuItem.setMnemonic(KeyEvent.VK_R);
        exportRawMenuItem.addActionListener((ae)->{exportRawData();});

        exportBitmapMenuItem = new JMenuItem("As bitmap...");
        exportBitmapMenuItem.setMnemonic(KeyEvent.VK_B);
        exportBitmapMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/picture16.png")));
        exportBitmapMenuItem.addActionListener((ae) -> {
            exportBitmap();
        });

        exportMenu = new JMenu("Export");
        exportMenu.setMnemonic(KeyEvent.VK_E);
        exportMenu.add(exportPRGMenuItem);
        exportMenu.add(exportBitmapMenuItem);
        exportMenu.add(exportRawMenuItem);

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(importPRGMenuItem);
        fileMenu.add(importRawMenuItem);
        fileMenu.add(exportMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/cut16.png")));
        cutMenuItem.setMnemonic(KeyEvent.VK_T);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cutMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().cut();
        });
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/copy16.png")));
        copyMenuItem.setMnemonic(KeyEvent.VK_Y);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        copyMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().copy();
        });
        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/paste16.png")));
        pasteMenuItem.setMnemonic(KeyEvent.VK_P);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        pasteMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().paste();
        });
        deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/bin16.png")));
        deleteMenuItem.setMnemonic(KeyEvent.VK_D);
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.CTRL_DOWN_MASK));
        deleteMenuItem.addActionListener((ae) -> {
            memoryPanel.getMemoryView().delete();
        });

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.addSeparator();
        editMenu.add(deleteMenuItem);

        aboutMenuItem = new JMenuItem("About... ");
        aboutMenuItem.setMnemonic(KeyEvent.VK_A);
        aboutMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/info16.png")));
        aboutMenuItem.addActionListener((ae) -> {
            aboutDialog.setVisible(true);
        });

        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(this.aboutMenuItem);

        menu = new JMenuBar();
        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(helpMenu);
    }
}
