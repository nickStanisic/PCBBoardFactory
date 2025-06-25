package com.pcb.ecosystem.core.mvc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


// Redis Storage 
public class SimulationRunStorage {
    
    private static JedisPool jedisPool;
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    // Redis key patterns
    private static final String RUN_KEY_PREFIX = "pcb:simulation:run:";
    private static final String RUN_INDEX_KEY = "pcb:simulation:runs:index";
    private static final String LATEST_RUN_KEY = "pcb:latest_report";
    private static final String RUN_COUNTER_KEY = "pcb:simulation:counter";
    
    public SimulationRunStorage() {
        initializeRedis();
    }
    
    private void initializeRedis() {
        try {
            if (jedisPool == null) {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(10);
                config.setMaxIdle(5);
                config.setMinIdle(1);
                
                // connect to Redis
                jedisPool = new JedisPool(config, "localhost", 6379);
                
                // Test connection
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.ping();
                    System.out.println("Redis connection established");
                }
            }
        } catch (Exception e) {
            System.out.println("Redis not available: " + e.getMessage());
            System.out.println("Continuing without Redis storage...");
        }
    }
    
    public Long saveSimulationRun(SimulationRun run) {
        if (jedisPool == null) {
            System.out.println("Redis not available, simulation not saved");
            return null;
        }
        
        try (Jedis jedis = jedisPool.getResource()) {
            Long runId = jedis.incr(RUN_COUNTER_KEY);
            run.setRunId(runId);
            run.setTimestamp(LocalDateTime.now());
            
            String runJson = objectMapper.writeValueAsString(run);
            
            String runKey = RUN_KEY_PREFIX + runId;
            jedis.set(runKey, runJson);
            jedis.lpush(RUN_INDEX_KEY, runId.toString());
            jedis.set(LATEST_RUN_KEY, runJson);
            
            System.out.printf("Simulation run %d saved to Redis%n", runId);
            return runId;
            
        } catch (Exception e) {
            System.err.println("Failed to save simulation run to Redis: " + e.getMessage());
            return null;
        }
    }
    
    public SimulationRun getSimulationRun(Long runId) {
        if (jedisPool == null) return null;
        
        try (Jedis jedis = jedisPool.getResource()) {
            String runKey = RUN_KEY_PREFIX + runId;
            String runJson = jedis.get(runKey);
            
            if (runJson != null) {
                return objectMapper.readValue(runJson, SimulationRun.class);
            }
        } catch (Exception e) {
            System.err.println("Failed to get simulation run from Redis: " + e.getMessage());
        }
        return null;
    }
    
    public List<SimulationRun> getAllSimulationRuns() {
        if (jedisPool == null) return new ArrayList<>();
        
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> runIds = jedis.lrange(RUN_INDEX_KEY, 0, -1);
            if (runIds == null || runIds.isEmpty()) return new ArrayList<>();
            
            List<SimulationRun> runs = new ArrayList<>();
            for (String runIdStr : runIds) {
                Long runId = Long.parseLong(runIdStr);
                SimulationRun run = getSimulationRun(runId);
                if (run != null) runs.add(run);
            }
            
            runs.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
            return runs;
            
        } catch (Exception e) {
            System.err.println("Failed to get all simulation runs from Redis: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<SimulationRun> searchRunsByBoardType(String boardType) {
        return getAllSimulationRuns().stream()
            .filter(run -> run.getBoardType().equals(boardType))
            .collect(Collectors.toList());
    }
    
    public int getRunCount() {
        if (jedisPool == null) return 0;
        
        try (Jedis jedis = jedisPool.getResource()) {
            Long count = jedis.llen(RUN_INDEX_KEY);
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    // Static methods for compatibility
    public static void setApplicationContext(Object context) {
        // No longer needed - keeping for compatibility
        System.out.println("Redis storage initialized");
    }
}