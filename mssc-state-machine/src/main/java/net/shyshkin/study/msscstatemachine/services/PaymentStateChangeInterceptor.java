package net.shyshkin.study.msscstatemachine.services;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.msscstatemachine.domain.Payment;
import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import net.shyshkin.study.msscstatemachine.repository.PaymentRepository;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state,
                               Message<PaymentEvent> message,
                               Transition<PaymentState, PaymentEvent> transition,
                               StateMachine<PaymentState, PaymentEvent> stateMachine) {

        Optional.ofNullable(message)
                .ifPresent(msg ->
                        Optional.ofNullable(
                                Long.class.cast(msg.getHeaders().getOrDefault(PaymentService.PAYMENT_ID_HEADER, -1L))
                        )
                                .ifPresent(paymentId -> {
                                    Payment payment = paymentRepository.getOne(paymentId);
                                    payment.setState(state.getId());
                                    paymentRepository.save(payment);
                                })
                );
    }
}