package com.pcb.ecosystem.core.station_hierarchy.defect_detection_stations;

import com.pcb.ecosystem.core.abstract_classes.PCBDefectStation;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.station_hierarchy.non_defect_stations.CleaningStation;

public class HandSolderingAssemblyStation extends PCBDefectStation {
    
    public HandSolderingAssemblyStation() {
        super("Hand Soldering Assembly");
        // Set specific defect rates for each board type (higher due to manual process)
        this.testBoardDefectRate = 0.05;    // 5% defect rate for TestBoard
        this.sensorBoardDefectRate = 0.004;  // .4% defect rate for SensorBoard
        this.gatewayBoardDefectRate = 0.008; // .8% defect rate for GatewayBoard
        
        // Hard-coded next station
        this.next = new CleaningStation();
    }
    
}