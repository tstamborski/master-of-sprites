/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.tstamborski.masterofsprites;

import javax.swing.SwingUtilities;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class MasterofSprites {
    public static final String PROGRAM_NAME = "Master of Sprites";
    public static final String PROGRAM_VERSION = "version 0.4.3";
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
    
}
