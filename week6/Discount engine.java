import java.util.Scanner;

interface DiscountRule {
    double apply(double price);
}

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        double[] prices = {1000, 2000, 3000};

        System.out.println("1. 10% Discount");
        System.out.println("2. 20% Discount");
        System.out.println("3. 500 Flat Discount");

        int choice = sc.nextInt();

        DiscountRule rule;

        if (choice == 1) {
            rule = price -> price * 0.90;
        } else if (choice == 2) {
            rule = price -> price * 0.80;
        } else {
            rule = price -> price - 500;
        }
        for (int i = 0; i < prices.length; i++) {
            System.out.println("Original: " + prices[i]);
            System.out.println("Discounted: " + rule.apply(prices[i]));
        }
        sc.close();
    }
}
