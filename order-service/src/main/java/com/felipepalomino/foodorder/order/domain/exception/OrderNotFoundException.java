package com.felipepalomino.foodorder.order.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Pedido no encontrado con id: " + id);
    }
}

