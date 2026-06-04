package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * ClientHandler
 *
 * Each connected client gets one ClientHandler running on its own thread.
 * Reads messages from the client and broadcasts them to every other
 * connected client.
 *
 * OOP concepts used:
 *   - Implements Runnable (threading interface)
 *   - Encapsulation: client state kept private
 *   - Single Responsibility: only handles one client's I/O
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final List<ClientHandler> allClients;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket, List<ClientHandler> allClients) {
        this.socket = socket;
        this.allClients = allClients;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
        ) {
            out = new PrintWriter(socket.getOutputStream(), true);

            // First message from client is the username
            out.println("Enter your username:");
            username = in.readLine();

            if (username == null || username.isBlank()) {
                username = "Anonymous";
            }

            System.out.println(username + " has joined the chat.");
            broadcast("[Server] " + username + " joined the chat!", null);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("/quit")) {
                    break;
                }
                System.out.println("[" + username + "]: " + message);
                broadcast("[" + username + "]: " + message, this);
            }

        } catch (IOException e) {
            System.out.println(username + " disconnected unexpectedly.");
        } finally {
            disconnect();
        }
    }

    // Send a message to all clients except the excluded one
    private void broadcast(String message, ClientHandler exclude) {
        synchronized (allClients) {
            for (ClientHandler client : allClients) {
                if (client != exclude) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void disconnect() {
        try {
            socket.close();
        } catch (IOException ignored) {}

        synchronized (allClients) {
            allClients.remove(this);
        }

        if (username != null) {
            System.out.println(username + " has left the chat.");
            broadcast("[Server] " + username + " left the chat.", null);
        }
    }

    public String getUsername() {
        return username;
    }
}
