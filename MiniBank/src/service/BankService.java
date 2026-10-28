package service;

import java.util.ArrayList;

import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.AccountStore;
import model.Repository;
import model.SavingsAccount;
import util.AnnotationValidator;

public class BankService {

    private final Repository<Account> repository;
    private final AccountStore accountStore;

    public BankService(Repository<Account> repository,
            AccountStore accountStore) {
        this.repository = repository;
        this.accountStore = accountStore;
    }

    public Account openAccount(String ownerName, long openingBalance)
            throws BankException {

        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new BankException("Owner name cannot be empty.");
        }

        if (openingBalance < 0) {
            throw new InvalidAmountException(
                    "Opening balance cannot be negative.");
        }

        Account account = new SavingsAccount(ownerName, openingBalance, 1000);

        AnnotationValidator.validate(account);

        repository.save(account);
        accountStore.add(account);

        accountStore.addTransaction(
                "Account opened: " + account.getAccountNumber());

        return account;
    }

    public void deposit(String accountNumber, long amount)
            throws BankException {

        Account account = findAccount(accountNumber);

        account.deposit(amount);

        repository.save(account);
        accountStore.add(account);

        accountStore.addTransaction(
                "Deposit: " + amount + " to " + accountNumber);
    }

    public void withdraw(String accountNumber, long amount)
            throws BankException {

        Account account = findAccount(accountNumber);

        account.withdraw(amount);

        repository.save(account);
        accountStore.add(account);

        accountStore.addTransaction(
                "Withdrawal: " + amount + " from " + accountNumber);
    }

    public void transfer(String fromAccountNumber,
            String toAccountNumber, long amount) throws BankException {

        Account from = findAccount(fromAccountNumber);
        Account to = findAccount(toAccountNumber);

        from.transfer(to, amount);

        repository.save(from);
        repository.save(to);

        accountStore.add(from);
        accountStore.add(to);

        accountStore.addTransaction(
                "Transfer: " + amount + " from "
                        + fromAccountNumber + " to " + toAccountNumber);
    }

    public Account statement(String accountNumber)
            throws BankException {

        return findAccount(accountNumber);
    }

    public ArrayList<Account> listAccounts() {
        return repository.findAll();
    }

    private Account findAccount(String accountNumber)
            throws BankException {

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new BankException("Account number cannot be empty.");
        }

        Account account = repository.findById(accountNumber);

        if (account == null) {
            account = accountStore.get(accountNumber);
        }

        if (account == null) {
            throw new BankException(
                    "Account not found: " + accountNumber);
        }

        return account;
    }
}
