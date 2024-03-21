/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import java.awt.AWTEvent;
import java.util.ArrayList;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SelectionEvent extends AWTEvent {
    private final ArrayList<Integer> selection;
    
    public SelectionEvent(Object source, ArrayList<Integer> selection) {
        super(source, 0);
        this.selection = selection;
    }

    public ArrayList<Integer> getSelection() {
        return selection;
    }
}
