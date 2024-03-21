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
    
    private final Timer timer;

    private final JTabbedPane centralPane;
    private final MemoryPanel memoryPanel;
    private final EditorPanel editorPanel;
    private final StatusBar statusBar;
    private JMenuBar menu;

    private AboutDialog aboutDialog;
    private ExportPRGDialog addressDialog;

    private JFileChooser prgDialog, rawDialog, bitmapDialog;
    private FileNameExtensionFilter prg_filter, png_filter, jpg_filter, bmp_filter;
    
    private JMenuItem newMenuItem;
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
        Palette palette = new StandardPalette();
        
        setTitle(MasterofSprites.PROGRAM_NAME);
        setIconImage(new ImageIcon(getClass().getResource("icons/commodore-tool32.png")).getImage());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        centralPane = new JTabbedPane();
        memoryPanel = new MemoryPanel(palette);
        centralPane.add("Memory", memoryPanel);
        editorPanel = new EditorPanel(palette);

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
    }

    public final void newFile() {
        project = SpriteProject.getNewProject(64, false);
        memoryPanel.getMemoryView().setMemoryData(project.getMemoryData());
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
            memoryPanel.getMemoryView().setMemoryData(project.getMemoryData());
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
            memoryPanel.getMemoryView().setMemoryData(project.getMemoryData());
        }
    }
    
    public void exportPRGFile() {
        addressDialog.setAddress(project.getDefaultAddress());
        
        if (addressDialog.showDialog()) {
            short addr = (short)addressDialog.getAddress();
            OutputStream ostream;

            if (prgDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                if (showOverwriteDialog(prgDialog.getSelectedFile()) == false)
                    return;
                
                try {
                    ostream = new FileOutputStream(prgDialog.getSelectedFile());
                    project.exportToPRGData(ostream, addr);
                }
                catch (FileNotFoundException e) {
                    Util.showError(this, e.getMessage());
                }
                catch (IOException e) {
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
            }
            catch (IOException e) {
                Util.showError(this, e.getMessage());
            }
        }
    }

    public void exportBitmap() {
        File file;
        FileNameExtensionFilter filter;
        String extensions[];

        if (bitmapDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (showOverwriteDialog(bitmapDialog.getSelectedFile()) == false)
                return;
            
            file = bitmapDialog.getSelectedFile();
            filter = (FileNameExtensionFilter) bitmapDialog.getFileFilter();
            extensions = filter.getExtensions();

            if (!Util.hasExtension(file, extensions)) {
                file = new File(file.getAbsolutePath() + "." + extensions[0]);
            }

            try {
                if (extensions[0].equals(png_filter.getExtensions()[0])) {
                    ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_ARGB), extensions[0], file);
                } else if (extensions[0].equals(jpg_filter.getExtensions()[0])) {
                    ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_RGB), extensions[0], file);
                } else if (extensions[0].equals(bmp_filter.getExtensions()[0])) {
                    ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_BGR), extensions[0], file);
                } else {
                    ImageIO.write(memoryPanel.getMemoryView().toBufferedImage(BufferedImage.TYPE_INT_ARGB), "png", file);
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
        aboutDialog.setApplicationExtraInfo("The Commodore 64 sprite editor made in Java!");
        try {
            aboutDialog.setApplicationLicense(getClass().getResourceAsStream("LICENSE"));
        } catch (IOException e) {
            Util.showError(this, e.getMessage());
        }
        
        addressDialog = new ExportPRGDialog(this);
        addressDialog.setIconImage(new ImageIcon(getClass().getResource("icons/commodore16.png")).getImage());
    }

    private void createFileDialogs() {
        prg_filter
                = new FileNameExtensionFilter("PRG Files", "prg");
        png_filter
                = new FileNameExtensionFilter("PNG Image", "png");
        jpg_filter
                = new FileNameExtensionFilter("JPG Image", "jpg", "jpeg");
        bmp_filter
                = new FileNameExtensionFilter("BMP Image", "bmp");

        prgDialog = new JFileChooser();
        prgDialog.setFileFilter(prg_filter);
        prgDialog.setMultiSelectionEnabled(false);

        rawDialog = new JFileChooser();
        rawDialog.setMultiSelectionEnabled(false);

        bitmapDialog = new JFileChooser();
        bitmapDialog.addChoosableFileFilter(png_filter);
        bitmapDialog.addChoosableFileFilter(jpg_filter);
        bitmapDialog.addChoosableFileFilter(bmp_filter);
        bitmapDialog.setFileFilter(png_filter);
        bitmapDialog.setMultiSelectionEnabled(false);
    }
    
    private boolean showOverwriteDialog(File file) {
        if (file.exists()) {
            return JOptionPane.showConfirmDialog(this, "Do you want to overwrite?", "File Exist!", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION;
        }
        else {
            return true;
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
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
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
