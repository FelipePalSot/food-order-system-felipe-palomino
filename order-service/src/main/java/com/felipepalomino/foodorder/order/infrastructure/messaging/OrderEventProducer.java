package com.felipepalomino.foodorder.order.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * INFRAESTRUCTURA - Mensajería: Publica eventos de pedidos en Kafka.
 *
 * FLUJO ASÍNCRONO:
 *  order-service publica → "order.created"  → payment-service escucha
 *  order-service publica → "order.confirmed" → delivery-service escucha
 */
@Component
public class OrderEventProducer {

    public static final String TOPIC_ORDER_CREATED   = "order.created";
    public static final String TOPIC_ORDER_CONFIRMED = "order.confirmed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publica el evento cuando se crea un pedido.
     * payment-service escucha este evento para procesar el pago.
     */
    public void publishOrderCreated(Long orderId, Long userId, Double totalAmount, String deliveryAddress) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ORDER_CREATED");
        event.put("orderId", orderId);
        event.put("userId", userId);
        event.put("totalAmount", totalAmount);
        event.put("deliveryAddress", deliveryAddress);

        kafkaTemplate.send(TOPIC_ORDER_CREATED, String.valueOf(orderId), event);
        System.out.println("📤 Kafka → order.created publicado: orderId=" + orderId);
    }

    /**
     * Publica el evento cuando el pedido es confirmado (pago exitoso).
     * delivery-service escucha este evento para asignar la entrega.
     */
    public void publishOrderConfirmed(Long orderId, Long userId, String deliveryAddress) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "ORDER_CONFIRMED");
        event.put("orderId", orderId);
        event.put("userId", userId);
        event.put("deliveryAddress", deliveryAddress);

        kafkaTemplate.send(TOPIC_ORDER_CONFIRMED, String.valueOf(orderId), event);
        System.out.println("📤 Kafka → order.confirmed publicado: orderId=" + orderId);
    }
}

