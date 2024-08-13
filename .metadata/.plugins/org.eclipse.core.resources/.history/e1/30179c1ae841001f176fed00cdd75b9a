package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EmployeeHandler {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private int choice;

    public EmployeeHandler(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() throws IOException {
        do {
            System.out.println("Employee Menu:");
            System.out.println("1. View Notifications");
            System.out.println("2. Vote for Today's Rolled Out Items");
            System.out.println("3. View Full Menu");
            System.out.println("4. Give Feedback");
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
                viewNotifications();
                break;
            case 2:
                voteForRolledOutItems();
                break;
            case 3:
                viewFullMenu();
                break;
            case 4:
                giveFeedback();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void viewNotifications() throws IOException {
        out.println("Employee_VIEW_NOTIFICATIONS");
        out.flush(); 
        receiveAndPrintMultipleResponses("END_OF_NOTIFICATIONS");
    }

    private void voteForRolledOutItems() throws IOException {
        out.println("Employee_VIEW_ROLLED_OUT_ITEMS");
        out.flush(); 
        
        System.out.println("Today's Rolled Out Items:");
        receiveAndPrintMultipleResponses("END_OF_ROLLED_OUT_ITEMS");

        System.out.print("Enter the item ID to vote for: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        out.println("Employee_VOTE " + itemId);
        out.flush();
        System.out.println(in.readLine()); // Read the vote result
    }

    private void viewFullMenu() throws IOException {
        out.println("Employee_VIEW_FULL_MENU");
        out.flush(); // Ensure the command is sent immediately
        receiveAndPrintMultipleResponses("END_OF_MENU_ITEMS");
    }

    private void giveFeedback() throws IOException {
        System.out.print("Enter Menu Item ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Comment: ");
        String comment = scanner.nextLine();

        out.println("Employee_GIVE_FEEDBACK " + itemId + " " + rating + " " + comment);
        out.flush();
        System.out.println(in.readLine()); 
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

    private void receiveAndPrintMultipleResponses(String endSignal) {
        try {
            String response;
            while (!(response = in.readLine()).equals(endSignal)) {
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
