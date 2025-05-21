/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.Util;
import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.SpriteColor;
import com.tstamborski.masterofsprites.model.SpriteData;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JComponent;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SpriteEditor extends JComponent {
    private Palette palette;
    private SpriteImage spriteImg;
    private SpriteData spriteData;
    private Selection selection;
    private int selectionIndex;
    
    private BufferedImage fgGhostImg, bgGhostImg;
    private GhostSkinning ghostSkinning;
    
    private BufferedImage gridImg;
    private boolean gridFlag;
    
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
        
        if (leftButton)
            draw(e.getX(), e.getY(), currentSpriteColor);
        if (rightButton)
            draw(e.getX(), e.getY(), SpriteColor.BackgroundColor);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        
        if (!isEnabled())
            return;
        
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
            if (e.getButton() == MouseEvent.BUTTON1)
                if (e.isControlDown()) {
                    floodfill(e.getX(), e.getY(), currentSpriteColor);
                    fireActionEvent();
                } else {
                    leftButton = true;
                    draw(e.getX(), e.getY(), currentSpriteColor);
                }
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (e.isControlDown()) {
                    floodfill(e.getX(), e.getY(), SpriteColor.BackgroundColor);
                    fireActionEvent();
                } else {
                    rightButton = true;
                    draw(e.getX(), e.getY(), SpriteColor.BackgroundColor);
                }
            }
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
        
        if (bgGhostImg != null)
            g2d.drawImage(bgGhostImg, 0, 0, getWidth(), getHeight(), this);
        if (spriteImg != null)
            g2d.drawImage(spriteImg, 0, 0, getWidth(), getHeight(), this);
        if (fgGhostImg != null)
            g2d.drawImage(fgGhostImg, 0, 0, getWidth(), getHeight(), this);
        
        if (gridFlag)
            g2d.drawImage(gridImg, 0, 0, this);
    }
    
    public void refresh() {
        if (ghostSkinning != null && selection != null) {
            fgGhostImg = ghostSkinning.getFgImage(selection, selectionIndex, palette);
            bgGhostImg = ghostSkinning.getBgImage(selection, selectionIndex, palette);
        }
        
        if (spriteImg != null)
            spriteImg.redraw();
        
        createGridIamge();
        
        repaint();
    }
    
    public void onCurrentSpriteData(SpriteDataOperation op) {
        op.performOperation(spriteData);
        
        if (spriteImg != null)
            spriteImg.redraw();
        
        repaint();
        fireActionEvent();
    }
    
    public void setSelection(Selection selection, int selectionIndex) {
        this.selection = selection;
        this.selectionIndex = selectionIndex;
        
        spriteData = 
                selection.getSpriteProject().getMemoryData().get(selection.get(selectionIndex));
        
        spriteImg = new SpriteImage(spriteData, palette);
        spriteImg.setMulti0Color(multi0Color);
        spriteImg.setMulti1Color(multi1Color);
        
        refresh();
    }
    
    public void setGhostSkinning(GhostSkinning skinning) {
        this.ghostSkinning = skinning;
        if (skinning == null) {
            fgGhostImg = null;
            bgGhostImg = null;
        }
        
        refresh();
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
        spriteImg = new SpriteImage(spriteData, palette);
        spriteImg.redraw();
        repaint();
    }

    public C64Color getBgC64Color() {
        return bgColor;
    }

    public void setBgC64Color(C64Color bgColor) {
        this.bgColor = bgColor;
        createGridIamge();
        repaint();
    }
    
    public void setSpriteC64Color(C64Color color) {
        if (spriteData == null)
            return;
        
        spriteData.setSpriteC64Color(color);
        refresh();
        fireActionEvent();
    }
    
    public void setMulti0C64Color(C64Color color) {
        multi0Color = color;
        
        if (spriteImg == null)
            return;
        
        spriteImg.setMulti0Color(color);
        spriteImg.redraw();
        repaint();
    }
    
    public void setMulti1C64Color(C64Color color) {
        multi1Color = color;
        
        if (spriteImg == null)
            return;
        
        spriteImg.setMulti1Color(color);
        spriteImg.redraw();
        repaint();
    }
    
    public void setMulticolor(boolean b) {
        if (spriteData != null) {
            spriteData.setMulticolor(b);
            refresh();
            
            fireActionEvent();
        }
    }
    
    public void setOverlay(boolean b) {
        if (spriteData != null) {
            spriteData.setOverlay(b);
            fireActionEvent();
        }
    }
    
    public SpriteColor getCurrentSpriteColor() {
        return currentSpriteColor;
    }
    
    public void setCurrentSpriteColor(SpriteColor color) {
        currentSpriteColor = color;
    }
    
    public void setGrid(boolean b) {
        if (gridFlag == b)
            return;
        
        gridFlag = b;
        repaint();
    }
    
    public boolean isGrid() {
        return gridFlag;
    }
    
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }
    
    public int getSpriteX(int mousex) {
        int x = mousex / zoom;
        if (spriteData.isMulticolor())
            x /= 2;
        
        return x;
    }
    
    public int getSpriteY(int mousey) {
        return mousey / zoom;
    }
    
    private void draw(int x, int y, SpriteColor color) {
        spriteData.setPixel(getSpriteX(x), getSpriteY(y), color);
        refresh();
    }
    
    private void floodfill(int x, int y, SpriteColor targetColor) {
        Queue<Point> q = new LinkedList<>();
        SpriteColor baseColor;
        
        x = getSpriteX(x);
        y = getSpriteY(y);
        
        baseColor = spriteData.getPixel(x, y);
        if (baseColor == targetColor)
            return;
        else
            q.add(new Point(x, y));
        
        while (!q.isEmpty()) {
            Point p = q.poll();
            if (Util.isInBound(p.x, 0, spriteData.getWidth()-1) &&
                    Util.isInBound(p.y, 0, spriteData.getHeight()-1) &&
                    spriteData.getPixel(p.x, p.y) == baseColor) {
                spriteData.setPixel(p.x, p.y, targetColor);
                q.add(new Point(p.x+1, p.y));
                q.add(new Point(p.x-1, p.y));
                q.add(new Point(p.x, p.y+1));
                q.add(new Point(p.x, p.y-1));
            }
        }
        
        refresh();
    }
    
    private void fireActionEvent() {
        actionListeners.forEach(al -> al.actionPerformed(new ActionEvent(
                this, ActionEvent.ACTION_PERFORMED, null
            )));
    }
    
    private void createGridIamge() {
        int width = zoom * SpriteImage.WIDTH;
        int height = zoom * SpriteImage.HEIGHT;
        int stepx = (spriteData != null && spriteData.isMulticolor()) ? zoom*2 : zoom;
        int stepy = zoom;
        Color gridColor = 
                (bgColor == C64Color.Black || bgColor == C64Color.Blue) ? Color.WHITE : Color.BLACK;
        
        if (gridImg == null)
            gridImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = gridImg.createGraphics();
        g2d.setBackground(new Color(0, 0, 0, 0));
        g2d.clearRect(0, 0, width, height);
        g2d.setColor(gridColor);
        
        for (int x = 0; x < width; x += stepx)
            g2d.drawLine(x, 0, x, height);
        g2d.drawLine(width-1, 0, width-1, height);
        
        for (int y = 0; y < height; y += stepy)
            g2d.drawLine(0, y, width, y);
        g2d.drawLine(0, height-1, width, height-1);
        
        g2d.dispose();
    }
}
