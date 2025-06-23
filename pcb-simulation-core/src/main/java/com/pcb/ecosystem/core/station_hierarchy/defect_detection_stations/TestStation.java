package com.pcb.ecosystem.core.station_hierarchy.defect_detection_stations;

import com.pcb.ecosystem.core.abstract_classes.PCBDefectStation;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;

public class TestStation extends PCBDefectStation {
    
    public TestStation() {
        super("Test");
        // Set specific defect rates for each board type (final comprehensive test)
        this.testBoardDefectRate = 0.1;    // 10% defect rate for TestBoard
        this.sensorBoardDefectRate = 0.004;  // .4% defect rate for SensorBoard
        this.gatewayBoardDefectRate = 0.008; // .8% defect rate for GatewayBoard
        
        // Last station - no next station
        this.next = null;
    }
    
}