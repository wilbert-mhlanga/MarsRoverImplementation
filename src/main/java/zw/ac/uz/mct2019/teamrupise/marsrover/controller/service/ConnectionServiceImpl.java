package zw.ac.uz.mct2019.teamrupise.marsrover.controller.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.exception.MarsRoverControllerException;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.model.ConnectionRequest;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.model.MessageTag;

import java.io.*;
import java.net.Socket;

@Data
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {

    private Socket clientSocket;
    private InputStreamReader in;
    private static ConnectionServiceImpl instance;
    private MessageProcessingService messageProcessingService;

    private ConnectionServiceImpl() {

    }

    public static ConnectionServiceImpl getInstance(MessageProcessingService messageProcessingService) {
        if (instance == null) {
            instance = new ConnectionServiceImpl();
            instance.setMessageProcessingService(messageProcessingService);
        }
        return instance;
    }

    public void startConnection(ConnectionRequest connectionRequest) {
        if (connectionRequest == null) {
            throw new IllegalArgumentException("Connection Request is required");
        }
        if (connectionRequest.getHostName() == null || connectionRequest.getHostName().isEmpty()) {
            throw new IllegalArgumentException("Hostname is required");
        }
        if (connectionRequest.getPort() <= 0) {
            throw new IllegalArgumentException("Port should be positive");
        }
        try {
            clientSocket = new Socket(connectionRequest.getHostName(), connectionRequest.getPort());
        } catch (IOException e) {
            log.debug("Error when initializing connection", e);
            throw new MarsRoverControllerException("Connection could not be established");
        }
    }


    public void receiveAndProcessMessage() {
        try {
            in = new InputStreamReader(clientSocket.getInputStream());
            int character;
            StringBuilder messageBuilder = new StringBuilder();
            while ((character = in.read()) != -1) {
                final char END_OF_MESSAGE_CHARACTER = ';';
                if ((int) END_OF_MESSAGE_CHARACTER == character) {
                    final String message = messageBuilder.toString();
                    messageBuilder = new StringBuilder();
                    if (message.startsWith(MessageTag.INITIALIZATION.getValue())) {
                        messageProcessingService.processInitializationData(message);
                    } else if (message.startsWith(MessageTag.TELEMETRY.getValue())) {
                        String controlInstruction = messageProcessingService.processTelemetryStream(message);
                        sendControlInstruction(controlInstruction + END_OF_MESSAGE_CHARACTER);
                    } else if (message.startsWith(MessageTag.SUCCESS.getValue())) {
                        messageProcessingService.processSuccessMessage(message);
                    } else {
                        messageProcessingService.processAdverseEvent(message);
                    }
                } else {
                    messageBuilder.append((char) character);
                }
            }
        } catch (IOException ioe) {
            log.debug("Error while receiving initialization data connection", ioe);
            throw new MarsRoverControllerException("Connection could not be established");
        }
    }


    private void sendControlInstruction(String controlInstruction) throws IOException {
        final OutputStream os = clientSocket.getOutputStream();
        final OutputStreamWriter osw = new OutputStreamWriter(os);
        final BufferedWriter bw = new BufferedWriter(osw);
        bw.write(controlInstruction);
        bw.flush();
    }

    public void stopConnection() {
        try {
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            log.debug("error when closing", e);
        }
    }
}




