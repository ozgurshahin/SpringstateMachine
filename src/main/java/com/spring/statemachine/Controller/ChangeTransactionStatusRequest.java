package com.spring.statemachine.Controller;

import com.spring.statemachine.domain.TransactionStatus;
import lombok.Data;

@Data
public class ChangeTransactionStatusRequest {
    Long transactionId;
    TransactionStatus newStatus;
}
