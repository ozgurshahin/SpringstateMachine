package com.spring.statemachine.request;

import com.spring.statemachine.domain.TransactionStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeTransactionStatusRequest {
    Long transactionId;
    TransactionStatus newStatus;
}
