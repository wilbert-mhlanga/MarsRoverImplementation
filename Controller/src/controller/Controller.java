/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Wilbert
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Controller {

    /**
     * @param args the command line arguments
     */
    
     private static String SERVERHOST = "127.0.0.1";
     private static int SERVERPORT = 8080;
     
     
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        //Initialize the socket connection
        Socket echoSocket = new Socket(SERVERHOST, SERVERPORT);
        PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

        //Initialize stdio reader
        BufferedReader ioReader =  new BufferedReader(new InputStreamReader(System.in));
        String input = "";

        while (!"exit".equals(input)){
            
            System.out.println("Enter 'exit' to stop program OR Enter message 'Get Iniatialisation data' from the Rover:");
            System.out.println();
            input = ioReader.readLine();

            out.write(String.format("%s\n", input));
            out.flush();

            System.out.println("echo from the SERVER: " + in.readLine());
            System.out.println();
            System.out.println();
        }
    }
    
}
