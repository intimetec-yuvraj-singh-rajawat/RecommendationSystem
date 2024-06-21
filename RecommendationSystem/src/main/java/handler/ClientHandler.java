package handler;

import Service.FeedbackService;
import Service.MenuService;
import Service.UserService;
import model.Feedback;
import model.MenuItem;
import recommendations.RecommendationEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final UserService userService;
    private final MenuService menuService;
    private PrintWriter out;
    private BufferedReader in;
    private String roleName;

    public ClientHandler(Socket clientSocket) throws SQLException {
        this.clientSocket = clientSocket;
        this.userService = new UserService();
        this.menuService = new MenuService();
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("LOGIN ")) {
                    handleLogin(inputLine);
                } else if (inputLine.equalsIgnoreCase("LOGOUT")) {
                    handleLogout();
                    break;
                } else if (roleName != null) {
                    if (roleName.equalsIgnoreCase("Admin")) {
                        handleMenuOperations(inputLine);
                    } else if (roleName.equalsIgnoreCase("Chef")) {
                        handleChefOperations(inputLine);
                    }
                } else {
                    out.println("You need to login first.");
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

    private void handleLogin(String inputLine) throws SQLException {
        String[] parts = inputLine.split(" ");
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];
            if (userService.validateLogin(username, password)) {
                out.println("LOGIN SUCCESSFUL");
                roleName = userService.getRoleName(username, password);
                out.println("ROLE: " + roleName);
                if (roleName.equalsIgnoreCase("Admin")) {
                    showMenu();
                } else if (roleName.equalsIgnoreCase("Chef")) {
                    showChefCommand();
                }
            } else {
                out.println("LOGIN FAILED");
            }
        } else {
            out.println("Invalid LOGIN command");
        }
    }

    private void showMenu() {
        out.println("Select an option:");
        out.println("1. View all menu items");
        out.println("2. Add a new menu item");
        out.println("3. Update an existing menu item");
        out.println("4. Delete a menu item");
        out.println("5. Type Logout to disconnect");
    }

    private void showChefCommand() {
        out.println("Select an option:");
        out.println("1. Get recommendations");
        out.println("2. Logout");
    }

    private void handleChefOperations(String inputLine) throws IOException, SQLException {
    	String[] parts = inputLine.split(" ", 2);
        String operation = parts[1];
        switch (operation) {
            case "1":
                getRecommendations();
                break;
            case "2":
                handleLogout();
                break;
            default:
                out.println("Unknown command");
                showChefCommand();
                break;
        }
    }

    private void getRecommendations() {
        List<Feedback> feedbacks = FeedbackService.getFeedbacks();
        RecommendationEngine recommendation = new RecommendationEngine(feedbacks);
        for (Integer itemId : recommendation.itemRatings.keySet()) {
        	out.println(itemId + " - " + (RecommendationEngine.itemRatings.get(itemId) +" "+ RecommendationEngine.itemFeedbacks.get(itemId)));        }
        showChefCommand();
    }

    private void handleLogout() {
        out.println("LOGOUT SUCCESSFUL");
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMenuOperations(String inputLine) throws IOException, SQLException {
        String[] parts = inputLine.split(" ", 2);
        String operation = parts[0];
        if (parts.length > 1) {
            String operationDetails = parts[1];
            switch (operation) {
                case "View":
                    viewAllMenuItems();
                    break;
                case "ADD":
                    handleAddMenuItem(operationDetails);
                    break;
                case "UPDATE":
                    handleUpdateMenuItem(operationDetails);
                    break;
                case "DELETE":
                    handleDeleteMenuItem(operationDetails);
                    break;
                default:
                    out.println("Unknown command");
                    showMenu();
                    break;
            }
        } else {
            out.println("Invalid command format");
            showMenu();
        }
    }

    private void viewAllMenuItems() throws SQLException {
        List<MenuItem> menuItems = menuService.getAllMenuItems();
        for (MenuItem item : menuItems) {
            out.println(item);
        }
        showMenu();
    }

    private void handleAddMenuItem(String operationDetails) throws IOException, SQLException {
        String[] parts = operationDetails.split(" ");
        if (parts.length == 4) {
            String name = parts[0];
            String description = parts[1];
            double price = Double.parseDouble(parts[2]);
            boolean available = Boolean.parseBoolean(parts[3]);

            MenuItem menuItem = new MenuItem(0, name, description, price, available);
            menuService.addMenuItem(menuItem);
            out.println("Menu item added successfully.");
            showMenu();
        } else {
            out.println("Invalid ADD command format");
            showMenu();
        }
    }

    private void handleUpdateMenuItem(String operationDetails) throws IOException, SQLException {
        String[] parts = operationDetails.split(" ");
        if (parts.length == 5) {
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String description = parts[2];
            double price = Double.parseDouble(parts[3]);
            boolean available = Boolean.parseBoolean(parts[4]);

            MenuItem menuItem = new MenuItem(id, name, description, price, available);
            menuService.updateMenuItem(menuItem);
            out.println("Menu item updated successfully.");
            showMenu();
        } else {
            out.println("Invalid UPDATE command format");
            showMenu();
        }
    }

    private void handleDeleteMenuItem(String operationDetails) throws IOException, SQLException {
        try {
            int id = Integer.parseInt(operationDetails);
            menuService.deleteMenuItem(id);
            out.println("Menu item deleted successfully.");
            showMenu();
        } catch (NumberFormatException e) {
            out.println("Invalid DELETE command format");
            showMenu();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
}
