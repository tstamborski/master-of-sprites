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
package com.tstamborski.masterofsprites.gui;

import com.tstamborski.masterofsprites.Arrangement;
import com.tstamborski.masterofsprites.DefaultPalette;
import com.tstamborski.masterofsprites.Palette;
import com.tstamborski.masterofsprites.SpriteImage;
import com.tstamborski.masterofsprites.SpriteRender;
import com.tstamborski.masterofsprites.model.SpriteData;
import com.tstamborski.masterofsprites.model.SpriteProject;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpritePreview extends JComponent {
    private final Palette palette;
    private SpriteProject project;
    private ArrayList<Integer> selection;
    private Arrangement arrangement;
    
    private final ArrayList<BufferedImage> animation;
    private int frameIndex, frameCount, zoom;
    
    private boolean reloadRequest;
    
    public SpritePreview() {
        palette = DefaultPalette.getInstance();
        zoom = 4;
        frameCount = 1;
        frameIndex = 0;
        animation = new ArrayList<>();
        arrangement = new Arrangement(0, 0);
    }
    
    public void setProject(SpriteProject p) {
        this.project = p;
    }
    
    public void setSelection(ArrayList<Integer> s) {
        this.selection = s;
    }
    
    public void setZoom(int z) {
        this.zoom = z;
    }
    
    public void setArrangement(Arrangement a) {
        this.arrangement = a;
    }
    
    public void setFrameCount(int c) {
        this.frameCount = c;
    }
    
    public final void reload() {
        reloadRequest = true;
        
        if (!isVisible() || !isEnabled())
            return;
        
        animation.clear();
        for (int i = 0; i < frameCount; i++) {
            animation.add(createAnimationFrame(i));
        }
        
        setPreferredSize(new Dimension(
                arrangement.width*SpriteImage.WIDTH*zoom,
                arrangement.height*SpriteImage.HEIGHT*zoom
        ));
        getParent().revalidate();
        setFrameIndex(0);
        
        reloadRequest = false;
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag && reloadRequest)
            reload();
    }
    
    
    public void setFrameIndex(int i) {
        this.frameIndex = i;
        repaint();
    }
    
    public int getFrameIndex() {
        return this.frameIndex;
    }
    
    public int getFrameCount() {
        return this.frameCount;
    }
    
    public void nextFrame() {
        setFrameIndex((frameIndex + 1) % frameCount);
    }
    
    public void prevFrame() {
        if (frameIndex - 1 >= 0)
            setFrameIndex(frameIndex - 1);
        else
            setFrameIndex(frameCount - 1);
    }
    
    private BufferedImage createAnimationFrame(int index) {
        if (project == null || selection == null || arrangement == null)
            return null;
        if (selection.isEmpty() || arrangement.isNone())
            return null;
        
        int size = arrangement.width * arrangement.height;
        int memBankSize = project.getMemoryData().size();
        
        BufferedImage[] images = new BufferedImage[size];
        for (int i = 0; i < size; i++) {
            images[i] = new BufferedImage(
                    SpriteImage.WIDTH, SpriteImage.HEIGHT,
                    BufferedImage.TYPE_INT_ARGB
                );
            int offset = 0;
            
            for (int j = 0; j < 8; j++) {
                SpriteData sd = 
                    project.getMemoryData().get((selection.get(i) + index + offset) % memBankSize);
                
                if (sd.isMulticolor())
                    SpriteRender.renderMulticolor(
                            images[i], sd, palette, project.getMulti0Color(), project.getMulti1Color());
                else
                    SpriteRender.renderSinglecolor(images[i], sd, palette);
                
                if (sd.isOverlay())
                    offset += project.getOverlayDistance();
                else
                    break;
            }
        }
        
        BufferedImage frame = new BufferedImage(
            arrangement.width*SpriteImage.WIDTH*zoom, 
            arrangement.height*SpriteImage.HEIGHT*zoom, 
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = frame.createGraphics();
        g2d.setBackground(palette.getColor(project.getBgColor()));
        g2d.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        for (int y = 0; y < arrangement.height; y++)
            for (int x = 0; x < arrangement.width; x++)
                g2d.drawImage(images[y*arrangement.width+x], 
                            x*SpriteImage.WIDTH*zoom, y*SpriteImage.HEIGHT*zoom,
                            SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom, null);
        g2d.dispose();
        
        return frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!isVisible() || !isEnabled())
            return;
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(animation.get(frameIndex), 0, 0, null);
        g2d.dispose();
    }
}
