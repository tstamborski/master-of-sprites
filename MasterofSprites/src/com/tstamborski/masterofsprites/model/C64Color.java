/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tstamborski.masterofsprites.model;

/**
 *
 * @author Tobiasz
 */
public enum C64Color {
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
}
