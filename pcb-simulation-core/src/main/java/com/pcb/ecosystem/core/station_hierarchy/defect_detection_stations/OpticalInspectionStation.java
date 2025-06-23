package com.pcb.ecosystem.core.station_hierarchy.defect_detection_stations;

import com.pcb.ecosystem.core.abstract_classes.PCBDefectStation;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.station_hierarchy.defect_detection_stations.HandSolderingAssemblyStation;

public class OpticalInspectionStation extends PCBDefectStation {
    
    public OpticalInspectionStation() {
        super("Optical Inspection");
        // Set specific defect rates for each board type
        this.testBoardDefectRate = 0.1;   // 10% defect rate for TestBoard
        this.sensorBoardDefectRate = 0.002; // .2% defect rate for SensorBoard
        this.gatewayBoardDefectRate = 0.004; // .4% defect rate for GatewayBoard
        
        // Hard-coded next station
        this.next = new HandSolderingAssemblyStation();
    }
    
}