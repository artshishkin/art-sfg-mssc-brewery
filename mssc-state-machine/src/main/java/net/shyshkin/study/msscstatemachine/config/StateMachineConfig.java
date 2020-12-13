package net.shyshkin.study.msscstatemachine.config;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Random;

import static net.shyshkin.study.msscstatemachine.domain.PaymentState.*;
import static net.shyshkin.study.msscstatemachine.services.PaymentService.PAYMENT_ID_HEADER;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    private final Random random = new Random();

    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates()
                .initial(NEW)
                .states(EnumSet.allOf(PaymentState.class))
                .end(AUTH)
                .end(PRE_AUTH_ERROR)
                .end(AUTH_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions
                .withExternal().source(NEW).target(NEW).event(PaymentEvent.PRE_AUTHORIZE).action(preAuthAction())
                .and()
                .withExternal().source(NEW).target(PRE_AUTH).event(PaymentEvent.PRE_AUTH_APPROVED)
                .and()
                .withExternal().source(NEW).target(PRE_AUTH_ERROR).event(PaymentEvent.PRE_AUTH_DECLINED)
                .and()
                .withExternal().source(PRE_AUTH).target(PRE_AUTH).event(PaymentEvent.AUTHORIZE).action(authAction())
                .and()
                .withExternal().source(PRE_AUTH).target(AUTH).event(PaymentEvent.AUTH_APPROVED)
                .and()
                .withExternal().source(PRE_AUTH).target(AUTH_ERROR).event(PaymentEvent.AUTH_DECLINED);
    }

    private Action<PaymentState, PaymentEvent> preAuthAction() {

        return context -> {
            log.debug("preAuthAction was called");

            boolean isApproved = random.nextInt(10) < 8;
            log.debug("{}", isApproved ? "Approved" : "Declined! NO CREDIT!!!");

            PaymentEvent eventToSend = isApproved ? PaymentEvent.PRE_AUTH_APPROVED : PaymentEvent.PRE_AUTH_DECLINED;

            Message<PaymentEvent> message = MessageBuilder
                    .withPayload(eventToSend)
                    .setHeader(PAYMENT_ID_HEADER, context.getMessageHeader(PAYMENT_ID_HEADER))
                    .build();

            context.getStateMachine().sendEvent(message);
        };
    }

    private Action<PaymentState, PaymentEvent> authAction() {

        return context -> {
            log.debug("authAction was called");

            boolean isApproved = random.nextInt(10) < 8;
            log.debug("{}", isApproved ? "Approved" : "Declined! NO CREDIT!!!");

            PaymentEvent eventToSend = isApproved ? PaymentEvent.AUTH_APPROVED : PaymentEvent.AUTH_DECLINED;

            Message<PaymentEvent> message = MessageBuilder
                    .withPayload(eventToSend)
                    .setHeader(PAYMENT_ID_HEADER, context.getMessageHeader(PAYMENT_ID_HEADER))
                    .build();

            context.getStateMachine().sendEvent(message);
        };
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {

        StateMachineListener<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
                log.info("stateChanged(from: {}, to: {})", from, to);
            }
        };
        config.withConfiguration().listener(adapter);
    }
}
