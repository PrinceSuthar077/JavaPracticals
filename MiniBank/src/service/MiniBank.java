package service;

import java.util.Scanner;

import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

import static java.lang.System.out;

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
            out.println(accounts[i]);
            out.println("Interest Rate: " + accounts[i].interestRate() + "%");
            out.println();
        }

        Account selectedAccount = accounts[0];

        if (selectedAccount instanceof SavingsAccount) {
            out.println("This is a Savings Account");
        }

        WithdrawRule rule1 = new WithdrawRule() {
            public boolean allow(Account account, long amount) {
                return amount <= account.getBalance();
            }
        };

        WithdrawRule rule2 = (account, amount) ->
                amount <= account.getBalance();

        out.println("Anonymous Class Withdrawal: "
                + rule1.allow(selectedAccount, 2000));

        out.println("Lambda Withdrawal: "
                + rule2.allow(selectedAccount, 2000));

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT Branch");

        out.println("================================");
        out.println(bank);
        out.println("================================");

        while (true) {

            out.println("\n1. Open Account");
            out.println("2. Deposit");
            out.println("3. Withdraw");
            out.println("4. Transfer");
            out.println("5. Exit");
            out.print("Enter your choice: ");

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
                out.println("Invalid choice");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT ->
                    out.println("Open Account - to be implemented");

                case DEPOSIT ->
                    out.println("Deposit - to be implemented");

                case WITHDRAW ->
                    out.println("Withdraw - to be implemented");

                case TRANSFER ->
                    out.println("Transfer - to be implemented");

                case EXIT -> {
                    out.println("Thank you for using MiniBank!");
                    sc.close();
                    return;
                }
            }
        }
    }
}