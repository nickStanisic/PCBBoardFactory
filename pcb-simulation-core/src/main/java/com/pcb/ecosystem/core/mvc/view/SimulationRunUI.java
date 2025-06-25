package com.pcb.ecosystem.core.mvc.view;

import com.pcb.ecosystem.core.mvc.controller.ViewController;
import com.pcb.ecosystem.core.util.UIFormatter;
import java.util.Scanner;

/**
 * Simulation Run UI
 * Allows users to select board type, run count, and initiate simulation
 */
public class SimulationRunUI {
    
    private final Scanner scanner;
    private final ViewController controller;
    private final UIFormatter formatter;
    
    public static class RunRequest {
        private final String boardType;
        private final int runCount;
        
        public RunRequest(String boardType, int runCount) {
            this.boardType = boardType;
            this.runCount = runCount;
        }
        
        public String getBoardType() { return boardType; }
        public int getRunCount() { return runCount; }
    }
    
    public SimulationRunUI(Scanner scanner, ViewController controller) {
        this.scanner = scanner;
        this.controller = controller;
        this.formatter = new UIFormatter();
    }
    
    public RunRequest displayAndGetRunRequest() {
        formatter.clearScreen();
        System.out.println(formatter.createHeader("Simulation Run UI"));
        
        String boardType = getBoardTypeSelection();
        if (boardType == null) return null;
        
        int runCount = getRunCountSelection();
        if (runCount == 0) return null;
        
        if (confirmSelection(boardType, runCount)) {
            return new RunRequest(boardType, runCount);
        }
        
        return null;
    }
    
    // presents board types
    private String getBoardTypeSelection() {
        String[] boardTypes = controller.getModelStorage().getSupportedBoardTypes();
        
        System.out.println("Available Board Types:");
        System.out.println(formatter.createSeparator(50));
        
        // Display with failure rate information
        for (int i = 0; i < boardTypes.length; i++) {
            String description = getBoardDescription(boardTypes[i]);
            System.out.printf("%d. %s%n", i + 1, description);
        }
        
        System.out.println("0. Return to Main Menu");
        System.out.println(formatter.createSeparator(50));
        System.out.print("Select board type: ");
        
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                
                if (choice == 0) return null;
                if (choice >= 1 && choice <= boardTypes.length) {
                    return boardTypes[choice - 1];
                }
                System.out.print("Invalid choice. Please try again: ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
    
    // gives descriptions for each board type
    private String getBoardDescription(String boardType) {
        switch (boardType) {
            case "TestBoard":
                return "Test Board      - High failure rates (5-10%) - Testing purposes";
            case "SensorBoard":
                return "Sensor Board    - Low failure rates (0.2-0.4%) - High reliability";
            case "GatewayBoard":
                return "Gateway Board   - Medium failure rates (0.4-0.8%) - Networking";
            default:
                return boardType;
        }
    }
    
    // gets number of runs from user
    private int getRunCountSelection() {
        System.out.println("\nSimulation Run Configuration:");
        System.out.println("Enter number of PCB runs (1-10000, or 0 to cancel): ");
        System.out.print("Number of runs: ");
        
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int count = Integer.parseInt(input);
                
                if (count == 0) return 0;
                if (count >= 1 && count <= 10000) return count;
                System.out.print("Please enter 1-10000 (or 0 to cancel): ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
    
    //confirms selections before running
    private boolean confirmSelection(String boardType, int runCount) {
        System.out.println();
        System.out.println(formatter.createSeparator(50));
        System.out.println("SIMULATION CONFIRMATION");
        System.out.println(formatter.createSeparator(50));
        System.out.printf("Board Type: %s%n", boardType);
        System.out.printf("Run Count: %,d%n", runCount);
        
        // Estimate time
        double estimatedSeconds = runCount * 0.001;
        System.out.printf("Estimated Time: %.1f seconds%n", estimatedSeconds);
        System.out.println(formatter.createSeparator(50));
        System.out.print("Proceed with simulation? (y/n): ");
        
        while (true) {
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y") || response.equals("yes")) return true;
            if (response.equals("n") || response.equals("no")) return false;
            System.out.print("Please enter 'y' or 'n': ");
        }
    }
}