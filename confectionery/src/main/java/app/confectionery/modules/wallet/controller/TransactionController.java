package app.confectionery.modules.wallet.controller;

import app.confectionery.modules.wallet.model.DTO.DepositRequest;
import app.confectionery.modules.wallet.model.Transaction;
import app.confectionery.modules.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("{userId}/deposit")
    public ResponseEntity<?> deposit(@PathVariable UUID userId, @RequestBody DepositRequest depositRequest) {
        Transaction transaction = transactionService.deposit(userId, depositRequest);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("{userId}/balance")
    public ResponseEntity<BigDecimal> getCurrentUserBalance(@PathVariable UUID userId) {
        BigDecimal balance = transactionService.getCurrentUserBalance(userId);
        return ResponseEntity.ok(balance);
    }

}
