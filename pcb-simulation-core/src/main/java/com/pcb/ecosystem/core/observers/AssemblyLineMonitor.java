package com.pcb.ecosystem.core.observers;

import com.pcb.ecosystem.core.interfaces.Observer;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.abstract_classes.Station;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * monitors assembly line events
 */
public class AssemblyLineMonitor implements Observer {
    
    private String monitorName;
    private String pcbType;
    private int pcbsRun;
    private int boardsCompleted;
    
    // Station failure tracking 
    private Map<String, Integer> stationFailures;
    
    // PCB defect tracking 
    private Map<String, Integer> pcbDefectFailures;
    
    // Station name mapping for consistent output
    private Map<String, String> stationDisplayNames;
    
    
    public AssemblyLineMonitor(String monitorName) {
        this.monitorName = monitorName;
        this.pcbType = "";
        this.pcbsRun = 0;
        this.boardsCompleted = 0;
        
        // Initialize tracking maps with ordered station names
        this.stationFailures = new LinkedHashMap<>();
        this.pcbDefectFailures = new LinkedHashMap<>();
        this.stationDisplayNames = new HashMap<>();
        
        initializeStationMaps();
    }
    
    private void initializeStationMaps() {
        // Initialize all stations with 0 counts to ensure they appear in output
        stationFailures.put("Apply Solder Paste", 0);
        stationFailures.put("Place Components", 0);
        stationFailures.put("Reflow Solder", 0);
        stationFailures.put("Optical Inspection", 0);
        stationFailures.put("Hand Soldering Assembly", 0);
        stationFailures.put("Cleaning", 0);
        stationFailures.put("Depanelization", 0);
        stationFailures.put("Test", 0);
        
        // Only defect detection stations for PCB defects
        pcbDefectFailures.put("Place Components", 0);
        pcbDefectFailures.put("Optical Inspection", 0);
        pcbDefectFailures.put("Hand Soldering Assembly", 0);
        pcbDefectFailures.put("Test", 0);
        
        // Map actual station names to display names
        stationDisplayNames.put("Apply Solder Paste", "Apply Solder Paste");
        stationDisplayNames.put("Place Components", "Place Components");
        stationDisplayNames.put("Reflow Solder", "Reflow Solder");
        stationDisplayNames.put("Optical Inspection", "Optical Inspection");
        stationDisplayNames.put("Hand Soldering Assembly", "Hand Soldering/Assembly");
        stationDisplayNames.put("Cleaning", "Cleaning");
        stationDisplayNames.put("Depanelization", "Depanelization");
        stationDisplayNames.put("Test", "Test (ICT or Flying Probe)");
    }
    
    public void setPcbType(String pcbType) {
        this.pcbType = pcbType;
    }
    
    public void setPcbsRun(int pcbsRun) {
        this.pcbsRun = pcbsRun;
    }
    
    @Override
    public void onDefectDetected(Station station, PCBBoard board) {
        String stationName = station.getStationName();
        pcbDefectFailures.put(stationName, pcbDefectFailures.getOrDefault(stationName, 0) + 1);
    }
    
    @Override
    public void onStationFailure(Station station, PCBBoard board) {
        String stationName = station.getStationName();
        stationFailures.put(stationName, stationFailures.getOrDefault(stationName, 0) + 1);
    }
    
    @Override
    public void onBoardCompleted(PCBBoard board) {
        boardsCompleted++;
    }
    
    // Method to print detailed statistics in the requested format
    public void printDetailedStatistics() {
        System.out.println("PCB type: " + pcbType);
        System.out.println("PCBs run: " + pcbsRun);
        System.out.println();
        
        // Station Failures
        System.out.println("Station Failures");
        for (Map.Entry<String, Integer> entry : stationFailures.entrySet()) {
            String displayName = stationDisplayNames.getOrDefault(entry.getKey(), entry.getKey());
            System.out.println(displayName + ": " + entry.getValue());
        }
        System.out.println();
        
        // PCB Defect Failures
        System.out.println("PCB Defect Failures");
        for (Map.Entry<String, Integer> entry : pcbDefectFailures.entrySet()) {
            if (entry.getValue() > 0) { // Only show stations with defects
                String displayName = stationDisplayNames.getOrDefault(entry.getKey(), entry.getKey());
                System.out.println(displayName + " " + entry.getValue());
            }
        }
        System.out.println();
        
        // Final Results
        int totalFailedPcbs = getTotalFailedPcbs();
        int totalPcbsProduced = pcbsRun - totalFailedPcbs;
        
        System.out.println("Final Results");
        System.out.println("Total failed PCBs: " + totalFailedPcbs);
        System.out.println("Total PCBs produced: " + totalPcbsProduced);
    }
    
    // Calculate total failed PCBs (station failures + defect failures)
    public int getTotalFailedPcbs() {
        int totalStationFailures = stationFailures.values().stream().mapToInt(Integer::intValue).sum();
        int totalDefectFailures = pcbDefectFailures.values().stream().mapToInt(Integer::intValue).sum();
        return totalStationFailures + totalDefectFailures;
    }
    
}