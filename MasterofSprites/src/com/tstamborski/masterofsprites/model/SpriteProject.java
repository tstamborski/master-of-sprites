/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpriteProject implements Serializable {
    private static final long serialVersionUID = 66623L;
    
    MemoryData memoryData;
    C64Color multi0Color, multi1Color, bgColor;
    Time workTime;
    int overlayDistance;
    short defaultAddress;

    private SpriteProject() {
        multi0Color = C64Color.LightGray;
        multi1Color = C64Color.White;
        bgColor = C64Color.Black;
        
        workTime = new Time();
        defaultAddress = 0x3000;
    }
    
    public static SpriteProject getNewProject(int quantity, boolean isMulticolor) {
        SpriteProject project = new SpriteProject();
        project.memoryData = MemoryData.getEmpty(quantity, C64Color.Green, isMulticolor);
        return project;
    }
    
    public static SpriteProject importRAWData(InputStream istream) throws IOException {
        SpriteProject project = new SpriteProject();
        
        project.memoryData = MemoryData.fromInputStream(istream, 0);
        if (project.memoryData.size() <= 64)
            project.setDefaultAddress((short)0x3000);
        else if (project.memoryData.size() <= 512)
            project.setDefaultAddress((short)0x4000);
        else
            project.setDefaultAddress((short)(0x10000-project.memoryData.size()*SpriteData.SIZE));
        
        return project;
    }
    
    public static SpriteProject importPRGData(InputStream istream) throws IOException {
        SpriteProject project = new SpriteProject();
        short addr;
        
        addr = (short)istream.read();
        addr += (short)(istream.read() << 8);
        project.setDefaultAddress(addr);
        project.memoryData = MemoryData.fromInputStream(istream, addr % SpriteData.SIZE);
        
        return project;
    }
    
    public void exportToRAWData(OutputStream ostream) throws IOException {
        memoryData.toOutputStream(ostream, 0);
    }
    
    public void exportToPRGData(OutputStream ostream, short address) throws IOException {
        ostream.write(address & 0x00ff);
        ostream.write(address >> 8);
        memoryData.toOutputStream(ostream, address % SpriteData.SIZE);
    }

    public C64Color getMulti0Color() {
        return multi0Color;
    }

    public void setMulti0Color(C64Color multi0Color) {
        this.multi0Color = multi0Color;
    }

    public C64Color getMulti1Color() {
        return multi1Color;
    }

    public void setMulti1Color(C64Color multi1Color) {
        this.multi1Color = multi1Color;
    }

    public C64Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(C64Color bgColor) {
        this.bgColor = bgColor;
    }

    public MemoryData getMemoryData() {
        return memoryData;
    }

    public Time getWorkTime() {
        return workTime;
    }

    public short getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(short defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
    
    public int getOverlayDistance() {
        return overlayDistance;
    }

    public void setOverlayDistance(int overlayDistance) {
        this.overlayDistance = overlayDistance;
    }
}
