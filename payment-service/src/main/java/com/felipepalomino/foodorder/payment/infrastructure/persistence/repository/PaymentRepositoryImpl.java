package com.felipepalomino.foodorder.payment.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.payment.domain.model.Payment;
import com.felipepalomino.foodorder.payment.domain.model.PaymentMethod;
import com.felipepalomino.foodorder.payment.domain.model.PaymentStatus;
import com.felipepalomino.foodorder.payment.domain.repository.PaymentRepository;
import com.felipepalomino.foodorder.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JpaPaymentRepository jpa;

    public PaymentRepositoryImpl(JpaPaymentRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Payment save(Payment p) { return toDomain(jpa.save(toEntity(p))); }

    @Override
    public Optional<Payment> findById(Long id) { return jpa.findById(id).map(this::toDomain); }

    @Override
    public List<Payment> findByOrderId(Long orderId) {
        return jpa.findByOrderId(orderId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Payment> findAll() { return jpa.findAll().stream().map(this::toDomain).collect(Collectors.toList()); }

    private Payment toDomain(PaymentEntity e) {
        Payment p = new Payment();
        p.setId(e.getId());
        p.setOrderId(e.getOrderId());
        p.setUserId(e.getUserId());
        p.setAmount(e.getAmount());
        p.setMethod(PaymentMethod.valueOf(e.getMethod()));
        p.setStatus(PaymentStatus.valueOf(e.getStatus()));
        p.setTransactionRef(e.getTransactionRef());
        p.setCreatedAt(e.getCreatedAt());
        return p;
    }

    private PaymentEntity toEntity(Payment p) {
        PaymentEntity e = new PaymentEntity();
        e.setId(p.getId());
        e.setOrderId(p.getOrderId());
        e.setUserId(p.getUserId());
        e.setAmount(p.getAmount());
        e.setMethod(p.getMethod() != null ? p.getMethod().name() : PaymentMethod.CASH.name());
        e.setStatus(p.getStatus() != null ? p.getStatus().name() : PaymentStatus.PENDING.name());
        e.setTransactionRef(p.getTransactionRef());
        e.setCreatedAt(p.getCreatedAt());
        return e;
    }
}

