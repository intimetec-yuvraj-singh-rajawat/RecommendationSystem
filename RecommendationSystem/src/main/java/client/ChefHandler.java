package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChefHandler {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private int choice;

    public ChefHandler(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() throws IOException {
        do {
            System.out.println("Select an option:");
            System.out.println("1. View all menu items");
            System.out.println("2. See recommended items");
            System.out.println("3. Roll out item(s)");
            System.out.println("4. Request detailed feedback");
            System.out.println("5. View Discard Menu");
            System.out.println("6. Delete Discard Menu Item(s)");
            System.out.println("7. Logout");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline after nextInt()
            handleUserChoice(choice);
        } while (choice != 7);

        System.out.println("Logged out. Goodbye!");
    }

    private void handleUserChoice(int choice) throws IOException {
        switch (choice) {
            case 1:
                viewMenu();
                break;
            case 2:
                seeRecommendedItems();
                break;
            case 3:
                rollOutItems();
                break;
            case 4:
                requestDetailedFeedback();
                break;
            case 5:
                discardMenuItems();
                break;
            case 6:
                deleteDiscardMenuItems();
                break;
            case 7:
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void viewMenu() {
        out.println("Chef_VIEW");
        out.flush();
        receiveAndPrintMultipleResponses();
    }

    private void seeRecommendedItems() {
        System.out.print("Enter the number of breakfast items to recommend: ");
        int breakfastItems = scanner.nextInt();
        System.out.print("Enter the number of lunch items to recommend: ");
        int lunchItems = scanner.nextInt();
        System.out.print("Enter the number of dinner items to recommend: ");
        int dinnerItems = scanner.nextInt();
        scanner.nextLine();

        out.println("Chef_RECOMMEND breakfast " + breakfastItems);
        out.flush();
        receiveAndPrintRecommendations("Breakfast");

        out.println("Chef_RECOMMEND lunch " + lunchItems);
        out.flush();
        receiveAndPrintRecommendations("Lunch");

        out.println("Chef_RECOMMEND dinner " + dinnerItems);
        out.flush();
        receiveAndPrintRecommendations("Dinner");
    }

    private void rollOutItems() {
        System.out.print("Enter IDs of the menu items to roll out (comma separated): ");
        String ids = scanner.nextLine();
        out.println("Chef_ROLLOUT " + ids);
        out.flush();
        receiveAndPrintSingleResponse();
    }

    private void requestDetailedFeedback() {
        System.out.print("Enter the ID of the menu item to request feedback for: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter the name of the menu item: ");
        String itemName = scanner.nextLine();

        out.println("Chef_REQUEST_DETAILED_FEEDBACK " +"#"+ itemId + "#" + itemName);
        out.flush();
        receiveAndPrintSingleResponse();
    }

    private void discardMenuItems() throws IOException {
        out.println("Chef_Discard_Menu");
        String inputLine;
        while (!(inputLine = in.readLine()).equals("END_OF_ITEMS")) {
            System.out.println(inputLine);
        }
    }

    private void deleteDiscardMenuItems() {
        System.out.print("Enter IDs of the discard menu items to delete (comma separated): ");
        String ids = scanner.nextLine();
        out.println("Chef_DELETE_DISCARD " + ids);
        out.flush();
        receiveAndPrintSingleResponse();
    }

    private void logout() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing socket: " + e.getMessage());
        }
    }

    private void receiveAndPrintSingleResponse() {
        try {
            String response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveAndPrintMultipleResponses() {
        try {
            String response;
            while (!(response = in.readLine()).equals("END_OF_ITEMS")) {
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveAndPrintRecommendations(String mealCategory) {
        System.out.println("Recommended " + mealCategory + " Items:");
        try {
            String response;
            while (!(response = in.readLine()).equals("END_OF_RECOMMENDATIONS")) {
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
