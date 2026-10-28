package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AccountStore {

    private final ConcurrentHashMap<String, Account> accounts =
            new ConcurrentHashMap<>();

    private final CopyOnWriteArrayList<String> transactionHistory =
            new CopyOnWriteArrayList<>();

    public void add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public void addTransaction(String transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException(
                    "Transaction cannot be null.");
        }

        transactionHistory.add(transaction);
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sortedAccounts = getAll();

        sortedAccounts.sort(
                Comparator.comparingLong(Account::getBalance));

        return sortedAccounts;
    }
}