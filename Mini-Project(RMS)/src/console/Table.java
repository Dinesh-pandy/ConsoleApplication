package console;

public class Table {
    private int tableId;
    private int capacity;
    private boolean isReserved;

    public Table(int tableId, int capacity, boolean isReserved) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.isReserved = isReserved;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    @Override
    public String toString() {
        return "Table " + tableId + " - Capacity: " + capacity + " - Reserved: " + isReserved;
    }
}
