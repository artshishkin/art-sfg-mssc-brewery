package net.shyshkin.study.msscstatemachine.repository;

import net.shyshkin.study.msscstatemachine.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
