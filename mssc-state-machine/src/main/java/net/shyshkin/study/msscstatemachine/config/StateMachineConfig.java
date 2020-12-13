package net.shyshkin.study.msscstatemachine.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.msscstatemachine.actions.PaymentSMActions;
import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import net.shyshkin.study.msscstatemachine.guards.PaymentIdGuard;
import org.springframework.context.annotation.Configuration;
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

import static net.shyshkin.study.msscstatemachine.domain.PaymentState.*;

@Slf4j
@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

    private final PaymentSMActions paymentSMActions;
    private final Action<PaymentState, PaymentEvent> newToPreAuthAction;
    private final Action<PaymentState, PaymentEvent> newToPreAuthErrorAction;
    private final Action<PaymentState, PaymentEvent> preAuthToAuthAction;
    private final Action<PaymentState, PaymentEvent> preAuthToAuthErrorAction;

    private final PaymentIdGuard paymentIdGuard;

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
                .withExternal().source(NEW).target(NEW).event(PaymentEvent.PRE_AUTHORIZE)
                .action(paymentSMActions.preAuthAction()).guard(paymentIdGuard)
                .and()

                .withExternal().source(NEW).target(PRE_AUTH).event(PaymentEvent.PRE_AUTH_APPROVED)
                .action(newToPreAuthAction).guard(paymentIdGuard)
                .and()

                .withExternal().source(NEW).target(PRE_AUTH_ERROR).event(PaymentEvent.PRE_AUTH_DECLINED)
                .action(newToPreAuthErrorAction)
                .and()

                .withExternal().source(PRE_AUTH).target(PRE_AUTH).event(PaymentEvent.AUTHORIZE)
                .action(paymentSMActions.authAction()).guard(paymentIdGuard)
                .and()

                .withExternal().source(PRE_AUTH).target(AUTH).event(PaymentEvent.AUTH_APPROVED)
                .action(preAuthToAuthAction).guard(paymentIdGuard)
                .and()

                .withExternal().source(PRE_AUTH).target(AUTH_ERROR).event(PaymentEvent.AUTH_DECLINED)
                .action(preAuthToAuthErrorAction);
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
