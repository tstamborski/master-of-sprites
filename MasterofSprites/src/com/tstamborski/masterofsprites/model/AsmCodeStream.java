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
package com.tstamborski.masterofsprites.model;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class AsmCodeStream extends PrintStream {
    public static final int KICKASS_SYNTAX = 1;
    public static final int ACME_SYNTAX = 2;
    public static final int TMPX_SYNTAX = 3;
    
    private String commentPrefix, byteOperand, labelSufix;
    
    public AsmCodeStream(OutputStream ostream, int syntax) {
        super(ostream, false, Charset.defaultCharset());
        
        switch (syntax) {
            case TMPX_SYNTAX:
                commentPrefix = ";";
                byteOperand = ".byte";
                labelSufix = "";
                break;
            case ACME_SYNTAX:
                commentPrefix = ";";
                byteOperand = "!byte";
                labelSufix = ":";
                break;
            default: //KICKASS_SYNTAX
                commentPrefix = "//";
                byteOperand = ".byte";
                labelSufix = ":";
                break;
        }
    }
    
    public void printSpriteProject(SpriteProject project) {
        println(commentPrefix + String.format("Background Color: %d", project.getBgColor().ordinal()));
        println(commentPrefix + String.format("Multi-0 Color: %d", project.getMulti0Color().ordinal()));
        println(commentPrefix + String.format("Multi-1 Color: %d", project.getMulti1Color().ordinal()));
        println("");
        
        println(commentPrefix + String.format("lda #$%02x", project.getBgColor().ordinal()));
        println(commentPrefix + "sta $d021");
        println(commentPrefix + String.format("lda #$%02x", project.getMulti0Color().ordinal()));
        println(commentPrefix + "sta $d025");
        println(commentPrefix + String.format("lda #$%02x", project.getMulti1Color().ordinal()));
        println(commentPrefix + "sta $d026");
        println("");
        
        for (int i = 0; i < project.getMemoryData().size(); i++) {
            SpriteData sd = project.getMemoryData().get(i);
            
            if (sd.isMulticolor())
                println(commentPrefix + 
                        String.format("Multicolor / Sprite Color: %d", sd.getSpriteC64Color().ordinal()));
            else
                println(commentPrefix + 
                        String.format("Singlecolor / Sprite Color: %d", sd.getSpriteC64Color().ordinal()));
            
            println(String.format("sprite%d", i) + labelSufix);
            
            byte[] array = sd.toByteArray();
            for (int j = 0; j < array.length; j += 8)
                println(String.format("\t%s $%02x, $%02x, $%02x, $%02x, $%02x, $%02x, $%02x, $%02x", 
                            byteOperand, 
                            Byte.toUnsignedInt(array[j]), Byte.toUnsignedInt(array[j+1]), 
                            Byte.toUnsignedInt(array[j+2]), Byte.toUnsignedInt(array[j+3]),
                            Byte.toUnsignedInt(array[j+4]), Byte.toUnsignedInt(array[j+5]), 
                            Byte.toUnsignedInt(array[j+6]), Byte.toUnsignedInt(array[j+7])));
            
            println("");
        }
        
        flush();
    }
}
