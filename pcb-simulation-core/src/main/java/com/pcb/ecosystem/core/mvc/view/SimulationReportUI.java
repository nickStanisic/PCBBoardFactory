package com.pcb.ecosystem.core.mvc.view;

import com.pcb.ecosystem.core.mvc.controller.ViewController;
import com.pcb.ecosystem.core.mvc.model.SimulationRun;
import com.pcb.ecosystem.core.util.UIFormatter;
import java.util.Scanner;

/**
 * Simulation Report UI
 * Displays selected run in standard failure report format
 */
public class SimulationReportUI {
    
    private final Scanner scanner;
    private final ViewController controller;
    private final UIFormatter formatter;
    private Long currentRunId;
    
    public enum ReportAction {
        BACK_TO_QUERY, BACK_TO_MAIN, NEW_SIMULATION
    }
    
    public SimulationReportUI(Scanner scanner, ViewController controller) {
        this.scanner = scanner;
        this.controller = controller;
        this.formatter = new UIFormatter();
    }
    
    public void setCurrentRunId(Long runId) {
        this.currentRunId = runId;
    }
    
    public ReportAction displayAndGetAction() {
        if (currentRunId == null) {
            System.err.println("No run ID set for report view");
            return ReportAction.BACK_TO_MAIN;
        }
        
        SimulationRun run = controller.getRunStorage().getSimulationRun(currentRunId);
        if (run == null) {
            System.err.println("Run not found: " + currentRunId);
            return ReportAction.BACK_TO_QUERY;
        }
        
        displayDetailedReport(run);
        return getActionChoice();
    }
    
    private void displayDetailedReport(SimulationRun run) {
        formatter.clearScreen();
        System.out.println(formatter.createHeader("Simulation Report UI - Detailed Results"));
        
        System.out.println(run.generateReportText());
        
        System.out.println(formatter.createSeparator(60));
    }
    
    private ReportAction getActionChoice() {
        System.out.println("Report Options:");
        System.out.println("1. Back to Query UI");
        System.out.println("2. Back to Main Menu");
        System.out.println("3. Run New Simulation");
        System.out.print("Enter choice (1-4): ");
        
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        return ReportAction.BACK_TO_QUERY;
                    case 2:
                        return ReportAction.BACK_TO_MAIN;
                    case 3:
                        return ReportAction.NEW_SIMULATION;
                    default:
                        System.out.print("Please enter 1, 2, 3, or 4: ");
                        continue;
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number (1-4): ");
            }
        }
    }
    
}