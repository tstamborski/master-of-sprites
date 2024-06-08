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
        rgbArray = new int[C64Color.values().length];
        
        rgbArray[C64Color.Black.ordinal()] = 0xff000000; //new Color(0,0,0);
        rgbArray[C64Color.White.ordinal()] = 0xffffffff; //new Color(255,255,255);
        rgbArray[C64Color.Red.ordinal()] = 0xffab3126; //new Color(0xab, 0x31, 0x26);
        rgbArray[C64Color.Cyan.ordinal()] = 0xff66daff; //new Color(0x66, 0xda, 0xff);
        rgbArray[C64Color.Purple.ordinal()] = 0xffbb3fb8; //new Color(0xbb, 0x3f, 0xb8);
        rgbArray[C64Color.Green.ordinal()] = 0xff55ce58; //new Color(0x55, 0xce, 0x58);
        rgbArray[C64Color.Blue.ordinal()] = 0xff1d0e97; //new Color(0x1d, 0x0e, 0x97);
        rgbArray[C64Color.Yellow.ordinal()] = 0xffeaf57c; //new Color(0xea, 0xf5, 0x7c);
        
        rgbArray[C64Color.Orange.ordinal()] = 0xffb97418; //new Color(0xb9, 0x74, 0x18);
        rgbArray[C64Color.Brown.ordinal()] = 0xff785300; //new Color(0x78, 0x53, 0x00);
        rgbArray[C64Color.Pink.ordinal()] = 0xffdd9387; //new Color(0xdd, 0x93, 0x87);
        rgbArray[C64Color.DarkGray.ordinal()] = 0xff5b5b5b; //new Color(0x5b, 0x5b, 0x5b);
        rgbArray[C64Color.MiddleGray.ordinal()] = 0xff8b8b8b; //new Color(0x8b, 0x8b, 0x8b);
        rgbArray[C64Color.LightGreen.ordinal()] = 0xffb0f4ac; //new Color(0xb0, 0xf4, 0xac);
        rgbArray[C64Color.LightBlue.ordinal()] = 0xffaa9def; //new Color(0xaa, 0x9d, 0xef);
        rgbArray[C64Color.LightGray.ordinal()] = 0xffb8b8b8; //new Color(0xb8, 0xb8, 0xb8);
    }
    
    public static DefaultPalette getInstance() {
        if (instance == null)
            instance = new DefaultPalette();
        
        return instance;
    }
    
    @Override
    public Color getColor(C64Color c) {
        return new Color(rgbArray[c.ordinal()]);
    }
    
    private final int[] rgbArray;
    private static DefaultPalette instance;
}
