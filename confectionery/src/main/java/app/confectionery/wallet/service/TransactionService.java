package app.confectionery.wallet.service;

import app.confectionery.wallet.model.DTO.DepositRequest;
import app.confectionery.wallet.model.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionService {

    Transaction deposit(UUID userId, DepositRequest depositRequest);

    BigDecimal getCurrentUserBalance(UUID userId);

    void recordTransaction(UUID userId, BigDecimal amount, String transactionType);

}
