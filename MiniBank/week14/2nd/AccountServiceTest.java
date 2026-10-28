package service;

import model.Account;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {
    @Test
    public void testAddAccountIncreasesCount() {
        AccountService service = new AccountService();
        service.addAccount(new Account(1, "Prince"));
        assertEquals(1, service.getAccountCount());
    }

    @Test
    public void testInvalidAccountThrowsException() {
        AccountService service = new AccountService();
        assertThrows(IllegalArgumentException.class,
                () -> service.addAccount(null));
    }

    @Test
    public void testSaveAndReloadData() throws Exception {
        Path file = Files.createTempFile("accounts", ".txt");

        try {
            AccountService original = new AccountService();
            original.addAccount(new Account(1, "Prince"));
            original.addAccount(new Account(2, "Rahul"));
            original.saveToFile(file.toString());

            AccountService restored = new AccountService();
            restored.loadFromFile(file.toString());

            assertEquals(original.getAccountCount(), restored.getAccountCount());

            for (int i = 0; i < original.getAccountCount(); i++) {
                assertEquals(original.getAccounts().get(i).toString(),
                        restored.getAccounts().get(i).toString());
            }
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
