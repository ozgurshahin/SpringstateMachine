package com.spring.statemachine.domain;

import lombok.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends DomainEntity {
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TransactionState state = TransactionState.WAITING;
    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;
    private BigDecimal amount;

    public void confirmed() {
        sendEvent(TransactionEvent.CONFIRMED);
    }

    public void declined() {
        sendEvent(TransactionEvent.DECLINED);
    }

    private void sendEvent(TransactionEvent event) {
        StateMachine<TransactionState, TransactionEvent> transactionEventStateMachine = sm();
        Message<TransactionEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader("transactionMessage", this)
                .build();
        transactionEventStateMachine.sendEvent(message);
        transactionEventStateMachine.stop();
    }

    private StateMachine<TransactionState, TransactionEvent> sm() {
        StateMachineInterceptor<TransactionState, TransactionEvent> transactionStateMachineInterceptor = (StateMachineInterceptor<TransactionState, TransactionEvent>) DomainContextProvider.getApplicationContext().getBean("transactionStateMachineInterceptor");
        StateMachineFactory stateMachineFactory = DomainContextProvider.getApplicationContext().getBeansOfType(StateMachineFactory.class).get("transactionStateMachine");
        StateMachine<TransactionState, TransactionEvent> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.resetStateMachine(new DefaultStateMachineContext<>(getState(), null, null, null));
            sma.addStateMachineInterceptor(transactionStateMachineInterceptor);
        });
        stateMachine.start();
        return stateMachine;
    }
}
