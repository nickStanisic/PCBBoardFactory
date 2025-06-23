package com.pcb.ecosystem.core.station_hierarchy.non_defect_stations;

import com.pcb.ecosystem.core.abstract_classes.Station;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.station_hierarchy.defect_detection_stations.TestStation;

public class DepanelizationStation extends Station {
    
    public DepanelizationStation() {
        super("Depanelization", false); 
        this.next = new TestStation();
    }

}