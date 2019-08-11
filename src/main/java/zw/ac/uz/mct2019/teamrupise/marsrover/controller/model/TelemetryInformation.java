package zw.ac.uz.mct2019.teamrupise.marsrover.controller.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

@Data
@Slf4j
public class TelemetryInformation implements Serializable {

    private double numberOfMillisecondsSinceStart;
    private String currentVehicleState;
    private double vehiclesXCordinate;
    private double vehiclesYCordinate ;
    private double vehicleDirection ;
    private double vehicleSpeedInMetersPerSecond;
    private List<EnemyInformation> enemyInformationList;
}




