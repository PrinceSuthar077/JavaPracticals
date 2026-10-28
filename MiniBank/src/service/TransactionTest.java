
package service;

import exception.BankException;
import model.Account;
import model.SavingsAccount;

public class TransactionTest {

    public static void main(String[] args) {

        Account account = new SavingsAccount("Thread Test", 10000, 0);

        TransactionProcessor processor = new TransactionProcessor();

        for (int i = 0; i < 10; i++) {
            processor.submit(() -> {
                try {
                    account.deposit(100);
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            });

            processor.submit(() -> {
                try {
                    account.withdraw(50);
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            });
        }

        processor.stop();

        long expectedBalance = 10500;
        long actualBalance = account.getBalance();

        System.out.println("Expected balance: " + expectedBalance);
        System.out.println("Actual balance: " + actualBalance);

        if (actualBalance == expectedBalance) {
            System.out.println("Multithreading test passed.");
        } else {
            System.out.println("Multithreading test failed.");
        }
    }
}
