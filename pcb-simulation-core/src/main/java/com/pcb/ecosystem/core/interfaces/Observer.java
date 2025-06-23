package com.pcb.ecosystem.core.interfaces;

import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.abstract_classes.Station;

/**
 * Observer interface for the Observer pattern
 * Objects implementing this interface can observe events from Subjects
 */
public interface Observer {
    
    /**
     * Called when a PCB defect is detected at a station
     * @param station The station where the defect was detected
     * @param board The board with the defect
     */
    void onDefectDetected(Station station, PCBBoard board);
    
    /**
     * Called when a station failure occurs
     * @param station The station that failed
     * @param board The board being processed when failure occurred
     */
    void onStationFailure(Station station, PCBBoard board);
    
    /**
     * Called when a board completes the entire assembly line
     * @param board The board that completed the assembly line
     */
    void onBoardCompleted(PCBBoard board);
}