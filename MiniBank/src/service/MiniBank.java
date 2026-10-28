
package service;

import java.util.ArrayList;
import java.util.Scanner;

import model.Account;
import model.AccountWorker;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import exception.BankException;
import exception.AccountNotFoundException;
import util.AnnotationValidator;

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

    public static Account findAccount(ArrayList<Account> accounts, String accountNumber)
            throws AccountNotFoundException {

        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }

        throw new AccountNotFoundException(
                "Account not found: " + accountNumber);
    }

    public static void main(String[] args) {

        ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(new SavingsAccount("Prince", 5000, 1000));
        accounts.add(new CurrentAccount("Rahul", 3000, 2000));
        accounts.add(new FixedDepositAccount("Amit", 10000));

        for (int i = 0; i < accounts.size(); i++) {
            out.println(accounts.get(i));
            out.println("Interest Rate: "
                    + accounts.get(i).interestRate() + "%");
            out.println();
        }

        Account selectedAccount = accounts.get(0);

        if (selectedAccount instanceof SavingsAccount) {
            out.println("This is a Savings Account");
        }

        WithdrawRule rule1 = new WithdrawRule() {
            @Override
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

        out.println("\n===== Annotation Validation =====");

        Account testAccount = new SavingsAccount("Test User", -500, 100);

        String[] errors = AnnotationValidator.validate(testAccount);

        if (errors.length == 0) {
            out.println("No validation errors found.");
        } else {
            for (String error : errors) {
                out.println(error);
            }
        }

        out.println("\n===== Multithreading Test =====");

        Account sharedAccount = new SavingsAccount("Thread Test", 0, 0);
        int threadCount = 10;
        int times = 10000;
        long amount = 1;

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            AccountWorker worker = new AccountWorker(
                    sharedAccount, times, amount);

            threads[i] = new Thread(worker, "Worker-" + (i + 1));
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                out.println("Main thread interrupted.");
                return;
            }
        }

        long expectedBalance = (long) threadCount * times * amount;

        out.println("Expected balance: " + expectedBalance);
        out.println("Actual balance: " + sharedAccount.getBalance());

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

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                out.println("Please enter a valid menu number.");
                continue;
            }

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                out.println("Invalid choice.");
                continue;
            }

            try {
                switch (option) {
                    case OPEN_ACCOUNT -> {
                        out.println("1. Savings Account");
                        out.println("2. Current Account");
                        out.println("3. Fixed Deposit Account");
                        out.print("Choose account type: ");

                        int type = Integer.parseInt(sc.nextLine());

                        out.print("Enter owner name: ");
                        String name = sc.nextLine();

                        out.print("Enter opening balance: ");
                        long balance = Long.parseLong(sc.nextLine());

                        Account account;

                        switch (type) {
                            case 1 -> {
                                out.print("Enter minimum balance: ");
                                long minimum = Long.parseLong(sc.nextLine());
                                account = new SavingsAccount(
                                        name, balance, minimum);
                            }
                            case 2 -> {
                                out.print("Enter overdraft limit: ");
                                long limit = Long.parseLong(sc.nextLine());
                                account = new CurrentAccount(
                                        name, balance, limit);
                            }
                            case 3 -> account =
                                    new FixedDepositAccount(name, balance);
                            default -> {
                                out.println("Invalid account type.");
                                continue;
                            }
                        }

                        accounts.add(account);
                        out.println("Account opened successfully.");
                        out.println(account);
                    }

                    case DEPOSIT -> {
                        out.print("Enter account number: ");
                        String number = sc.nextLine();

                        Account account = findAccount(accounts, number);

                        out.print("Enter deposit amount: ");
                        long depositAmount = Long.parseLong(sc.nextLine());

                        account.deposit(depositAmount);

                        out.println("Deposit successful.");
                        out.println("Updated balance: "
                                + account.getBalance());
                    }

                    case WITHDRAW -> {
                        out.print("Enter account number: ");
                        String number = sc.nextLine();

                        Account account = findAccount(accounts, number);

                        out.print("Enter withdrawal amount: ");
                        long withdrawalAmount = Long.parseLong(sc.nextLine());

                        account.withdraw(withdrawalAmount);

                        out.println("Withdrawal successful.");
                        out.println("Updated balance: "
                                + account.getBalance());
                    }

                    case TRANSFER -> {
                        out.print("Enter source account number: ");
                        String sourceNumber = sc.nextLine();

                        Account source = findAccount(
                                accounts, sourceNumber);

                        out.print("Enter destination account number: ");
                        String destinationNumber = sc.nextLine();

                        Account destination = findAccount(
                                accounts, destinationNumber);

                        out.print("Enter transfer amount: ");
                        long transferAmount = Long.parseLong(sc.nextLine());

                        source.transfer(destination, transferAmount);

                        out.println("Transfer successful.");
                        out.println("Source balance: "
                                + source.getBalance());
                        out.println("Destination balance: "
                                + destination.getBalance());
                    }

                    case EXIT -> {
                        out.println("Thank you for using MiniBank!");
                        sc.close();
                        return;
                    }
                }
            } catch (BankException e) {
                out.println("Banking error: " + e.getMessage());
            } catch (NumberFormatException e) {
                out.println("Invalid number. Please try again.");
            }
        }
    }
}
