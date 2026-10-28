import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {
    @Test
    public void testAddAccountIncreasesCount() {
        AccountService service = new AccountService();
        service.addAccount(new Account(1, "Prince"));
        assertEquals(1, service.getAccountCount());
    }

    @Test
    public void testAddingTwoAccounts() {
        AccountService service = new AccountService();
        service.addAccount(new Account(1, "Prince"));
        service.addAccount(new Account(2, "Rahul"));
        assertEquals(2, service.getAccountCount());
    }

    @Test
    public void testInvalidActionThrowsException() {
        AccountService service = new AccountService();
        assertThrows(IllegalArgumentException.class,
                () -> service.performAction("INVALID"));
    }
}
