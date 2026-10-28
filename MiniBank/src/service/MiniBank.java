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
    STATEMENT,
    REPORT,
    EXIT
}

public class MiniBank {

    private static final Path DATA_FOLDER = Path.of("data");
    private static final Path ACCOUNTS_FILE = DATA_FOLDER.resolve("accounts.dat");
    private static final Path LOGS_FOLDER = Path.of("logs");
    private static final Path REPORT_FILE = DATA_FOLDER.resolve("bank-report.txt");

    public static Account findAccount(ArrayList<Account> accounts,
            String accountNumber) throws AccountNotFoundException {

        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }

        throw new AccountNotFoundException(
                "Account not found: " + accountNumber);
    }

    private static void saveAccounts(ArrayList<Account> accounts)
            throws IOException {

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

    private static void registerAccount(Account account,
            ArrayList<Account> accounts,
            Repository<Account> repository,
            AccountStore accountStore,
            TransactionProcessor transactionProcessor) {

        if (!accounts.contains(account)) {
            accounts.add(account);
        }

        repository.save(account);
        accountStore.add(account);
        transactionProcessor.addAccount(account);
    }

    public static void main(String[] args) {

        ArrayList<Account> accounts = new ArrayList<>();
        Repository<Account> accountRepository = new Repository<>();
        AccountStore accountStore = new AccountStore();
        TransactionProcessor transactionProcessor = new TransactionProcessor();

        try {
            if (Files.exists(ACCOUNTS_FILE)) {
                Account[] savedAccounts = StatePersister.load(ACCOUNTS_FILE);

                for (Account account : savedAccounts) {
                    registerAccount(
                            account,
                            accounts,
                            accountRepository,
                            accountStore,
                            transactionProcessor);
                }

                Account.synchronizeAccountCounter(accounts);
                out.println("Saved accounts loaded successfully.");
            } else {
                accounts.add(new SavingsAccount("Prince", 5000, 1000));
                accounts.add(new CurrentAccount("Rahul", 3000, 2000));
                accounts.add(new FixedDepositAccount("Amit", 10000));

                for (Account account : accounts) {
                    registerAccount(
                            account,
                            accounts,
                            accountRepository,
                            accountStore,
                            transactionProcessor);
                }

                Account.synchronizeAccountCounter(accounts);
            }
        } catch (IOException | ClassNotFoundException e) {
            out.println("Could not load saved accounts: " + e.getMessage());
            transactionProcessor.stop();
            return;
        }

        out.println("\n===== Repository: Find Account =====");

        if (!accounts.isEmpty()) {
            Account foundAccount = accountRepository.findById(
                    accounts.get(0).getAccountNumber());
            out.println(foundAccount);
        } else {
            out.println("No accounts available.");
        }

        out.println("\n===== Repository: All Accounts =====");

        for (Account account : accountRepository.findAll()) {
            out.println(account);
        }

        Integer[] numbers = {10, 20, 30, 40, 50};
        out.println("\nAverage: " + Repository.average(numbers));

        out.println("\n===== Accounts Sorted by Balance =====");

        for (Account account : accountStore.sortedByBalance()) {
            out.println(account);
        }

        for (Account account : accounts) {
            out.println(account);
            out.println("Interest Rate: " + account.interestRate() + "%");
            out.println();
        }

        if (!accounts.isEmpty()) {
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
        }

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

        boolean interrupted = false;

        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                interrupted = true;
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (interrupted) {
            transactionProcessor.stop();
            out.println("Main thread interrupted.");
            return;
        }

        long expectedBalance = (long) threadCount * times * amount;

        out.println("Expected balance: " + expectedBalance);
        out.println("Actual balance: " + sharedAccount.getBalance());

        BankService bankService = new BankService(
                accountRepository, accountStore);

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
            out.println("5. Statement");
            out.println("6. Save, Load and Generate Report");
            out.println("7. Exit");
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
                case 5 -> MenuOption.STATEMENT;
                case 6 -> MenuOption.REPORT;
                case 7 -> MenuOption.EXIT;
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

                        if (type < 1 || type > 3) {
                            out.println("Invalid account type.");
                            continue;
                        }

                        out.print("Enter owner name: ");
                        String name = sc.nextLine();

                        out.print("Enter opening balance: ");
                        long balance = Long.parseLong(sc.nextLine());

                        Account account;

                        switch (type) {
                            case 1 -> account = bankService.openAccount(
                                    name, balance);

                            case 2 -> {
                                out.print("Enter overdraft limit: ");
                                long limit = Long.parseLong(sc.nextLine());

                                account = new CurrentAccount(
                                        name, balance, limit);

                                String[] validationErrors =
                                        AnnotationValidator.validate(account);

                                if (validationErrors.length > 0) {
                                    throw new BankException(
                                            String.join(", ", validationErrors));
                                }

                                registerAccount(
                                        account, accounts, accountRepository,
                                        accountStore, transactionProcessor);

                                accountStore.addTransaction(
                                        "Account opened: "
                                                + account.getAccountNumber());
                            }

                            case 3 -> {
                                account = new FixedDepositAccount(
                                        name, balance);

                                String[] validationErrors =
                                        AnnotationValidator.validate(account);

                                if (validationErrors.length > 0) {
                                    throw new BankException(
                                            String.join(", ", validationErrors));
                                }

                                registerAccount(
                                        account, accounts, accountRepository,
                                        accountStore, transactionProcessor);

                                accountStore.addTransaction(
                                        "Account opened: "
                                                + account.getAccountNumber());
                            }

                            default -> throw new BankException(
                                    "Invalid account type.");
                        }

                        if (type == 1) {
                            registerAccount(
                                    account, accounts, accountRepository,
                                    accountStore, transactionProcessor);
                        }

                        out.println("Account opened successfully.");
                        out.println(account);
                    }

                    case DEPOSIT -> {
                        out.print("Enter account number: ");
                        String number = sc.nextLine();

                        out.print("Enter deposit amount: ");
                        long depositAmount = Long.parseLong(sc.nextLine());

                        bankService.deposit(number, depositAmount);

                        Account account = bankService.statement(number);
                        logTransaction("Deposit", account, depositAmount);

                        out.println("Deposit successful.");
                        out.println("Updated balance: "
                                + account.getBalance());
                    }

                    case WITHDRAW -> {
                        out.print("Enter account number: ");
                        String number = sc.nextLine();

                        out.print("Enter withdrawal amount: ");
                        long withdrawalAmount = Long.parseLong(sc.nextLine());

                        bankService.withdraw(number, withdrawalAmount);

                        Account account = bankService.statement(number);
                        logTransaction("Withdrawal", account, withdrawalAmount);

                        out.println("Withdrawal successful.");
                        out.println("Updated balance: "
                                + account.getBalance());
                    }

                    case TRANSFER -> {
                        out.print("Enter source account number: ");
                        String sourceNumber = sc.nextLine();

                        out.print("Enter destination account number: ");
                        String destinationNumber = sc.nextLine();

                        out.print("Enter transfer amount: ");
                        long transferAmount = Long.parseLong(sc.nextLine());

                        bankService.transfer(
                                sourceNumber, destinationNumber, transferAmount);

                        Account source = bankService.statement(sourceNumber);
                        Account destination =
                                bankService.statement(destinationNumber);

                        logTransaction("Withdrawal", source, transferAmount);
                        logTransaction("Deposit", destination, transferAmount);

                        out.println("Transfer successful.");
                        out.println("Source balance: " + source.getBalance());
                        out.println("Destination balance: "
                                + destination.getBalance());
                    }

                    case STATEMENT -> {
                        out.print("Enter account number: ");
                        String number = sc.nextLine();

                        Account account = bankService.statement(number);
                        out.println("\n===== Account Statement =====");
                        out.println(account);
                        out.println("Interest Rate: "
                                + account.interestRate() + "%");
                        out.println("Active: " + account.isActive());
                    }

                    case REPORT -> {
                        saveAccounts(accounts);
                        out.println("Accounts saved successfully.");

                        Account[] loadedAccounts =
                                StatePersister.load(ACCOUNTS_FILE);

                        out.println("\n===== Reloaded Accounts =====");

                        for (Account account : loadedAccounts) {
                            out.println(account);
                        }

                        out.println("\n===== All Accounts =====");

                        for (Account account : bankService.listAccounts()) {
                            out.println(account);
                        }

                        out.println("\n===== Accounts Sorted by Balance =====");

                        for (Account account : accountStore.sortedByBalance()) {
                            out.println(account);
                        }

                        out.println("\n===== Transaction History =====");

                        for (String transaction :
                                accountStore.getTransactionHistory()) {
                            out.println(transaction);
                        }

                        generateReport();

                        if (Files.exists(REPORT_FILE)) {
                            out.println(Files.readString(REPORT_FILE));
                        }
                    }

                    case EXIT -> {
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
