package com.pcb.ecosystem.core.observers;

import com.pcb.ecosystem.core.interfaces.Observer;
import com.pcb.ecosystem.core.mvc.model.SimulationRun;
import com.pcb.ecosystem.core.abstract_classes.Station;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AssemblyLineMonitor implements Observer {
    
    // Your existing fields
    private String monitorName;
    private String pcbType;
    private int pcbsRun;
    private int boardsCompleted;
    private Map<String, Integer> stationFailures;
    private Map<String, Integer> pcbDefectFailures;
    private Map<String, String> stationDisplayNames;
    
    // Static fields for web access and Redis
    private static Map<String, Object> latestReportData = new HashMap<>();
    private static JedisPool jedisPool;
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    // Constructor
    public AssemblyLineMonitor(String monitorName) {
        this.monitorName = monitorName;
        this.stationFailures = new HashMap<>();
        this.pcbDefectFailures = new HashMap<>();
        this.stationDisplayNames = new HashMap<>();
        setupStationDisplayNames();
    }
    
    public static void setApplicationContext(Object context) {
        // Initialize Redis connection
        try {
            if (jedisPool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(10);
                jedisPool = new JedisPool(config, "localhost", 6379);
                
                // Test connection
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.ping();
                    System.out.println("✅ Redis connection established");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️  Redis not available: " + e.getMessage());
        }
    }
    
    // Setters for Manager to use
    public void setPcbType(String pcbType) {
        this.pcbType = pcbType;
    }
    
    public void setPcbsRun(int pcbsRun) {
        this.pcbsRun = pcbsRun;
    }
    
    // Observer methods
    @Override
    public void onDefectDetected(Station station, PCBBoard board) {
        String stationName = station.getStationName();
        pcbDefectFailures.put(stationName, pcbDefectFailures.getOrDefault(stationName, 0) + 1);
    }
    
    @Override
    public void onStationFailure(Station station, PCBBoard board) {
        String stationName = station.getStationName();
        stationFailures.put(stationName, stationFailures.getOrDefault(stationName, 0) + 1);
    }
    
    @Override
    public void onBoardCompleted(PCBBoard board) {
        boardsCompleted++;
    }
    
    // Setup display names
    private void setupStationDisplayNames() {
        stationDisplayNames.put("Apply Solder Paste", "Apply Solder Paste");
        stationDisplayNames.put("Place Components", "Place Components");
        stationDisplayNames.put("Reflow Solder", "Reflow Solder");
        stationDisplayNames.put("Optical Inspection", "Optical Inspection");
        stationDisplayNames.put("Hand Soldering Assembly", "Hand Soldering/Assembly");
        stationDisplayNames.put("Cleaning", "Cleaning");
        stationDisplayNames.put("Depanelization", "Depanelization");
        stationDisplayNames.put("Test", "Test (ICT or Flying Probe)");
    }
    
    // Calculate total failed PCBs
    public int getTotalFailedPcbs() {
        int stationFailureTotal = stationFailures.values().stream().mapToInt(Integer::intValue).sum();
        int defectFailureTotal = pcbDefectFailures.values().stream().mapToInt(Integer::intValue).sum();
        return stationFailureTotal + defectFailureTotal;
    }
    
    // Print detailed statistics (your existing method)
    public void printDetailedStatistics() {
        System.out.println("PCB type: " + pcbType);
        System.out.println("PCBs run: " + pcbsRun);
        System.out.println();
        
        // Station Failures
        System.out.println("Station Failures");
        for (Map.Entry<String, Integer> entry : stationFailures.entrySet()) {
            String displayName = stationDisplayNames.getOrDefault(entry.getKey(), entry.getKey());
            System.out.println(displayName + ": " + entry.getValue());
        }
        System.out.println();
        
        // PCB Defect Failures
        System.out.println("PCB Defect Failures");
        for (Map.Entry<String, Integer> entry : pcbDefectFailures.entrySet()) {
            if (entry.getValue() > 0) {
                String displayName = stationDisplayNames.getOrDefault(entry.getKey(), entry.getKey());
                System.out.println(displayName + " " + entry.getValue());
            }
        }
        System.out.println();
        
        // Final Results
        int totalFailedPcbs = getTotalFailedPcbs();
        int totalPcbsProduced = pcbsRun - totalFailedPcbs;
        
        System.out.println("Final Results");
        System.out.println("Total failed PCBs: " + totalFailedPcbs);
        System.out.println("Total PCBs produced: " + totalPcbsProduced);
        
        // Save to redis
        saveReportData();
    }
    
    /**
     * Get latest report from Redis
     */
    public static Map<String, Object> getLatestReportFromRedis() {
        try {
            if (jedisPool != null) {
                try (Jedis jedis = jedisPool.getResource()) {
                    String jsonData = jedis.get("pcb:latest_report");
                    if (jsonData != null) {
                        return objectMapper.readValue(jsonData, Map.class);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to read from Redis: " + e.getMessage());
        }
        return new HashMap<>();
    }
    
    /**
     * Save report data to Redis
     */
    private void saveToRedis(Map<String, Object> reportData) {
        try {
            if (jedisPool != null) {
                try (Jedis jedis = jedisPool.getResource()) {
                    String jsonData = objectMapper.writeValueAsString(reportData);
                    jedis.set("pcb:latest_report", jsonData);
                    System.out.println("Report saved to Redis");
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to save to Redis: " + e.getMessage());
        }
    }
    
    /**
     * Save report data (called from printDetailedStatistics)
     */
    private void saveReportData() {
        Map<String, Object> report = new HashMap<>();
        report.put("pcbType", pcbType);
        report.put("pcbsRun", pcbsRun);
        report.put("boardsCompleted", boardsCompleted);
        report.put("totalFailedPcbs", getTotalFailedPcbs());
        
        double successRate = pcbsRun > 0 ? ((double) boardsCompleted / pcbsRun) * 100 : 0;
        report.put("successRate", Math.round(successRate * 100.0) / 100.0);
        
        report.put("stationFailures", new HashMap<>(stationFailures));
        report.put("defectFailures", new HashMap<>(pcbDefectFailures));
        report.put("timestamp", java.time.LocalDateTime.now().toString());
        
        latestReportData = report;
        
        // Save to Redis
        saveToRedis(report);
    }
    
    /**
     * Get latest report data (static method for web access)
     */
    public static Map<String, Object> getLatestReportData() {
        return latestReportData;
    }

    public Map<String, Integer> getStationFailuresData() {
        return new HashMap<>(stationFailures);
    }

    public Map<String, Integer> getDefectFailuresData() {
        return new HashMap<>(pcbDefectFailures);
    }

    public SimulationRun createSimulationRun() {
        SimulationRun run = new SimulationRun(pcbType, pcbsRun);
        run.setStationFailures(new HashMap<>(stationFailures));
        run.setDefectFailures(new HashMap<>(pcbDefectFailures));
        run.setTotalProduced(boardsCompleted);
        run.calculateTotals();
        return run;
    }
}