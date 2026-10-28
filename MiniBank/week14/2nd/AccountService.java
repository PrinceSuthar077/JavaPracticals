package service;

import model.Account;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AccountService {
    private ArrayList<Account> accounts = new ArrayList<>();

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

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Account account : accounts) {
                writer.write(account.getId() + "," + account.getName());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        ArrayList<Account> loadedAccounts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 2);
                if (data.length != 2) {
                    throw new IOException("Invalid account data");
                }

                try {
                    loadedAccounts.add(new Account(
                            Integer.parseInt(data[0]),
                            data[1]));
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid account ID", e);
                }
            }
        }

        accounts = loadedAccounts;
    }
}
