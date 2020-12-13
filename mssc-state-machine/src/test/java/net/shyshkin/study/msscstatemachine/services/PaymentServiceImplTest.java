package net.shyshkin.study.msscstatemachine.services;

import net.shyshkin.study.msscstatemachine.domain.Payment;
import net.shyshkin.study.msscstatemachine.domain.PaymentEvent;
import net.shyshkin.study.msscstatemachine.domain.PaymentState;
import net.shyshkin.study.msscstatemachine.repository.PaymentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static net.shyshkin.study.msscstatemachine.domain.PaymentState.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    static Map<PaymentState, Integer> repetitions = new HashMap<>();

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PreAuth2Auth {

        @Transactional
        @RepeatedTest(100)
        @DisplayName("Testing change states NEW -> PRE_AUTH -> AUTH")
        void auth() {
            //given
            Payment savedPayment = paymentService.newPayment(payment);

            //when
            StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
            paymentService.authorizePayment(savedPayment.getId());
            //then
            Payment paymentRepositoryOne = paymentRepository.getOne(savedPayment.getId());

            PaymentState state = paymentRepositoryOne.getState();

            repetitions.putIfAbsent(state, 0);
            repetitions.merge(state, 1, Integer::sum);

            assertThat(state).isIn(AUTH, AUTH_ERROR, PRE_AUTH_ERROR);
        }

        @Test
        @Order(Integer.MAX_VALUE)
        @DisplayName("Percentage of result states should match AUTH(64%), AUTH_ERROR(16%), PRE_AUTH_ERROR(20%)")
        void repetitionPercentageTest() {
            int totalCount = repetitions.values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            assertAll(
                    () -> assertThat(100 * repetitions.get(AUTH) / totalCount).isBetween(55, 73),              //theoretically 64
                    () -> assertThat(100 * repetitions.get(PRE_AUTH_ERROR) / totalCount).isBetween(14, 26),    //theoretically 20
                    () -> assertThat(100 * repetitions.get(AUTH_ERROR) / totalCount).isBetween(10, 22)         //theoretically 16
            );
        }
    }
}
