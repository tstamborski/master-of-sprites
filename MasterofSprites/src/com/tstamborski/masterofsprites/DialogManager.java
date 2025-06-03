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

import com.tstamborski.AboutDialog;
import com.tstamborski.ManualDialog;
import com.tstamborski.Util;
import com.tstamborski.masterofsprites.gui.AsmSyntaxDialog;
import com.tstamborski.masterofsprites.gui.C64ColorDialog;
import com.tstamborski.masterofsprites.gui.ExportPRGDialog;
import com.tstamborski.masterofsprites.gui.FlagDialog;
import com.tstamborski.masterofsprites.gui.GhostSkinningDialog;
import com.tstamborski.masterofsprites.gui.RotationDialog;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class DialogManager {
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
    
    public DialogManager() {
        createCustomDialogs();
        createFileDialogs();
    }
    
    private void createCustomDialogs() {
        aboutDialog = new AboutDialog();
        aboutDialog.setApplicationIcon(new ImageIcon(getClass().getResource("gui/icons/commodore-puppet48.png")));
        aboutDialog.setApplicationName(MasterofSprites.PROGRAM_NAME);
        aboutDialog.setApplicationVersion(MasterofSprites.PROGRAM_VERSION);
        aboutDialog.setApplicationCopyright(MasterofSprites.PROGRAM_COPYRIGHT);
        aboutDialog.setApplicationExtraInfo(
                "<html><i>Master of sprites, I'm pulling your strings<br>Twisting your mind and smashing your dreams<br></i><html>"
        );
        try {
            aboutDialog.setApplicationLicense(getClass().getResource("gui/docs/license.txt"));
        } catch (IOException e) {
            Util.showError(null, e.getMessage());
        }
        
        try {
            manDialog = new ManualDialog(null, getClass().getResource("gui/docs/manual.html"));
            manDialog.setIconImage(
                    new ImageIcon(getClass().getResource("gui/icons/handbook16.png")).getImage());
        } catch (IOException e) {
            Util.showError(null, e.getMessage());
        }
        
        addressDialog = new ExportPRGDialog();
        addressDialog.setIconImage(
                new ImageIcon(getClass().getResource("gui/icons/commodore16.png")).getImage());
        asmSyntaxDialog = new AsmSyntaxDialog();
        asmSyntaxDialog.setIconImage(
                new ImageIcon(getClass().getResource("gui/icons/asm-file16.png")).getImage());
        
        rotateDialog = new RotationDialog();
        
        applyColorDialog = new C64ColorDialog();
        applyColorDialog.setIconImage(
                new ImageIcon(getClass().getResource("gui/icons/palette16.png")).getImage());
        applyMulticolorDialog = new FlagDialog("Multicolor? ");
        applyMulticolorDialog.setIconImage(
                new ImageIcon(getClass().getResource("gui/icons/flag-red16.png")).getImage());
        applyOverlayDialog = new FlagDialog("Overlay? ");
        applyOverlayDialog.setIconImage(
                new ImageIcon(getClass().getResource("gui/icons/flag-blue16.png")).getImage());
        
        ghostDialog = new GhostSkinningDialog();
        ghostDialog.setIconImage(new ImageIcon(getClass().getResource("gui/icons/ghost16.png")).getImage());
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
    
    public AboutDialog getAboutDialog() {
        return aboutDialog;
    }

    public ManualDialog getManDialog() {
        return manDialog;
    }

    public ExportPRGDialog getAddressDialog() {
        return addressDialog;
    }

    public AsmSyntaxDialog getAsmSyntaxDialog() {
        return asmSyntaxDialog;
    }

    public RotationDialog getRotateDialog() {
        return rotateDialog;
    }

    public C64ColorDialog getApplyColorDialog() {
        return applyColorDialog;
    }

    public FlagDialog getApplyMulticolorDialog() {
        return applyMulticolorDialog;
    }

    public FlagDialog getApplyOverlayDialog() {
        return applyOverlayDialog;
    }

    public GhostSkinningDialog getGhostDialog() {
        return ghostDialog;
    }

    public JFileChooser getPrgDialog() {
        return prgDialog;
    }

    public JFileChooser getRawDialog() {
        return rawDialog;
    }

    public JFileChooser getBitmapDialog() {
        return bitmapDialog;
    }

    public JFileChooser getProjectDialog() {
        return projectDialog;
    }

    public JFileChooser getAsmDialog() {
        return asmDialog;
    }
}
