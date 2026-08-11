package com.felipepalomino.foodorder.payment.domain.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super("Pago no encontrado con id: " + id);
    }
}

