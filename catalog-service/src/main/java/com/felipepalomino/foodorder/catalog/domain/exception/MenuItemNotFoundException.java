package com.felipepalomino.foodorder.catalog.domain.exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(Long id) {
        super("Item de menú no encontrado con id: " + id);
    }
}

