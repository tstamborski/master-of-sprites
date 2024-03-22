/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
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
    private C64Color bgColor;

    public SpriteEditor(Palette pal, int zoom) {
        this.palette = pal;
        bgColor = C64Color.Black;
        
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
        spriteImg = new SpriteImage(data, palette);
        repaint();
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        repaint();
    }

    public C64Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(C64Color bgColor) {
        this.bgColor = bgColor;
        repaint();
    }
    
}
