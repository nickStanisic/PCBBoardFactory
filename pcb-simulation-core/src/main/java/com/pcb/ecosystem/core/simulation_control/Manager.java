package com.pcb.ecosystem.core.simulation_control;

import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.simulation_control.AssemblyLine;
import com.pcb.ecosystem.core.observers.AssemblyLineMonitor;

/**
 * Manager coordinates the simulation of PCB production
 * Creates assembly line, monitor, and runs the simulation
 */
public class Manager {
    
    private PCBBoard board;
    private int numberOfIterations;
    private AssemblyLine assemblyLine;
    private AssemblyLineMonitor monitor;
    
    public Manager(PCBBoard boardTemplate, int numberOfIterations) {
        this.board = boardTemplate;
        this.numberOfIterations = numberOfIterations;
        
        // Create assembly line and monitor
        this.assemblyLine = new AssemblyLine();
        this.monitor = new AssemblyLineMonitor("Production Monitor");
        
        // Set up monitor and pair assembly line/monitor relationship
        this.monitor.setPcbType(board.toString());
        this.monitor.setPcbsRun(numberOfIterations);
        this.assemblyLine.addObserver(monitor);
    }
    
    /**
     * Run the simulation for the specified number of iterations
     */
    public void sim() {
        System.out.println("Starting simulation for " + numberOfIterations + " " + board.toString() + " boards");
        System.out.println("=".repeat(80));
        
        for (int i = 1; i <= numberOfIterations; i++) {
            // Process board 
            assemblyLine.processBoard(board);

            // could update this to have unique board each iteration with ID
            
        }
        
        // Get final report from monitor
        monitor.printDetailedStatistics();
    }

    public AssemblyLineMonitor getMonitor() {
        return monitor;
    }
    
}