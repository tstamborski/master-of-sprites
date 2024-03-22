/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import java.awt.BasicStroke;
import java.awt.Color;
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
public class C64ColorPicker extends JComponent {
    private final Palette palette;
    private final ArrayList<ActionListener> actionListeners;
    private C64Color currentC64Color;

    public C64ColorPicker(Palette pal) {
        this.palette = pal;
        currentC64Color = C64Color.White;
        actionListeners = new ArrayList<>();
        
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
        
        setPreferredSize(new Dimension(SpriteImage.WIDTH*8, SpriteImage.HEIGHT*2));
        setMaximumSize(new Dimension(SpriteImage.WIDTH*8, SpriteImage.HEIGHT*2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        int w = getWidth();
        int h = getHeight();
        int c = currentC64Color.ordinal();
        
        g2d.setBackground(Color.white);
        g2d.clearRect(0, 0, w, h);
        
        for (int i = 0; i < 16; i++) {
            g2d.setColor(palette.getColor(C64Color.fromInteger(i)));
            g2d.fillRect((i%8)*(w/8), 
                    (i/8)*(h/2), 
                    (w/8), 
                    (h/2));
        }
        
        if (currentC64Color == C64Color.Black || currentC64Color == C64Color.Blue)
            g2d.setColor(Color.white);
        else
            g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawRect((c%8)*(w/8)+2, (c/8)*(h/2)+2, (w/8)-4, (h/2)-4);
        g2d.setStroke(new BasicStroke(1.0f));
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e); 
        
        if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1) {
            setCurrentC64Color(
                C64Color.fromInteger(
                            (e.getY()/(getHeight()/2))*8 + e.getX()/(getWidth()/8)
                    )
            );
            fireActionEvent();
        }
    }

    private void fireActionEvent() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
        actionListeners.forEach((al)->al.actionPerformed(event));
    }
    
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }
    
    public C64Color getCurrentC64Color() {
        return currentC64Color;
    }

    public void setCurrentC64Color(C64Color currentC64Color) {
        if (this.currentC64Color == currentC64Color)
            return;
        
        this.currentC64Color = currentC64Color;
        repaint();
    }
    
    
    
}
