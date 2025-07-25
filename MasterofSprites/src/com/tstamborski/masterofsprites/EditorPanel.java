/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.C64Color;
import com.tstamborski.masterofsprites.model.SpriteColor;
import com.tstamborski.masterofsprites.model.SpriteData;
import com.tstamborski.masterofsprites.model.SpriteProject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class EditorPanel extends JPanel {
    private JPanel spriteChooserPanel, editPanel, colorChoosePanel;
    private JPanel toolsPanel, attrPanel;
    private JButton slideUpButton, slideDownButton, slideLeftButton, slideRightButton;
    private JButton flipHorzButton, flipVertButton;
    private JButton prevButton;
    private JButton nextButton;
    private JLabel spriteChooserLabel;
    private SpriteEditor editor;
    private JRadioButton sprColorButton, multi0ColorButton;
    private JRadioButton multi1ColorButton, bgColorButton;
    private ButtonGroup colorButtonGroup;
    private JCheckBox multicolorCheckBox, overlayCheckBox;
    private C64ColorLabel sprColorLabel, multi0ColorLabel, multi1ColorLabel, bgColorLabel;
    private C64ColorPicker colorPicker;
    private JLabel overlayDistLabel;
    private JSpinner overlayDistSpinner;
    
    private final ArrayList<ActionListener> actionListeners;
    private final ArrayList<PreviewListener> previewListeners;
    
    private SpriteProject project;
    private Selection selection;
    private int selectionIndex;
    
    public EditorPanel() {
        actionListeners = new ArrayList<>();
        previewListeners = new ArrayList<>();
        
        createControls();
        layoutControls();
        
        setProject(null);
    }
    
    private void createControls() {
        editor = new SpriteEditor(8);
        editor.setBorder(BorderFactory.createLoweredBevelBorder());
        editor.addMouseWheelListener((MouseWheelEvent e) -> {
            if (!editor.isEnabled())
                return;
            
            if (e.getWheelRotation() > 0)
                nextSpriteColor();
            else if (e.getWheelRotation() < 0)
                prevSpriteColor();
        });
        
        spriteChooserLabel = new JLabel("none");
        spriteChooserLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        prevButton = new JButton(new ImageIcon(getClass().getResource("icons/rewind-backward16.png")));
        prevButton.setMnemonic(KeyEvent.VK_LEFT);
        prevButton.addActionListener((ae)->setSprite(--selectionIndex));
        nextButton = new JButton(new ImageIcon(getClass().getResource("icons/rewind-forward16.png")));
        nextButton.setMnemonic(KeyEvent.VK_RIGHT);
        nextButton.addActionListener((ae)->setSprite(++selectionIndex));
        
        slideUpButton = new JButton(new ImageIcon(getClass().getResource("icons/dir-up16.png")));
        slideUpButton.addActionListener(ae -> editor.onCurrentSpriteData((sd)->sd.slideUp()));
        slideDownButton = new JButton(new ImageIcon(getClass().getResource("icons/dir-down16.png")));
        slideDownButton.addActionListener(ae -> editor.onCurrentSpriteData((sd)->sd.slideDown()));
        slideLeftButton = new JButton(new ImageIcon(getClass().getResource("icons/dir-left16.png")));
        slideLeftButton.addActionListener(ae -> editor.onCurrentSpriteData((sd)->sd.slideLeft()));
        slideRightButton = new JButton(new ImageIcon(getClass().getResource("icons/dir-right16.png")));
        slideRightButton.addActionListener(ae -> editor.onCurrentSpriteData((sd)->sd.slideRight()));
        flipHorzButton = new JButton(new ImageIcon(getClass().getResource("icons/flip-horz16.png")));
        flipHorzButton.addActionListener(ae -> editor.onCurrentSpriteData((sd)->sd.flipHorizontally()));
        flipVertButton = new JButton(new ImageIcon(getClass().getResource("icons/flip-vert16.png")));
        flipVertButton.addActionListener(ae -> editor.onCurrentSpriteData((sd)->sd.flipVertically()));
        
        sprColorButton = new JRadioButton("Sprite Color");
        sprColorButton.setSelected(true);
        multi0ColorButton = new JRadioButton("Multi-0 Color");
        multi1ColorButton = new JRadioButton("Multi-1 Color");
        bgColorButton = new JRadioButton("Background Color");
        colorButtonGroup = new ButtonGroup();
        colorButtonGroup.add(sprColorButton);
        colorButtonGroup.add(multi0ColorButton);
        colorButtonGroup.add(multi1ColorButton);
        colorButtonGroup.add(bgColorButton);
        sprColorButton.addActionListener(ae -> setCurrentSpriteColor(SpriteColor.SpriteColor));
        multi0ColorButton.addActionListener(ae -> setCurrentSpriteColor(SpriteColor.Multi0Color));
        multi1ColorButton.addActionListener(ae -> setCurrentSpriteColor(SpriteColor.Multi1Color));
        bgColorButton.addActionListener(ae -> setCurrentSpriteColor(SpriteColor.BackgroundColor));
        
        sprColorLabel = new C64ColorLabel();
        sprColorLabel.setC64Color(C64Color.Green);
        multi0ColorLabel = new C64ColorLabel();
        multi0ColorLabel.setC64Color(C64Color.LightGray);
        multi1ColorLabel = new C64ColorLabel();
        multi1ColorLabel.setC64Color(C64Color.White);
        bgColorLabel = new C64ColorLabel();
        bgColorLabel.setC64Color(C64Color.Black);
        
        colorPicker = new C64ColorPicker();
        colorPicker.setBorder(BorderFactory.createLineBorder(Color.black));
        colorPicker.addActionListener(ae -> setCurrentC64Color(colorPicker.getCurrentC64Color()));
        
        multicolorCheckBox = new JCheckBox("Multicolor Mode");
        multicolorCheckBox.setMnemonic('M');
        multicolorCheckBox.addActionListener(ae -> {
                editor.setMulticolor(multicolorCheckBox.isSelected());
                setSprite(selectionIndex);
            });
        overlayCheckBox = new JCheckBox("Overlay by Next Sprite");
        overlayCheckBox.setMnemonic('O');
        overlayCheckBox.addActionListener(ae -> {
                editor.setOverlay(overlayCheckBox.isSelected());
            });
        overlayDistSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 128, 1));
        overlayDistSpinner.addChangeListener(che -> {
                project.setOverlayDistance((Integer)overlayDistSpinner.getValue());
                editor.refresh();
                firePreviewEvent();
            });
        overlayDistLabel = new JLabel("Overlay Distance:    ");
        overlayDistLabel.setDisplayedMnemonic('D');
        overlayDistLabel.setLabelFor(overlayDistSpinner);
    }
    
    private void layoutControls() {
        spriteChooserPanel = new JPanel();
        spriteChooserPanel.setLayout(new BoxLayout(spriteChooserPanel, BoxLayout.X_AXIS));
        spriteChooserPanel.add(prevButton);
        spriteChooserPanel.add(Box.createHorizontalGlue());
        spriteChooserPanel.add(spriteChooserLabel);
        spriteChooserPanel.add(Box.createHorizontalGlue());
        spriteChooserPanel.add(nextButton);
        
        editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        editPanel.setMinimumSize(new Dimension(editor.getWidth(), editor.getHeight()));
        editPanel.add(editor);
        editPanel.setMaximumSize(editPanel.getPreferredSize());
        editPanel.setMinimumSize(editPanel.getPreferredSize());
        
        toolsPanel = new JPanel();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
        toolsPanel.add(slideUpButton);
        toolsPanel.add(slideDownButton);
        toolsPanel.add(slideLeftButton);
        toolsPanel.add(slideRightButton);
        toolsPanel.add(Box.createHorizontalGlue());
        toolsPanel.add(flipHorzButton);
        toolsPanel.add(flipVertButton);
        
        colorChoosePanel = new JPanel();
        colorChoosePanel.setLayout(new GridBagLayout());
        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.gridx = 0;
        lc.gridy = 0;
        colorChoosePanel.add(sprColorLabel, lc);
        lc.gridx = 1;
        colorChoosePanel.add(sprColorButton, lc);
        lc.gridx = 0;
        lc.gridy = 1;
        colorChoosePanel.add(multi0ColorLabel, lc);
        lc.gridx = 1;
        colorChoosePanel.add(multi0ColorButton, lc);
        lc.gridx = 0;
        lc.gridy = 2;
        colorChoosePanel.add(multi1ColorLabel, lc);
        lc.gridx = 1;
        colorChoosePanel.add(multi1ColorButton, lc);
        lc.gridx = 0;
        lc.gridy = 3;
        colorChoosePanel.add(bgColorLabel, lc);
        lc.gridx = 1;
        colorChoosePanel.add(bgColorButton, lc);
        colorChoosePanel.setMaximumSize(new Dimension(
                Short.MAX_VALUE, 
                colorChoosePanel.getPreferredSize().height));
        
        attrPanel = new JPanel();
        attrPanel.setLayout(new GridBagLayout());
        lc.gridx = 0;
        lc.gridy = 0;
        lc.gridwidth = 2;
        attrPanel.add(multicolorCheckBox, lc);
        lc.gridy = 1;
        attrPanel.add(overlayCheckBox, lc);
        lc.gridwidth = 1;
        lc.gridy = 2;
        lc.gridx = 0;
        attrPanel.add(overlayDistLabel, lc);
        lc.gridx = 1;
        attrPanel.add(overlayDistSpinner, lc);
        attrPanel.setMaximumSize(new Dimension(
                Short.MAX_VALUE,
                attrPanel.getPreferredSize().height
            ));
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(spriteChooserPanel);
        add(editPanel);
        add(toolsPanel);
        add(Box.createVerticalGlue());
        add(colorChoosePanel);
        add(colorPicker);
        add(Box.createVerticalGlue());
        add(attrPanel);
        add(Box.createVerticalGlue());
    }
    
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }
    
    public void addPreviewListener(PreviewListener listener) {
        previewListeners.add(listener);
    }
    
    public SpriteEditor getSpriteEditor() {
        return editor;
    }
    
    public int getSpriteIndex() {
        return selection.get(selectionIndex);
    }
    
    public final void setProject(SpriteProject proj) {
        this.project = proj;
        
        if (proj != null) {
            setMulti0C64Color(proj.getMulti0Color(), false);
            setMulti1C64Color(proj.getMulti1Color(), false);
            setBgC64Color(proj.getBgColor(), false);
            overlayDistSpinner.setValue(proj.getOverlayDistance());
        }
        
        setSelection(null);
    }
    
    private void nextSpriteColor() {
        switch (editor.getCurrentSpriteColor()) {
            case SpriteColor:
                if (multi0ColorButton.isEnabled()) {
                    setCurrentSpriteColor(SpriteColor.Multi0Color);
                    break;
                }
            case Multi0Color:
                if (multi1ColorButton.isEnabled()) {
                    setCurrentSpriteColor(SpriteColor.Multi1Color);
                    break;
                }
            case Multi1Color:
                if (bgColorButton.isEnabled()) {
                    setCurrentSpriteColor(SpriteColor.BackgroundColor);
                    break;
                }
            default: //BackgroundColor
                break;
        }
    }
    
    private void prevSpriteColor() {
        switch (editor.getCurrentSpriteColor()) {
            case BackgroundColor:
                if (multi1ColorButton.isEnabled()) {
                    setCurrentSpriteColor(SpriteColor.Multi1Color);
                    break;
                }
            case Multi1Color:
                if (multi0ColorButton.isEnabled()) {
                    setCurrentSpriteColor(SpriteColor.Multi0Color);
                    break;
                }
            case Multi0Color:
                if (sprColorButton.isEnabled()) {
                    setCurrentSpriteColor(SpriteColor.SpriteColor);
                    break;
                }
            default: //SpriteColor
                break;
        }
    }
    
    private void setCurrentSpriteColor(SpriteColor color) {
        editor.setCurrentSpriteColor(color);
        
        switch (color) {
            case SpriteColor:
                sprColorButton.setSelected(true);
                colorPicker.setCurrentC64Color(sprColorLabel.getC64Color());
                break;
            case Multi0Color:
                multi0ColorButton.setSelected(true);
                colorPicker.setCurrentC64Color(multi0ColorLabel.getC64Color());
                break;
            case Multi1Color:
                multi1ColorButton.setSelected(true);
                colorPicker.setCurrentC64Color(multi1ColorLabel.getC64Color());
                break;
            default:
                bgColorButton.setSelected(true);
                colorPicker.setCurrentC64Color(bgColorLabel.getC64Color());
                break;
        }
    }
    
    private void setCurrentC64Color(C64Color color) {
        if (sprColorButton.isSelected())
            setSpriteC64Color(color);
        else if (multi0ColorButton.isSelected())
            setMulti0C64Color(color, true);
        else if (multi1ColorButton.isSelected())
            setMulti1C64Color(color, true);
        else
            setBgC64Color(color, true);
    }
    
    private void setSpriteC64Color(C64Color color) {
        editor.setSpriteC64Color(color);
        sprColorLabel.setC64Color(color);
        if (sprColorButton.isSelected())
            colorPicker.setCurrentC64Color(color);
    }
    
    private void setMulti0C64Color(C64Color color, boolean action) {
        project.setMulti0Color(color);
        editor.setMulti0C64Color(color);
        multi0ColorLabel.setC64Color(color);
        if (multi0ColorButton.isSelected())
            colorPicker.setCurrentC64Color(color);
        
        if (action)
            fireActionEvent();
    }
    
    private void setMulti1C64Color(C64Color color, boolean action) {
        project.setMulti1Color(color);
        editor.setMulti1C64Color(color);
        multi1ColorLabel.setC64Color(color);
        if (multi1ColorButton.isSelected())
            colorPicker.setCurrentC64Color(color);
        
        if (action)
            fireActionEvent();
    }
    
    private void setBgC64Color(C64Color color, boolean action) {
        project.setBgColor(color);
        editor.setBgC64Color(color);
        bgColorLabel.setC64Color(color);
        if (bgColorButton.isSelected())
            colorPicker.setCurrentC64Color(color);
        
        if (action)
            fireActionEvent();
    }
    
    public final void setSelection(Selection sel) {
        this.selection = sel;
        
        if (sel == null || sel.isEmpty()) {
            spriteChooserLabel.setText("none");
            
            editor.setEnabled(false);
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
            
            slideUpButton.setEnabled(false);
            slideDownButton.setEnabled(false);
            slideLeftButton.setEnabled(false);
            slideRightButton.setEnabled(false);
            flipVertButton.setEnabled(false);
            flipHorzButton.setEnabled(false);
            
            sprColorLabel.setEnabled(false);
            sprColorButton.setEnabled(false);
            multi0ColorButton.setEnabled(true);
            multi0ColorLabel.setEnabled(true);
            multi1ColorButton.setEnabled(true);
            multi1ColorLabel.setEnabled(true);
            if (sprColorButton.isSelected())
                setCurrentSpriteColor(SpriteColor.Multi0Color);
            
            multicolorCheckBox.setEnabled(false);
            overlayCheckBox.setEnabled(false);
        } else {
            editor.setEnabled(true);
            sprColorLabel.setEnabled(true);
            sprColorButton.setEnabled(true);
            
            slideUpButton.setEnabled(true);
            slideDownButton.setEnabled(true);
            slideLeftButton.setEnabled(true);
            slideRightButton.setEnabled(true);
            flipVertButton.setEnabled(true);
            flipHorzButton.setEnabled(true);
            
            multicolorCheckBox.setEnabled(true);
            overlayCheckBox.setEnabled(true);
            
            if (selectionIndex < 0 || selectionIndex > sel.size()-1)
                selectionIndex = 0;
            setSprite(selectionIndex);
        }
    }
    
    public void reload() {
        if (selection != null && !selection.isEmpty()) {
            if (selectionIndex < 0 || selectionIndex > selection.size()-1)
                selectionIndex = 0;
            setSprite(selectionIndex);
        }
    }
    
    private void setSprite(int index) {
        if (project != null) {
            editor.setSelection(selection, index);
            
            SpriteData sd = project.getMemoryData().get(selection.get(index));
            sprColorLabel.setC64Color(sd.getSpriteC64Color());
            if (sprColorButton.isSelected())
                colorPicker.setCurrentC64Color(sd.getSpriteC64Color());
            
            if (!sd.isMulticolor()) {
                multi0ColorButton.setEnabled(false);
                //multi0ColorLabel.setEnabled(false);
                multi1ColorButton.setEnabled(false);
                //multi1ColorLabel.setEnabled(false);
                if (multi0ColorButton.isSelected() || multi1ColorButton.isSelected())
                    setCurrentSpriteColor(SpriteColor.SpriteColor);
            } else {
                multi0ColorButton.setEnabled(true);
                multi0ColorLabel.setEnabled(true);
                multi1ColorButton.setEnabled(true);
                multi1ColorLabel.setEnabled(true);
            }
            
            multicolorCheckBox.setSelected(sd.isMulticolor());
            overlayCheckBox.setSelected(sd.isOverlay());
        }
        
        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < selection.size()-1);
        spriteChooserLabel.setText(String.format("%d of %d", index+1, 
                selection.size()));
    }
    
    private void fireActionEvent() {
        actionListeners.forEach(
                al -> al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null))
        );
    }
    
    private void firePreviewEvent() {
        previewListeners.forEach(
                al -> al.previewChanged(new PreviewEvent(this, PreviewEvent.REFRESH_REQUEST, null))
        );
    }
}
