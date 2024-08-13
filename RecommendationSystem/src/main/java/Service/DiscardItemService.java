package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db.Database;
import model.MenuItem;

public class DiscardItemService {
    private Connection connection;

    public DiscardItemService() {
        try {
            this.connection = Database.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MenuItem> getDiscardMenuItems() throws SQLException {
        List<MenuItem> discardMenuItems = new ArrayList<>();

        String sql = "SELECT mi.id, mi.name, mi.description, mi.price, mi.availability " +
                     "FROM menuitem mi " +
                     "JOIN feedback f ON mi.id = f.item_id " +
                     "GROUP BY mi.id " +
                     "HAVING AVG(f.rating) < 2";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                boolean availability = resultSet.getBoolean("availability");

                MenuItem menuItem = new MenuItem(id, name, description, price, availability);
                discardMenuItems.add(menuItem);
            }
        }

        return discardMenuItems;
    }

    public void addItemsToDiscardTable(List<MenuItem> discardMenuItems) throws SQLException {
        String sql = "INSERT INTO discarditem (item_id, date) VALUES (?, CURDATE())";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (MenuItem menuItem : discardMenuItems) {
                statement.setInt(1, menuItem.getId());
                statement.executeUpdate();
            }
        }
    }

    public void processDiscardItems() throws SQLException {
        List<MenuItem> discardMenuItems = getDiscardMenuItems();
        addItemsToDiscardTable(discardMenuItems);
    }
}
