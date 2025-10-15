/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package network_project;

/**
 *
 * @author FATIMAH
 */

import java.io.*; 
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

public class NewServer{
    private static ArrayList<NewClient> clients =new ArrayList<>(); //to store the client threads
    private static ArrayList<String> usernames = new ArrayList<>(); //to store usernames
    private static ArrayList<String> passwords = new ArrayList<>(); //to store passwords
    private static ArrayList<String> reservedSlots = new ArrayList<>(); // to store reserved slots (to check availability)

    
    public static void main(String[] args) throws IOException{
        
        
        ServerSocket serverSocket = new ServerSocket(9090); //opens a server socket on port 9090

        while (true){ //while socket port 9090 is open
         System.out.println("Waiting for client connection"); 
         
         Socket client=serverSocket.accept(); //waiting connection
         System.out.println("Connected to client");
         NewClient clientThread = new NewClient(client, clients, usernames, passwords, reservedSlots); // new thread for new client
         clients.add(clientThread); 
         new Thread (clientThread).start(); //---------------------------------
         
        }
    }
}
