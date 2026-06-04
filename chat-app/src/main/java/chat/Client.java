package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ChatClient
 *
 * Connects to the ChatServer over TCP.
 * Runs two threads simultaneously:
 *   1. ReadThread  — listens for incoming messages from the server and prints them
 *   2. Main thread — reads user input from the terminal and sends it to the server
 *
 * Type /quit to disconnect.
 */
public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Connecting to chat server at " + HOST + ":" + PORT + "...");

        try (
            Socket socket = new Socket(HOST, PORT);
            BufferedReader serverIn = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userIn = new BufferedReader(
                new InputStreamReader(System.in)
            );
        ) {
            System.out.println("Connected! Type /quit to exit.\n");

            // Background thread: print messages received from server
            Thread readThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = serverIn.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            readThread.setDaemon(true);
            readThread.start();

            // Main thread: send user input to server
            String userMessage;
            while ((userMessage = userIn.readLine()) != null) {
                serverOut.println(userMessage);
                if (userMessage.equalsIgnoreCase("/quit")) {
                    break;
                }
            }

            System.out.println("You left the chat.");

        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
            System.err.println("Make sure the server is running first.");
        }
    }
}
