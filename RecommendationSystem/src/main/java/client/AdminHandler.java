package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AdminHandler {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private int choice;

    public AdminHandler(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() throws IOException {
        do {
            System.out.println("Select an option:");
            System.out.println("1. View all menu items");
            System.out.println("2. Add a new menu item");
            System.out.println("3. Update an existing menu item");
            System.out.println("4. Delete a menu item");
            System.out.println("5. Logout");
            choice = scanner.nextInt();
            scanner.nextLine();
            handleUserChoice(choice);
        } while (choice != 5);
        
        System.out.println("Logged out. Goodbye!");
    }

    private void handleUserChoice(int choice) throws IOException {
        switch (choice) {
            case 1:
                viewMenu();
                break;
            case 2:
                addMenuItems();
                break;
            case 3:
                updateMenuItems();
                break;
            case 4:
                deleteMenuItems();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void addMenuItems() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Is available (true/false): ");
        boolean available = Boolean.parseBoolean(scanner.nextLine());
        out.println("Admin_ADD"+"#" + name + "#" + description + "#" + price + "#" + available);
        out.flush();
        receiveAndPrintSingleResponse();
    }

    private void updateMenuItems() throws IOException {
        System.out.print("Enter ID of the menu item to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Is available (true/false): ");
        boolean available = Boolean.parseBoolean(scanner.nextLine());
        out.println("Admin_UPDATE"+ "#" + id + "#" + name + "#" + description + "#" + price + "#" + available);
        out.flush();
        receiveAndPrintSingleResponse();
    }

    private void deleteMenuItems() {
        System.out.print("Enter ID of the menu item to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        out.println("Admin_DELETE"+"#" + id);
        out.flush();
        receiveAndPrintSingleResponse();
    }

    private void viewMenu() {
        out.println("Admin_VIEW");
        out.flush();
        receiveAndPrintMultipleResponses();
    }
    
    private void logout() {
        try {
            out.println("LOGOUT");
            out.flush();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing socket: " + e.getMessage());
        }
    }
    
    private void receiveAndPrintSingleResponse() {
        try {
            String response = in.readLine();
            System.out.println("Server reply: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void receiveAndPrintMultipleResponses() {
        try {
            String response;
            System.out.println("Server reply: ");
            while (!(response = in.readLine()).equals("END_OF_MENU_ITEMS")) {
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
