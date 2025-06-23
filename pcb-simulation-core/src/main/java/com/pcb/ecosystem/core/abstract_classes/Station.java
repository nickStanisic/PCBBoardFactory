package com.pcb.ecosystem.core.abstract_classes;

import java.util.Random;

/**
 * Abstract base class for all assembly line stations
 */
public abstract class Station {
    protected String stationName;
    protected Station next;
    protected boolean canDetectPCBDefects;
    protected Random random;
    
    public Station(String stationName, boolean canDetectPCBDefects) {
        this.stationName = stationName;
        this.canDetectPCBDefects = canDetectPCBDefects;
        this.random = new Random();
    }
    
    // Getters
    public String getStationName() {
        return stationName;
    }
    
    public Station getNext() {
        return next;
    }
    
    public boolean canDetectPCBDefects() {
        return canDetectPCBDefects;
    }
    
    // Setter for next station
    public void setNext(Station next) {
        this.next = next;
    }
    
    // Abstract method for station failure check
    public boolean checkStationFailure() {
        return random.nextDouble() < 0.002; 
    };
    
    @Override
    public String toString() {
        return stationName + " Station";
    }
}