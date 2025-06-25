package com.pcb.ecosystem.core;

import com.pcb.ecosystem.core.mvc.controller.ViewController;
import com.pcb.ecosystem.core.mvc.model.*;

import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=".repeat(60));
        System.out.println("PCB Assembly Simulation System");
        System.out.println("MVC Interface");
        System.out.println("=".repeat(60));
        
        try {
            // Initialize storage systems
            SimulationRunStorage runStorage = new SimulationRunStorage();
            SimulationModelStorage modelStorage = new SimulationModelStorage();
            
            // Start MVC application
            ViewController controller = new ViewController(scanner, runStorage, modelStorage);
            controller.startApplication();
            
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("\nThank you for using PCB Simulation System!");
            scanner.close();
        }
    }
}