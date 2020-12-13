package net.shyshkin.study.msscstatemachine.actions;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Slf4j
public abstract class GenericNotificationAction implements Action<PaymentState, PaymentEvent> {
    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        PaymentState sourceState = context.getTransition().getSource().getId();
        PaymentState targetState = context.getTransition().getTarget().getId();
        log.debug("Action during transition {} -> {}", sourceState, targetState);
    }
}
