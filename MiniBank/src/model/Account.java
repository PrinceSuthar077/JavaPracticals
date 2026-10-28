package model;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.io.Serializable;
import java.util.Objects;
import model.annotation.Id;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {

    private static final long serialVersionUID = 1L;

    @Id
    private final String accountNumber;

    private String ownerName;

    @Positive
    private long balance;

    private boolean active;

    private static long accountCounter = 0;

    private static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    private static synchronized void updateAccountCounter(String accountNumber) {
        if (accountNumber != null && accountNumber.matches("AC\\d+")) {
            try {
                long number = Long.parseLong(accountNumber.substring(2));

                if (number > accountCounter) {
                    accountCounter = number;
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Invalid account number: " + accountNumber, e);
            }
        }
    }

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public static void synchronizeAccountCounter(Iterable<Account> accounts) {
        if (accounts == null) {
            return;
        }

        for (Account account : accounts) {
            if (account != null) {
                updateAccountCounter(account.getAccountNumber());
            }
        }
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero.");
        }

        if (Long.MAX_VALUE - balance < amount) {
            throw new InvalidAmountException("Deposit amount is too large.");
        }

        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero.");
        }

        if (!canWithdraw(amount)) {
            long shortfall = amount > balance ? amount - balance : 0;

            throw new InsufficientFundsException(
                    "Withdrawal is not allowed or funds are insufficient.",
                    shortfall);
        }

        balance -= amount;
        return true;
    }

    public void transfer(Account to, long amount) throws BankException {
        try {
            if (to == null) {
                throw new BankException(
                        "Destination account cannot be null.");
            }

            if (to == this) {
                throw new BankException(
                        "Cannot transfer to the same account.");
            }

            if (amount <= 0) {
                throw new InvalidAmountException(
                        "Transfer amount must be greater than zero.");
            }

            withdraw(amount);

            try {
                to.deposit(amount);
            } catch (BankException e) {
                deposit(amount);
                throw e;
            }

        } catch (BankException e) {
            throw e;
        } finally {
            System.out.println("Transfer operation completed.");
        }
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString() {
        return "Account{accountNumber='" + accountNumber +
                "', ownerName='" + ownerName +
                "', balance=" + balance + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)) {
            return false;
        }

        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
