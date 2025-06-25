package com.pcb.ecosystem.core.mvc.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Simulation Run data model - Redis serializable
 * Represents a complete simulation run with all statistics
 */
public class SimulationRun {
    
    private Long runId;
    private String boardType;
    private int pcbsRun;
    private int totalProduced;
    private int totalFailed;
    private double successRate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
    
    private Map<String, Integer> stationFailures = new HashMap<>();
    private Map<String, Integer> defectFailures = new HashMap<>();
    
    public SimulationRun() {
        this.stationFailures = new HashMap<>();
        this.defectFailures = new HashMap<>();
        this.timestamp = LocalDateTime.now();
    }
    
    public SimulationRun(String boardType, int pcbsRun) {
        this();
        this.boardType = boardType;
        this.pcbsRun = pcbsRun;
    }
    
    public void calculateTotals() {
        this.totalFailed = stationFailures.values().stream().mapToInt(Integer::intValue).sum() +
                          defectFailures.values().stream().mapToInt(Integer::intValue).sum();
        this.totalProduced = pcbsRun - totalFailed;
        this.successRate = pcbsRun > 0 ? (double) totalProduced / pcbsRun * 100.0 : 0.0;
    }
    
    /**
     * Generate report text in EXACT capstone format
     */
    public String generateReportText() {
        StringBuilder report = new StringBuilder();
        
        report.append(String.format("PCB type: %s%n", boardType));
        report.append(String.format("PCBs run: %,d%n", pcbsRun));
        report.append(String.format("Timestamp: %s%n", 
            timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        report.append("\n");
        
        report.append("Station Failures\n");
        // Display in exact order from capstone requirements
        String[] stationOrder = {
            "Apply Solder Paste", "Place Components", "Reflow Solder", 
            "Optical Inspection", "Hand Soldering/Assembly", "Cleaning", 
            "Depanelization", "Test (ICT or Flying Probe)"
        };
        
        for (String station : stationOrder) {
            int failures = stationFailures.getOrDefault(station, 0);
            report.append(String.format("%s: %d%n", station, failures));
        }
        
        report.append("\nPCB Defect Failures\n");
        // Only the 4 stations that can detect defects
        String[] defectStations = {
            "Place Components", "Optical Inspection", 
            "Hand Soldering/Assembly", "Test (ICT or Flying Probe)"
        };
        
        for (String station : defectStations) {
            int failures = defectFailures.getOrDefault(station, 0);
            report.append(String.format("%s: %d%n", station, failures));
        }
        
        report.append("\nFinal Results\n");
        report.append(String.format("Total failed PCBs: %,d%n", totalFailed));
        report.append(String.format("Total PCBs produced: %,d%n", totalProduced));
        report.append(String.format("Success rate: %.2f%%%n", successRate));
        
        return report.toString();
    }
    
    // Getters and Setters
    public Long getRunId() { return runId; }
    public void setRunId(Long runId) { this.runId = runId; }
    
    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }
    
    public int getPcbsRun() { return pcbsRun; }
    public void setPcbsRun(int pcbsRun) { this.pcbsRun = pcbsRun; }
    
    public int getTotalProduced() { return totalProduced; }
    public void setTotalProduced(int totalProduced) { this.totalProduced = totalProduced; }
    
    public int getTotalFailed() { return totalFailed; }
    public void setTotalFailed(int totalFailed) { this.totalFailed = totalFailed; }
    
    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public Map<String, Integer> getStationFailures() { return stationFailures; }
    public void setStationFailures(Map<String, Integer> stationFailures) { this.stationFailures = stationFailures; }
    
    public Map<String, Integer> getDefectFailures() { return defectFailures; }
    public void setDefectFailures(Map<String, Integer> defectFailures) { this.defectFailures = defectFailures; }
}