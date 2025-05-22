/*
 * The MIT License
 *
 * Copyright 2024 Tobiasz Stamborski <tstamborski@outlook.com>.
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

import com.tstamborski.masterofsprites.model.AsmCodeStream;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class Settings {
    public static final String PROJECT_FILE_DLG = "projectDialog";
    public static final String ASM_FILE_DLG = "asmDialog";
    public static final String BITMAP_FILE_DLG = "bitmapDialog";
    public static final String RAW_FILE_DLG = "rawDialog";
    public static final String PRG_FILE_DLG = "prgDialog";
    
    private static final String ASM_SYNTAX_DLG = "asmSyntaxDialog";
    private static final String GHOST_SKINNING_DLG = "ghostDialog";
    private static final String SPRITE_EDITOR = "spriteEditor";
    
    private static final String CURRENT_DIR = ".currentDir";
    private static final String SYNTAX = ".syntax";
    private static final String SKINNING = ".skinning";
    private static final String GRID = ".grid";
    
    
    private final Preferences prefs;
    
    public Settings() {
        prefs = Preferences.userNodeForPackage(getClass());
    }
    
    public void saveFileChooser(JFileChooser dlg, String keyPrefix) {
        prefs.put(keyPrefix+CURRENT_DIR, dlg.getCurrentDirectory().getAbsolutePath());
    }
    
    public void loadFileChooser(JFileChooser dlg, String keyPrefix) {
        dlg.setCurrentDirectory(
                new File(prefs.get(keyPrefix+CURRENT_DIR, System.getProperty("user.home")))
        );
    }
    
    public void saveAsmSyntaxDialog(AsmSyntaxDialog dlg) {
        prefs.putInt(ASM_SYNTAX_DLG+SYNTAX, dlg.getAsmSyntax());
    }
    
    public void loadAsmSyntaxDialog(AsmSyntaxDialog dlg) {
        dlg.setAsmSyntax(prefs.getInt(ASM_SYNTAX_DLG+SYNTAX, AsmCodeStream.KICKASS_SYNTAX));
    }
    
    public void saveGhostSkinningDialog(GhostSkinningDialog dlg) {
        prefs.putInt(GHOST_SKINNING_DLG+SKINNING, dlg.getSkinningIndex());
    }
    
    public void loadGhostSkinningDialog(GhostSkinningDialog dlg) {
        dlg.setSkinningIndex(prefs.getInt(GHOST_SKINNING_DLG+SKINNING, 0));
    }
    
    public void saveSpriteEditor(SpriteEditor ed) {
        prefs.putBoolean(SPRITE_EDITOR+GRID, ed.isGrid());
    }
    
    public void loadSpriteEditor(SpriteEditor ed) {
        ed.setGrid(prefs.getBoolean(SPRITE_EDITOR+GRID, false));
    }
}
