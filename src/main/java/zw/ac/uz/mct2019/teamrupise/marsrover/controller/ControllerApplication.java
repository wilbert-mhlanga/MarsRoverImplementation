package zw.ac.uz.mct2019.teamrupise.marsrover.controller;

import zw.ac.uz.mct2019.teamrupise.marsrover.controller.model.ConnectionRequest;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.service.ConnectionService;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.service.ConnectionServiceImpl;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.service.MessageProcessingServiceImpl;

import java.util.Scanner;


public class ControllerApplication {

    private static Scanner input=new Scanner(System.in);

    public static void main(String [] args) throws Exception{
        System.out.print("Please enter the IP Address for the server : ");
        final String host = input.nextLine();
        System.out.println("Please enter server port: ");
        final int port = input.nextInt();
        ConnectionService connectionService = ConnectionServiceImpl.getInstance(new MessageProcessingServiceImpl());
        connectionService.startConnection(new ConnectionRequest(host,port));
        connectionService.receiveAndProcessMessage();
    }

}
