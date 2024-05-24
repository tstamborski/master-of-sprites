/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tstamborski.masterofsprites.model;

import java.io.Serializable;

/**
 *
 * @author Tobiasz Stamborski <tstamborski@outlook.com>
 */
public class Time implements Serializable {
    private long seconds;

    public Time() {
        seconds = 0;
    }
    
    public void advance(int secondsCount) {
        this.seconds += secondsCount;
    }
    
    public int getSeconds() {
        return (int)(seconds % 60);
    }
    
    public int getMinutes() {
        return (int)((seconds / 60) % 60);
    }
    
    public int getHours() {
        return (int)(seconds / (60*60));
    }

    @Override
    public String toString() {
        return String.format("%dh %02dm %02ds", getHours(), getMinutes(), getSeconds());
    }
    
    
}
