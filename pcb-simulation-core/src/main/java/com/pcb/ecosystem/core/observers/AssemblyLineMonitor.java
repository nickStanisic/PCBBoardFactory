// ADD these imports at the top
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

// ADD @Component annotation to your class
@Component
public class AssemblyLineMonitor implements Observer {
    
    // Your existing fields stay exactly the same
    private String monitorName;
    private String pcbType;
    private int pcbsRun;
    private int boardsCompleted;
    private Map<String, Integer> stationFailures;
    private Map<String, Integer> pcbDefectFailures;
    private Map<String, String> stationDisplayNames;
    
    private static Map<String, Object> latestReportData = new HashMap<>();
    private static RedisTemplate<String, String> redisTemplate;
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        AssemblyLineMonitor.redisTemplate = redisTemplate;
    }
    
    
    /**
     * Get latest report from Redis
     */
    public static Map<String, Object> getLatestReportFromRedis() {
        try {
            if (redisTemplate != null) {
                String jsonData = redisTemplate.opsForValue().get("pcb:latest_report");
                if (jsonData != null) {
                    return objectMapper.readValue(jsonData, Map.class);
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
            if (redisTemplate != null) {
                String jsonData = objectMapper.writeValueAsString(reportData);
                redisTemplate.opsForValue().set("pcb:latest_report", jsonData);
                System.out.println("Report saved to Redis");
            }
        } catch (Exception e) {
            System.err.println("Failed to save to Redis: " + e.getMessage());
        }
    }
    
    // UPDATE your existing saveReportData method to add ONE line:
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
        
        // ADD THIS ONE LINE
        saveToRedis(report);
    }
    
    // Your existing getLatestReportData method stays the same
    public static Map<String, Object> getLatestReportData() {
        return latestReportData;
    }
}