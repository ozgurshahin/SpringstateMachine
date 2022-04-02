package com.spring.statemachine.services;

import com.spring.statemachine.domain.Transaction;
import com.spring.statemachine.domain.TransactionEvent;
import com.spring.statemachine.domain.TransactionState;
import com.spring.statemachine.domain.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @NotNull}))
@Transactional
@Slf4j
public class TransactionMessageService {


    public void confirmed(StateContext<TransactionState, TransactionEvent> context) {
        Transaction transaction = getTransaction(context);
        assert transaction != null;
        transaction.setState(context.getTarget().getId());
        transaction.setStatus(TransactionStatus.APPROVED);
        changeTransactionStatus(transaction, TransactionStatus.APPROVED);
    }


    public void declined(StateContext<TransactionState, TransactionEvent> context) {
        Transaction transaction = getTransaction(context);
        assert transaction != null;
        transaction.setState(context.getTarget().getId());
        transaction.setStatus(TransactionStatus.DECLINED);
        changeTransactionStatus(transaction, TransactionStatus.DECLINED);
    }


    private void changeTransactionStatus(Transaction transaction, TransactionStatus transactionStatus) {
        transaction.setStatus(transactionStatus);
    }

    public Transaction getTransaction(StateContext<TransactionState, TransactionEvent> context) {
        return context.getMessage().getHeaders().get("transactionMessage", Transaction.class);
    }
}
