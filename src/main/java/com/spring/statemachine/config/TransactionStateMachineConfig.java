package com.spring.statemachine.config;

import com.spring.statemachine.domain.TransactionEvent;
import com.spring.statemachine.domain.TransactionState;
import com.spring.statemachine.services.TransactionMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

import javax.validation.constraints.NotNull;
import java.util.EnumSet;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableStateMachineFactory(name = "transactionStateMachine")
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @NotNull}))
public class TransactionStateMachineConfig extends EnumStateMachineConfigurerAdapter<TransactionState, TransactionEvent> {

    private final TransactionMessageService transactionMessageService;

    @Override
    public void configure(StateMachineConfigurationConfigurer<TransactionState, TransactionEvent> config) throws Exception {
        config.withConfiguration()
                .listener(listener());
    }

    @Override
    public void configure(StateMachineConfigBuilder<TransactionState, TransactionEvent> config) throws Exception {
        super.configure(config);
    }

    @Override
    public void configure(StateMachineStateConfigurer<TransactionState, TransactionEvent> states) throws Exception {
        states
                .withStates()
                .initial(TransactionState.WAITING)
                .states(EnumSet.allOf(TransactionState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TransactionState, TransactionEvent> transitions) throws Exception {
        transitions

                .withExternal()
                .event(TransactionEvent.DECLINED)
                .source(TransactionState.WAITING)
                .target(TransactionState.DECLINED)
                .action(transactionMessageService::declined)
                .and()

                .withExternal()
                .event(TransactionEvent.CONFIRMED)
                .source(TransactionState.WAITING)
                .target(TransactionState.CONFIRMED)
                .action(transactionMessageService::confirmed)
                .and();
    }


    @Bean("transactionStateMachineListener") // Bu bean i koymasa ne degisir?
    public StateMachineListener<TransactionState, TransactionEvent> listener() {
        return new StateMachineListenerAdapter<TransactionState, TransactionEvent>() {
            @Override
            public void stateChanged(State<TransactionState, TransactionEvent> from, State<TransactionState, TransactionEvent> to) {
                log.info("stateChanged : {} {}", from, to);
            }

            @Override
            public void eventNotAccepted(Message<TransactionEvent> event) {
                log.error("eventNotAccepted : {}", event);
            }

            @Override
            public void stateMachineError(StateMachine<TransactionState, TransactionEvent> stateMachine, Exception exception) {
                log.error("stateMachineError : {}", exception.getMessage());
            }

            @Override
            public void stateEntered(State<TransactionState, TransactionEvent> state) {
                log.info("stateEntered : {}", state);
            }

            @Override
            public void transition(Transition<TransactionState, TransactionEvent> transition) {
                log.info("transition : {}", transition);
            }

            @Override
            public void stateMachineStarted(StateMachine<TransactionState, TransactionEvent> stateMachine) {
                log.info("stateMachineStarted");
            }

            @Override
            public void stateExited(State<TransactionState, TransactionEvent> state) {
                log.info("stateExited :{}", state);
            }

            @Override
            public void stateMachineStopped(StateMachine<TransactionState, TransactionEvent> stateMachine) {
                log.info("stateMachineStopped :{}", stateMachine.getId());
            }

            @Override
            public void extendedStateChanged(Object key, Object value) {
                log.info("extendedStateChanged");
            }

            @Override
            public void transitionEnded(Transition<TransactionState, TransactionEvent> transition) {
                log.info("transitionEnded : {}", transition);
            }

            @Override
            public void transitionStarted(Transition<TransactionState, TransactionEvent> transition) {
                log.info("transitionStarted : {}", transition);
            }
        };
    }

    @Bean("transactionStateMachineInterceptor")
    public StateMachineInterceptor<TransactionState, TransactionEvent> interceptor() {
        return new StateMachineInterceptorAdapter<TransactionState, TransactionEvent>() {
            @Override
            public void postStateChange(State<TransactionState, TransactionEvent> state, Message<TransactionEvent> message, Transition<TransactionState, TransactionEvent> transition, StateMachine<TransactionState, TransactionEvent> stateMachine, StateMachine<TransactionState, TransactionEvent> rootStateMachine) {
                log.info("postStateChange states : {}", state.getStates().stream().map(State::getId).collect(Collectors.toList()));

            }
        };
    }
}
