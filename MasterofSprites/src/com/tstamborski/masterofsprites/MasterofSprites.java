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
    public static final String PROGRAM_VERSION = "version 1.0rc1";
    public static final String PROGRAM_COPYRIGHT = "Copyright (c) 2025 Tobiasz Stamborski";
    
    private static String inputPath = null;
    private static String outputPath = null;
    private static boolean exportOption = false;
    private static String exportFormat = null;
    private static boolean helpOption = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 0;
        OUTER:
        while (i < args.length) {
            switch (args[i]) {
                case "-e":
                case "--export":
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
                case "--output":
                    i++;
                    if (i >= args.length) {
                        break OUTER;
                    }
                    outputPath = args[i];
                    break;
                case "-h":
                case "--help":
                    helpOption = true;
                    break;
                default:
                    if (inputPath == null)
                        inputPath = args[i];
                    break;
            }
            i++;
        }
        
        if (helpOption) {
            printUsageInfo();
            return;
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
        SpriteProject project;
        
        if (inputPath == null) {
            System.out.println("Error: Filename not specified.");
            return;
        }
        
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
        
        if (exportFormat == null)
            exportRawBinary(project);
        else if (exportFormat.startsWith("0x"))
            exportPRG(project);
        else
            exportASM(project);
    }
    
    private static void exportASM(SpriteProject project) {
        OutputStream ostream;
        AsmCodeStream asmStream;
        
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
    
    private static void exportPRG(SpriteProject project) {
        OutputStream ostream;
        
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
    }
    
    private static void exportRawBinary(SpriteProject project) {
        OutputStream ostream;
        
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
    }

    private static void printUsageInfo() {
        System.out.println(MasterofSprites.PROGRAM_NAME + " " + MasterofSprites.PROGRAM_VERSION);
        System.out.println();
        System.out.println("Usage: java -jar MasterofSprites.jar [projectfile]");
        System.out.println("       java -jar MasterofSprites.jar -e [format] projectfile [-o outfile]");
        System.out.println();
        System.out.println("Format Options:");
        System.out.println("       EMPTY format specifier choose export as a raw binary.");
        System.out.println();
        System.out.println("       HEXADECIMAL NUMBER WITH 0x PREFIX choose export as a prg file with");
        System.out.println("       start address specified by this number.");
        System.out.println();
        System.out.println("       KICKASS, ACME or TMPX choose export as assembly source code with");
        System.out.println("       syntax of respective assembler.");
        System.out.println();
    }
}
