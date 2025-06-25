package com.pcb.ecosystem.core.mvc.controller;

import com.pcb.ecosystem.core.mvc.view.*;

import com.pcb.ecosystem.core.mvc.model.SimulationModelStorage;
import com.pcb.ecosystem.core.mvc.model.SimulationRun;
import com.pcb.ecosystem.core.mvc.model.SimulationRunStorage;
import com.pcb.ecosystem.core.mvc.view.MainMenuView;
import com.pcb.ecosystem.core.mvc.view.SimulationQueryUI;
import com.pcb.ecosystem.core.mvc.view.SimulationReportUI;
import com.pcb.ecosystem.core.mvc.view.SimulationRunUI;

import com.pcb.ecosystem.core.mvc.model.*;
import java.util.Scanner;
import java.util.List;

/**
 * MVC Controller - Central coordinator for all views and business logic
 * Handles navigation between the three required views
 */
public class ViewController {
    
    // Models
    private final SimulationRunStorage runStorage;
    private final SimulationModelStorage modelStorage;
    private final SimulationEngine simulationEngine;
    
    // Views
    private final MainMenuView mainMenuView;
    private final SimulationRunUI simulationRunUI;
    private final SimulationQueryUI simulationQueryUI;
    private final SimulationReportUI simulationReportUI;
    
    // Application state
    private final Scanner scanner;
    private boolean running;
    private String currentView;
    
    public ViewController(Scanner scanner, SimulationRunStorage runStorage, 
                         SimulationModelStorage modelStorage) {
        this.scanner = scanner;
        this.runStorage = runStorage;
        this.modelStorage = modelStorage;
        this.simulationEngine = new SimulationEngine(modelStorage);
        this.running = true;
        this.currentView = "MAIN_MENU";
        
        // Initialize Views
        this.mainMenuView = new MainMenuView(scanner, this);
        this.simulationRunUI = new SimulationRunUI(scanner, this);
        this.simulationQueryUI = new SimulationQueryUI(scanner, this);
        this.simulationReportUI = new SimulationReportUI(scanner, this);
    }
    
    
    //S tart the MVC application
    public void startApplication() {
        System.out.println("Starting PCB Simulation MVC Application");
        
        while (running) {
            try {
                handleCurrentView();
            } catch (Exception e) {
                System.err.println("Error in view: " + e.getMessage());
                pauseForUser();
            }
        }
    }
    
    /**
     * Handle the current view based on application state
     */
    private void handleCurrentView() {
        switch (currentView) {
            case "MAIN_MENU":
                handleMainMenu();
                break;
            case "SIMULATION_RUN":
                handleSimulationRunView();
                break;
            case "SIMULATION_QUERY":
                handleSimulationQueryView();
                break;
            case "SIMULATION_REPORT":
                handleSimulationReportView();
                break;
            case "EXIT":
                handleExit();
                break;
            default:
                System.err.println("Unknown view: " + currentView);
                navigateToView("MAIN_MENU");
                break;
        }
    }
    
    private void handleMainMenu() {
        MainMenuView.MenuChoice choice = mainMenuView.displayAndGetChoice();
        switch (choice) {
            case SIMULATION_RUN:
                navigateToView("SIMULATION_RUN");
                break;
            case SIMULATION_QUERY:
                navigateToView("SIMULATION_QUERY");
                break;
            case EXIT:
                navigateToView("EXIT");
                break;
        }
    }
    
    private void handleSimulationRunView() {
        SimulationRunUI.RunRequest request = simulationRunUI.displayAndGetRunRequest();
        if (request == null) {
            navigateToView("MAIN_MENU");
            return;
        }
        
        if (validateRunParameters(request.getBoardType(), request.getRunCount())) {
            executeSimulation(request);
        } else {
            System.out.println("Invalid simulation parameters");
            pauseForUser();
        }
    }
    
    private void handleSimulationQueryView() {
        SimulationQueryUI.QueryResult result = simulationQueryUI.displayAndGetSelection();
        if (result == null) {
            navigateToView("MAIN_MENU");
        } else if (result.getSelectedRunId() != null) {
            navigateToReportView(result.getSelectedRunId());
        }
    }
    
    private void handleSimulationReportView() {
        SimulationReportUI.ReportAction action = simulationReportUI.displayAndGetAction();
        switch (action) {
            case BACK_TO_QUERY:
                navigateToView("SIMULATION_QUERY");
                break;
            case BACK_TO_MAIN:
                navigateToView("MAIN_MENU");
                break;
            case NEW_SIMULATION:
                navigateToView("SIMULATION_RUN");
                break;
        }
    }
    
    // update view
    public void navigateToView(String viewName) {
        this.currentView = viewName;
    }
    
    // change views to report
    public void navigateToReportView(Long runId) {
        simulationReportUI.setCurrentRunId(runId);
        navigateToView("SIMULATION_REPORT");
    }
    
    // run simulation
    private void executeSimulation(SimulationRunUI.RunRequest request) {
        try {
            System.out.println("Executing simulation...");
            
            SimulationRun result = simulationEngine.executeSimulation(
                request.getBoardType(), 
                request.getRunCount()
            );
            
            Long runId = runStorage.saveSimulationRun(result);
            result.setRunId(runId);
            
            System.out.printf("Simulation completed! Run ID: %d%n", runId);
            
            String choice = askUserNextAction();
            String lowerChoice = choice.toLowerCase();
            
            if (lowerChoice.equals("view")) {
                navigateToReportView(runId);
            } else if (lowerChoice.equals("new")) {
                navigateToView("SIMULATION_RUN");
            } else if (lowerChoice.equals("query")) {
                navigateToView("SIMULATION_QUERY");
            } else {
                navigateToView("MAIN_MENU");
            }
            
        } catch (Exception e) {
            System.err.println("Simulation failed: " + e.getMessage());
            pauseForUser();
        }
    }
    
    private String askUserNextAction() {
        System.out.println("\nWhat would you like to do next?");
        System.out.println("1. View this report (view)");
        System.out.println("2. Run new simulation (new)");
        System.out.println("3. Browse all reports (query)");
        System.out.println("4. Main menu (menu)");
        System.out.print("Enter choice: ");
        return scanner.nextLine().trim();
    }
    
    private boolean validateRunParameters(String boardType, int runCount) {
        return modelStorage.validateBoardType(boardType) 
            && runCount > 0 
            && runCount <= 10000;
    }
    
    private void handleExit() {
        System.out.println("Exiting...");
        running = false;
    }
    
    private void pauseForUser() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
    
    // Getters for views
    public SimulationRunStorage getRunStorage() { return runStorage; }
    public SimulationModelStorage getModelStorage() { return modelStorage; }
}
