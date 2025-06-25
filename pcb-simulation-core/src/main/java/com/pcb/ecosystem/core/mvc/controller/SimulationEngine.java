package com.pcb.ecosystem.core.mvc.controller;

import com.pcb.ecosystem.core.factory.PCBBoardFactory;
import com.pcb.ecosystem.core.simulation_control.Manager;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.observers.AssemblyLineMonitor;
import com.pcb.ecosystem.core.mvc.model.*;

/**
 * Wrapper around existing simulation logic
 */
public class SimulationEngine {
    
    private final SimulationModelStorage modelStorage;
    
    public SimulationEngine(SimulationModelStorage modelStorage) {
        this.modelStorage = modelStorage;
    }
    
    // Execute simulation using existing Manager class 
    public SimulationRun executeSimulation(String boardType, int runCount) {
        try {
            System.out.printf("Setting up simulation: %s x %d%n", boardType, runCount);
            
            PCBBoard board = createBoardByType(boardType);
            Manager manager = new Manager(board, runCount);
            
            manager.sim();
            
            // Extract results from monitor
            return extractSimulationResults(manager, boardType, runCount);
            
        } catch (Exception e) {
            throw new RuntimeException("Simulation execution failed: " + e.getMessage(), e);
        }
    }
    
    private PCBBoard createBoardByType(String boardType) {
        switch (boardType) {
            case "SensorBoard":
                return PCBBoardFactory.createSensorBoard();
            case "TestBoard":
                return PCBBoardFactory.createTestBoard();
            case "GatewayBoard":
                return PCBBoardFactory.createGatewayBoard();
            default:
                throw new IllegalArgumentException("Unknown board type: " + boardType);
        }
    }
    
    private SimulationRun extractSimulationResults(Manager manager, String boardType, int runCount) {
        // Get monitor from manager
        AssemblyLineMonitor monitor = manager.getMonitor();
        
        return monitor.createSimulationRun();
    }
    
    public String[] getAvailableBoardTypes() {
        return modelStorage.getSupportedBoardTypes();
    }
}