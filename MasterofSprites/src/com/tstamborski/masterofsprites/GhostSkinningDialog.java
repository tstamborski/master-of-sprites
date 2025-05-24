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

import com.tstamborski.AbstractInputDialog;
import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.MemoryData;
import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class GhostSkinningDialog extends AbstractInputDialog {
    ButtonGroup buttonGroup;
    JRadioButton noSkinningBtn, memSkinningBtn, selSkinningBtn, ovSkinningBtn, iSkinningBtn;
    GhostSkinning intelligentMode, overlayMode, selectionMode, memoryMode;
    
    public GhostSkinningDialog(JFrame parent) {
        super(parent);
        
        intelligentMode = new IntelligentSkinning();
        overlayMode = new OverlaySkinning();
        selectionMode = new SelectionSkinning();
        memoryMode = new MemorySkinning();
        
        noSkinningBtn = new JRadioButton("0. No ghost skinning.");
        noSkinningBtn.setMnemonic('0');
        memSkinningBtn = new JRadioButton("1. Previous in memory skinning.");
        memSkinningBtn.setMnemonic('1');
        selSkinningBtn = new JRadioButton("2. Previous in selection skinning.");
        selSkinningBtn.setMnemonic('2');
        ovSkinningBtn = new JRadioButton("3. Overlay stack skinning.");
        ovSkinningBtn.setMnemonic('3');
        iSkinningBtn = new JRadioButton("4. Intelligent animation & overlay mode.");
        iSkinningBtn.setMnemonic('4');
        buttonGroup = new ButtonGroup();
        buttonGroup.add(noSkinningBtn);
        buttonGroup.add(memSkinningBtn);
        buttonGroup.add(selSkinningBtn);
        buttonGroup.add(ovSkinningBtn);
        buttonGroup.add(iSkinningBtn);
        noSkinningBtn.setSelected(true);
        
        JPanel panel = getCentralPanel();
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), 
                        BorderFactory.createEmptyBorder(8, 8, 8, 8))
        );
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(noSkinningBtn);
        panel.add(memSkinningBtn);
        panel.add(selSkinningBtn);
        panel.add(ovSkinningBtn);
        panel.add(iSkinningBtn);
        
        setTitle("Set Ghost Skinning... ");
        pack();
    }
    
    public int getSkinningIndex() {
        if (iSkinningBtn.isSelected())
            return 4;
        else if (ovSkinningBtn.isSelected())
            return 3;
        else if (selSkinningBtn.isSelected())
            return 2;
        else if (memSkinningBtn.isSelected())
            return 1;
        else
            return 0;
    }
    
    public void setSkinningIndex(int i) {
        switch (i) {
            case 4:
                iSkinningBtn.setSelected(true);
                break;
            case 3:
                ovSkinningBtn.setSelected(true);
                break;
            case 2:
                selSkinningBtn.setSelected(true);
                break;
            case 1:
                memSkinningBtn.setSelected(true);
                break;
            default:
                noSkinningBtn.setSelected(true);
                break;
        }
    }
    
    public GhostSkinning getGhostSkinning() {
        if (iSkinningBtn.isSelected())
            return intelligentMode;
        else if (ovSkinningBtn.isSelected())
            return overlayMode;
        else if (selSkinningBtn.isSelected())
            return selectionMode;
        else if (memSkinningBtn.isSelected())
            return memoryMode;
        else
            return null;
    }
}

class MemorySkinning implements GhostSkinning {
    private final BufferedImage bgImage;
    
