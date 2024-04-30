package app.confectionery.wallet.service;

import app.confectionery.wallet.model.DepositRequest;
import app.confectionery.wallet.model.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionService {

    Transaction deposit(DepositRequest depositRequest);

    Transaction withdraw(UUID userId, BigDecimal amount);

    BigDecimal getCurrentUserBalance(UUID userId);

    void recordTransaction(UUID userId, BigDecimal amount, String transactionType);
}
