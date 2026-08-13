package com.felipepalomino.foodorder.payment.application.service;

import com.felipepalomino.foodorder.payment.domain.exception.PaymentNotFoundException;
import com.felipepalomino.foodorder.payment.domain.model.Payment;
import com.felipepalomino.foodorder.payment.domain.model.PaymentMethod;
import com.felipepalomino.foodorder.payment.domain.model.PaymentStatus;
import com.felipepalomino.foodorder.payment.domain.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PaymentApplicationService {

    private final PaymentRepository paymentRepository;

    public PaymentApplicationService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ============================================================
    // CASO DE USO: Iniciar pago
    // ============================================================
    public Payment processPayment(Long orderId, Long userId, BigDecimal amount, String method) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setMethod(PaymentMethod.valueOf(method.toUpperCase()));
        payment.setCreatedAt(LocalDateTime.now());

        // Simulación: pagos online/card son automáticamente completados
        // Cash queda pendiente hasta confirmación presencial
        if (PaymentMethod.CASH.name().equals(method.toUpperCase())) {
            payment.setStatus(PaymentStatus.PENDING);
            payment.setTransactionRef(null);
        } else {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setTransactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment confirmPayment(Long id) {
        Payment p = getPaymentById(id);
        p.setStatus(PaymentStatus.COMPLETED);
        p.setTransactionRef("TXN-CASH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return paymentRepository.save(p);
    }

    // Cambiar a cualquier estado (útil para demo/clase)
    public Payment updateStatus(Long id, String newStatus) {
        Payment p = getPaymentById(id);
        p.setStatus(PaymentStatus.valueOf(newStatus.toUpperCase()));
        return paymentRepository.save(p);
    }
}
