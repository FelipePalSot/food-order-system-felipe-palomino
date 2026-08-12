package com.felipepalomino.foodorder.catalog.domain.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long id) {
        super("Restaurante no encontrado con id: " + id);
    }
}

