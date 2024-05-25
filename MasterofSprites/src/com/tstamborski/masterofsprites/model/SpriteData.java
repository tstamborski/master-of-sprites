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
public class SpriteData implements Serializable {
    private static final long serialVersionUID = 14145L;
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
    
    public int getWidth() {
        return (isMulticolor() ? 12 : 24);
    }
    
    public int getHeight() {
        return 21;
    }
    
    public void slideUp() {
        byte temp[] = new byte[3];
        
        System.arraycopy(data, 0, temp, 0, 3);
        
        for (int i = 3; i < 63; i+=3)
            for (int j = 0; j < 3; j++)
                data[i+j-3] = data[i+j];
        
        System.arraycopy(temp, 0, data, 60, 3);
    }
    
    public void slideDown() {
        byte temp[] = new byte[3];
        
        System.arraycopy(data, 60, temp, 0, 3);
        
        for (int i = 60; i >= 3; i-=3)
            for (int j = 0; j < 3; j++)
                data[i+j] = data[i+j-3];
        
        System.arraycopy(temp, 0, data, 0, 3);
    }
    
    public void slideLeft() {
        if (isMulticolor())
            multiSlideLeft(2);
        else
            multiSlideLeft(1);
    }
    
    public void slideRight() {
        if (isMulticolor())
            multiSlideRight(2);
        else
            multiSlideRight(1);
    }
    
    public void flipHorizontally() {
        byte temp;
        
        for (int i = 0; i < 63; i += 3) {
            if (isMulticolor()) {
                data[i+1] = flipBitsMultiColor(data[i+1]);
                temp = flipBitsMultiColor(data[i]);
                data[i] = flipBitsMultiColor(data[i+2]);
                data[i+2] = temp;
            } else {
                data[i+1] = flipBitsSingleColor(data[i+1]);
                temp = flipBitsSingleColor(data[i]);
                data[i] = flipBitsSingleColor(data[i+2]);
                data[i+2] = temp;
            }
        }
    }
    
    public void flipVertically() {
        byte[] temp = new byte[3];
        
        for (int i = 0; i < 10; i++) {
            temp[0] = data[i*3];
            temp[1] = data[i*3+1];
            temp[2] = data[i*3+2];
            
            data[i*3] = data[60-i*3];
            data[i*3+1] = data[60-i*3+1];
            data[i*3+2] = data[60-i*3+2];
            
            data[60-i*3] = temp[0];
            data[60-i*3+1] = temp[1];
            data[60-i*3+2] = temp[2];
        }
    }
    
    private void multiSlideLeft(int times) {
        while (times > 0) {
            int temp[] = new int[3];
            
            for (int i = 0; i < 63; i += 3) {
                temp[2] = data[i+2] << 1;
                data[i+2] = (byte)(temp[2] & 0xfe);
                temp[1] = data[i+1] << 1;
                data[i+1] = (byte)(temp[1] & 0xfe);
                temp[0] = data[i] << 1;
                data[i] = (byte)(temp[0] & 0xfe);
                
                data[i+2] |= (byte)((temp[0] >> 8) & 0x01);
                data[i+1] |= (byte)((temp[2] >> 8) & 0x01);
                data[i] |= (byte)((temp[1] >> 8) & 0x01);
            }
            
            times--;
        }
    }
    
    private void multiSlideRight(int times) {
        while (times > 0) {
            int temp[] = new int[3];
            
            for (int i = 0; i < 63; i += 3) {
                temp[2] = Integer.rotateRight(data[i+2], 1);
                data[i+2] = (byte)(temp[2] & 0x7f);
                temp[1] = Integer.rotateRight(data[i+1], 1);
                data[i+1] = (byte)(temp[1] & 0x7f);
                temp[0] = Integer.rotateRight(data[i], 1);
                data[i] = (byte)(temp[0] & 0x7f);
                
                data[i+2] |= (byte)(Integer.rotateLeft(temp[1], 8) & 0x80);
                data[i+1] |= (byte)(Integer.rotateLeft(temp[0], 8) & 0x80);
                data[i] |= (byte)(Integer.rotateLeft(temp[2], 8) & 0x80);
            }
            
            times--;
        }
    }
    
    private byte flipBitsSingleColor(byte b) {
        byte temp = 0x00;
        
        for (int i = 0; i < 4; i++) {
            temp |= (b & (0x08 >> i)) << (2*i+1);
            temp |= (b & (0x10 << i)) >> (2*i+1);
        }
        
        return temp;
    }
    
    private byte flipBitsMultiColor(byte b) {
        byte temp = 0x00;
        
        for (int i = 0; i < 2; i++) {
            temp |= (b & (0x0c >> 2*i)) << (4*i+2);
            temp |= (b & (0x30 << 2*i)) >> (4*i+2);
        }
        
        return temp;
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
