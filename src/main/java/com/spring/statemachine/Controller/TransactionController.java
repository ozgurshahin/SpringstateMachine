package com.spring.statemachine.controller;

import com.spring.statemachine.domain.Transaction;
import com.spring.statemachine.domain.TransactionStatus;
import com.spring.statemachine.repository.TransactionRepository;
import com.spring.statemachine.request.ChangeTransactionStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@RestController("transactionController")
@RequestMapping("/transaction")
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @NotNull}))
@Slf4j
public class TransactionController {
    private final TransactionRepository transactionRepository;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Transaction> createTransaction(@RequestBody(required = false) String amount) {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(amount))
                .build();
        transactionRepository.save(transaction);
        return ResponseEntity.ok(Objects.requireNonNull(transaction));
    }

    @PostMapping("/change-status")
    @Transactional
    public ResponseEntity<Transaction> changeTransactionStatus(@RequestBody(required = false) ChangeTransactionStatusRequest request) {
        Transaction transaction = transactionRepository.getById(request.getTransactionId());
        if (TransactionStatus.APPROVED.equals(request.getNewStatus())) {
            transaction.confirmed();
        } else if (TransactionStatus.DECLINED.equals(request.getNewStatus())) {
            transaction.declined();
        }
        transactionRepository.save(transaction);
        return ResponseEntity.ok(Objects.requireNonNull(transaction));
    }
}
