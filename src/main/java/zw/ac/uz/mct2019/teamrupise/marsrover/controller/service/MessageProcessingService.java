package zw.ac.uz.mct2019.teamrupise.marsrover.controller.service;

public interface MessageProcessingService {

    String processTelemetryStream(String spaceDelimitedTelemtryToken);
    void processAdverseEvent(String spaceDelimitedToken);
    void processSuccessMessage(String spaceDelimitedToken);
    void processInitializationData(String spaceDelimitedInitializationToken);
}
