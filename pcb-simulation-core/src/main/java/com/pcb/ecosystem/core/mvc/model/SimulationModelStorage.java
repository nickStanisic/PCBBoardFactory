package com.pcb.ecosystem.core.mvc.model;

import java.util.HashMap;
import java.util.Map;

//contains failure rates
public class SimulationModelStorage {
    
    private static final Map<String, BoardFailureRates> BOARD_CONFIGURATIONS = new HashMap<>();
    
    static {
        // EXACT rates from capstone requirements
        BOARD_CONFIGURATIONS.put("TestBoard", new BoardFailureRates(
            0.05,  
            0.10,   
            0.05,   
            0.10 
        ));
        
        BOARD_CONFIGURATIONS.put("SensorBoard", new BoardFailureRates(
            0.002,
            0.002,  
            0.004,  
            0.004 
        ));
        
        BOARD_CONFIGURATIONS.put("GatewayBoard", new BoardFailureRates(
            0.004, 
            0.004, 
            0.008, 
            0.008   
        ));
    }
    
    // Station failure rate: 0.2% for all stations and all boards
    private static final double STATION_FAILURE_RATE = 0.002;
    
    public BoardFailureRates getBoardFailureRates(String boardType) {
        return BOARD_CONFIGURATIONS.get(boardType);
    }
    
    public double getStationFailureRate() {
        return STATION_FAILURE_RATE;
    }
    
    public boolean validateBoardType(String boardType) {
        return BOARD_CONFIGURATIONS.containsKey(boardType);
    }
    
    public String[] getSupportedBoardTypes() {
        return BOARD_CONFIGURATIONS.keySet().toArray(new String[0]);
    }
}