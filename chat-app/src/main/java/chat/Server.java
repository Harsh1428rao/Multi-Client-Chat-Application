package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * ChatServer
 *
 * Listens for incoming client connections and spins up a
 * ClientHandler thread for each one, enabling concurrent
 * multi-client messaging over TCP sockets.
 */
public class Server {

    private static final int PORT = 12345;
    private static final List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, clients);
                clients.add(handler);

                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Called by ClientHandler when a client disconnects
    public static synchronized void removeClient(ClientHandler handler) {
        clients.remove(handler);
    }
}
