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

import com.tstamborski.masterofsprites.model.C64Color;
import static com.tstamborski.masterofsprites.model.SpriteColor.Multi0Color;
import static com.tstamborski.masterofsprites.model.SpriteColor.Multi1Color;
import static com.tstamborski.masterofsprites.model.SpriteColor.SpriteColor;
import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpriteRender {
    public static void renderSinglecolor(BufferedImage dst, SpriteData src, Palette pal) {
        clearImage(dst);
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++)
                if (src.getPixel(x, y) == SpriteColor.SpriteColor)
                    dst.setRGB(x, y, pal.getColor(src.getSpriteC64Color()).getRGB());
    }
    
    protected static void renderReverseSinglecolor(SpriteData dst, BufferedImage src, Palette pal) {
        C64Color c = dst.getSpriteC64Color();
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++)
                if (src.getRGB(x, y) == pal.getColor(c).getRGB())
                    dst.setPixel(x, y, SpriteColor.SpriteColor);
                else
                    dst.setPixel(x, y, SpriteColor.BackgroundColor);
    }
    
    public static void renderMulticolor(BufferedImage dst, SpriteData src, Palette pal, C64Color multi0, C64Color multi1) {
        clearImage(dst);
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                switch (src.getPixel(x, y)) {
                    case SpriteColor:
                        dst.setRGB(x*2, y, pal.getColor(src.getSpriteC64Color()).getRGB());
                        dst.setRGB(x*2+1, y, pal.getColor(src.getSpriteC64Color()).getRGB());
                        break;
                    case Multi0Color:
                        dst.setRGB(x*2, y, pal.getColor(multi0).getRGB());
                        dst.setRGB(x*2+1, y, pal.getColor(multi0).getRGB());
                        break;
                    case Multi1Color:
                        dst.setRGB(x*2, y, pal.getColor(multi1).getRGB());
                        dst.setRGB(x*2+1, y, pal.getColor(multi1).getRGB());
                        break;
                    default:
                        break;
                }
            }
    }
    
    protected static void renderReverseMulticolor(SpriteData dst, BufferedImage src, 
            Palette pal, C64Color multi0, C64Color multi1) {
        C64Color color = dst.getSpriteC64Color();
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                if (src.getRGB(x, y) == pal.getColor(color).getRGB()) {
                    dst.setPixel(x / 2, y, SpriteColor.SpriteColor);
                } else if (src.getRGB(x, y) == pal.getColor(multi0).getRGB()) {
                    dst.setPixel(x / 2, y, SpriteColor.Multi0Color);
                } else if (src.getRGB(x, y) == pal.getColor(multi1).getRGB()) {
                    dst.setPixel(x / 2, y, SpriteColor.Multi1Color);
                } else {
                    dst.setPixel(x / 2, y, SpriteColor.BackgroundColor);
                }
            }
    }
    
    public static void rotateSinglecolor(SpriteData spr, BufferedImage img, double angle, Palette pal) {
        BufferedImage buffer = 
                new BufferedImage(img.getWidth(), img.getHeight(), 
                        BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        
        
        g2d.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
        g2d.rotate(angle, buffer.getWidth() / 2, buffer.getHeight() / 2);
        g2d.drawImage(img, 0, 0, null);
        
        renderReverseSinglecolor(spr, buffer, pal);
    }
    
    public static void rotateMulticolor(SpriteData spr, BufferedImage img, double angle,
            Palette pal, C64Color multi0, C64Color multi1) {
        BufferedImage buffer = 
                new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        
        g2d.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
        g2d.rotate(angle, buffer.getWidth() / 2, buffer.getHeight() / 2);
        g2d.drawImage(img, 0, 0, null);
        
        renderReverseMulticolor(spr, buffer, pal, multi0, multi1);
    }
    
    protected static void clearImage(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
    }
}
