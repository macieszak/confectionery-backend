package app.confectionery.wallet.controller;

import app.confectionery.wallet.model.DepositRequest;
import app.confectionery.wallet.model.Transaction;
import app.confectionery.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;


@RestController
@RequestMapping("api/wallet")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {
        Transaction transaction = transactionService.deposit(depositRequest);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getCurrentUserBalance(@PathVariable UUID userId) {
        BigDecimal balance = transactionService.getCurrentUserBalance(userId);
        return ResponseEntity.ok(balance);
    }

}
