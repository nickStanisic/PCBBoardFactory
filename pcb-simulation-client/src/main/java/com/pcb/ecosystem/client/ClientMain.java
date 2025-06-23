package com.pcb.ecosystem.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import java.util.Map;

@SpringBootApplication
public class ClientMain {
    
    public static void main(String[] args) {
        SpringApplication.run(ClientMain.class, args);
    }
    
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
            .baseUrl("http://localhost:8080/api")
            .build();
    }
    
    @Bean
    public CommandLineRunner run(RestClient restClient) {
        return args -> {
            
            try {
                // Check server status first
                checkServerStatus(restClient);
                
                // Wait a moment for simulation to complete
                Thread.sleep(2000);
                
                // Get and print the report
                getAndPrintReport(restClient);
                
            } catch (Exception e) {
                System.out.println("Cannot connect to server: " + e.getMessage());
            }
        };
    }
    
    private void checkServerStatus(RestClient restClient) {
        try {
            Map<String, Object> status = restClient.get()
                .uri("/status")
                .retrieve()
                .body(Map.class);
            
            System.out.println("Server Status: " + status.get("status"));
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("Server check failed: " + e.getMessage());
            throw e;
        }
    }
    
    private void getAndPrintReport(RestClient restClient) {
        try {
            Map<String, Object> report = restClient.get()
                .uri("/report")
                .retrieve()
                .body(Map.class);
            
            // Print report in same format as server
            printFormattedReport(report);
            
        } catch (Exception e) {
            System.out.println("Failed to get report: " + e.getMessage());
        }
    }
    
    private void printFormattedReport(Map<String, Object> report) {
        System.out.println("Simulation Data (Retrieved from Server)");
        System.out.println("=".repeat(80));
        
        // Basic info
        System.out.println("PCB type: " + report.get("pcbType"));
        System.out.println("PCBs run: " + report.get("pcbsRun"));
        System.out.println();
        
        // Station Failures
        System.out.println("Station Failures");
        Map<String, Object> stationFailures = (Map<String, Object>) report.get("stationFailures");
        if (stationFailures != null) {
            for (Map.Entry<String, Object> entry : stationFailures.entrySet()) {
                String stationName = entry.getKey();
                Integer failures = (Integer) entry.getValue();
                System.out.println(stationName + ": " + failures);
            }
        }
        System.out.println();
        
        // PCB Defect Failures
        System.out.println("PCB Defect Failures");
        Map<String, Object> defectFailures = (Map<String, Object>) report.get("defectFailures");
        if (defectFailures != null) {
            for (Map.Entry<String, Object> entry : defectFailures.entrySet()) {
                String stationName = entry.getKey();
                Integer defects = (Integer) entry.getValue();
                if (defects > 0) {
                    System.out.println(stationName + " " + defects);
                }
            }
        }
        System.out.println();
        
        // Final Results
        System.out.println("Final Results");
        Integer totalFailed = (Integer) report.get("totalFailedPcbs");
        Integer totalRun = (Integer) report.get("pcbsRun");
        Integer totalProduced = totalRun - totalFailed;
        
        System.out.println("Total failed PCBs: " + totalFailed);
        System.out.println("Total PCBs produced: " + totalProduced);
        
        
        System.out.println("=".repeat(80));
    }
    
}