/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites.gui;

import com.tstamborski.masterofsprites.model.MemoryData;
import com.tstamborski.masterofsprites.model.SpriteProject;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class MemoryPanel extends JPanel {
    private final MemoryView memoryView;
    private final JSpinner quantitySpinner;
    private final JCheckBox gridCheckBox;
    private final JLabel quantityLabel;
    
    private final JScrollPane scrollPane;
    private final JPanel innerPanel;
    private final JPanel southPanel;

    public MemoryPanel() {
        super(new BorderLayout());
        
        memoryView = new MemoryView(2, 8);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(0,
                0, MemoryData.MAX_SIZE, 1));
        gridCheckBox = new JCheckBox("Grid: ");
        gridCheckBox.setMnemonic('G');
        gridCheckBox.setHorizontalTextPosition(JCheckBox.LEFT);
        quantityLabel = new JLabel("Quantity: ");
        quantityLabel.setLabelFor(quantitySpinner);
        quantityLabel.setDisplayedMnemonic('Q');
        
        
        scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        innerPanel = new JPanel(new GridBagLayout());
        innerPanel.add(memoryView);
        innerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        scrollPane.setViewportView(innerPanel);
        
        southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(quantityLabel);
        southPanel.add(quantitySpinner);
        southPanel.add(gridCheckBox);
        
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        
        memoryView.addActionListener(ae->{
            innerPanel.revalidate();
            quantitySpinner.setValue(memoryView.getQuantity());
            gridCheckBox.setSelected(memoryView.isGrid());
        });
        quantitySpinner.addChangeListener(e->memoryView.setQuantity(
                (Integer)quantitySpinner.getValue()));
        gridCheckBox.addActionListener(e->memoryView.setGrid(gridCheckBox.isSelected()));
    }

    public void reload() {
        memoryView.reload();
        quantitySpinner.setValue(memoryView.getProject().getMemoryData().size());
        innerPanel.revalidate();
    }
    
    public void setProject(SpriteProject project) {
        memoryView.setProject(project);
        quantitySpinner.setValue(project.getMemoryData().size());
        innerPanel.revalidate();
    }
    
    public MemoryView getMemoryView() {
        return memoryView;
    }
    
}
