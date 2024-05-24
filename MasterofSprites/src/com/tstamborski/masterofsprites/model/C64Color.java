/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites.model;

import java.io.Serializable;

/**
 *
 * @author Tobiasz
 */
public enum C64Color implements Serializable {
    Black,
    White,
    Red,
    Cyan,
    Purple,
    Green,
    Blue,
    Yellow,
    Orange,
    Brown,
    Pink,
    DarkGray,
    MiddleGray,
    LightGreen,
    LightBlue,
    LightGray;
    
    public static C64Color fromInteger(int i) {
        return values()[i & 0x0f];
    }
    
    private static final long serialVersionUID = 77145L;
}
