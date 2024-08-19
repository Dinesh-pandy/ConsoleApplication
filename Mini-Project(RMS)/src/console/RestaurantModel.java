package console;

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class RestaurantModel {
	private List<Table> tables;
	private List<MenuItem> menuItems;
    private List<Order> orders;
    private Connection connection;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";
    private static final String PASSWORD = "dinesh@2610";

    public RestaurantModel() {
        tables = new ArrayList<>();
        menuItems = new ArrayList<>();
        orders = new ArrayList<>();
        connection = null;
    }

    public void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public void closeDatabaseConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }

    public void addMenuItem(MenuItem menuItem) {
        try {
            // Prepare the SQL query to insert a new menu item into the database
            String sql = "INSERT INTO menu_items (item_name, price) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, menuItem.getItemName());
            preparedStatement.setDouble(2, menuItem.getPrice());

            // Execute the query to insert the new menu item
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // If the insert was successful, get the auto-generated item ID
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int itemId = generatedKeys.getInt(1);
                    menuItem.setItemId(itemId);
                    // Add the new menu item to the local menuItems list
                    menuItems.add(menuItem);
                    System.out.println("Menu item added: " + menuItem);
                }
            } else {
                System.out.println("Failed to add the menu item.");
            }

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error adding menu item: " + e.getMessage());
        }
    }

    public void updateMenuItem(MenuItem menuItem) {
        try {
            // Prepare the SQL query to update the menu item in the database
            String sql = "UPDATE menu_items SET item_name = ?, price = ? WHERE item_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set the parameters for the prepared statement
            preparedStatement.setString(1, menuItem.getItemName());
            preparedStatement.setDouble(2, menuItem.getPrice());
            preparedStatement.setInt(3, menuItem.getItemId());

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Menu item updated: " + menuItem);
            } else {
                System.out.println("Menu item with ID " + menuItem.getItemId() + " not found. No update performed.");
            }

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error updating menu item: " + e.getMessage());
        }
    }


    public void deleteMenuItem(MenuItem menuItem) {
        try {
            // Prepare the SQL query to delete the menu item from the database
            String sql = "DELETE FROM menu_items WHERE item_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set the parameter for the prepared statement
            preparedStatement.setInt(1, menuItem.getItemId());

            // Execute the delete query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Remove the menu item from the local menuItems list
                menuItems.remove(menuItem);
                System.out.println("Menu item deleted: " + menuItem);
            } else {
                System.out.println("Menu item with ID " + menuItem.getItemId() + " not found. No deletion performed.");
            }

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error deleting menu item: " + e.getMessage());
        }
    }

    public List<Table> getTables() {
        List<Table> tables = new ArrayList<>();

        try {
            // Prepare the SQL query to select all tables from the database
            String sql = "SELECT table_id, capacity, is_reserved FROM tables";
            Statement statement = connection.createStatement();

            // Execute the query
            ResultSet resultSet = statement.executeQuery(sql);

            // Process the result set and create Table objects
            while (resultSet.next()) {
                int tableId = resultSet.getInt("table_id");
                int capacity = resultSet.getInt("capacity");
                boolean isReserved = resultSet.getBoolean("is_reserved");
                Table table = new Table(tableId, capacity, isReserved);
                tables.add(table);
            }

            // Close the Statement and ResultSet
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching tables: " + e.getMessage());
        }

        return tables;
    }

    public List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();

        try {
            // Prepare the SQL query to select all menu items from the database
            String sql = "SELECT item_id, item_name, price FROM menu_items";
            Statement statement = connection.createStatement();

            // Execute the query
            ResultSet resultSet = statement.executeQuery(sql);

            // Process the result set and create MenuItem objects
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                String itemName = resultSet.getString("item_name");
                double price = resultSet.getDouble("price");
                MenuItem menuItem = new MenuItem(itemId, itemName, price);
                menuItems.add(menuItem);
            }

            // Close the Statement and ResultSet
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching menu items: " + e.getMessage());
        }

        return menuItems;
    }

    public MenuItem findMenuItemById(int itemId) {
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getItemId() == itemId) {
                return menuItem;
            }
        }
        return null; // Return null if no matching menu item is found
    }

    public int placeOrder(Table table, List<MenuItem> items) {
        int orderId = -1; // Initialize the order ID to a default value

        try {
            // Prepare the SQL query to insert a new order into the database
            String sql = "INSERT INTO orders (table_id, order_date) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set the parameters for the prepared statement
            preparedStatement.setInt(1, table.getTableId());
//            preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime()));

            // Execute the query to insert the new order
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // If the insert was successful, get the auto-generated order ID
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);

                    // Prepare the SQL query to insert the order items into the database
                    sql = "INSERT INTO order_items (order_id, item_id) VALUES (?, ?)";
                    preparedStatement = connection.prepareStatement(sql);

                    // Set the parameters for the prepared statement and execute the batch insert
                    for (MenuItem item : items) {
                        preparedStatement.setInt(1, orderId);
                        preparedStatement.setInt(2, item.getItemId());
                        preparedStatement.addBatch();
                    }

                    // Execute the batch insert
                    preparedStatement.executeBatch();
                }
            } else {
                System.out.println("Failed to place the order.");
            }

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("");
        }

        return orderId;
    }

    public void updateTableReservationStatus(Table selectedTable) {
        try {
            // Prepare the SQL query to update the reservation status of the table in the database
            String sql = "UPDATE tables SET is_reserved = ? WHERE table_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set the parameters for the prepared statement
            preparedStatement.setBoolean(1, selectedTable.isReserved());
            preparedStatement.setInt(2, selectedTable.getTableId());

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Table reservation status updated for Table " + selectedTable.getTableId());
            } else {
                System.out.println("Table with ID " + selectedTable.getTableId() + " not found. No update performed.");
            }

            // Close the PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error updating table reservation status: " + e.getMessage());
        }
    }


    // Implement other methods for managing tables, orders, and other database operations...
}
class Management extends RestaurantModel {
	public static void main(String[] args) throws ClassNotFoundException {
		RestaurantManagementSystem.main(args);
	}
}
