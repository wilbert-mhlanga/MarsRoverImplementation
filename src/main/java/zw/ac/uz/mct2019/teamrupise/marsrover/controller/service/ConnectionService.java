package zw.ac.uz.mct2019.teamrupise.marsrover.controller.service;

import zw.ac.uz.mct2019.teamrupise.marsrover.controller.model.ConnectionRequest;

public interface ConnectionService {

    void startConnection(ConnectionRequest connectionRequest);
    void receiveAndProcessMessage();
    void stopConnection();
}




