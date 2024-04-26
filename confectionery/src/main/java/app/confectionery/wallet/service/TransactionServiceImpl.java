package app.confectionery.wallet.service;

import app.confectionery.user.model.User;
import app.confectionery.user.repository.UserRepository;
import app.confectionery.wallet.model.DepositRequest;
import app.confectionery.wallet.model.Transaction;
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
    public Transaction deposit(DepositRequest depositRequest) {
        BigDecimal amount = depositRequest.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        User user = userRepository.findById(depositRequest.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setBalance(user.getBalance().add(depositRequest.getAmount()));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(depositRequest.getAmount());
        transaction.setTransactionType("DEPOSIT");
        transactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public Transaction withdraw(UUID userId, BigDecimal amount) {
        return null;
    }

    @Override
    public BigDecimal getCurrentUserBalance(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getBalance();
    }


}
