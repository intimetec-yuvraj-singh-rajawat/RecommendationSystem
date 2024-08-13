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
            handleViewRolledOutItems(command);
        } else if (command.startsWith("Employee_VOTE")) {
            handleVote(command);
        } else if (command.startsWith("Employee_VIEW_FULL_MENU")) {
            handleViewFullMenu();
        } else if (command.startsWith("Employee_GIVE_FEEDBACK")) {
            handleGiveFeedback(command);
        } else if (command.startsWith("Employee_UPDATE_PROFILE")) {
            handleUpdateProfile(command);
        } else if (command.startsWith("Employee_SUBMIT_DETAILED_FEEDBACK")) {
            handleSubmitDetailedFeedback(command);
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

    private void handleViewRolledOutItems(String command) throws IOException {
        String[] parts = command.split(" ");
        if (parts.length == 2) {
            int userId = Integer.parseInt(parts[1]);
            try {
                List<String> rolledOutItems = employeeService.getRolledOutItemsForToday(userId);
                for (String item : rolledOutItems) {
                    out.println(item);
                }
                out.println("END_OF_ROLLED_OUT_ITEMS");
                out.flush();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            out.println("Invalid VIEW_ROLLED_OUT_ITEMS command.");
            out.flush();
        }
    }

    private void handleVote(String command) throws IOException {
        String[] parts = command.split(" ");
        if (parts.length == 2) {
            int itemId = Integer.parseInt(parts[1]);
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

    private void handleUpdateProfile(String command) throws IOException {
        String[] parts = command.split("#", 6);
        if (parts.length == 6) {
            int userId = Integer.parseInt(parts[1]);
            String preference = parts[2];
            String spiceLevel = parts[3];
            String cuisine = parts[4];
            String sweetTooth = parts[5];
            try {
                employeeService.updateProfile(userId, preference, spiceLevel, cuisine, sweetTooth);
                out.println("Profile updated successfully.");
            } catch (SQLException e) {
                out.println("Error updating profile: " + e.getMessage());
            }
            out.flush();
        } else {
            out.println("Invalid UPDATE_PROFILE command.");
            out.flush();
        }
    }

    private void handleSubmitDetailedFeedback(String command) throws IOException {
        String[] parts = command.split("#", 5);
        if (parts.length == 5) {
            int itemId = Integer.parseInt(parts[1]);
            String question1 = parts[2];
            String question2 = parts[3];
            String question3 = parts[4];
            try {
                employeeService.submitDetailedFeedback(itemId, question1, question2, question3);
                out.println("Detailed feedback submitted successfully.");
            } catch (SQLException e) {
                out.println("Error submitting detailed feedback: " + e.getMessage());
            }
            out.flush();
        } else {
            out.println("Invalid SUBMIT_DETAILED_FEEDBACK command.");
            out.flush();
        }
    }
}
