package com.pcb.ecosystem.core.mvc.model;

/**
 * Board Failure Rates - Value object containing the 4 failure percentages
 * Represents failure rates for the 4 defect detection stations
 */
public class BoardFailureRates {
    private final double placeComponentsRate;
    private final double opticalInspectionRate;
    private final double handSolderingRate;
    private final double testStationRate;
    
    public BoardFailureRates(double placeComponentsRate, 
                           double opticalInspectionRate,
                           double handSolderingRate, 
                           double testStationRate) {
        this.placeComponentsRate = placeComponentsRate;
        this.opticalInspectionRate = opticalInspectionRate;
        this.handSolderingRate = handSolderingRate;
        this.testStationRate = testStationRate;
    }
    
    public double getPlaceComponentsRate() { return placeComponentsRate; }
    public double getOpticalInspectionRate() { return opticalInspectionRate; }
    public double getHandSolderingRate() { return handSolderingRate; }
    public double getTestStationRate() { return testStationRate; }
    
    public double getFailureRateForStation(String stationName) {
        switch (stationName) {
            case "Place Components":
                return placeComponentsRate;
            case "Optical Inspection":
                return opticalInspectionRate;
            case "Hand Soldering/Assembly":
                return handSolderingRate;
            case "Test (ICT or Flying Probe)":
                return testStationRate;
            default:
                return 0.0; // Non-defect stations
        }
    }
    
    @Override
    public String toString() {
        return String.format("BoardFailureRates{place=%.3f%%, optical=%.3f%%, handSolder=%.3f%%, test=%.3f%%}",
            placeComponentsRate * 100, opticalInspectionRate * 100, 
            handSolderingRate * 100, testStationRate * 100);
    }
}