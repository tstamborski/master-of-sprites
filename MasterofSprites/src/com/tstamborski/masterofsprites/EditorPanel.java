/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.SpriteProject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class EditorPanel extends JPanel {
    private final JPanel spriteChooserPanel, editPanel, colorChoosePanel;
    private final JPanel toolsPanel;
    private final JButton slideUpButton, slideDownButton, slideLeftButton, slideRightButton;
    private final JButton flipHorzButton, flipVertButton;
    private final JButton prevButton;
    private final JButton nextButton;
    private final JLabel spriteChooserLabel;
    private final SpriteEditor editor;
    private final JRadioButton sprColorButton, multi0ColorButton;
    private final JRadioButton multi1ColorButton, bgColorButton;
    private final ButtonGroup colorButtonGroup;
    private final C64ColorPicker colorPicker;
    
    private SpriteProject project;
    private ArrayList<Integer> selection;
    private int selectionIndex;
    
    public EditorPanel() {
        editor = new SpriteEditor(8);
        editor.setBorder(BorderFactory.createLoweredBevelBorder());
        
        spriteChooserLabel = new JLabel("none");
        spriteChooserLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        prevButton = new JButton(new ImageIcon(getClass().getResource("icons/rewind-backward16.png")));
        prevButton.addActionListener((ae)->setSprite(--selectionIndex));
        nextButton = new JButton(new ImageIcon(getClass().getResource("icons/rewind-forward16.png")));
        nextButton.addActionListener((ae)->setSprite(++selectionIndex));
        
        slideUpButton = new JButton(new ImageIcon(getClass().getResource("icons/dir-up16.png")));
        slideDownButton = new JButton(new ImageIcon(getClass().getResource("icons/dir-down16.png")));
        slideLeftButton = new JButton(new ImageIcon(getClass().getResource("icons/dir-left16.png")));
        slideRightButton = new JButton(new ImageIcon(getClass().getResource("icons/dir-right16.png")));
        flipHorzButton = new JButton(new ImageIcon(getClass().getResource("icons/flip-horz16.png")));
        flipVertButton = new JButton(new ImageIcon(getClass().getResource("icons/flip-vert16.png")));
        
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
        
        colorPicker = new C64ColorPicker();
        colorPicker.setBorder(BorderFactory.createLineBorder(Color.black));
        
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
        colorChoosePanel.setLayout(new GridLayout(4,1));
        colorChoosePanel.add(sprColorButton);
        colorChoosePanel.add(multi0ColorButton);
        colorChoosePanel.add(multi1ColorButton);
        colorChoosePanel.add(bgColorButton);
        colorChoosePanel.setMaximumSize(new Dimension(
                Short.MAX_VALUE, 
                colorChoosePanel.getPreferredSize().height));
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(spriteChooserPanel);
        add(editPanel);
        add(toolsPanel);
        add(colorChoosePanel);
        add(Box.createVerticalGlue());
        add(colorPicker);
        add(Box.createVerticalGlue());
        
        setProject(null);
        setSelection(null);
    }
    
    public SpriteEditor getSpriteEditor() {
        return editor;
    }
    
    public final void setProject(SpriteProject proj) {
        this.project = proj;
    }
    
    public final void setSelection(ArrayList<Integer> sel) {
        this.selection = sel;
        
        if (sel == null || sel.isEmpty()) {
            spriteChooserLabel.setText("none");
            
            editor.setEnabled(false);
            prevButton.setEnabled(false);
            spriteChooserLabel.setEnabled(false);
            nextButton.setEnabled(false);
        } else {
            editor.setEnabled(true);
            spriteChooserLabel.setEnabled(true);
            
            if (selectionIndex < 0 || selectionIndex > sel.size()-1)
                selectionIndex = 0;
            setSprite(selectionIndex);
        }
    }
    
    private void setSprite(int index) {
        if (project != null)
            editor.setSpriteData(project.getMemoryData().get(selection.get(index)));
        
        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < selection.size()-1);
        spriteChooserLabel.setText(String.format("%d of %d", index+1, 
                selection.size()));
    }
}
