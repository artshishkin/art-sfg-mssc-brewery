package net.shyshkin.study.msscstatemachine.actions;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

import static net.shyshkin.study.msscstatemachine.services.PaymentService.PAYMENT_ID_HEADER;

@Slf4j
@Component
public class PaymentSMActions {

    public Action<PaymentState, PaymentEvent> preAuthAction() {

        return context -> {
            log.debug("preAuthAction was called");

            boolean isApproved = ThreadLocalRandom.current().nextInt(10) < 8;
            log.debug("{}", isApproved ? "Approved" : "Declined! NO CREDIT!!!");

            PaymentEvent eventToSend = isApproved ? PaymentEvent.PRE_AUTH_APPROVED : PaymentEvent.PRE_AUTH_DECLINED;

            Message<PaymentEvent> message = MessageBuilder
                    .withPayload(eventToSend)
                    .setHeader(PAYMENT_ID_HEADER, context.getMessageHeader(PAYMENT_ID_HEADER))
                    .build();

            context.getStateMachine().sendEvent(message);
        };
    }

    public Action<PaymentState, PaymentEvent> authAction() {

        return context -> {
            log.debug("authAction was called");

            boolean isApproved = ThreadLocalRandom.current().nextInt(10) < 8;
            log.debug("{}", isApproved ? "Approved" : "Declined! NO CREDIT!!!");

            PaymentEvent eventToSend = isApproved ? PaymentEvent.AUTH_APPROVED : PaymentEvent.AUTH_DECLINED;

            Message<PaymentEvent> message = MessageBuilder
                    .withPayload(eventToSend)
                    .setHeader(PAYMENT_ID_HEADER, context.getMessageHeader(PAYMENT_ID_HEADER))
                    .build();

            context.getStateMachine().sendEvent(message);
        };
    }
}
