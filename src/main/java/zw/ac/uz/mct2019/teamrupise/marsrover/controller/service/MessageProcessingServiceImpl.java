package zw.ac.uz.mct2019.teamrupise.marsrover.controller.service;

import lombok.extern.slf4j.Slf4j;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.exception.MarsRoverControllerException;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.model.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessageProcessingServiceImpl implements MessageProcessingService{

    @Override
    public String processTelemetryStream(String spaceDelimitedTelemtryToken){
        log.debug("Processing telemetry message: {}",spaceDelimitedTelemtryToken);
        if(spaceDelimitedTelemtryToken==null || !spaceDelimitedTelemtryToken.startsWith(MessageTag.TELEMETRY.getValue())){
            throw new MarsRoverControllerException("Telemetry token must start with "+MessageTag.TELEMETRY.getValue());
        }
        TelemetryInformation telemetryInformation=getTelemtryInformation(spaceDelimitedTelemtryToken);
        return controlDecision(telemetryInformation);
    }

    @Override
    public void processInitializationData(String spaceDelimitedInitializationToken){
        log.debug("Processing initialization message: {}",spaceDelimitedInitializationToken);
        if(spaceDelimitedInitializationToken==null || !spaceDelimitedInitializationToken.startsWith(MessageTag.INITIALIZATION.getValue())){
            throw new MarsRoverControllerException("Initialization token must start with "+MessageTag.INITIALIZATION.getValue());
        }
        final String [] mapInfoArray=spaceDelimitedInitializationToken.split("\\s+");
        if(mapInfoArray.length!=9){
            throw new MarsRoverControllerException("Invalid initialization token");
        }
        MapInformation mapInformation=MapInformation.getInstance();
        mapInformation.setSpanOfXAxis(new Double(mapInfoArray[1]));
        mapInformation.setSpanOfYAxis(new Double(mapInfoArray[2]));
        mapInformation.setTimeLimitInMilliSeconds(new Double(mapInfoArray[3]));
        mapInformation.setMinRangeOfVehiceVisualSensorsInMeters(new Double(mapInfoArray[4]));
        mapInformation.setMaxRangeOfVehiceVisualSensorsInMeters(new Double(mapInfoArray[5]));
        mapInformation.setMaxVehicleSpeedInMetersPerSecond(new Double(mapInfoArray[6]));
        mapInformation.setMaxTurningSpeedInDegreesPerSecond(new Double(mapInfoArray[7]));
        mapInformation.setMaxHardTurningSpeedInDegreesPerSecond(new Double(mapInfoArray[8]));
        log.info("Map Information: {}",mapInformation);
    }


    @Override
    public void processAdverseEvent(String spaceDelimitedToken){
        log.debug("Processing adverse event: {}",spaceDelimitedToken);
        if(spaceDelimitedToken.startsWith(MessageTag.KILLED_BY_MARTIAN.getValue())){
            throw new MarsRoverControllerException("Rover was killed by a martian");
        }
        if(spaceDelimitedToken.startsWith(MessageTag.HIT_BOULDER_OR_MAP_EDGE.getValue())){
            throw new MarsRoverControllerException("Rover hit boulder or map edge");
        }
        if(spaceDelimitedToken.startsWith(MessageTag.FELL_INTO_A_CRATER.getValue())){
            throw new MarsRoverControllerException("Rover fell into a crater");
        }
        if(spaceDelimitedToken.startsWith(MessageTag.END_OF_LIFE.getValue())){
            throw new MarsRoverControllerException("Timeout");
        }
        else {
            throw new MarsRoverControllerException("Unrecognized adverse event occurred");
        }
    }

    @Override
    public void processSuccessMessage(String spaceDelimitedTelemtryToken){
        log.debug("Processing success message: {}",spaceDelimitedTelemtryToken);
        log.info("Rover is safely home!!");
    }

    private String controlDecision(TelemetryInformation telemetryInformation){
        boolean vehicleIsInFirstQuadrant= telemetryInformation.getVehiclesXCordinate()>0&& telemetryInformation.getVehiclesYCordinate()>0;
        boolean vehicleIsInSecondQuadrant= telemetryInformation.getVehiclesXCordinate()<0&& telemetryInformation.getVehiclesYCordinate()>0;
        boolean vehicleIsInThirdQuadrant= telemetryInformation.getVehiclesXCordinate()<0&& telemetryInformation.getVehiclesYCordinate()<0;

        boolean vehicleIsGoingStraightNorth= telemetryInformation.getVehicleDirection()==90;
        boolean vehicleIsGoingStraightEast= telemetryInformation.getVehicleDirection()==0;
        boolean vehicleIsGoingNorthEast= telemetryInformation.getVehicleDirection()>0&&telemetryInformation.getVehicleDirection()<90;
        boolean vehicleIsGoingStraightWest= telemetryInformation.getVehicleDirection()==180;
        boolean vehicleIsGoingNorthWest= telemetryInformation.getVehicleDirection()>90&&telemetryInformation.getVehicleDirection()<180;
        boolean vehicleIsGoingStraightSouth= telemetryInformation.getVehicleDirection()==270;
        boolean vehicleIsGoingSouthEast= telemetryInformation.getVehicleDirection()>270;

        if(vehicleIsInFirstQuadrant){
            if(vehicleIsGoingSouthEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightNorth){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_RIGHT.getValue();
            }
            if(vehicleIsGoingNorthEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingNorthWest){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightWest){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightSouth){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_RIGHT.getValue();
            }
            else {
                return ControlInstruction.ACCELERATE.getValue();
            }
        }


        if(vehicleIsInSecondQuadrant){
            if(vehicleIsGoingSouthEast){
                return ControlInstruction.ACCELERATE.getValue();
            }
            if(vehicleIsGoingStraightNorth){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingNorthEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_RIGHT.getValue();
            }
            if(vehicleIsGoingNorthWest){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightWest){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightSouth){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            else {
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
        }


        if(vehicleIsInThirdQuadrant){
            if(vehicleIsGoingSouthEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightNorth){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_RIGHT.getValue();
            }
            if(vehicleIsGoingNorthEast){
                return ControlInstruction.ACCELERATE.getValue();
            }
            if(vehicleIsGoingNorthWest){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_RIGHT.getValue();
            }
            if(vehicleIsGoingStraightEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightWest){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightSouth){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_RIGHT.getValue();
            }
            else {
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
        }

        else {
            if(vehicleIsGoingSouthEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightNorth){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingNorthEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingNorthWest){
                return ControlInstruction.ACCELERATE.getValue();
            }
            if(vehicleIsGoingStraightEast){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            if(vehicleIsGoingStraightWest){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_RIGHT.getValue();
            }
            if(vehicleIsGoingStraightSouth){
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
            else {
                return ControlInstruction.ACCELERATE.getValue()+ ControlInstruction.TURN_LEFT.getValue();
            }
        }
    }


    private TelemetryInformation getTelemtryInformation(String spaceDelimitedTelemtryToken){
        final String [] telemetryInfoArray=spaceDelimitedTelemtryToken.split("\\s+");
        TelemetryInformation telemetryInformation=new TelemetryInformation();
        telemetryInformation.setNumberOfMillisecondsSinceStart(new Double(telemetryInfoArray[1]));
        telemetryInformation.setCurrentVehicleState(telemetryInfoArray[2]);
        telemetryInformation.setVehiclesXCordinate(new Double(telemetryInfoArray[3]));
        telemetryInformation.setVehiclesYCordinate(new Double(telemetryInfoArray[4]));
        telemetryInformation.setVehicleDirection(new Double(telemetryInfoArray[5]));
        telemetryInformation.setVehicleSpeedInMetersPerSecond(new Double(telemetryInfoArray[6]));
        boolean telemetryStreamContainsEnemyInfo=telemetryInfoArray.length>7;
        if(telemetryStreamContainsEnemyInfo){
            List<EnemyInformation> enemyInformationList=new ArrayList<>();
            for(int position=7;position <telemetryInfoArray.length;position++){
                EnemyInformation enemyInformation=new EnemyInformation();
                if(telemetryInfoArray[position].equalsIgnoreCase(EnemyTag.MARTIAN.getValue()) ||
                        telemetryInfoArray[position].equalsIgnoreCase(EnemyTag.CRATER.getValue()) ||
                        telemetryInfoArray[position].equalsIgnoreCase(EnemyTag.BOULDER.getValue())) {
                    enemyInformation.setEnemy(telemetryInfoArray[position]);
                    enemyInformation.setXCordinate(new Double(telemetryInfoArray[position+1]));
                    enemyInformation.setYCordinate(new Double(telemetryInfoArray[position+2]));
                    enemyInformation.setSpeedInMetersPerSecond(new Double(telemetryInfoArray[position+3]));
                    enemyInformationList.add(enemyInformation);
                    continue;
                }
                if(telemetryInfoArray[position].equalsIgnoreCase(EnemyTag.MARTIAN.getValue())){
                    enemyInformation.setSpeedInMetersPerSecond(new Double(telemetryInfoArray[position+4]));
                    enemyInformationList.add(enemyInformation);
                }
            }
            telemetryInformation.setEnemyInformationList(enemyInformationList);
        }
        log.debug("Telemetry info: {}",telemetryInformation);
        return telemetryInformation;
    }
}
