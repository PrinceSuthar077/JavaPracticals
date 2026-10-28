import model.SavingsAccount;
import model.Account;
import model.AccountStore;
import model.Repository;
import service.BankService;
import service.TransactionProcessor;
import exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class BankAccountTest {

    @Test
    void depositIncreasesBalance() throws Exception {
        SavingsAccount account = new SavingsAccount("Prince", 1000, 100);

        account.deposit(500);

        assertEquals(1500, account.getBalance());
    }

    @Test
    void withdrawalBelowMinimumBalanceThrowsException() {
        SavingsAccount account = new SavingsAccount("Prince", 1000, 500);

        assertThrows(InsufficientFundsException.class, () -> {
            account.withdraw(600);
        });

        assertEquals(1000, account.getBalance());
    }

    @Test
    void concurrentDepositsPreserveCorrectBalance() throws Exception {
        SavingsAccount account = new SavingsAccount("Prince", 1000, 100);
        int numberOfDeposits = 100;
        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            for (int i = 0; i < numberOfDeposits; i++) {
                executor.submit(() -> {
                    try {
                        account.deposit(100);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } finally {
            executor.shutdown();
        }

        assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));
        assertEquals(11000, account.getBalance());
    }

    @Test
    void batchDepositsUpdateAccountBalance() throws Exception {
        SavingsAccount account = new SavingsAccount("Prince", 1000, 100);
        Repository<Account> repository = new Repository<>();
        AccountStore accountStore = new AccountStore();

        repository.save(account);
        accountStore.add(account);

        BankService bankService = new BankService(repository, accountStore);
        TransactionProcessor processor = new TransactionProcessor();

        try {
            List<Long> amounts = List.of(100L, 200L, 300L, 400L);

            processor.processDeposits(
                    bankService, account.getAccountNumber(), amounts);

            assertEquals(2000, account.getBalance());
        } finally {
            processor.stop();
        }
    }
}
