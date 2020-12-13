package net.shyshkin.study.msscstatemachine.services;

import net.shyshkin.study.msscstatemachine.domain.Payment;
import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import net.shyshkin.study.msscstatemachine.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static net.shyshkin.study.msscstatemachine.domain.PaymentState.PRE_AUTH;
import static net.shyshkin.study.msscstatemachine.domain.PaymentState.PRE_AUTH_ERROR;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @RepeatedTest(10)
    void preAuth() {
        //given
        Payment savedPayment = paymentService.newPayment(this.payment);

        //when
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

        //then
        Payment paymentRepositoryOne = paymentRepository.getOne(savedPayment.getId());
//        assertThat(paymentRepositoryOne.getState()).isEqualTo(PaymentState.PRE_AUTH);
        assertThat(paymentRepositoryOne.getState()).isIn(PRE_AUTH, PRE_AUTH_ERROR);
    }
}