package com.pcb.ecosystem.core.station_hierarchy.non_defect_stations;

import com.pcb.ecosystem.core.abstract_classes.Station;
import com.pcb.ecosystem.core.abstract_classes.PCBBoard;
import com.pcb.ecosystem.core.station_hierarchy.defect_detection_stations.OpticalInspectionStation;

public class ReflowSolderStation extends Station {
    
    public ReflowSolderStation() {
        super("Reflow Solder", false); 
        this.next = new OpticalInspectionStation();
    }
    
}