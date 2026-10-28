
package model;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.util.Objects;
import model.annotation.Id;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing {

    @Id
    private final String accountNumber;

    private String ownerName;

    @Positive
    private long balance;

    private boolean active;

    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
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
    public String toString() {
        return "Account{accountNumber='" + accountNumber +
                "', ownerName='" + ownerName +
                "', balance=" + balance + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Account))
            return false;

        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
