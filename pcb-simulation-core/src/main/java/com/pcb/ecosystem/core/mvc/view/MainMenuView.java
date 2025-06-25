package com.pcb.ecosystem.core.mvc.view;

import com.pcb.ecosystem.core.mvc.controller.ViewController;
import com.pcb.ecosystem.core.util.UIFormatter;
import java.util.Scanner;

/**
 * Main Menu View - Navigation hub for the three required views
 */
public class MainMenuView {
    
    private final Scanner scanner;
    private final ViewController controller;
    private final UIFormatter formatter;
    
    public enum MenuChoice {
        SIMULATION_RUN, SIMULATION_QUERY, EXIT
    }
    
    public MainMenuView(Scanner scanner, ViewController controller) {
        this.scanner = scanner;
        this.controller = controller;
        this.formatter = new UIFormatter();
    }
    
    // runs the menu
    public MenuChoice displayAndGetChoice() {
        formatter.clearScreen();
        displayHeader();
        displayMenuOptions();
        return getUserMenuChoice();
    }
    
    private void displayHeader() {
        System.out.println(formatter.createHeader("PCB Simulation MVC System"));
        System.out.println("Printed Circuit Board Assembly Simulation");
        System.out.println("Three Views: Run UI | Query UI | Report UI");
        System.out.println();
    }
    
    private void displayMenuOptions() {
        System.out.println("Main Menu - Select a view");
        System.out.println(formatter.createSeparator(60));
        System.out.println("1. Simulation Run UI");
        System.out.println("   └─ Select board type, set run count, execute simulation");
        System.out.println();
        System.out.println("2. Simulation Query UI");
        System.out.println("   └─ Browse and search stored simulation runs");
        System.out.println();
        System.out.println("3. Exit Main Application");
        System.out.println(formatter.createSeparator(60));
        System.out.print("Enter your choice (1-3): ");
    }
    
    private MenuChoice getUserMenuChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1:
                        return MenuChoice.SIMULATION_RUN;
                    case 2:
                        return MenuChoice.SIMULATION_QUERY;
                    case 3:
                        return MenuChoice.EXIT;
                    default:
                        System.out.print("Please enter 1, 2, or 3: ");
                        continue;
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number (1-3): ");
            }
        }
    }
}