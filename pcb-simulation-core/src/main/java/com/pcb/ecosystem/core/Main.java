package com.pcb.ecosystem.core;

import com.pcb.ecosystem.core.board_hierarchy.*;
import com.pcb.ecosystem.core.simulation_control.Manager;

import com.pcb.ecosystem.core.factory.PCBBoardFactory; 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        
        SpringApplication.run(Main.class, args);
        
    
        // Create boards using Factory
        var sensorBoard = PCBBoardFactory.createSensorBoard();
        var testBoard = PCBBoardFactory.createTestBoard();
        var gatewayBoard = PCBBoardFactory.createGatewayBoard();
        
        Manager manager = new Manager(gatewayBoard, 1000);
        manager.sim();
        
        
        System.out.println("Server running at: http://localhost:8080");
    }
}