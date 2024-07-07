package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import Service.UserService;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private AdminController adminController;
    private ChefController chefController;
    private final UserService userService;
    private int userId;

    public ClientHandler(Socket clientSocket) throws SQLException {
        this.clientSocket = clientSocket;
        this.userService = new UserService();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (inputLine.startsWith("LOGIN")) {
                    handleLogin(inputLine, out);
                } else if (inputLine.startsWith("LOGOUT")) {
                    handleLogout(out);
                } else if (inputLine.startsWith("Admin_")) {
                    if (adminController == null) {
                        adminController = new AdminController(out, in);
                    }
                    adminController.processCommand(inputLine);
                } else if (inputLine.startsWith("Chef_")) {
                    if (chefController == null) {
                        chefController = new ChefController(out, in);
                    }
                    chefController.processCommand(inputLine);
                } else {
                    out.println("Unknown command");
                    out.flush();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected");
        }
    }

    private void handleLogin(String inputLine, PrintWriter out) throws SQLException {
        String[] parts = inputLine.split(" ");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];
            if (userService.validateLogin(username, password)) {
                out.println("LOGIN SUCCESSFUL");
                out.flush();
                String roleId = userService.getRoleName(username, password);
                userId = userService.getUserId(username, password);
                out.println(roleId);
                out.flush();
                out.println(userId);
                out.flush();
            } else {
                out.println("LOGIN FAILED");
                out.flush();
            }
        } else {
            out.println("Invalid LOGIN command");
            out.flush();
        }
    }

    private void handleLogout(PrintWriter out) throws SQLException {
        // Implement logout handling logic
    }
}
