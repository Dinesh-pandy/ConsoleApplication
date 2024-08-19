package console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Exit implements Printable {
    @Override
    public void print() {
        System.out.println("Have a nice day!");
    }
}

class Message implements Printable {
    @Override
    public void print() {
        System.out.println("Thank you for using our Restaurant Management System.");
    }
}

interface Printable {
    void print();
}

public abstract class RestaurantManagementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static RestaurantModel restaurantModel = new RestaurantModel();

    public static void main(String[] args) {
        restaurantModel.connectToDatabase();
        // Perform other operations if needed, like loading data from the database.

        displayMainMenu();

        restaurantModel.closeDatabaseConnection();
    }

    private static void displayMainMenu() {
        while (true) {
            System.out.println("========== Restaurant Management System ==========");
            System.out.println("1. Table Reservation");
            System.out.println("2. Menu Management");
            System.out.println("3. Order Processing");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    handleTableReservation();
                    break;
                case 2:
                    handleMenuManagement();
                    break;
                case 3:
                    handleOrderProcessing();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void handleTableReservation() {
        System.out.println("=== Table Reservation ===");
        // Display available tables
        List<Table> availableTables = new ArrayList<>();
        for (Table table : restaurantModel.getTables()) {
            if (!table.isReserved()) {
                availableTables.add(table);
            }
        }

        if (availableTables.isEmpty()) {
            System.out.println("No available tables for reservation.");
            return;
        }

        System.out.println("Available Tables:");
        for (int i = 0; i < availableTables.size(); i++) {
            System.out.println((i + 1) + ". " + availableTables.get(i));
        }

        System.out.print("Enter the table number to reserve (0 to cancel): ");
        int tableNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (tableNumber != 0 && tableNumber <= availableTables.size()) {
            Table selectedTable = availableTables.get(tableNumber - 1);
            selectedTable.setReserved(true);

            // Update the table reservation status in the database
            restaurantModel.updateTableReservationStatus(selectedTable);

            System.out.println("Table " + selectedTable.getTableId() + " has been reserved.");
        } else {
            System.out.println("Invalid table number or reservation canceled.");
        }
    }


    private static void handleMenuManagement() {
        while (true) {
            System.out.println("=== Menu Management ===");
            System.out.println("1. Add Menu Item");
            System.out.println("2. Update Menu Item");
            System.out.println("3. Delete Menu Item");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    addMenuItem();
                    break;
                case 2:
                    updateMenuItem();
                    break;
                case 3:
                    deleteMenuItem();
                    break;
                case 0:
                    // Back to main menu
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    protected static void addMenuItem() {
        System.out.println("=== Add Menu Item ===");
        System.out.print("Enter the item_id: ");
        int itemId = scanner.nextInt();
        System.out.print("Enter the item name: ");
        String itemName = scanner.next();
        System.out.print("Enter the item price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        MenuItem newItem = new MenuItem(itemId,itemName, price);
        restaurantModel.addMenuItem(newItem);
        System.out.println("Menu item added: " + newItem);
    }

    protected static void updateMenuItem() {
        System.out.println("=== Update Menu Item ===");
        System.out.print("Enter the item ID to update: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        MenuItem itemToUpdate = restaurantModel.findMenuItemById(itemId);
        if (itemToUpdate != null) {
            System.out.println("Current details: " + itemToUpdate);
            System.out.print("Enter the new item name (leave empty to keep current): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                itemToUpdate.setItemName(newName);
            }

            System.out.print("Enter the new item price (leave 0 to keep current): ");
            double newPrice = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character
            if (newPrice > 0) {
                itemToUpdate.setPrice(newPrice);
            }

            System.out.println("Menu item updated: " + itemToUpdate);
        } else {
            System.out.println("Item not found. No update performed.");
        }
    }

    protected static void deleteMenuItem() {
        System.out.println("=== Delete Menu Item ===");
        System.out.print("Enter the item ID to delete: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        MenuItem itemToDelete = restaurantModel.findMenuItemById(itemId);
        if (itemToDelete != null) {
            restaurantModel.deleteMenuItem(itemToDelete);
            System.out.println("Menu item deleted: " + itemToDelete);
        } else {
            System.out.println("Item not found. No deletion performed.");
        }
    }

    private static void handleOrderProcessing() {
        System.out.println("=== Order Processing ===");
        // Display available tables
        List<Table> availableTables = new ArrayList<>();
        for (Table table : restaurantModel.getTables()) {
            if (!table.isReserved()) {
                availableTables.add(table);
            }
        }

        if (availableTables.isEmpty()) {
            System.out.println("No available tables to place an order.");
            return;
        }

        System.out.println("Available Tables:");
        for (int i = 0; i < availableTables.size(); i++) {
            System.out.println((i + 1) + ". " + availableTables.get(i));
        }

        System.out.print("Enter the table number to place an order (0 to cancel): ");
        int tableNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (tableNumber != 0 && tableNumber <= availableTables.size()) {
            Table selectedTable = availableTables.get(tableNumber - 1);
            List<MenuItem> menuItems = restaurantModel.getMenuItems();
            List<MenuItem> orderedItems = new ArrayList<>();

            while (true) {
                System.out.println("Menu Items:");
                for (int i = 0; i < menuItems.size(); i++) {
                    System.out.println((i + 1) + ". " + menuItems.get(i));
                }

                System.out.print("Enter the menu item number to add to the order (0 to finish): ");
                int itemNumber = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (itemNumber == 0) {
                    break;
                } else if (itemNumber > 0 && itemNumber <= menuItems.size()) {
                    MenuItem selectedMenuItem = menuItems.get(itemNumber - 1);
                    orderedItems.add(selectedMenuItem);
                } else {
                    System.out.println("Invalid menu item number. Please try again.");
                }
            }

            if (!orderedItems.isEmpty()) {
                int orderId = restaurantModel.placeOrder(selectedTable, orderedItems);
                System.out.println("Order placed successfully for Table " + selectedTable.getTableId() );
            } else {
                System.out.println("No items added to the order. Order canceled.");
            }
        } else {
            System.out.println("Invalid table number or order canceled.");
        }
    }


}
