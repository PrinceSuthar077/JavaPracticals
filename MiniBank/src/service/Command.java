package service;
import util.TransactionType;

public record Command(
        TransactionType type,
        String accountNumber,
        long amount) {
}
