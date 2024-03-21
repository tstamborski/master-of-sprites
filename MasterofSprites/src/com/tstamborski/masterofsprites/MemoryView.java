
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.SpriteData;
import com.tstamborski.masterofsprites.model.MemoryData;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class MemoryView extends JComponent implements ClipboardOwner {
    private final Color SELECT_COLOR = Color.WHITE;
    private final Color GRID_COLOR = Color.GREEN;
    
    private int zoom;
    private int columns;
    private boolean grid;
    private Palette palette;
    
    C64Color backgroundC64Color, multi0C64Color, multi1C64Color;
    
    private MemoryData data;
    private final ArrayList<Sprite> sprites;
    
    private BufferedImage selection_img, grid_img, background_img;
    private final ArrayList<Integer> selection;
    
    private final ArrayList<ActionListener> actionListeners;
    private final ArrayList<SelectionListener> selectionListeners;
    
    public MemoryView(Palette palette, int zoom, int columns) {
        this.palette = palette;
        this.zoom = zoom;
        this.columns = columns;
        grid = false;
        
        selection = new ArrayList<>();
        sprites = new ArrayList<>();
        createSelectionImage();
        createGridImage();
        setBackgroundC64Color(C64Color.Black);
        setMulti0C64Color(C64Color.LightGray);
        setMulti1C64Color(C64Color.White);
        
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
        actionListeners = new ArrayList<>();
        selectionListeners = new ArrayList<>();
    }
    
    public void cut() {
        copy();
        delete();
    }
    
    public void copy() {
        Clipboard clip = getToolkit().getSystemClipboard();
        SpriteDataTransferable transfer = new SpriteDataTransferable();
        
        getSelection();
        selection.forEach((i) -> {
            transfer.add(data.get(i).deepCopy());
        });
        clip.setContents(transfer, this);
    }
    
    public void paste() { //TODO
        Clipboard clip = getToolkit().getSystemClipboard();
        Transferable transfer = clip.getContents(this);
        SpriteDataTransferable transferData;
        
        try {
            transferData = (SpriteDataTransferable)transfer.getTransferData(
                    SpriteDataTransferable.C64_SPRITEDATA_FLAVOR);
            //TODO
        } catch (UnsupportedFlavorException | IOException ex) {
            return;
        }
        
        getSelection();
        for (int i = 0; i < selection.size(); i++) {
            SpriteData element = transferData.get(i);
            if (element != null)
                System.arraycopy(element.toByteArray(), 0, 
                        data.get(selection.get(i)).toByteArray(), 0,
                        SpriteData.SIZE);
            sprites.get(selection.get(i)).redraw();
        }
        
        repaint();
    }
    
    public int getQuantity() {
        return data.size();
    }
    
    public void setQuantity(int quantity) {
        if (quantity < data.size()) {
            for (int i = data.size()-1; i >= quantity; i--) {
                data.remove(i);
                sprites.remove(i);
            }
            selection.removeIf((i) -> {return i >= data.size();});
        }
        else {
            while (data.size() != quantity) {
                SpriteData new_sdata = SpriteData.getEmpty(C64Color.Green, false, false);
                data.add(new_sdata);
                sprites.add(new Sprite(new_sdata, palette));
            }
        }
        
        setPreferredSize();
        repaint();
        fireSelectionEvent();
    }
    
    public void delete() {
        for (int i = selection.size()-1; i >= 0; i--) {
            data.get(selection.get(i)).clear();
            sprites.get(selection.get(i)).redraw();
        }
        
        setPreferredSize();
        repaint();
    }
    
    public final void setMemoryData(MemoryData d) {
        data = d;
        sprites.clear();
        for (int i = 0; i < data.size(); i++) {
            sprites.add(new Sprite(data.get(i), palette));
        }
        
        sprites.forEach(s->{
            s.setMulti0Color(multi0C64Color);
            s.setMulti1Color(multi1C64Color);
            s.setPalette(palette);
        });
        
        selection.clear();
        setPreferredSize();
        repaint();
        fireSelectionEvent();
    }
    
    public void setPalette(Palette palette) {
        this.palette = palette;
        sprites.forEach(s -> s.setPalette(palette));
        repaint();
    }
    
    public BufferedImage toBufferedImage(int type) {
        Graphics2D g2d;
        BufferedImage img;
        
        if (data.size()%columns == 0)
            img = new BufferedImage(columns*24, (data.size()/columns)*21, type);
        else
            if (data.size() > columns)
                img = new BufferedImage(columns*24, (data.size()/columns+1)*21, type);
            else
                img = new BufferedImage(data.size()*24, 21, type);
        
        g2d = img.createGraphics();
        if (img.getTransparency() == Transparency.OPAQUE) {
            g2d.setBackground(palette.getColor(backgroundC64Color));
            g2d.clearRect(0, 0, img.getWidth(), img.getHeight());
        }
        else {
            g2d.setComposite(AlphaComposite.Clear);
            g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
            g2d.setComposite(AlphaComposite.Src);
        }
        for (int i = 0; i < sprites.size(); i++)
            g2d.drawImage(sprites.get(i).getImage(), 
                    (i%columns)*24, (i/columns)*21, this);
        
        return img;
    }
    
    public void setSpriteC64Color(C64Color c) {
        getSelection().forEach(i -> {
            sprites.get(i).setSpriteColor(c);
        });
        repaint();
    }

    public final void setMulti0C64Color(C64Color multi0C64Color) {
        this.multi0C64Color = multi0C64Color;
        
        sprites.forEach(s -> {
            s.setMulti0Color(multi0C64Color);
        });
        repaint();
    }

    public final void setMulti1C64Color(C64Color multi1C64Color) {
        this.multi1C64Color = multi1C64Color;
        
        sprites.forEach(s -> {
            s.setMulti1Color(multi1C64Color);
        });
        repaint();
    }
    
    public final void setBackgroundC64Color(C64Color c) {
        backgroundC64Color = c;
        createBackgroundImage();
        repaint();
    }
    
    public void setColumns(int c) {
        columns = c;
        setPreferredSize();
        repaint();
    }
    
    public void setZoom(int z) {
        zoom = z;
        createSelectionImage();
        createBackgroundImage();
        setPreferredSize();
        repaint();
    }
    
    public ArrayList<Integer> getSelection() {
        Collections.sort(selection);
        return selection;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        if (data == null || palette == null)
            return;
        
        for (int i = 0; i < sprites.size(); i++) {
            g2d.drawImage(background_img, (i%columns)*24*zoom, (i/columns)*21*zoom, this);
            g2d.drawImage(sprites.get(i).getImage(), 
                    (i%columns)*24*zoom, (i/columns)*21*zoom, 
                    24*zoom, 21*zoom, this);
        }
        
        if (grid)
            for (int i = 0; i < sprites.size(); i++)
                g2d.drawImage(grid_img, (i%columns)*24*zoom, (i/columns)*21*zoom, this);
        
        for (int i = 0; i < selection.size(); i++)
            g2d.drawImage(selection_img, 
                    (selection.get(i)%columns)*24*zoom, (selection.get(i)/columns)*21*zoom, this);
    }
    
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e); //To change body of generated methods, choose Tools | Templates.
        
        Integer new_selection, old_selection;
        
        if (e.getID() != MouseEvent.MOUSE_PRESSED || e.getButton() != MouseEvent.BUTTON1)
            return;
        if (data == null)
            return;
        
        if (getIndexAt(e.getX(),e.getY()) < sprites.size()) {
            if (!e.isShiftDown()) { //bez klawisza shift
                new_selection = getIndexAt(e.getX(),e.getY());
                
                if (e.isControlDown()) {
                    if (!selection.contains(new_selection))
                        selection.add(new_selection);
                }
                else {
                    selection.clear();
                    selection.add(new_selection);
                }
            }
            else { //shift wcisniety
                if (getSelection().isEmpty())
                    old_selection = 0;
                else
                    old_selection = getSelection().get(0);
                
                new_selection = getIndexAt(e.getX(),e.getY());
                selection.clear();
                
                if (new_selection > old_selection)
                    for (int i = old_selection; i <= new_selection; i++)
                        selection.add(i);
                else
                    for (int i = new_selection; i <= old_selection; i++)
                        selection.add(i);
            }
            
            repaint();
            fireSelectionEvent();
        }
    }
    
    private void setPreferredSize() {
        if (data.size()%columns == 0)
            setPreferredSize(new Dimension(columns*24*zoom, (data.size()/columns)*21*zoom));
        else
            if (data.size() > columns)
                setPreferredSize(new Dimension(columns*24*zoom, (data.size()/columns+1)*21*zoom));
            else
                setPreferredSize(new Dimension(data.size()*24*zoom, 21*zoom));
        
        fireActionEvent();
    }
    
    private void createSelectionImage() {
        Graphics2D g2d;
        
        selection_img = new BufferedImage(24*zoom, 21*zoom, BufferedImage.TYPE_INT_ARGB);
        
        g2d = selection_img.createGraphics();
        g2d.setColor(new Color(SELECT_COLOR.getRed(),SELECT_COLOR.getGreen(),SELECT_COLOR.getBlue(),0x30));
        g2d.fillRect(0, 0, 24*zoom, 21*zoom);
        g2d.setColor(SELECT_COLOR);
        g2d.drawRect(0, 0, 24*zoom-1, 21*zoom-1);
    }
    
    private void createGridImage() {
        Graphics2D g2d;
        
        grid_img = new BufferedImage(Sprite.WIDTH*zoom, Sprite.HEIGHT*zoom, BufferedImage.TYPE_INT_ARGB);
        
        g2d = grid_img.createGraphics();
        g2d.setColor(new Color(0x00,0x00,0x00,0x00));
        g2d.fillRect(0, 0, Sprite.WIDTH*zoom, Sprite.HEIGHT*zoom);
        g2d.setColor(GRID_COLOR);
        g2d.drawRect(0, 0, Sprite.WIDTH*zoom-1, Sprite.HEIGHT*zoom-1);
    }
    
    private void createBackgroundImage() {
        background_img = new BufferedImage(Sprite.WIDTH*zoom, Sprite.HEIGHT*zoom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = background_img.createGraphics();
        
        g.setColor(palette.getColor(backgroundC64Color));
        g.fillRect(0, 0, Sprite.WIDTH*zoom, Sprite.HEIGHT*zoom);
    }
    
    private int getIndexAt(int x, int y) {
        return (y/(21*zoom))*columns + x/(24*zoom);
    }

    private void fireActionEvent() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
        actionListeners.forEach((al)->al.actionPerformed(event));
    }
    
    private void fireSelectionEvent() {
        SelectionEvent event = new SelectionEvent(this, getSelection());
        selectionListeners.forEach((sl)->sl.selectionPerformed(event));
    }
    
    public void addActionListener(ActionListener al) {
        actionListeners.add(al);
    }
    
    public void addSelectionListener(SelectionListener sl) {
        selectionListeners.add(sl);
    }
    
    public boolean isGrid() {
        return grid;
    }
    
    public void setGrid(boolean grid) {
        this.grid = grid;
        repaint();
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
