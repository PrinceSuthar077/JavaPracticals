import java.util.*;

class OutOfStockException extends Exception {
    int shortfall;

    OutOfStockException(int shortfall) {
        super("Out of stock. Shortfall: " + shortfall);
        this.shortfall = shortfall;
    }
}

class InvalidQuantityException extends Exception {
    InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {

    int stock = 10;

    void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {

        if (qty <= 0)
            throw new InvalidQuantityException("Invalid quantity");

        if (qty > stock)
            throw new OutOfStockException(qty - stock);

        stock -= qty;

        System.out.println(qty + " " + item + " issued");
    }
}
public class Main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        String[] items = {"Pen", "Book", "Bag", "Pencil"};
        int[] quantities = {3, 12, 0, 4};
        for (int i = 0; i < items.length; i++) {
            try {
                warehouse.issue(items[i], quantities[i]);
            } catch (OutOfStockException e) {
                System.out.println(items[i] + ": " + e.getMessage());
            } catch (InvalidQuantityException e) {
                System.out.println(items[i] + ": " + e.getMessage());
            }
        }
    }
}
