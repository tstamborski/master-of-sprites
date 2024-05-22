/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.SpriteColor;
import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpriteEditor extends JComponent {
    private Palette palette;
    private SpriteImage spriteImg;
    private SpriteData spriteData;
    private C64Color multi0Color, multi1Color, bgColor;
    private SpriteColor currentSpriteColor;
    
    private final int zoom;
    private boolean leftButton, rightButton;
    
    private final ArrayList<ActionListener> actionListeners;

    public SpriteEditor(int zoom) {
        this.palette = DefaultPalette.getInstance();
        
        bgColor = C64Color.Black;
        multi0Color = C64Color.LightGray;
        multi1Color = C64Color.White;
        currentSpriteColor = SpriteColor.BackgroundColor;
        this.zoom = zoom;
        
        actionListeners = new ArrayList<>();
        
        setPreferredSize(new Dimension(SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom));
        setMaximumSize(new Dimension(SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom));
        
        enableEvents(MouseEvent.MOUSE_EVENT_MASK | MouseEvent.MOUSE_MOTION_EVENT_MASK);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        
        if (!isEnabled())
            return;
        
        draw(e.getX(), e.getY());
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        
        if (!isEnabled())
            return;
        
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            if (e.getButton() == MouseEvent.BUTTON1)
                leftButton = true;
            if (e.getButton() == MouseEvent.BUTTON3)
                rightButton = true;
            
            draw(e.getX(), e.getY());
        } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
            if (e.getButton() == MouseEvent.BUTTON1)
                leftButton = false;
            if (e.getButton() == MouseEvent.BUTTON3)
                rightButton = false;
            
            fireActionEvent();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        if (!isEnabled())
            return;
        
        g2d.setColor(palette.getColor(bgColor));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        if (spriteImg != null)
            g2d.drawImage(spriteImg.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
    
    public void refresh() {
        if (spriteImg != null)
            spriteImg.redraw();
        repaint();
    }
    
    public void setSpriteData(SpriteData data) {
        spriteData = data;
        spriteImg = new SpriteImage(data, palette);
        spriteImg.setMulti0Color(multi0Color);
        spriteImg.setMulti1Color(multi1Color);
        
        repaint();
    }
    
    public SpriteData getSpriteData() {
        return spriteData;
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        spriteImg = new SpriteImage(spriteData, palette);
        repaint();
    }

    public C64Color getBgC64Color() {
        return bgColor;
    }

    public void setBgC64Color(C64Color bgColor) {
        this.bgColor = bgColor;
        repaint();
    }
    
    public void setSpriteC64Color(C64Color color) {
        if (spriteData == null)
            return;
        
        spriteData.setSpriteC64Color(color);
        refresh();
    }
    
    public void setMulti0C64Color(C64Color color) {
        multi0Color = color;
        
        if (spriteImg == null)
            return;
        
        spriteImg.setMulti0Color(color);
        repaint();
    }
    
    public void setMulti1C64Color(C64Color color) {
        multi1Color = color;
        
        if (spriteImg == null)
            return;
        
        spriteImg.setMulti1Color(color);
        repaint();
    }
    
    public SpriteColor getCurrentSpriteColor() {
        return currentSpriteColor;
    }
    
    public void setCurrentSpriteColor(SpriteColor color) {
        currentSpriteColor = color;
    }
    
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }
    
    private void draw(int x, int y) {
        x = x / zoom;
        y = y / zoom;
        if (spriteData.isMulticolor())
            x /= 2;
            
        if (leftButton)
            spriteData.setPixel(x, y, currentSpriteColor);
        if (rightButton)
            spriteData.setPixel(x, y, SpriteColor.BackgroundColor);
        
        if (leftButton || rightButton)
            refresh();
    }
    
    private void fireActionEvent() {
        actionListeners.forEach(al -> al.actionPerformed(new ActionEvent(
                this, ActionEvent.ACTION_PERFORMED, null
            )));
    }
}
