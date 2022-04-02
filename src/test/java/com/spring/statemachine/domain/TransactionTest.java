package com.spring.statemachine.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
//@RunWith(SpringRunner.class)
class TransactionTest {

    @Test
    void confirmedBySource() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(100L))
                .build();

        transaction.confirmed();

        System.out.println(transaction);
    }

    @Test
    void declinedByDestination() {
    }
}
