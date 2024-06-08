
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.SpriteData;
import com.tstamborski.masterofsprites.model.SpriteProject;

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
    private int zoom;
    private int columns;
    private boolean grid;
    private Palette palette;
    private boolean refreshRequest, reloadRequest;
    
    private SpriteProject project;
    private final ArrayList<SpriteImage> sprites;
    
    private AbstractUtilImage selection_img, grid_img, background_img;
    private final ArrayList<Integer> selection;
    
    private final ArrayList<ActionListener> actionListeners;
    private final ArrayList<SelectionListener> selectionListeners;
    
    private ClipboardPopupMenu popup;
    
    public MemoryView(int zoom, int columns) {
        this.palette = DefaultPalette.getInstance();
        this.zoom = zoom;
        this.columns = columns;
        grid = false;
        
        selection = new ArrayList<>();
        sprites = new ArrayList<>();
        
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
        actionListeners = new ArrayList<>();
        selectionListeners = new ArrayList<>();

        createPopupMenu();
        
        getToolkit().getSystemClipboard().addFlavorListener(fe -> popup.enable(selection));
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
            transfer.add(project.getMemoryData().get(i).deepCopy());
        });
        clip.setContents(transfer, this);
    }
    
    public void paste() {
        Clipboard clip = getToolkit().getSystemClipboard();
        Transferable transfer = clip.getContents(this);
        SpriteDataTransferable transferData;
        
        try {
            transferData = (SpriteDataTransferable)transfer.getTransferData(
                    SpriteDataTransferable.C64_SPRITEDATA_FLAVOR);
        } catch (UnsupportedFlavorException | IOException ex) {
            return;
        }
        
        getSelection();
        for (int i = 0; i < selection.size(); i++) {
            SpriteData element = transferData.get(i % transferData.size());
            System.arraycopy(element.toByteArray(), 0, 
                    project.getMemoryData().get(selection.get(i)).toByteArray(), 0,
                    SpriteData.SIZE);
            
            sprites.get(selection.get(i)).redraw();
        }
        
        fireActionEvent();
        repaint();
    }
    
    public void specialPaste(ByteOperation op) {
        Clipboard clip = getToolkit().getSystemClipboard();
        Transferable transfer = clip.getContents(this);
        SpriteDataTransferable transferData;
        
        try {
            transferData = (SpriteDataTransferable)transfer.getTransferData(
                    SpriteDataTransferable.C64_SPRITEDATA_FLAVOR);
        } catch (UnsupportedFlavorException | IOException ex) {
            return;
        }
        
        getSelection();
        for (int i = 0; i < selection.size(); i++) {
            byte[] src = transferData.get(i % transferData.size()).toByteArray();
            byte[] dst = project.getMemoryData().get(selection.get(i)).toByteArray();
            
            for (int j = 0; j < src.length - 1; j++) //pomijamy bajt atrybutow
                dst[j] = op.performOperation(dst[j], src[j]);
            
            sprites.get(selection.get(i)).redraw();
        }
        
        fireActionEvent();
        repaint();
    }
    
    public int getQuantity() {
        return project.getMemoryData().size();
    }
    
    public void setQuantity(int quantity) {
        if (quantity == project.getMemoryData().size())
            return;
        
        if (quantity < project.getMemoryData().size()) {
            for (int i = project.getMemoryData().size()-1; i >= quantity; i--) {
                project.getMemoryData().remove(i);
                sprites.remove(i);
            }
            selection.removeIf((i) -> {return i >= project.getMemoryData().size();});
        }
        else {
            while (project.getMemoryData().size() != quantity) {
                SpriteData new_sdata = SpriteData.getEmpty(C64Color.Green, false, false);
                project.getMemoryData().add(new_sdata);
                
                SpriteImage si = new SpriteImage(new_sdata, palette);
                si.redraw();
                sprites.add(si);
            }
        }
        
        setPreferredSize();
        fireSelectionEvent();
        fireActionEvent();
        repaint();
    }
    
    public void delete() {
        for (int i = selection.size()-1; i >= 0; i--) {
            project.getMemoryData().get(selection.get(i)).clear();
            sprites.get(selection.get(i)).redraw();
        }
        
        fireActionEvent();
        repaint();
    }
    
    public void reload() {
        reloadRequest = true;
        
        if (!isVisible())
            return;
        
        sprites.clear();
        for (int i = 0; i < project.getMemoryData().size(); i++) {
            SpriteImage si = new SpriteImage(project.getMemoryData().get(i), palette);
            si.setMulti0Color(project.getMulti0Color());
            si.setMulti1Color(project.getMulti1Color());
            si.redraw();
            sprites.add(si);
        }
        setPreferredSize();
        repaint();
        
        reloadRequest = false;
    }

    public SpriteProject getProject() {
        return project;
    }
    
    public void setProject(SpriteProject project) {
        this.project = project;
        
        sprites.clear();
        for (int i = 0; i < project.getMemoryData().size(); i++) {
            SpriteImage si = new SpriteImage(project.getMemoryData().get(i), palette);
            si.setMulti0Color(project.getMulti0Color());
            si.setMulti1Color(project.getMulti1Color());
            si.redraw();
            sprites.add(si);
        }
        
        selection.clear();
        popup.enable(selection);
        createUtilImages();
        setPreferredSize();
        repaint();
    }
    
    public void setPalette(Palette palette) {
        this.palette = palette;
        sprites.forEach(s -> s.setPalette(palette));
        repaint();
    }
    
    public BufferedImage toBufferedImage(int type) {
        Graphics2D g2d;
        BufferedImage img;
        
        if (project.getMemoryData().size()%columns == 0)
            img = new BufferedImage(columns*24, (project.getMemoryData().size()/columns)*21, type);
        else
            if (project.getMemoryData().size() > columns)
                img = new BufferedImage(columns*24, (project.getMemoryData().size()/columns+1)*21, type);
            else
                img = new BufferedImage(project.getMemoryData().size()*24, 21, type);
        
        g2d = img.createGraphics();
        if (img.getTransparency() == Transparency.OPAQUE) {
            g2d.setBackground(palette.getColor(project.getBgColor()));
            g2d.clearRect(0, 0, img.getWidth(), img.getHeight());
        }
        else {
            g2d.setComposite(AlphaComposite.Clear);
            g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
            g2d.setComposite(AlphaComposite.Src);
        }
        for (int i = 0; i < sprites.size(); i++)
            g2d.drawImage(sprites.get(i), 
                    (i%columns)*24, (i/columns)*21, this);
        
        return img;
    }
    
    public void setColumns(int c) {
        columns = c;
        setPreferredSize();
        repaint();
    }
    
    public void setZoom(int z) {
        zoom = z;
        createUtilImages();
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
        
        if (project == null)
            return;
        
        for (int i = 0; i < sprites.size(); i++) {
            g2d.drawImage(background_img, 
                    (i%columns)*SpriteImage.WIDTH*zoom, (i/columns)*SpriteImage.HEIGHT*zoom, 
                    this);
            g2d.drawImage(sprites.get(i), 
                    (i%columns)*SpriteImage.WIDTH*zoom, (i/columns)*SpriteImage.HEIGHT*zoom, 
                    SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom, this);
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
        super.processMouseEvent(e);
        
        Integer new_selection, old_selection;
        
        if (project == null || project.getMemoryData() == null)
            return;
        if (e.getID() != MouseEvent.MOUSE_PRESSED || e.getButton() != MouseEvent.BUTTON1)
            return;
        
        if (getIndexAt(e.getX(),e.getY()) < sprites.size()) {
            if (!e.isShiftDown()) { //bez klawisza shift
                new_selection = getIndexAt(e.getX(),e.getY());
                
                if (e.isControlDown()) { //control wcisniety
                    if (!selection.contains(new_selection))
                        selection.add(new_selection);
                    else
                        selection.remove(new_selection);
                }
                else { //bez klawisza control
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
        if (project.getMemoryData().size()%columns == 0)
            setPreferredSize(new Dimension(columns*24*zoom, (project.getMemoryData().size()/columns)*21*zoom));
        else
            if (project.getMemoryData().size() > columns)
                setPreferredSize(new Dimension(columns*24*zoom, (project.getMemoryData().size()/columns+1)*21*zoom));
            else
                setPreferredSize(new Dimension(project.getMemoryData().size()*24*zoom, 21*zoom));
    }
    
    private void createPopupMenu() {
        popup = new ClipboardPopupMenu();
        
        popup.cutMenuItem.addActionListener((ae) -> {
            cut();
        });
        popup.copyMenuItem.addActionListener((ae) -> {
            copy();
        });
        popup.pasteMenuItem.addActionListener((ae) -> {
            paste();
        });
        
        popup.orPasteMenuItem.addActionListener((ae) -> {
            specialPaste((a,b) -> (byte)(a | b));
        });
        popup.andPasteMenuItem.addActionListener((ae) -> {
            specialPaste((a,b) -> (byte)(a & b));
        });
        popup.xorPasteMenuItem.addActionListener((ae) -> {
            specialPaste((a,b) -> (byte)(a ^ b));
        });
        
        popup.deleteMenuItem.addActionListener((ae) -> {
            delete();
        });
        
        setComponentPopupMenu(popup);
    }
    
    private void createSelectionImage() {
        Color c;
        C64Color bg = project.getBgColor();
        
        if (bg == C64Color.White || bg == C64Color.Yellow || bg == C64Color.LightGreen)
            c = Color.BLACK;
        else
            c = Color.WHITE;
        
        selection_img = new SelectionImage(SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom, c);
    }
    
    private void createGridImage() {
        Color c;
        C64Color bg = project.getBgColor();
        
        if (bg == C64Color.Blue || bg == C64Color.LightBlue)
            c = Color.RED;
        else
            c = Color.BLUE;
        
        grid_img = new GridImage(SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom, c);
    }
    
    private void createBackgroundImage() {
        background_img = new BackgroundImage(
                SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom, palette.getColor(project.getBgColor())
        );
    }
    
    private void createUtilImages() {
        createBackgroundImage();
        createGridImage();
        createSelectionImage();
    }
    
    public int getIndexAt(int x, int y) {
        return (y/(21*zoom))*columns + x/(24*zoom);
    }

    private void fireActionEvent() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
        actionListeners.forEach((al)->al.actionPerformed(event));
    }
    
    private void fireSelectionEvent() {
        SelectionEvent event = new SelectionEvent(this, getSelection());
        selectionListeners.forEach((sl)->sl.selectionPerformed(event));
        popup.enable(selection);
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

    public void refresh() {
        refreshRequest = true;
        
        if (!isVisible())
            return;
        
        createUtilImages();
        sprites.forEach((si)->{
            si.setMulti0Color(project.getMulti0Color());
            si.setMulti1Color(project.getMulti1Color());
            si.redraw();
        });
        repaint();
        
        refreshRequest = false;
    }
    
    public void refreshSelection() {
        refreshRequest = true;
        
        if (!isVisible())
            return;
        
        selection.forEach(i->sprites.get(i).redraw());
        repaint();
        
        refreshRequest = false;
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        
        if (aFlag)
            if (reloadRequest)
                reload();
            else if (refreshRequest)
                refresh();
    }
    
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