    public MemorySkinning() {
        bgImage = new BufferedImage(SpriteImage.WIDTH, SpriteImage.HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public BufferedImage getFgImage(Selection selection, int selectionIndex, Palette palette) {
        return null;
    }

    @Override
    public BufferedImage getBgImage(Selection selection, int selectionIndex, Palette palette) {
        if (selection.get(selectionIndex) - 1 < 0)
            return null;
        
        MemoryData mem = selection.getSpriteProject().getMemoryData();
        C64Color m0col = selection.getSpriteProject().getMulti0Color();
        C64Color m1col = selection.getSpriteProject().getMulti1Color();
        SpriteData sd = mem.get(selection.get(selectionIndex) - 1);
        
        SpriteRender.clearImage(bgImage);
        if (sd.isMulticolor())
            SpriteRender.renderMulticolorAlpha(
                    bgImage, sd, palette, m0col, m1col, DEFAULT_ALPHA
            );
        else
            SpriteRender.renderSinglecolorAlpha(bgImage, sd, palette, DEFAULT_ALPHA);
        
        return bgImage;
    }
}

class SelectionSkinning implements GhostSkinning {
    private final BufferedImage bgImage;
    
    public SelectionSkinning() {
        bgImage = new BufferedImage(SpriteImage.WIDTH, SpriteImage.HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public BufferedImage getFgImage(Selection selection, int selectionIndex, Palette palette) {
        return null;
    }

    @Override
    public BufferedImage getBgImage(Selection selection, int selectionIndex, Palette palette) {
        if (selectionIndex - 1 < 0)
            return null;
        
        MemoryData mem = selection.getSpriteProject().getMemoryData();
        C64Color m0col = selection.getSpriteProject().getMulti0Color();
        C64Color m1col = selection.getSpriteProject().getMulti1Color();
        SpriteData sd = mem.get(selection.get(selectionIndex-1));
        
        SpriteRender.clearImage(bgImage);
        if (sd.isMulticolor())
            SpriteRender.renderMulticolorAlpha(
                    bgImage, sd, palette, m0col, m1col, DEFAULT_ALPHA
            );
        else
            SpriteRender.renderSinglecolorAlpha(bgImage, sd, palette, DEFAULT_ALPHA);
        
        return bgImage;
    }
}

class OverlaySkinning implements GhostSkinning {
    private final BufferedImage fgImage;
    
    public OverlaySkinning() {
        fgImage = new BufferedImage(SpriteImage.WIDTH, SpriteImage.HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public BufferedImage getFgImage(Selection selection, int selectionIndex, Palette palette) {
        SpriteRender.clearImage(fgImage);
        
        int dist = selection.getSpriteProject().getOverlayDistance();
        int index = selection.get(selectionIndex);
        int i = 0;
        MemoryData mem = selection.getSpriteProject().getMemoryData();
        C64Color m0color = selection.getSpriteProject().getMulti0Color();
        C64Color m1color = selection.getSpriteProject().getMulti1Color();
        SpriteData sd = mem.get(index);
        while (i < 7) {
            if (!sd.isOverlay())
                break;
            
            index += dist;
            i++;
            
            if (index >= mem.size())
                break;
            sd = mem.get(index);
            
            if (sd.isMulticolor())
                SpriteRender.renderMulticolor(fgImage, sd, palette, m0color, m1color);
            else
                SpriteRender.renderSinglecolor(fgImage, sd, palette);
        }
        
        if (i == 0)
            return null;
        return fgImage;
    }

    @Override
    public BufferedImage getBgImage(Selection selection, int selectionIndex, Palette palette) {
        return null;
    }
}

class IntelligentSkinning implements GhostSkinning {
    private final BufferedImage fgImage, bgImage;
    
    public IntelligentSkinning() {
        fgImage = new BufferedImage(SpriteImage.WIDTH, SpriteImage.HEIGHT, BufferedImage.TYPE_INT_ARGB);
        bgImage = new BufferedImage(SpriteImage.WIDTH, SpriteImage.HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public BufferedImage getFgImage(Selection selection, int selectionIndex, Palette palette) {
        SpriteRender.clearImage(fgImage);
        
        int dist = selection.getSpriteProject().getOverlayDistance();
        int index = selection.get(selectionIndex);
        int i = 0;
        MemoryData mem = selection.getSpriteProject().getMemoryData();
        C64Color m0color = selection.getSpriteProject().getMulti0Color();
        C64Color m1color = selection.getSpriteProject().getMulti1Color();
        SpriteData sd = mem.get(index);
        while (i < 7) {
            if (!sd.isOverlay())
                break;
            
            index += dist;
            i++;
            
            if (index >= mem.size())
                break;
            sd = mem.get(index);
            
            if (sd.isMulticolor())
                SpriteRender.renderMulticolor(fgImage, sd, palette, m0color, m1color);
            else
                SpriteRender.renderSinglecolor(fgImage, sd, palette);
        }
        
        if (i == 0)
            return null;
        return fgImage;
    }

    @Override
    public BufferedImage getBgImage(Selection selection, int selectionIndex, Palette palette) {
        SpriteRender.clearImage(bgImage);
        
        int dist = selection.getSpriteProject().getOverlayDistance();
        int index = selection.get(selectionIndex) - 1;
        if (index < 0 || index % dist == dist - 1)
            return null;
        int i = 0;
        MemoryData mem = selection.getSpriteProject().getMemoryData();
        C64Color m0color = selection.getSpriteProject().getMulti0Color();
        C64Color m1color = selection.getSpriteProject().getMulti1Color();
        while (i < 8) {
            if (index >= mem.size())
                break;
            SpriteData sd = mem.get(index);
            
            if (sd.isMulticolor())
                SpriteRender.renderMulticolorAlpha(
                        bgImage, sd, palette, m0color, m1color, DEFAULT_ALPHA);
            else
                SpriteRender.renderSinglecolorAlpha(
                        bgImage, sd, palette, DEFAULT_ALPHA);
            
            if (!sd.isOverlay())
                break;
            index += dist;
            i++;
        }
        
        return bgImage;
    }
}
