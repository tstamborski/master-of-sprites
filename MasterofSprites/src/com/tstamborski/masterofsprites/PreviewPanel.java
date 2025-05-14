/*
 * The MIT License
 *
 * Copyright 2024 Tobiasz Stamborski <tstamborski@outlook.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tstamborski.masterofsprites;

import com.tstamborski.masterofsprites.model.SpriteProject;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class PreviewPanel extends JPanel {
    private final JPanel centralPanel, northPanel, southPanel;
    private final JScrollPane scrollPane;
    
    private final SpritePreview preview;
    private final JLabel arrangementLabel, zoomLabel;
    private final JComboBox arrangementBox;
    private final ArrangementComboBoxModel arrangementModel;
    private final JSpinner zoomSpinner;
    private final JToggleButton lockButton, pingpongButton;
    
    private final JButton nextButton, prevButton, stopButton;
    private final JToggleButton playButton, pauseButton;
    private final JSpinner frameCountSpinner, frameDelaySpinner;
    private final JLabel frameCountLabel, frameDelayLabel;
    
    private SpriteProject project;
    private ArrayList<Integer> requestedSelection, currentSelection;
    private Timer timer;
    
    private boolean directionFlag;
    
    public PreviewPanel() {
        arrangementBox = new JComboBox(arrangementModel = new ArrangementComboBoxModel());
        arrangementBox.setEditable(false);
        arrangementBox.setMaximumSize(arrangementBox.getMinimumSize());
        zoomSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 16, 1));
        zoomSpinner.setMaximumSize(zoomSpinner.getMinimumSize());
        arrangementLabel = new JLabel(" Arrangement: ");
        arrangementLabel.setDisplayedMnemonic(KeyEvent.VK_A);
        arrangementLabel.setLabelFor(arrangementBox);
        zoomLabel = new JLabel(" Zoom: ");
        zoomLabel.setDisplayedMnemonic(KeyEvent.VK_Z);
        zoomLabel.setLabelFor(zoomSpinner);
        pingpongButton = new JToggleButton(
                new ImageIcon(getClass().getResource("icons/pingpong16.png"))
        );
        lockButton = new JToggleButton(
                new ImageIcon(getClass().getResource("icons/lock-open16.png"))
        );
        lockButton.setSelectedIcon(new ImageIcon(getClass().getResource("icons/lock16.png")));
        lockButton.setMnemonic(KeyEvent.VK_BACK_QUOTE);
        
        prevButton = new JButton(new ImageIcon(getClass().getResource("icons/prev16.png")));
        playButton = new JToggleButton(new ImageIcon(getClass().getResource("icons/play16.png")));
        pauseButton = new JToggleButton(new ImageIcon(getClass().getResource("icons/pause16.png")));
        stopButton = new JButton(new ImageIcon(getClass().getResource("icons/stop16.png")));
        nextButton = new JButton(new ImageIcon(getClass().getResource("icons/next16.png")));
        frameCountSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 16, 1));
        frameCountSpinner.setMaximumSize(frameCountSpinner.getMinimumSize());
        frameDelaySpinner = new JSpinner(new SpinnerNumberModel(100, 20, 2000, 20));
        frameDelaySpinner.setMaximumSize(frameDelaySpinner.getMinimumSize());
        frameCountLabel = new JLabel(" Count: ");
        frameCountLabel.setDisplayedMnemonic(KeyEvent.VK_C);
        frameCountLabel.setLabelFor(frameCountSpinner);
        frameDelayLabel = new JLabel(" Delay: ");
        frameDelayLabel.setDisplayedMnemonic(KeyEvent.VK_D);
        frameDelayLabel.setLabelFor(frameDelaySpinner);
        
        preview = new SpritePreview();
        preview.setFrameCount(4);
        
        centralPanel = new JPanel();
        centralPanel.setLayout(new GridBagLayout());
        centralPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        centralPanel.add(preview);
        
        scrollPane = new JScrollPane(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        scrollPane.setViewportView(centralPanel);
        
        northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.add(arrangementLabel);
        northPanel.add(arrangementBox);
        northPanel.add(zoomLabel);
        northPanel.add(zoomSpinner);
        northPanel.add(Box.createHorizontalGlue());
        northPanel.add(pingpongButton);
        northPanel.add(lockButton);
        
        southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        southPanel.add(prevButton);
        southPanel.add(playButton);
        southPanel.add(pauseButton);
        southPanel.add(stopButton);
        southPanel.add(nextButton);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(frameCountLabel);
        southPanel.add(frameCountSpinner);
        southPanel.add(frameDelayLabel);
        southPanel.add(frameDelaySpinner);
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        
        currentSelection = new ArrayList<>();
        
        timer = new Timer(100, ae->playHandler());
        frameDelaySpinner.addChangeListener(che->timer.setDelay((Integer)frameDelaySpinner.getValue()));
        frameCountSpinner.addChangeListener(che->{
            preview.setFrameCount((Integer)frameCountSpinner.getValue());
            preview.reload();
        });
        arrangementBox.addActionListener(ae->{
            Arrangement a = (Arrangement)arrangementBox.getSelectedItem();
            if (a != null && a.size() <= currentSelection.size()) {
                preview.setArrangement(a);
                preview.reload();
            }
        });
        zoomSpinner.addChangeListener(che->{
            preview.setZoom((Integer)zoomSpinner.getValue());
            preview.reload();
        });
        
        lockButton.addActionListener(ae->setSelection(requestedSelection));
        playButton.addActionListener(ae->play());
        pauseButton.addActionListener(ae->pause());
        stopButton.addActionListener(ae->stop());
        nextButton.addActionListener(ae->nextFrame());
        prevButton.addActionListener(ae->prevFrame());
    }
    
    public void setProject(SpriteProject p) {
        this.project = p;
        
        lockButton.setSelected(false);
        stop();
        
        preview.setProject(this.project);
        setSelection(null);
        preview.reload();
    }
    
    public void setSelection(ArrayList<Integer> s) {
        requestedSelection = s;
        
        if (!lockButton.isSelected()) {
            currentSelection.clear();
            if (requestedSelection != null)
                requestedSelection.forEach(i->currentSelection.add(i));
            
            if (currentSelection == null)
                arrangementModel.setSelectionSize(0);
            else
                arrangementModel.setSelectionSize(currentSelection.size());
            
            preview.setSelection(currentSelection);
            preview.reload();
        }
    }
    
    public void reload() {
        preview.reload();
    }
    
    public void play() {
        pauseButton.setSelected(false);
        playButton.setSelected(true);
        
        timer.start();
    }
    
    public void pause() {
        playButton.setSelected(false);
        pauseButton.setSelected(true);
        
        timer.stop();
    }
    
    public void stop() {
        playButton.setSelected(false);
        pauseButton.setSelected(false);
        
        timer.stop();
        preview.setFrameIndex(0);
    }
    
    public void nextFrame() {
        if (preview.getFrameIndex() < preview.getFrameCount() - 1)
            preview.nextFrame();
    }
    
    public void prevFrame() {
        if (preview.getFrameIndex() > 0)
            preview.prevFrame();
    }
    
    protected void playHandler() {
        if (!pingpongButton.isSelected()) {
            preview.nextFrame();
        } else {
            int frame = preview.getFrameIndex();
            int frame_count = preview.getFrameCount();
            
            if (frame == 0)
                directionFlag = false;
            if (frame == frame_count - 1)
                directionFlag = true;
            
            if (!directionFlag)
                preview.nextFrame();
            else
                preview.prevFrame();
        }
    }
}