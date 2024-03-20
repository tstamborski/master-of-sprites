/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites.model;

import java.io.*;

/**
 *
 * @author Tobiasz
 */
public class SpriteData {
    public static final int SIZE = 64;
    
    private final byte data[];
    
    private SpriteData() {
        data = new byte[SIZE];
    }
    
    public static SpriteData getEmpty(C64Color c, boolean ismulticolor, boolean isoverlay) {
        SpriteData sprite_data = new SpriteData();
        
        sprite_data.clear();
        sprite_data.setSpriteC64Color(c);
        sprite_data.setMulticolor(ismulticolor);
        sprite_data.setOverlay(isoverlay);
        
        return sprite_data;
    }

    public SpriteData deepCopy() {
        SpriteData spr_data = new SpriteData();
        System.arraycopy(this.data, 0, spr_data.data, 0, SIZE);
        return spr_data;
    }
    
    public void toOutputStream(OutputStream ostream) throws IOException {
        ostream.write(data);
    }
    
    public void toOutputStream(OutputStream ostream, int offset) throws IOException {
        for (int i = offset; i < data.length; i++)
            ostream.write(data[i]);
    }
    
    public static SpriteData fromInputStream(InputStream istream)
    throws IOException, EOFException {
        return fromInputStream(istream, 0);
    }
    
    public static SpriteData fromInputStream(InputStream istream, int offs)
    throws IOException, EOFException {
        SpriteData sprite_data = new SpriteData();
        
        for (int i = 0; i < offs; i++)
            sprite_data.data[i] = 0x00;
        if (istream.read(sprite_data.data, offs, 64-offs) < 64-offs)
            throw new EOFException("EOF before end of sprite data.");
        
        return sprite_data;
    }
    
    public void clear() {
        for (int i = 0; i < 63; i++)
            data[i] = 0;
    }
    
    public byte[] toByteArray() {
        return data;
    }
    
    public byte getByte(int index) {
        return data[index];
    }
    
    public SpriteColor getPixel(int x, int y) {
        int byte_index;
        byte data_byte;
        
        byte_index = isMulticolor() ? 3*y + x/4 : 3*y + x/8;
        data_byte = data[byte_index];
        if (isMulticolor())
            return SpriteColor.fromInteger((data_byte >> (3-x%4)*2) & 0x03);
        else
            if (((data_byte >> (7-x%8)) & 0x01) != 0)
                return SpriteColor.SpriteColor;
            else
                return SpriteColor.BackgroundColor;
    }
    
    public void setPixel(int x, int y, SpriteColor c) {
        int byte_index;
        
        byte_index = isMulticolor() ? 3*y + x/4 : 3*y + x/8;
        
        if (isMulticolor())
            data[byte_index] = (byte)((data[byte_index] & (~(0x03 << (3-x%4)*2))) | (c.ordinal() << ((3-x%4)*2)));
        else
            if (c == SpriteColor.SpriteColor)
                data[byte_index] = (byte)((data[byte_index] & (~(0x01 << (7-x%8)))) | (0x01 << (7-x%8)));
            else
                data[byte_index] = (byte)(data[byte_index] & (~(0x01 << (7-x%8))));
    }
    
    public boolean isMulticolor() {
        return (data[63] & 0x80) != 0;
    }
    
    public void setMulticolor(boolean mode) {
        data[63] = mode ? (byte)(data[63] | 0x80) : (byte)(data[63] & 0x7f);
    }
    
    public boolean isOverlay() {
        return (data[63] & 0x40) != 0;
    }
    
    public void setOverlay(boolean overlay) {
        data[63] = overlay ? (byte)(data[63] | 0x40) : (byte)(data[63] & 0xbf);
    }
    
    public C64Color getSpriteC64Color() {
        return C64Color.fromInteger(data[63]);
    }
    
    public void setSpriteC64Color(C64Color color) {
        data[63] = (byte)((data[63] & 0xf0) | color.ordinal());
    }
}
