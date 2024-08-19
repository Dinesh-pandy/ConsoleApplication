package console;

import java.util.List;

public class Order {
    private int orderId;
    private int tableId;
    private List<MenuItem> items;

    public Order(int orderId, int tableId, List<MenuItem> items) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.items = items;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
