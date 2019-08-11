package zw.ac.uz.mct2019.teamrupise.marsrover.controller.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public class MapInformation implements Serializable {

    private double spanOfXAxis;
    private double spanOfYAxis;
    private double timeLimitInMilliSeconds;
    private double minRangeOfVehiceVisualSensorsInMeters ;
    private double maxRangeOfVehiceVisualSensorsInMeters ;
    private double maxVehicleSpeedInMetersPerSecond;
    private double maxTurningSpeedInDegreesPerSecond;
    private double maxHardTurningSpeedInDegreesPerSecond;
    private static MapInformation instance;

    private MapInformation(){

    }

    public static MapInformation getInstance(){
        if (instance == null){
            instance = new MapInformation();
        }
        return instance;
    }

}




