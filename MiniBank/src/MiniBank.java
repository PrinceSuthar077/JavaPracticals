import java.util.Scanner;

record BankInfo(String name, String branch) {
}

enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}

public class MiniBank {

    public static void main(String[] args) {

        Account[] accounts = {
            new SavingsAccount("Prince", 5000, 1000),
            new CurrentAccount("Rahul", 3000, 2000),
            new FixedDepositAccount("Amit", 10000)
        };

        for (int i = 0; i < accounts.length; i++) {
            System.out.println(accounts[i]);
            System.out.println("Interest Rate: " + accounts[i].interestRate() + "%");
            System.out.println();
        }

        Account account = accounts[0];

        if (account instanceof SavingsAccount) {
            System.out.println("This is a Savings Account");
        }

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT Branch");

        System.out.println("================================");
        System.out.println(bank);
        System.out.println("================================");

        while (true) {

            System.out.println("\n1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT ->
                    System.out.println("Open Account - to be implemented");
                case DEPOSIT ->
                    System.out.println("Deposit - to be implemented");
                case WITHDRAW ->
                    System.out.println("Withdraw - to be implemented");
                case TRANSFER ->
                    System.out.println("Transfer - to be implemented");
                case EXIT -> {
                    System.out.println("Thank you for using MiniBank!");
                    sc.close();
                    return;
                }
            }
        }
    }
}
