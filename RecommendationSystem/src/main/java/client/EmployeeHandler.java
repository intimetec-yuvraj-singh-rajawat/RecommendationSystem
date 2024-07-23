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
    private int userId;

    public EmployeeHandler(Socket socket, PrintWriter out, BufferedReader in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu(int userId) throws IOException {
        int choice;
        do {
            System.out.println("1. View Notifications");
            System.out.println("2. View Rolled Out Items");
            System.out.println("3. Vote for Item");
            System.out.println("4. View Full Menu");
            System.out.println("5. Give Feedback");
            System.out.println("6. Submit Detailed Feedback");
            System.out.println("7. Update Profile");
            System.out.println("8. Logout");
            choice = scanner.nextInt();
            scanner.nextLine(); 
            handleUserChoice(choice, userId);
        } while (choice != 8);

        System.out.println("Logged out. Goodbye!");
    }

    private void handleUserChoice(int choice, int userId) throws IOException {
        switch (choice) {
            case 1:
                viewNotifications();
                break;
            case 2:
                viewRolledOutItems(userId);
                break;
            case 3:
                voteForItem();
                break;
            case 4:
                viewFullMenu();
                break;
            case 5:
                giveFeedback();
                break;
            case 6:
                submitDetailedFeedback();
                break;
            case 7:
                updateProfile(userId);
                break;
            case 8:
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
        String line;
        while (!(line = in.readLine()).equals("END_OF_NOTIFICATIONS")) {
            System.out.println(line);
        }
    }

    private void viewRolledOutItems(int userId) throws IOException {
        out.println("Employee_VIEW_ROLLED_OUT_ITEMS " + userId);
        out.flush();
        String line;
        while (!(line = in.readLine()).equals("END_OF_ROLLED_OUT_ITEMS")) {
            System.out.println(line);
        }
    }

    private void voteForItem() throws IOException {
        System.out.print("Enter Menu Item ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); 
        out.println("Employee_VOTE " + itemId);
        out.flush();
        System.out.println(in.readLine());
    }

    private void viewFullMenu() throws IOException {
        out.println("Employee_VIEW_FULL_MENU");
        out.flush();
        String line;
        while (!(line = in.readLine()).equals("END_OF_MENU_ITEMS")) {
            System.out.println(line);
        }
    }

    private void giveFeedback() throws IOException {
        System.out.print("Enter Menu Item ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter Rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter Comment: ");
        String comment = scanner.nextLine();

        out.println("Employee_GIVE_FEEDBACK " + itemId + " " + rating + " " + comment);
        out.flush();
        System.out.println(in.readLine());
    }

    private void submitDetailedFeedback() throws IOException {
        System.out.print("Enter Menu Item ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Q1. What didn’t you like about this item? ");
        String question1 = scanner.nextLine();

        System.out.print("Q2. How would you like this item to taste? ");
        String question2 = scanner.nextLine();

        System.out.print("Q3. Share your mom’s recipe: ");
        String question3 = scanner.nextLine();

        out.println("Employee_SUBMIT_DETAILED_FEEDBACK#" + itemId + "#" + question1 + "#" + question2 + "#" + question3);
        out.flush();
        System.out.println(in.readLine());
    }

    private void updateProfile(int userId) throws IOException {
        System.out.print("Enter Preference: ");
        String preference = scanner.nextLine();

        System.out.print("Enter Spice Level: ");
        String spiceLevel = scanner.nextLine();

        System.out.print("Enter Cuisine: ");
        String cuisine = scanner.nextLine();

        System.out.print("Enter Sweet Tooth: ");
        String sweetTooth = scanner.nextLine();

        out.println("Employee_UPDATE_PROFILE#" + userId + "#" + preference + "#" + spiceLevel + "#" + cuisine + "#" + sweetTooth);
        out.flush();
        System.out.println(in.readLine());
    }

    private void logout() throws IOException {
        out.println("Employee_LOGOUT");
        out.flush();
        System.out.println("Logged out successfully.");
    }
}
