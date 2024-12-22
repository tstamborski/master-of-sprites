/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.Util;
import com.tstamborski.masterofsprites.model.AsmCodeStream;
import com.tstamborski.masterofsprites.model.SpriteProject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import javax.swing.SwingUtilities;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class MasterofSprites {
    public static final String PROGRAM_NAME = "Master of Sprites";
    public static final String PROGRAM_VERSION = "version 0.10";
    public static final String PROGRAM_COPYRIGHT = "Copyright (c) 2024 Tobiasz Stamborski";
    
    private static String inputPath = null;
    private static String outputPath = null;
    private static boolean exportOption = false;
    private static String exportFormat = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 0;
        OUTER:
        while (i < args.length) {
            switch (args[i]) {
                case "-e":
                    exportOption = true;
                    i++;
                    if (i >= args.length) {
                        break OUTER;
                    }
                    if (args[i].startsWith("0x") ||
                            args[i].equalsIgnoreCase("kickass") ||
                            args[i].equalsIgnoreCase("acme") ||
                            args[i].equalsIgnoreCase("tmpx")) {
                        exportFormat = args[i];
                        i++;
                        if (i >= args.length) {
                            break OUTER;
                        }
                        inputPath = args[i];
                    } else {
                        inputPath = args[i];
                    }
                    break;
                case "-o":
                    i++;
                    if (i >= args.length) {
                        break OUTER;
                    }
                    outputPath = args[i];
                    break;
                default:
                    inputPath = args[i];
                    break;
            }
            i++;
        }
        
        if (exportOption) {
            export();
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWnd = new MainWindow();
            if (inputPath != null)
                mainWnd.openFile(new File(inputPath));
            mainWnd.setVisible(true);
        });
    }
    
    public static void runNewInstance() {
        String[] cmdArray = new String[3];
        File jarFile;
            
        cmdArray[0] = System.getProperty("java.home") + 
                File.separator + "bin" +
                File.separator + "java";
        
        cmdArray[1] = "-jar";
            
        try {
            jarFile = new File(MasterofSprites.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            cmdArray[2] = jarFile.getAbsolutePath();
        } catch (URISyntaxException ex) {
            Util.showError(null, ex.getMessage());
        }
            
        try {
            Runtime.getRuntime().exec(cmdArray);
        } catch (IOException ex) {
            Util.showError(null, ex.getMessage());
        }
    }

    private static void export() {
        InputStream istream;
        ObjectInputStream oistream;
        OutputStream ostream;
        SpriteProject project;
        
        try {
            istream = new FileInputStream(new File(inputPath));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return;
        }
        try {
            oistream = new ObjectInputStream(istream);
            project = (SpriteProject)oistream.readObject();
            oistream.close();
            istream.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            return;
        }
        
        if (exportFormat == null) {
            if (outputPath == null) {
                if (inputPath.endsWith(".spr"))
                    outputPath = Util.removeExtension(inputPath) + ".bin";
                else
                    outputPath = inputPath + ".bin";
            }
            
            try {
                ostream = new FileOutputStream(new File(outputPath));
            } catch (FileNotFoundException e) {
                System.out.println(e);
                return;
            }
            
            try {
                project.getMemoryData().saveAsRAW(ostream);
                ostream.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        } else if (exportFormat.startsWith("0x")) {
            if (outputPath == null) {
                if (inputPath.endsWith(".spr"))
                    outputPath = Util.removeExtension(inputPath) + ".prg";
                else
                    outputPath = inputPath + ".prg";
            }
            
            try {
                ostream = new FileOutputStream(new File(outputPath));
            } catch (FileNotFoundException e) {
                System.out.println(e);
                return;
            }
            
            try {
                project.getMemoryData().saveAsPRG(
                        ostream,
                        Short.parseShort(exportFormat.substring(2), 16)
                );
                ostream.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            if (outputPath == null) {
                if (inputPath.endsWith(".spr"))
                    outputPath = Util.removeExtension(inputPath) + ".asm";
                else
                    outputPath = inputPath + ".asm";
            }
            
            try {
                ostream = new FileOutputStream(new File(outputPath));
            } catch (FileNotFoundException e) {
                System.out.println(e);
                return;
            }
            
            AsmCodeStream asmStream;
            if (exportFormat.equalsIgnoreCase("tmpx"))
                asmStream = new AsmCodeStream(ostream, AsmCodeStream.TMPX_SYNTAX);
            else if (exportFormat.equalsIgnoreCase("acme"))
                asmStream = new AsmCodeStream(ostream, AsmCodeStream.ACME_SYNTAX);
            else
                asmStream = new AsmCodeStream(ostream, AsmCodeStream.KICKASS_SYNTAX);
            asmStream.printSpriteProject(project);
            
            try {
                ostream.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
