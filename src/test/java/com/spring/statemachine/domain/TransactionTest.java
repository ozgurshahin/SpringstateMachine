package com.spring.statemachine.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class TransactionTest {

    @Test
    void confirmedBySource() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(100L))
                .build();

        transaction.confirmed();
        Assertions.assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
        Assertions.assertEquals(TransactionState.CONFIRMED, transaction.getState());
    }

    @Test
    void declinedByDestination() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(100L))
                .build();

        transaction.declined();

        Assertions.assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
        Assertions.assertEquals(TransactionState.DECLINED, transaction.getState());
    }
}
