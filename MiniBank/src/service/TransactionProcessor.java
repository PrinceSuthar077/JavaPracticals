package service;

import exception.BankException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import model.Account;

public class TransactionProcessor {

    private final ExecutorService executor =
            Executors.newFixedThreadPool(4);

    private final ConcurrentHashMap<String, Account> accounts =
            new ConcurrentHashMap<>();

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ConcurrentHashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void submit(Runnable task) {
        executor.execute(task);
    }

    public void processDeposits(
            BankService bankService,
            String accountNumber,
            List<Long> amounts) throws Exception {

        List<Future<?>> tasks = new ArrayList<>();

        for (long amount : amounts) {
            tasks.add(executor.submit(() -> {
                try {
                    bankService.deposit(accountNumber, amount);
                } catch (BankException e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        for (Future<?> task : tasks) {
            task.get();
        }
    }

    public void stop() {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
                executor.awaitTermination(1, TimeUnit.MINUTES);
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
