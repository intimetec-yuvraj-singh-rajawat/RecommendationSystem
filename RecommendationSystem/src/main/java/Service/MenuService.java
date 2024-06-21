package Service;

import db.Database;
import model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuService {
    private final Connection connection;

    public MenuService() throws SQLException {
        this.connection = Database.getConnection();
    }

    public List<MenuItem> getAllMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM MenuItem";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getBoolean("availability")
                );
                menuItems.add(menuItem);
            }
        }
        return menuItems;
    }

    public MenuItem getMenuItemById(int id) throws SQLException {
        String sql = "SELECT * FROM MenuItem WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new MenuItem(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getDouble("price"),
                            resultSet.getBoolean("availability")
                    );
                }
            }
        }
        return null;
    }

    public void addMenuItem(MenuItem menuItem) throws SQLException {
        String sql = "INSERT INTO MenuItem (name, description, price, availability) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, menuItem.getName());
            preparedStatement.setString(2, menuItem.getDescription());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.setBoolean(4, menuItem.isAvailability());
            preparedStatement.executeUpdate();

            // Retrieve auto-generated ID
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                menuItem.setId(generatedKeys.getInt(1));
            }
        }
    }

    public void updateMenuItem(MenuItem menuItem) throws SQLException {
        String sql = "UPDATE MenuItem SET name = ?, description = ?, price = ?, availability = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, menuItem.getName());
            preparedStatement.setString(2, menuItem.getDescription());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.setBoolean(4, menuItem.isAvailability());
            preparedStatement.setInt(5, menuItem.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteMenuItem(int id) throws SQLException {
        String sql = "DELETE FROM MenuItem WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    
    public void addToDailyMenu(int itemId) throws SQLException {
        String sql = "INSERT INTO DailyMenuItem (date, item_id, vote) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new Date(System.currentTimeMillis()));
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, 0);
            pstmt.executeUpdate();
        }
    }
}
