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
public enum SpriteColor {
    BackgroundColor,
    Multi0Color,
    SpriteColor,
    Multi1Color;
    
    public static SpriteColor fromInteger(int i) {
        return values()[i & 0x03];
    }
}
