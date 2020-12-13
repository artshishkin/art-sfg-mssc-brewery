package net.shyshkin.study.msscstatemachine.guards;

import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import static net.shyshkin.study.msscstatemachine.services.PaymentService.PAYMENT_ID_HEADER;

@Component
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent>{

    @Override
    public boolean evaluate(StateContext<PaymentState, PaymentEvent> context) {
        return context.getMessageHeader(PAYMENT_ID_HEADER) != null;
    }
}
