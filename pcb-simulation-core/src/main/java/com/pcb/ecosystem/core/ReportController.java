package com.pcb.ecosystem.core;

import com.pcb.ecosystem.core.observers.AssemblyLineMonitor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*")
public class ReportController {
    
    @GetMapping("/api/status")
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "running");
        status.put("message", "PCB Simulation Server is active");
        return status;
    }
    
    @GetMapping("/api/report")
    public Map<String, Object> getReport() {
        Map<String, Object> reportData = AssemblyLineMonitor.getLatestReportData();
        
        if (reportData.isEmpty()) {
            Map<String, Object> emptyReport = new HashMap<>();
            emptyReport.put("message", "No simulation has been run yet");
            emptyReport.put("status", "waiting");
            return emptyReport;
        }
        
        return reportData;
    }
}