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
import java.util.ArrayList;

class NewClient implements Runnable{ //server thread
private Socket client;
private BufferedReader in;
private PrintWriter out;
private ArrayList<NewClient> clients; 
private ArrayList<String> usernames;
private ArrayList<String> passwords;
private ArrayList<String> reservedSlots;    

 

public NewClient(Socket c, ArrayList<NewClient> clients, ArrayList<String> usernames,
                 ArrayList<String> passwords, ArrayList<String> reservedSlots) throws IOException {
    this.client = c;
    this.clients = clients;
    this.usernames = usernames;
    this.passwords = passwords;
    this.reservedSlots = reservedSlots;

    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    out = new PrintWriter(client.getOutputStream(), true);
}


@Override
  public void run ()
  {
   try{    

            // Step 1: Ask for username and password
            out.println("Enter username:");
            String username = in.readLine();

            out.println("Enter password:");
            String password = in.readLine();

            // Step 2: Check if username exists
            synchronized (usernames) {
                if (usernames.contains(username)) {
                    out.println("Username already exists! Please try again.");
                    client.close();
                    return;
                } else {
                    // Step 3: Save user
                    usernames.add(username);
                    passwords.add(password);
                    out.println("Registration successful! Welcome, " + username + "!");
                }
            }
            
            // Step 4: After register, user can send commands (for later use)
            String line;
            while ((line = in.readLine()) != null) {
                
                // ===== Reservation System Logic (Phase 1 - Confirm Reservation) =====
                // Check that the command begins with "RESERVE " to validate the correct reservation format.
                if (line.startsWith("RESERVE") && line.length() > 7 && line.charAt(7) == ' ') {
                    // Expected command format: RESERVE <Sport> <Day> <Time> 
                    //Example input: RESERVE Tennis Monday 17:00
                    String[] parts = line.split(" ");
                    if (parts.length < 4) {
                        // Invalid input format
                        out.println("REJECTED: Invalid format. Use RESERVE <Sport> <Day> <Time>");
                        continue;
                    }
                   
                    // Extract reservation details
                    String sport = parts[1];
                    String day = parts[2];
                    String time = parts[3];

                    // Create a unique key for each reservation slot
                    String slotKey = sport + "-" + day + "-" + time;

                    // ===== Update Availability (Phase 1 - Actual Implementation) =====
                    // Use synchronized to prevent multiple clients from booking the same slot at the same time
                    synchronized (reservedSlots) {
                        if (reservedSlots.contains(slotKey)) {
                            // If slot already booked, reject the request
                            out.println("REJECTED: Slot not available. Please choose another time.");
                        } else {
                            // Otherwise, add it to the reserved list and confirm booking
                            reservedSlots.add(slotKey);
                            out.println("CONFIRMED: " + sport + " " + day + " " + time + " reserved!");
                        }
                    }
                                   
                        } else if (line.equalsIgnoreCase("quit")){
                            // Close connection if user types 'quit'
                            out.println("Goodbye!");
                            break; // Exit the loop
                        } else {
                            // Handle any unrecognized commands
                            out.println("UNKNOWN COMMAND");
                        }
            }
      
   }
    catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("Error closing connection.");
            }
          }
  }
}
