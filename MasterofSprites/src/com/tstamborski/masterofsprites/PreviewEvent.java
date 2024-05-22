/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites;

import java.awt.event.ActionEvent;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class PreviewEvent extends ActionEvent {
    public static int REFRESH_REQUEST = ActionEvent.ACTION_FIRST;
    
    public PreviewEvent(Object source, int id, String command) {
        super(source, id, command);
    }
}
