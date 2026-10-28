package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import model.Account;
import model.AccountStore;
import model.AccountWorker;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.Repository;
import exception.BankException;
import exception.AccountNotFoundException;
import util.AnnotationValidator;
import util.StatePersister;
import util.TransactionLog;
import util.ReportGenerator;

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

    private static final Path DATA_FOLDER = Path.of("data");
    private static final Path ACCOUNTS_FILE = DATA_FOLDER.resolve("accounts.dat");
    private static final Path LOGS_FOLDER = Path.of("logs");
    private static final Path REPORT_FILE = DATA_FOLDER.resolve("bank-report.txt");

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

    private static void saveAccounts(ArrayList<Account> accounts) throws IOException {
        Files.createDirectories(DATA_FOLDER);
        StatePersister.save(accounts.toArray(new Account[0]), ACCOUNTS_FILE);
    }

    private static void logTransaction(String type, Account account, long amount)
            throws IOException {
        Files.createDirectories(LOGS_FOLDER);

        Path logFile = LOGS_FOLDER.resolve("transactions.log");

        TransactionLog.append(
                logFile,
                type + ": " + amount
                        + " | Account: " + account.getAccountNumber()
                        + " | Owner: " + account.getOwnerName());
    }

    private static void generateReport() throws IOException {
        Files.createDirectories(DATA_FOLDER);
        ReportGenerator.generate(LOGS_FOLDER, REPORT_FILE);
        out.println("Report generated at: " + REPORT_FILE);
    }

    public static void main(String[] args) {

        ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(new SavingsAccount("Prince", 5000, 1000));
        accounts.add(new CurrentAccount("Rahul", 3000, 2000));
        accounts.add(new FixedDepositAccount("Amit", 10000));

        Repository<Account> accountRepository = new Repository<>();

        accountRepository.saveAll(accounts.toArray(new Account[0]));

        out.println("\n===== Repository: Find Account =====");

        Account foundAccount = accountRepository.findById("AC0001");
        out.println(foundAccount);

        out.println("\n===== Repository: All Accounts =====");

        for (Account account : accountRepository.findAll()) {
            out.println(account);
        }

        Integer[] numbers = {10, 20, 30, 40, 50};

        out.println("\nAverage: " + Repository.average(numbers));

        AccountStore accountStore = new AccountStore();
        TransactionProcessor transactionProcessor = new TransactionProcessor();

        for (Account account : accounts) {
            accountStore.add(account);
            transactionProcessor.addAccount(account);
        }

        out.println("\n===== Accounts Sorted by Balance =====");

        for (Account account : accountStore.sortedByBalance()) {
            out.println(account);
        }

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
                transactionProcessor.stop();
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
            out.println("5. Save, Load and Generate Report");
            out.println("6. Exit");
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
                case 5, 6 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                out.println("Invalid choice.");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> {
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
                                account = new SavingsAccount(name, balance, minimum);
                            }
                            case 2 -> {
                                out.print("Enter overdraft limit: ");
                                long limit = Long.parseLong(sc.nextLine());
                                account = new CurrentAccount(name, balance, limit);
                            }
                            case 3 -> account = new FixedDepositAccount(name, balance);
                            default -> {
                                out.println("Invalid account type.");
                                continue;
                            }
                        }

                        accounts.add(account);
                        accountStore.add(account);
                        transactionProcessor.addAccount(account);
                        accountRepository.save(account);

                        out.println("Account opened successfully.");
                        out.println(account);
                    }

                    case 2 -> {
                        out.print("Enter account number: ");
                        String number = sc.nextLine();

                        Account account = accountStore.get(number);

                        if (account == null) {
                            throw new AccountNotFoundException(
                                    "Account not found: " + number);
                        }

                        out.print("Enter deposit amount: ");
                        long depositAmount = Long.parseLong(sc.nextLine());

                        account.deposit(depositAmount);
                        logTransaction("Deposit", account, depositAmount);

                        accountStore.addTransaction(
                                "Deposit: " + depositAmount
                                        + " | Account: " + account.getAccountNumber());

                        out.println("Deposit successful.");
                        out.println("Updated balance: " + account.getBalance());
                    }

                    case 3 -> {
                        out.print("Enter account number: ");
                        String number = sc.nextLine();

                        Account account = accountStore.get(number);

                        if (account == null) {
                            throw new AccountNotFoundException(
                                    "Account not found: " + number);
                        }

                        out.print("Enter withdrawal amount: ");
                        long withdrawalAmount = Long.parseLong(sc.nextLine());

                        account.withdraw(withdrawalAmount);
                        logTransaction("Withdrawal", account, withdrawalAmount);

                        accountStore.addTransaction(
                                "Withdrawal: " + withdrawalAmount
                                        + " | Account: " + account.getAccountNumber());

                        out.println("Withdrawal successful.");
                        out.println("Updated balance: " + account.getBalance());
                    }

                    case 4 -> {
                        out.print("Enter source account number: ");
                        String sourceNumber = sc.nextLine();

                        Account source = accountStore.get(sourceNumber);

                        if (source == null) {
                            throw new AccountNotFoundException(
                                    "Source account not found: " + sourceNumber);
                        }

                        out.print("Enter destination account number: ");
                        String destinationNumber = sc.nextLine();

                        Account destination = accountStore.get(destinationNumber);

                        if (destination == null) {
                            throw new AccountNotFoundException(
                                    "Destination account not found: " + destinationNumber);
                        }

                        out.print("Enter transfer amount: ");
                        long transferAmount = Long.parseLong(sc.nextLine());

                        source.transfer(destination, transferAmount);

                        logTransaction("Withdrawal", source, transferAmount);
                        logTransaction("Deposit", destination, transferAmount);

                        accountStore.addTransaction(
                                "Transfer withdrawal: " + transferAmount
                                        + " | Account: " + source.getAccountNumber());

                        accountStore.addTransaction(
                                "Transfer deposit: " + transferAmount
                                        + " | Account: " + destination.getAccountNumber());

                        out.println("Transfer successful.");
                        out.println("Source balance: " + source.getBalance());
                        out.println("Destination balance: " + destination.getBalance());
                    }

                    case 5 -> {
                        Files.createDirectories(DATA_FOLDER);
                        saveAccounts(accounts);

                        out.println("Accounts saved successfully.");

                        Account[] loadedAccounts =
                                StatePersister.load(ACCOUNTS_FILE);

                        out.println("\n===== Reloaded Accounts =====");

                        for (Account account : loadedAccounts) {
                            out.println(account);
                        }

                        out.println("\n===== Accounts Sorted by Balance =====");

                        for (Account account : accountStore.sortedByBalance()) {
                            out.println(account);
                        }

                        out.println("\n===== Transaction History =====");

                        for (String transaction : accountStore.getTransactionHistory()) {
                            out.println(transaction);
                        }

                        generateReport();
                        out.println(Files.readString(REPORT_FILE));
                    }

                    case 6 -> {
                        saveAccounts(accounts);
                        out.println("Accounts saved successfully.");
                        out.println("Thank you for using MiniBank!");
                        transactionProcessor.stop();
                        sc.close();
                        return;
                    }
                }
            } catch (BankException e) {
                out.println("Banking error: " + e.getMessage());
            } catch (NumberFormatException e) {
                out.println("Invalid number. Please try again.");
            } catch (IOException e) {
                out.println("File operation error: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                out.println("Could not load saved accounts: " + e.getMessage());
            }
        }
    }
}
