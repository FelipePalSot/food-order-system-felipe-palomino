package com.felipepalomino.foodorder.payment.domain.repository;

import com.felipepalomino.foodorder.payment.domain.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findAll();
}

