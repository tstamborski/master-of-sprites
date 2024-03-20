/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.SpriteColor;
import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.Image;

/**
 *
 * @author Tobiasz
 */
public class Sprite {
    public static final int WIDTH = 24;
    public static final int HEIGHT = 21;
    
    private final SpriteData sprite_data;
    private Palette palette;
    
    private C64Color multi0_color, multi1_color;
    private final BufferedImage img;
    
    public Sprite(SpriteData data, Palette palette) {
        sprite_data = data;
        this.palette = palette;
        
        multi0_color = C64Color.LightGray;
        multi1_color = C64Color.White;
        
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        redraw();
    }
    
    public void setPalette(Palette palette) {
        this.palette = palette;
        redraw();
    }
    
    public final void redraw() {
        if (sprite_data.isMulticolor())
            redrawMultiColor();
        else
            redrawSingleColor();
    }
    
    public Image getImage() {
        return img;
    }
    
    public SpriteColor getPixel(int x, int y) {
        return sprite_data.getPixel(x, y);
    }
    
    public void setPixel(int x, int y, SpriteColor color) {
        sprite_data.setPixel(x, y, color);
    }
    
    public C64Color getSpriteColor() {
        return sprite_data.getSpriteC64Color();
    }
    
    public void setSpriteColor(C64Color c) {
        sprite_data.setSpriteC64Color(c);
        redraw();
    }
    
    public C64Color getMulti0Color() {
        return multi0_color;
    }
    
    public void setMulti0Color(C64Color c) {
        multi0_color = c;
        redraw();
    }
    
    public C64Color getMulti1Color() {
        return multi1_color;
    }
    
    public void setMulti1Color(C64Color c) {
        multi1_color = c;
        redraw();
    }
    
    private void redrawSingleColor() {
        clearImage();
        
        for (int y = 0; y < 21; y++)
            for (int x = 0; x < 24; x++)
                if (sprite_data.getPixel(x, y) == SpriteColor.SpriteColor)
                    img.setRGB(x, y, palette.getColor(sprite_data.getSpriteC64Color()).getRGB());
    }
    
    private void redrawMultiColor() {
        clearImage();
        
        for (int y = 0; y < 21; y++)
            for (int x = 0; x < 12; x++) {
                switch (sprite_data.getPixel(x, y)) {
                    case SpriteColor:
                        img.setRGB(x*2, y, palette.getColor(sprite_data.getSpriteC64Color()).getRGB());
                        img.setRGB(x*2+1, y, palette.getColor(sprite_data.getSpriteC64Color()).getRGB());
                        break;
                    case Multi0Color:
                        img.setRGB(x*2, y, palette.getColor(multi0_color).getRGB());
                        img.setRGB(x*2+1, y, palette.getColor(multi0_color).getRGB());
                        break;
                    case Multi1Color:
                        img.setRGB(x*2, y, palette.getColor(multi1_color).getRGB());
                        img.setRGB(x*2+1, y, palette.getColor(multi1_color).getRGB());
                        break;
                }
            }
    }
    
    private void clearImage() {
        Graphics2D g = img.createGraphics();
        
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }
}
