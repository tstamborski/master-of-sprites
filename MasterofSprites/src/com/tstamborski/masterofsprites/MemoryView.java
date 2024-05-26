
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
import java.awt.event.KeyEvent;
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
    
    private SpriteProject project;
    private final ArrayList<SpriteImage> sprites;
    
    private BufferedImage selection_img, grid_img, background_img;
    private final ArrayList<Integer> selection;
    
    private final ArrayList<ActionListener> actionListeners;
    private final ArrayList<SelectionListener> selectionListeners;
    
    private final JPopupMenu popup;
    private final JMenuItem cutMenuItem, copyMenuItem, pasteMenuItem, deleteMenuItem;
    
    public MemoryView(int zoom, int columns) {
        this.palette = DefaultPalette.getInstance();
        this.zoom = zoom;
        this.columns = columns;
        grid = false;
        
        selection = new ArrayList<>();
        sprites = new ArrayList<>();
        createSelectionImage();
        createGridImage();
        
        enableEvents(MouseEvent.MOUSE_EVENT_MASK);
        actionListeners = new ArrayList<>();
        selectionListeners = new ArrayList<>();
        
        popup = new JPopupMenu();
        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/cut16.png")));
        cutMenuItem.setMnemonic(KeyEvent.VK_T);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cutMenuItem.addActionListener((ae) -> {
            cut();
        });
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/copy16.png")));
        copyMenuItem.setMnemonic(KeyEvent.VK_Y);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        copyMenuItem.addActionListener((ae) -> {
            copy();
        });
        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/paste16.png")));
        pasteMenuItem.setMnemonic(KeyEvent.VK_P);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        pasteMenuItem.addActionListener((ae) -> {
            paste();
        });
        deleteMenuItem = new JMenuItem("Delete");
        deleteMenuItem.setIcon(new ImageIcon(getClass().getResource("icons/bin16.png")));
        deleteMenuItem.setMnemonic(KeyEvent.VK_D);
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteMenuItem.addActionListener((ae) -> {
            delete();
        });
        popup.add(cutMenuItem);
        popup.add(copyMenuItem);
        popup.add(pasteMenuItem);
        popup.addSeparator();
        popup.add(deleteMenuItem);
        
        getToolkit().getSystemClipboard().addFlavorListener(fe -> enablePopupMenuItems());
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
            SpriteData element = transferData.get(i % transferData.size());
            System.arraycopy(element.toByteArray(), 0, 
                    project.getMemoryData().get(selection.get(i)).toByteArray(), 0,
                    SpriteData.SIZE);
            
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
                sprites.add(new SpriteImage(new_sdata, palette));
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
        sprites.clear();
        for (int i = 0; i < project.getMemoryData().size(); i++) {
            sprites.add(new SpriteImage(project.getMemoryData().get(i), palette));
        }
        setPreferredSize();
        repaint();
    }

    public SpriteProject getProject() {
        return project;
    }
    
    public void setProject(SpriteProject project) {
        this.project = project;
        
        sprites.clear();
        for (int i = 0; i < project.getMemoryData().size(); i++) {
            sprites.add(new SpriteImage(project.getMemoryData().get(i), palette));
        }
        
        sprites.forEach(s->{
            s.setMulti0Color(project.getMulti0Color());
            s.setMulti1Color(project.getMulti1Color());
            s.setPalette(palette);
        });
        
        selection.clear();
        enablePopupMenuItems();
        createBackgroundImage();
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
            g2d.drawImage(sprites.get(i).getImage(), 
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
        createSelectionImage();
        createGridImage();
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
        
        if (project.getMemoryData() == null || palette == null)
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
        
        if (project == null || project.getMemoryData() == null)
            return;
        if (e.getID() != MouseEvent.MOUSE_PRESSED || e.getButton() != MouseEvent.BUTTON1) {
            if (e.getID() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON3)
                popup.show(this, e.getX(), e.getY());
            return;
        }
        
        if (getIndexAt(e.getX(),e.getY()) < sprites.size()) {
            if (!e.isShiftDown()) { //bez klawisza shift
                new_selection = getIndexAt(e.getX(),e.getY());
                
                if (e.isControlDown()) {
                    if (!selection.contains(new_selection))
                        selection.add(new_selection);
                    else
                        selection.remove(new_selection);
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
        if (project.getMemoryData().size()%columns == 0)
            setPreferredSize(new Dimension(columns*24*zoom, (project.getMemoryData().size()/columns)*21*zoom));
        else
            if (project.getMemoryData().size() > columns)
                setPreferredSize(new Dimension(columns*24*zoom, (project.getMemoryData().size()/columns+1)*21*zoom));
            else
                setPreferredSize(new Dimension(project.getMemoryData().size()*24*zoom, 21*zoom));
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
        
        grid_img = new BufferedImage(SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom, BufferedImage.TYPE_INT_ARGB);
        
        g2d = grid_img.createGraphics();
        g2d.setColor(new Color(0x00,0x00,0x00,0x00));
        g2d.fillRect(0, 0, SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom);
        g2d.setColor(GRID_COLOR);
        g2d.drawRect(0, 0, SpriteImage.WIDTH*zoom-1, SpriteImage.HEIGHT*zoom-1);
    }
    
    private void createBackgroundImage() {
        background_img = new BufferedImage(SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = background_img.createGraphics();
        
        g.setColor(palette.getColor(project.getBgColor()));
        g.fillRect(0, 0, SpriteImage.WIDTH*zoom, SpriteImage.HEIGHT*zoom);
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
        enablePopupMenuItems();
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
        createBackgroundImage();
        sprites.forEach((si)->{
            si.setMulti0Color(project.getMulti0Color());
            si.setMulti1Color(project.getMulti1Color());
            si.redraw();
        });
        repaint();
    }
    
    public void refreshSelection() {
        selection.forEach(i->sprites.get(i).redraw());
        repaint();
    }
    
    private void enablePopupMenuItems() {
        deleteMenuItem.setEnabled(!selection.isEmpty());
        cutMenuItem.setEnabled(!selection.isEmpty());
        copyMenuItem.setEnabled(!selection.isEmpty());
        
        pasteMenuItem.setEnabled(!selection.isEmpty() && 
                getToolkit().getSystemClipboard().isDataFlavorAvailable(SpriteDataTransferable.C64_SPRITEDATA_FLAVOR));
    }
    
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
