package app;

import model.Account;
import service.AccountService;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountService service = new AccountService();

        while (true) {
            System.out.println("\n1. Add Account");
            System.out.println("2. Display Accounts");
            System.out.println("3. Save Accounts");
            System.out.println("4. Load Accounts");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                if (choice == 1) {
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    service.addAccount(new Account(id, name));
                    System.out.println("Account added successfully");
                } else if (choice == 2) {
                    for (Account account : service.getAccounts()) {
                        System.out.println(account);
                    }
                } else if (choice == 3) {
                    service.saveToFile("accounts.txt");
                    System.out.println("Accounts saved successfully");
                } else if (choice == 4) {
                    service.loadFromFile("accounts.txt");
                    System.out.println("Accounts loaded successfully");
                } else if (choice == 5) {
                    break;
                } else {
                    System.out.println("Invalid choice");
                }
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }

        sc.close();
    }
}
