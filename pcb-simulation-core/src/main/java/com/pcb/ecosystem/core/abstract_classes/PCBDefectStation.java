package com.pcb.ecosystem.core.abstract_classes;

/**
 * Abstract class for stations that can detect PCB defects
 * Groups all defect detection stations
 */
public abstract class PCBDefectStation extends Station {
    // Defect rates 
    protected double testBoardDefectRate;
    protected double sensorBoardDefectRate;
    protected double gatewayBoardDefectRate;
    
    public PCBDefectStation(String stationName) {
        super(stationName, true); 
    }
    
    /**
     * Common method to calculate if a defect occurs
     * Uses the specific defect rates set by concrete subclasses
     */
    public boolean calculateDefect(PCBBoard board) {
        double defectRate = getDefectRateForBoard(board);
        return random.nextDouble() < defectRate;
    }
    
    /**
     * Get the appropriate defect rate based on board type
     */
    private double getDefectRateForBoard(PCBBoard board) {
        String boardType = board.getBoardType();
        
        switch (boardType) {
            case "TestBoard":
                return testBoardDefectRate;
            case "SensorBoard":
                return sensorBoardDefectRate;
            case "GatewayBoard":
                return gatewayBoardDefectRate;
            default:
                return 0.0; 
        }
    }
    
}