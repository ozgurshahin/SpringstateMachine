package com.spring.statemachine.controller;

import com.spring.statemachine.domain.Transaction;
import com.spring.statemachine.domain.TransactionState;
import com.spring.statemachine.domain.TransactionStatus;
import com.spring.statemachine.repository.TransactionRepository;
import com.spring.statemachine.request.ChangeTransactionStatusRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TransactionControllerTest {
    @Autowired
    private TransactionController controller;
    @Autowired
    private TransactionRepository repository;

    @Test
    void createTransaction() {
        ResponseEntity<Transaction> transaction = controller.createTransaction("100");
        assertNotNull(transaction);
    }

    @Test
    void changeTransactionStatus() {
        ResponseEntity<Transaction> transaction = controller.createTransaction("100");

        ResponseEntity<Transaction> updatedTransaction = controller.changeTransactionStatus(ChangeTransactionStatusRequest.builder()
                .transactionId(Objects.requireNonNull(transaction.getBody()).getId())
                .newStatus(TransactionStatus.APPROVED)
                .build());

        Assertions.assertEquals(TransactionStatus.APPROVED, Objects.requireNonNull(updatedTransaction.getBody()).getStatus());
        Assertions.assertEquals(TransactionState.CONFIRMED, updatedTransaction.getBody().getState());
    }
}
