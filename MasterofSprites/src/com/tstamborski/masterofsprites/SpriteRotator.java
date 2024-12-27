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
import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpriteRotator {
    private static final Palette palette = DefaultPalette.getInstance();
    
    public static void rotate(SpriteData sd, double angle) {
        BufferedImage imgNew, imgOld;
        C64Color multi0, multi1;
        Graphics2D g2d;
        
        imgOld = new BufferedImage(
                SpriteImage.WIDTH, SpriteImage.HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        imgNew = new BufferedImage(
                SpriteImage.WIDTH, SpriteImage.HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        
        if (sd.isMulticolor()) {
            multi0 = C64Color.fromInteger(sd.getSpriteC64Color().ordinal() + 1);
            multi1 = C64Color.fromInteger(sd.getSpriteC64Color().ordinal() + 2);
            SpriteRender.renderMulticolor(imgOld, sd, palette, multi0, multi1);
            
            g2d = imgNew.createGraphics();
            g2d.rotate(angle, SpriteImage.WIDTH / 2, SpriteImage.HEIGHT / 2);
            g2d.drawImage(imgOld, 0, 0, null);
            g2d.dispose();
            
            SpriteRender.renderReverseMulticolor(sd, imgNew, palette, multi0, multi1);
        } else {
            SpriteRender.renderSinglecolor(imgOld, sd, palette);
            
            g2d = imgNew.createGraphics();
            g2d.rotate(angle, SpriteImage.WIDTH / 2, SpriteImage.HEIGHT / 2);
            g2d.drawImage(imgOld, 0, 0, null);
            g2d.dispose();
            
            SpriteRender.renderReverseSinglecolor(sd, imgNew, palette);
        }
    }
}
