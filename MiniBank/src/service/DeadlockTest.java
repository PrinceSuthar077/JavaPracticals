package service;

import model.Account;
import model.SavingsAccount;

public class DeadlockTest {

    private static void demonstrateDeadlock(Account a, Account b)
            throws InterruptedException {

        Thread t1 = new Thread(() -> {
            synchronized (a) {
                System.out.println("Thread 1 locked account A");

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (b) {
                    System.out.println("Thread 1 locked account B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (b) {
                System.out.println("Thread 2 locked account B");

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                synchronized (a) {
                    System.out.println("Thread 2 locked account A");
                }
            }
        });

        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        t1.join(1000);
        t2.join(1000);

        if (t1.isAlive() && t2.isAlive()) {
            System.out.println("Deadlock detected: both threads are waiting.");
        } else {
            System.out.println("Deadlock did not occur in this run.");
        }
    }

    private static void safeTransfer(Account a, Account b) {
        Account first = a.getAccountNumber().compareTo(b.getAccountNumber()) < 0
                ? a : b;
        Account second = first == a ? b : a;

        synchronized (first) {
            synchronized (second) {
                try {
                    a.transfer(b, 100);
                    System.out.println("Safe transfer completed.");
                } catch (Exception e) {
                    System.out.println("Transfer failed: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account deadlockA = new SavingsAccount("Deadlock A", 1000, 0);
        Account deadlockB = new SavingsAccount("Deadlock B", 1000, 0);

        System.out.println("Demonstrating deadlock:");
        demonstrateDeadlock(deadlockA, deadlockB);

        System.out.println("\nTesting safe transfers with ordered locks:");

        Account a = new SavingsAccount("Account A", 1000, 0);
        Account b = new SavingsAccount("Account B", 1000, 0);

        System.out.println("Account A: " + a.getAccountNumber());
        System.out.println("Account B: " + b.getAccountNumber());

        Thread t1 = new Thread(() -> safeTransfer(a, b));
        Thread t2 = new Thread(() -> safeTransfer(b, a));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Safe transfer test finished.");
        System.out.println("Account A balance: " + a.getBalance());
        System.out.println("Account B balance: " + b.getBalance());
    }
}