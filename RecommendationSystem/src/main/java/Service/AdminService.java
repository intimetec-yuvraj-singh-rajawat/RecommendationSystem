package Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import model.MenuItem;

public class AdminService {
    private MenuService menuService = new MenuService();
    
    public String handleAddMenuItem(String operationDetails) throws IOException, SQLException {
        System.out.println("operationDetails" + operationDetails);
        String[] parts = operationDetails.split("#");
        if (parts.length == 5) {
            String name = parts[1];
            String description = parts[2];
            double price = Double.parseDouble(parts[3]);
            boolean available = Boolean.parseBoolean(parts[4]);

            MenuItem menuItem = new MenuItem(0, name, description, price, available);
            menuService.addMenuItem(menuItem);
            return "Menu item added successfully.";
        } else {
            return "Invalid ADD command format";
        }
    }

    public void handleUpdateMenuItem(String operationDetails, PrintWriter out) throws IOException, SQLException {
        String[] parts = operationDetails.split("#");
        if (parts.length == 6) {
            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            String description = parts[3];
            double price = Double.parseDouble(parts[4]);
            boolean available = Boolean.parseBoolean(parts[5]);

            MenuItem menuItem = new MenuItem(id, name, description, price, available);
            menuService.updateMenuItem(menuItem);
            out.println("Menu item updated successfully.");
            out.flush();
        } else {
            out.println("Invalid UPDATE command format");
            out.flush();
        }
    }

    public void handleDeleteMenuItem(String operationDetails, PrintWriter out) throws IOException, SQLException {
        String[] parts = operationDetails.split("#");
        try {
            int id = Integer.parseInt(parts[1]);
            menuService.deleteMenuItem(id);
            out.println("Menu item deleted successfully.");
            out.flush();
        } catch (NumberFormatException e) {
            out.println("Invalid DELETE command format");
            out.flush();
        }
    }
    
    public void viewAllMenuItems(PrintWriter out) throws SQLException {
        List<MenuItem> menuItems = menuService.getAllMenuItems();
        for (MenuItem item : menuItems) {
            out.println(item.toString());
        }
        out.println("END_OF_MENU_ITEMS");
        out.flush();
    }
}
