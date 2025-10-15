/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network_project;

/**
 *
 * @author FATIMAH
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String Server_IP ="localhost"; //------- Server IP and port # -----------
    private static final int Server_port =9090;
    
 
        public static void main(String[] args) throws IOException{
            
          try(Socket socket = new Socket (Server_IP,Server_port)) { //try block to be excuted only if connection is established
                          System.out.println("Connected to server!");

             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // variable that will get input from the server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);// variable that will send userinput from the server
             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in)); // variable that will get userinput
              
            String serverLine;
            while ((serverLine = in.readLine()) != null) {
                
                // Always print all responses from the server
                System.out.println(serverLine);
                
                // If the message is final (Goodbye), break the loop
                if (serverLine.contains("Goodbye!")) {
                    break;
                }
                
                // Read user input and send to server
                String userInput = keyboard.readLine();
                if (userInput == null) {
                    break; 
                }
                out.println(userInput); // Send command to the server
                              
            }


        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage()); //error message if try block fails execution
        }

        System.out.println("Client closed.");
    }
}