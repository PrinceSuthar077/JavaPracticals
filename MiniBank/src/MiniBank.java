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

        Scanner sc = new Scanner(System.in);

        BankInfo bank =
                new BankInfo("MiniBank", "CHARUSAT Branch");

        System.out.println("================================");
        System.out.println(bank);
        System.out.println("================================");

        System.out.println("\nValidator Tests:");

        System.out.println(
                "Valid Mobile: " +
                Validator.isValidMobile("9876543210")
        );

        System.out.println(
                "Invalid Mobile: " +
                Validator.isValidMobile("12345")
        );

        System.out.println(
                "Valid Email: " +
                Validator.isValidEmail("prince@gmail.com")
        );

        System.out.println(
                "Invalid Email: " +
                Validator.isValidEmail("prince@")
        );

        System.out.println(
                "Valid PAN: " +
                Validator.isValidPan("ABCDE1234F")
        );

        System.out.println(
                "Invalid PAN: " +
                Validator.isValidPan("ABC123")
        );

        System.out.println(
                "Valid IFSC: " +
                Validator.isValidIfsc("SBIN0001234")
        );

        System.out.println(
                "Invalid IFSC: " +
                Validator.isValidIfsc("ABC123")
        );

        System.out.println("\nCommand Test:");

        Command command =
                CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println("Type: " + command.type());
        System.out.println("Account: " + command.accountNumber());
        System.out.println("Amount: " + command.amount());

        Account[] accounts = {
                new Account("Prince", 5000),
                new Account("Rahul", 3000),
                new Account("Amit")
        };

        accounts[0].deposit(1000);
        accounts[0].withdraw(500);

        accounts[1].deposit(2000);
        accounts[1].withdraw(1000);

        accounts[2].deposit(5000);
        accounts[2].withdraw(1500);

        System.out.println("\nAccounts:");

        for (int i = 0; i < accounts.length; i++) {
            System.out.println(accounts[i]);
        }

        System.out.println("\nStatement:");

        System.out.println(
                StatementFormatter.buildStatement(accounts[0])
        );

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
                        System.out.println(
                                "Open Account - to be implemented in a later lab"
                        );

                case DEPOSIT ->
                        System.out.println(
                                "Deposit - to be implemented in a later lab"
                        );

                case WITHDRAW ->
                        System.out.println(
                                "Withdraw - to be implemented in a later lab"
                        );

                case TRANSFER ->
                        System.out.println(
                                "Transfer - to be implemented in a later lab"
                        );

                case EXIT -> {
                    System.out.println(
                            "Thank you for using MiniBank!"
                    );
                    sc.close();
                    return;
                }
            }
        }
    }
}
