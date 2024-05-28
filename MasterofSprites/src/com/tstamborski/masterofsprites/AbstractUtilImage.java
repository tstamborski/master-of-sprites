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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */

abstract class AbstractUtilImage extends BufferedImage {
    private final Color primaryColor;
    
    public AbstractUtilImage(int width, int height, Color color) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
        
        primaryColor = color;
        redraw();
    }
    
    public Color getPrimaryColor() {
        return primaryColor;
    }
    
    protected abstract void redraw();
}

class SelectionImage extends AbstractUtilImage {
    public SelectionImage(int w, int h, Color c) {
        super(w, h, c);
    }
    
    @Override
    protected void redraw() {
        Graphics2D g2d;
        
        g2d = createGraphics();
        g2d.setColor(new Color(
                getPrimaryColor().getRed(),getPrimaryColor().getGreen(),getPrimaryColor().getBlue(),0x30
        ));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(getPrimaryColor());
        g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
    }
}

class GridImage extends AbstractUtilImage {
    public GridImage(int width, int height, Color color) {
        super(width, height, color);
    }
    
    @Override
    protected void redraw() {
        Graphics2D g2d;
        
        g2d = createGraphics();
        g2d.setColor(getPrimaryColor());
        g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
    }
}

class BackgroundImage extends AbstractUtilImage {
    public BackgroundImage(int width, int height, Color color) {
        super(width, height, color);
    }
    
    @Override
    protected void redraw() {
        Graphics2D g2d;
        
        g2d = createGraphics();
        g2d.setColor(getPrimaryColor());
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
