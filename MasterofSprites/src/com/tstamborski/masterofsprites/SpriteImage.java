/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.SpriteColor;
import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.image.*;

/**
 *
 * @author Tobiasz
 */
public class SpriteImage extends BufferedImage {
    public static final int WIDTH = 24;
    public static final int HEIGHT = 21;
    
    private final SpriteData sprite_data;
    private Palette palette;
    
    private C64Color multi0_color, multi1_color;
    
    public SpriteImage(SpriteData data, Palette palette) {
        super(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        
        sprite_data = data;
        this.palette = palette;
        
        multi0_color = C64Color.LightGray;
        multi1_color = C64Color.White;
        
        redraw();
    }
    
    public void setPalette(Palette palette) {
        this.palette = palette;
        redraw();
    }
    
    public final void redraw() {
        if (sprite_data.isMulticolor())
            SpriteRender.renderMulticolor(this, sprite_data, palette, multi0_color, multi1_color);
        else
            SpriteRender.renderSinglecolor(this, sprite_data, palette);
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
}
