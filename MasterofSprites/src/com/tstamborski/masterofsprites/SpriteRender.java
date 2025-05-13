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
        int spriteRGB = pal.getColor(src.getSpriteC64Color()).getRGB();
        
        clearImage(dst);
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++)
                if (src.getPixel(x, y) == SpriteColor.SpriteColor)
                    dst.setRGB(x, y, spriteRGB);
    }
    
    public static void renderSinglecolorAlpha(BufferedImage dst, SpriteData src, Palette pal, int alpha) {
        int spriteRGB = (pal.getColor(src.getSpriteC64Color()).getRGB() & 0x00ffffff) | (alpha << 24);
        
        clearImage(dst);
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++)
                if (src.getPixel(x, y) == SpriteColor.SpriteColor)
                    dst.setRGB(x, y, spriteRGB);
    }
    
    public static void renderReverseSinglecolor(SpriteData dst, BufferedImage src, Palette pal) {
        final int scRGB = pal.getColor(dst.getSpriteC64Color()).getRGB();
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++)
                if (src.getRGB(x, y) == scRGB)
                    dst.setPixel(x, y, SpriteColor.SpriteColor);
                else
                    dst.setPixel(x, y, SpriteColor.BackgroundColor);
    }
    
    public static void renderMulticolor(BufferedImage dst, SpriteData src, Palette pal, C64Color multi0, C64Color multi1) {
        int spriteRGB = pal.getColor(src.getSpriteC64Color()).getRGB();
        int multi0RGB = pal.getColor(multi0).getRGB();
        int multi1RGB = pal.getColor(multi1).getRGB();
        
        clearImage(dst);
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                switch (src.getPixel(x, y)) {
                    case SpriteColor:
                        dst.setRGB(x*2, y, spriteRGB);
                        dst.setRGB(x*2+1, y, spriteRGB);
                        break;
                    case Multi0Color:
                        dst.setRGB(x*2, y, multi0RGB);
                        dst.setRGB(x*2+1, y, multi0RGB);
                        break;
                    case Multi1Color:
                        dst.setRGB(x*2, y, multi1RGB);
                        dst.setRGB(x*2+1, y, multi1RGB);
                        break;
                    default:
                        break;
                }
            }
    }
    
    public static void renderMulticolorAlpha(
            BufferedImage dst, SpriteData src, Palette pal, 
            C64Color multi0, C64Color multi1, int alpha
    ) {
        int spriteRGB = 
                (pal.getColor(src.getSpriteC64Color()).getRGB() & 0x00ffffff) | (alpha << 24);
        int multi0RGB = 
                (pal.getColor(multi0).getRGB() & 0x00ffffff) | (alpha << 24);
        int multi1RGB = 
                (pal.getColor(multi1).getRGB() & 0x00ffffff) | (alpha << 24);
        
        clearImage(dst);
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                switch (src.getPixel(x, y)) {
                    case SpriteColor:
                        dst.setRGB(x*2, y, spriteRGB);
                        dst.setRGB(x*2+1, y, spriteRGB);
                        break;
                    case Multi0Color:
                        dst.setRGB(x*2, y, multi0RGB);
                        dst.setRGB(x*2+1, y, multi0RGB);
                        break;
                    case Multi1Color:
                        dst.setRGB(x*2, y, multi1RGB);
                        dst.setRGB(x*2+1, y, multi1RGB);
                        break;
                    default:
                        break;
                }
            }
    }
    
    public static void renderReverseMulticolor(SpriteData dst, BufferedImage src, 
            Palette pal, C64Color multi0, C64Color multi1) {
        final int spriteRGB = pal.getColor(dst.getSpriteC64Color()).getRGB();
        final int multi0RGB = pal.getColor(multi0).getRGB();
        final int multi1RGB = pal.getColor(multi1).getRGB();
        
        for (int y = 0; y < src.getHeight(); y++)
            for (int x = 0; x < src.getWidth(); x++) {
                if (x % 2 != 0)
                    continue;
                
                int srcRGB = src.getRGB(x, y);
                
                if (srcRGB == spriteRGB)
                    dst.setPixel(x / 2, y, SpriteColor.SpriteColor);
                else if (srcRGB == multi0RGB)
                    dst.setPixel(x / 2, y, SpriteColor.Multi0Color);
                else if (srcRGB == multi1RGB)
                    dst.setPixel(x / 2, y, SpriteColor.Multi1Color);
                else
                    dst.setPixel(x / 2, y, SpriteColor.BackgroundColor);
            }
    }
    
    private static void clearImage(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        
        g.dispose();
    }
}
