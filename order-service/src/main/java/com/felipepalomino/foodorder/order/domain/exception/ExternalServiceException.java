package com.felipepalomino.foodorder.order.domain.exception;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String service, String message) {
        super("Error al comunicarse con " + service + ": " + message);
    }
}

