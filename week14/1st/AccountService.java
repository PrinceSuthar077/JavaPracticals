import java.util.ArrayList;

public class AccountService {
    private final ArrayList<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        accounts.add(account);
    }

    public int getAccountCount() {
        return accounts.size();
    }

    public ArrayList<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public void performAction(String action) {
        if (!"ADD".equals(action) && !"DISPLAY".equals(action)) {
            throw new IllegalArgumentException("Invalid action");
        }
    }
}
