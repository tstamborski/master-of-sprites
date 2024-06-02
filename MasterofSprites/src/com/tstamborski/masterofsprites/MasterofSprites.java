/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.Util;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.SwingUtilities;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class MasterofSprites {
    public static final String PROGRAM_NAME = "Master of Sprites";
    public static final String PROGRAM_VERSION = "version 0.8";
    public static final String PROGRAM_COPYRIGHT = "Copyright (c) 2024 Tobiasz Stamborski";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow main_wnd = new MainWindow();
            main_wnd.setVisible(true);
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
}
