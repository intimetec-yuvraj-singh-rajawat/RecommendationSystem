package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import Service.ChefService;

public class ChefController {
    private PrintWriter out;
    private BufferedReader in;
    private ChefService chefService;

    public ChefController(PrintWriter out, BufferedReader in) throws SQLException {
        this.out = out;
        this.in = in;
        this.chefService = new ChefService();
    }

    public void processCommand(String command) throws IOException, SQLException {
        if (command.startsWith("Chef_VIEW")) {
            chefService.viewAllMenuItems(out);
        } else if (command.startsWith("Chef_RECOMMEND")) {
            String[] parts = command.split(" ");
            if (parts.length >= 3) {
                String mealCategory = parts[1];
                int numberOfItems = Integer.parseInt(parts[2]);
                if (numberOfItems > 0) {
                    chefService.seeRecommendedItems(mealCategory, numberOfItems, out);
                } else {
                    out.println("Invalid number of items specified.");
                }
            } else {
                out.println("Invalid command format");
            }
        } else if (command.startsWith("Chef_ROLLOUT")) {
            chefService.rollOutItems(command.split(" ")[1], out);
        } else if (command.startsWith("Chef_Discard_Menu")) {
            chefService.viewDiscardItems(out);
        } else if (command.startsWith("Chef_DELETE_DISCARD")) {
            chefService.deleteDiscardMenuItems(command.split(" ")[1], out);
        } else if (command.startsWith("Chef_REQUEST_DETAILED_FEEDBACK")) {
            String[] parts = command.split("#");
            if (parts.length >= 3) {
                int itemId = Integer.parseInt(parts[1]);
                String itemName = parts[2];
                chefService.requestDetailedFeedback(itemId, itemName);
                out.println("Detailed feedback request has been sent for item ID " + itemId);
            } else {
                out.println("Invalid command format");
            }
        } else {
            out.println("Unknown chef command");
        }
        out.flush();
    }
}
