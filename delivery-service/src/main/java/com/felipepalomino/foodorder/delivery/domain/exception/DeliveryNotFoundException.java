package com.felipepalomino.foodorder.delivery.domain.exception;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(Long id) {
        super("Entrega no encontrada con id: " + id);
    }
}

