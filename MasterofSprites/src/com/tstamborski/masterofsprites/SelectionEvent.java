/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import java.awt.AWTEvent;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class SelectionEvent extends AWTEvent {
    private final Selection selection;
    
    public SelectionEvent(Object source, Selection selection) {
        super(source, 0);
        this.selection = selection;
    }

    public Selection getSelection() {
        return selection;
    }
}
