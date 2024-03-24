/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites;

import java.awt.Color;
import com.tstamborski.masterofsprites.model.C64Color;

/**
 *
 * @author Tobiasz
 */
public class DefaultPalette implements Palette {
    private DefaultPalette() {
        colors = new Color[C64Color.values().length];
        
        colors[C64Color.Black.ordinal()] = new Color(0,0,0);
        colors[C64Color.White.ordinal()] = new Color(255,255,255);
        colors[C64Color.Red.ordinal()] = new Color(0xab, 0x31, 0x26);
        colors[C64Color.Cyan.ordinal()] = new Color(0x66, 0xda, 0xff);
        colors[C64Color.Purple.ordinal()] = new Color(0xbb, 0x3f, 0xb8);
        colors[C64Color.Green.ordinal()] = new Color(0x55, 0xce, 0x58);
        colors[C64Color.Blue.ordinal()] = new Color(0x1d, 0x0e, 0x97);
        colors[C64Color.Yellow.ordinal()] = new Color(0xea, 0xf5, 0x7c);
        
        colors[C64Color.Orange.ordinal()] = new Color(0xb9, 0x74, 0x18);
        colors[C64Color.Brown.ordinal()] = new Color(0x78, 0x53, 0x00);
        colors[C64Color.Pink.ordinal()] = new Color(0xdd, 0x93, 0x87);
        colors[C64Color.DarkGray.ordinal()] = new Color(0x5b, 0x5b, 0x5b);
        colors[C64Color.MiddleGray.ordinal()] = new Color(0x8b, 0x8b, 0x8b);
        colors[C64Color.LightGreen.ordinal()] = new Color(0xb0, 0xf4, 0xac);
        colors[C64Color.LightBlue.ordinal()] = new Color(0xaa, 0x9d, 0xef);
        colors[C64Color.LightGray.ordinal()] = new Color(0xb8, 0xb8, 0xb8);
    }
    
    public static DefaultPalette getInstance() {
        if (instance == null)
            instance = new DefaultPalette();
        
        return instance;
    }
    
    @Override
    public Color getColor(C64Color c) {
        return colors[c.ordinal()];
    }
    
    private final Color[] colors;
    private static DefaultPalette instance;
}
