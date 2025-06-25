package com.pcb.ecosystem.core.util;

/**
 * UI Formatter utility for consistent console output formatting
 */
public class UIFormatter {
    
    public void clearScreen() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }
    
    public String createHeader(String title) {
        int width = Math.max(title.length() + 6, 60);
        StringBuilder sb = new StringBuilder();
        
        sb.append("╔").append("═".repeat(width - 2)).append("╗\n");
        sb.append("║ ").append(title);
        sb.append(" ".repeat(width - title.length() - 3)).append("║\n");
        sb.append("╚").append("═".repeat(width - 2)).append("╝");
        
        return sb.toString();
    }
    
    public String createSeparator(int length) {
        return "─".repeat(length);
    }
    
    public String formatTableRow(String... columns) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            sb.append(String.format("%-15s", columns[i]));
            if (i < columns.length - 1) {
                sb.append("│ ");
            }
        }
        return sb.toString();
    }
}