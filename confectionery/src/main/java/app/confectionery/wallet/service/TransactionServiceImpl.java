package app.confectionery.wallet.service;

import app.confectionery.user.model.User;
import app.confectionery.user.repository.UserRepository;
import app.confectionery.wallet.model.DTO.DepositRequest;
import app.confectionery.wallet.model.Transaction;
import app.confectionery.wallet.model.TransactionType;
import app.confectionery.wallet.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Override
    public Transaction deposit(UUID userId, DepositRequest depositRequest) {
        BigDecimal amount = depositRequest.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBalance(user.getBalance().add(depositRequest.getAmount()));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(depositRequest.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public BigDecimal getCurrentUserBalance(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getBalance();
    }

    @Override
    public void recordTransaction(UUID userId, BigDecimal amount, String transactionType) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.valueOf(transactionType));
        transactionRepository.save(transaction);
    }

}
