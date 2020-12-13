package net.shyshkin.study.msscstatemachine.config;

import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<PaymentState, PaymentEvent> factory;

    @Test
    void testNewStateMachine() {
        //given
        StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine();

        //when
        sm.start();
        System.out.println(sm.getState().toString());

//        sm.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        System.out.println(sm.getState().toString());

        sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
        System.out.println(sm.getState().toString());

        //No transition to PRE_AUTH_ERROR because we are in PRE_AUTH state already
        sm.sendEvent(PaymentEvent.PRE_AUTH_DECLINED);
        System.out.println(sm.getState().toString());

        //then
        assertThat(sm.getState().getId()).isEqualTo(PaymentState.PRE_AUTH);
    }
}