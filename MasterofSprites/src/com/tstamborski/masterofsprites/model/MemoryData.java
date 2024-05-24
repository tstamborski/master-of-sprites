/*
 * The MIT License
 *
 * Copyright 2023 Tobiasz Stamborski <tstamborski@outlook.com>.
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
package com.tstamborski.masterofsprites.model;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class MemoryData extends ArrayList<SpriteData> implements Serializable {
    private static final long serialVersionUID = 968555L;
    
    public static final int MAX_SIZE = 1024;
    
    private MemoryData() {
    }
    
    public static MemoryData getEmpty(int quantity, C64Color spritecolor, boolean ismulticolor) {
        MemoryData md = new MemoryData();
        
        for (int i = 0; i < quantity; i++)
            md.add(SpriteData.getEmpty(spritecolor, ismulticolor, false));
        
        return md;
    }
    
    public static MemoryData fromInputStream(InputStream istream, int offset) throws IOException {
        MemoryData md = new MemoryData();
        
        if (offset != 0)
            try {
                md.add(SpriteData.fromInputStream(istream, offset));
            }
            catch (EOFException e) {
                return null;
            }
        
        for (int i = 0; i < MAX_SIZE-1; i++) {
            try {
                md.add(SpriteData.fromInputStream(istream));
            }
            catch (EOFException e) {
                break;
            }
        }
        
        return md;
    }
    
    public void toOutputStream(OutputStream ostream, int offset) throws IOException {
        get(0).toOutputStream(ostream, offset);
        for (int i = 1; i < size(); i++)
            get(i).toOutputStream(ostream);
    }
    
    public void saveAsRAW(OutputStream ostream) throws IOException {
        for (SpriteData sd: this)
            ostream.write(sd.toByteArray());
    }
    
    public void saveAsPRG(OutputStream ostream, short addr) throws IOException {
        ostream.write(addr & 0x00ff);
        ostream.write(addr >> 8);
        saveAsRAW(ostream);
    }
}
