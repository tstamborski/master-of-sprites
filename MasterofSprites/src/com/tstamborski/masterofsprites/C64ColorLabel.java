/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class C64ColorLabel extends JComponent {
    private static final int LABEL_SIZE = 20;
    
    private C64Color color;
    private final Palette palette;

    public C64ColorLabel() {
        color = C64Color.White;
        palette = DefaultPalette.getInstance();
        
        setBorder(BorderFactory.createLoweredBevelBorder());
        setPreferredSize(new Dimension(LABEL_SIZE, LABEL_SIZE));
        setMinimumSize(new Dimension(LABEL_SIZE, LABEL_SIZE));
        setMaximumSize(new Dimension(LABEL_SIZE, LABEL_SIZE));
    }

    public C64Color getC64Color() {
        return color;
    }

    public void setC64Color(C64Color color) {
        this.color = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isEnabled())
            return;
        
        g.setColor(palette.getColor(color));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
