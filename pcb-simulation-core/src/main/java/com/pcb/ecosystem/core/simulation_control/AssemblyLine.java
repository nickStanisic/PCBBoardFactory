package com.pcb.ecosystem.core.simulation_control;

import com.pcb.ecosystem.core.interfaces.Subject;
import com.pcb.ecosystem.core.interfaces.Observer;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.abstract_classes.Station;
import com.pcb.ecosystem.core.abstract_classes.PCBDefectStation;
import com.pcb.ecosystem.core.station_hierarchy.non_defect_stations.ApplySolderPasteStation;
import java.util.ArrayList;
import java.util.List;

/**
 * AssemblyLine manages the flow of PCB boards through all stations
 * Implements Subject to notify observers of events
 */
public class AssemblyLine implements Subject {
    
    private List<Observer> observers;
    private Station firstStation;
    
    public AssemblyLine() {
        this.observers = new ArrayList<>();
        this.firstStation = new ApplySolderPasteStation();
    }
    
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers() {
    }
    
    /**
     * Process a board through the entire assembly line
     * @param board The PCB board to process
     */
    public void processBoard(PCBBoard board) {
        Station currentStation = firstStation;
        
        while (currentStation != null) {
            // Check for station failure
            if (currentStation.checkStationFailure()) {
                notifyStationFailure(currentStation, board);
                return; 
            }
            
            // Check for PCB defects 
            if (currentStation.canDetectPCBDefects()) {
                PCBDefectStation defectStation = (PCBDefectStation) currentStation;
                if (defectStation.calculateDefect(board)) {
                    notifyDefectDetected(currentStation, board);
                    return; 
                }
            }
            
            // Station processing successful - move to next station
            currentStation = currentStation.getNext();
        }
        
        // If we reach here, board completed successfully
        notifyBoardCompleted(board);
    }
    
    // Notification methods for observers
    private void notifyDefectDetected(Station station, PCBBoard board) {
        for (Observer observer : observers) {
            observer.onDefectDetected(station, board);
        }
    }
    
    private void notifyStationFailure(Station station, PCBBoard board) {
        for (Observer observer : observers) {
            observer.onStationFailure(station, board);
        }
    }
    
    private void notifyBoardCompleted(PCBBoard board) {
        for (Observer observer : observers) {
            observer.onBoardCompleted(board);
        }
    }
}