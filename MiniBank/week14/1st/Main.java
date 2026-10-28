import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountService service = new AccountService();

        while (true) {
            System.out.println("\n1. Add Account");
            System.out.println("2. Display Accounts");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

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
                break;
            } else {
                try {
                    service.performAction("INVALID");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        sc.close();
    }
}
