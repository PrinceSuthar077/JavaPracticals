package service;

import model.Account;
import model.SavingsAccount;
import exception.BankException;

public class TransactionTest {

    public static void main(String[] args) {

        Account account = new SavingsAccount("Thread Test", 10000, 0);

        TransactionProcessor processor = new TransactionProcessor();

        for (int i = 0; i < 10; i++) {
            processor.submit(() -> {
                try {
                    account.deposit(100);
                    System.out.println(Thread.currentThread().getName() + " deposited 100");
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            });

            processor.submit(() -> {
                try {
                    account.withdraw(50);
                    System.out.println(Thread.currentThread().getName() + " withdrew 50");
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            });
        }

        processor.stop();

        System.out.println("Final balance: " + account.getBalance());
    }
}