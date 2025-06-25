package com.pcb.ecosystem.core.mvc.view;

import com.pcb.ecosystem.core.mvc.controller.ViewController;
import com.pcb.ecosystem.core.mvc.model.SimulationRun;
import com.pcb.ecosystem.core.util.UIFormatter;
import java.util.Scanner;
import java.util.List;

/**
 * Simulation Query UI 
 * Allows users to search database of stored runs for display
 */
public class SimulationQueryUI {
    
    private final Scanner scanner;
    private final ViewController controller;
    private final UIFormatter formatter;
    
    public static class QueryResult {
        private final Long selectedRunId;
        
        public QueryResult(Long selectedRunId) {
            this.selectedRunId = selectedRunId;
        }
        
        public Long getSelectedRunId() { return selectedRunId; }
    }
    
    public SimulationQueryUI(Scanner scanner, ViewController controller) {
        this.scanner = scanner;
        this.controller = controller;
        this.formatter = new UIFormatter();
    }
    
    //try to display runs or give empty message
    public QueryResult displayAndGetSelection() {
        formatter.clearScreen();
        System.out.println(formatter.createHeader("Simulation Query UI - Browse Stored Runs"));
        
        List<SimulationRun> runs = controller.getRunStorage().getAllSimulationRuns();
        
        if (runs.isEmpty()) {
            displayEmptyMessage();
            return null;
        }
        
        displayRunsTable(runs);
        return getRunSelection(runs);
    }
    
    private void displayEmptyMessage() {
        System.out.println("No simulation runs found in database.");
        System.out.println("Run some simulations first using the Simulation Run UI.");
        System.out.println();
        System.out.print("Press Enter to return to main menu...");
        scanner.nextLine();
    }
    
    // prints out table format for data stored in database
    private void displayRunsTable(List<SimulationRun> runs) {
        System.out.printf("Found %d simulation runs in database:%n", runs.size());
        System.out.println(formatter.createSeparator(80));
        
        System.out.println(formatter.formatTableRow(
            "ID", "Board Type", "Runs", "Success Rate", "Date/Time"
        ));
        System.out.println(formatter.createSeparator(80));
        
        for (SimulationRun run : runs) {
            System.out.println(formatter.formatTableRow(
                run.getRunId().toString(),
                run.getBoardType(),
                String.format("%,d", run.getPcbsRun()),
                String.format("%.1f%%", run.getSuccessRate()),
                run.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm"))
            ));
        }
        
        System.out.println(formatter.createSeparator(80));
    }
    
    private QueryResult getRunSelection(List<SimulationRun> runs) {
        System.out.println("\nOptions:");
        System.out.println("• Enter Run ID to view detailed report");
        System.out.println("• Enter 'back' to return to main menu");
        System.out.print("Your choice: ");
        
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("back")) {
                return null;
            } else {
                try {
                    Long runId = Long.parseLong(input);
                    if (runExists(runs, runId)) {
                        return new QueryResult(runId);
                    } else {
                        System.out.print("Run ID not found. Please try again: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Invalid input. Enter Run ID or 'back': ");
                }
            }
        }
    }
    
    private boolean runExists(List<SimulationRun> runs, Long runId) {
        return runs.stream().anyMatch(run -> run.getRunId().equals(runId));
    }
}