/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.SpriteColor;
import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpriteEditor extends JComponent {
    private Palette palette;
    private SpriteImage spriteImg;
    private SpriteData spriteData;
    private C64Color multi0Color, multi1Color, bgColor;
    private SpriteColor currentSpriteColor;

    public SpriteEditor(int zoom) {
        this.palette = DefaultPalette.getInstance();
        bgColor = C64Color.Black;
        multi0Color = C64Color.LightGray;
        multi1Color = C64Color.White;
        
        setPreferredSize(new Dimension(SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom));
        setMaximumSize(new Dimension(SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        if (!isEnabled())
            return;
        
        g2d.setColor(palette.getColor(bgColor));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        if (spriteImg != null)
            g2d.drawImage(spriteImg.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
    
    public void refresh() {
        if (spriteImg != null)
            spriteImg.redraw();
        repaint();
    }
    
    public void setSpriteData(SpriteData data) {
        spriteData = data;
        spriteImg = new SpriteImage(data, palette);
        spriteImg.setMulti0Color(multi0Color);
        spriteImg.setMulti1Color(multi1Color);
        
        repaint();
    }
    
    public SpriteData getSpriteData() {
        return spriteData;
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        repaint();
    }

    public C64Color getBgC64Color() {
        return bgColor;
    }

    public void setBgC64Color(C64Color bgColor) {
        this.bgColor = bgColor;
        repaint();
    }
    
    public void setSpriteC64Color(C64Color color) {
        if (spriteData == null)
            return;
        
        spriteData.setSpriteC64Color(color);
        refresh();
    }
    
    public void setMulti0C64Color(C64Color color) {
        multi0Color = color;
        
        if (spriteImg == null)
            return;
        
        spriteImg.setMulti0Color(color);
        repaint();
    }
    
    public void setMulti1C64Color(C64Color color) {
        multi1Color = color;
        
        if (spriteImg == null)
            return;
        
        spriteImg.setMulti1Color(color);
        repaint();
    }
    
    public SpriteColor getCurrentSpriteColor() {
        return currentSpriteColor;
    }
    
    public void setCurrentSpriteColor(SpriteColor color) {
        currentSpriteColor = color;
    }
}
