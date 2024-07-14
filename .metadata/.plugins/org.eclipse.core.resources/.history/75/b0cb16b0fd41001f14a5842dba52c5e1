package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import Service.EmployeeService;
import Service.FeedbackService;
import Service.MenuService;
import model.Notification;

public class EmployeeController {
    private PrintWriter out;
    private BufferedReader in;
    private EmployeeService employeeService;
    private FeedbackService feedbackService;
    private MenuService menuService;

    public EmployeeController(PrintWriter out, BufferedReader in) throws SQLException {
        this.out = out;
        this.in = in;
        this.employeeService = new EmployeeService();
        this.feedbackService = new FeedbackService();
        this.menuService = new MenuService();
    }

    public void processCommand(String command) throws IOException {
        if (command.startsWith("Employee_VIEW_NOTIFICATIONS")) {
            handleViewNotifications();
        } else if (command.startsWith("Employee_VIEW_ROLLED_OUT_ITEMS")) {
            handleViewRolledOutItems();
        } else if (command.startsWith("Employee_VOTE")) {
            handleVote(command);
        } else if (command.startsWith("Employee_VIEW_FULL_MENU")) {
            handleViewFullMenu();
        } else if (command.startsWith("Employee_GIVE_FEEDBACK")) {
            handleGiveFeedback(command);
        }
    }

    private void handleViewNotifications() throws IOException {
        try {
            List<Notification> notifications = employeeService.getNotifications();
            for (Notification notification : notifications) {
                out.println(notification);
            }
            out.println("END_OF_NOTIFICATIONS");
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleViewRolledOutItems() throws IOException {
        try {
            List<String> rolledOutItems = employeeService.getRolledOutItemsForToday();
            for (String item : rolledOutItems) {
                out.println(item);
            }
            out.println("END_OF_ROLLED_OUT_ITEMS");
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleVote(String command) throws IOException {
        String[] parts = command.split(" ");
        if (parts.length == 2) {
            int itemId = Integer.parseInt(parts[1]);
            System.out.println("Voting for item ID: " + itemId); // Logging
            try {
                employeeService.voteForItem(itemId);
                out.println("Vote registered successfully.");
            } catch (SQLException e) {
                out.println("Error registering vote: " + e.getMessage());
            }
            out.flush();
        } else {
            out.println("Invalid VOTE command.");
            out.flush();
        }
    }

    private void handleViewFullMenu() throws IOException {
        try {
            List<String> menuItems = menuService.getAllMenuItemsFormatted();
            for (String item : menuItems) {
                out.println(item);
            }
            out.println("END_OF_MENU_ITEMS");
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleGiveFeedback(String command) throws IOException {
        String[] parts = command.split(" ", 4);
        if (parts.length == 4) {
            int itemId = Integer.parseInt(parts[1]);
            int rating = Integer.parseInt(parts[2]);
            String comment = parts[3];
            try {
                feedbackService.giveFeedback(itemId, rating, comment);
                out.println("Feedback submitted successfully.");
            } catch (SQLException e) {
                out.println("Error submitting feedback: " + e.getMessage());
            }
            out.flush();
        } else {
            out.println("Invalid GIVE_FEEDBACK command.");
            out.flush();
        }
    }
}
