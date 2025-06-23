package com.pcb.ecosystem.core;

import com.pcb.ecosystem.core.board_hierarchy.*;
import com.pcb.ecosystem.core.simulation_control.Manager;

public class Main {
    public static void main(String[] args) {
        // Create a board templates
        SensorBoard sensorBoardTemplate = new SensorBoard();
        TestBoard testBoardTemplate = new TestBoard();
        GatewayBoard gatewayBoardTemplate = new GatewayBoard();
        
        // Create manager with 1000 iterations and inject template of choice
        Manager manager = new Manager(gatewayBoardTemplate, 1000);
        
        // Run simulation
        manager.sim();
    }
    
}