
package com.tstamborski;

import java.awt.Component;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class Util {
    public static boolean hasExtension(File f, String[] ext) {
        for (String ext1 : ext) {
            if (f.getAbsolutePath().endsWith("." + ext1)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean hasExtension(File f, String ext) {
        return f.getAbsolutePath().endsWith("." + ext);
    }
    
    public static String removeExtension(String filename){
        return filename.substring(0, filename.lastIndexOf('.'));
    }
    
    public static int bound(int val, int min, int max) {
        if (min <= val && val <= max)
            return val;
        else
            if (val < min)
                return min;
            else
                return max;
    }
    
    public static void showError(Component parent, String str) {
        JOptionPane.showMessageDialog(parent, str, "Error!", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void criticalError(Exception ex, String description) {
        Logger.getLogger(Util.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        if (description == null)
            showError(null, ex.getMessage());
        else
            showError(null, description);
        System.exit(-1);
    }
    
    public static void casualError(Exception ex, String description) {
        Logger.getLogger(Util.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
        if (description == null)
            showError(null, ex.getMessage());
        else
            showError(null, description);
    }
}
