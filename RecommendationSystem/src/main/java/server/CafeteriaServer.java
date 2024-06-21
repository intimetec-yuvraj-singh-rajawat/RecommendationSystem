package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import handler.ClientHandler;

public class CafeteriaServer {
    private static final int PORT = 12345;
    private static ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws SQLException {
        System.out.println("Cafeteria Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();

                String clientId = clientSocket.getRemoteSocketAddress().toString();
                clients.put(clientId, clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void sendMessageToClient(String clientId, String message) {
        ClientHandler clientHandler = clients.get(clientId);
        if (clientHandler != null) {
            clientHandler.sendMessage(message);
        }
    }
}
