package com.felipepalomino.foodorder.payment.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByOrderId(Long orderId);
}

