package com.felipepalomino.foodorder.delivery.infrastructure.messaging;

import com.felipepalomino.foodorder.delivery.application.service.DeliveryApplicationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * INFRAESTRUCTURA - Mensajería: Escucha eventos de pedidos confirmados.
 *
 * Cuando el pago es exitoso, order-service publica "order.confirmed"
 * y delivery-service asigna automáticamente una entrega.
 */
@Component
public class DeliveryEventConsumer {

    private final DeliveryApplicationService deliveryService;

    public DeliveryEventConsumer(DeliveryApplicationService deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * Escucha el topic "order.confirmed" y crea la entrega.
     */
    @KafkaListener(topics = "order.confirmed", groupId = "delivery-service-group")
    public void handleOrderConfirmed(Map<String, Object> event) {
        System.out.println("📥 Kafka ← order.confirmed recibido: " + event);

        Long orderId         = Long.valueOf(event.get("orderId").toString());
        Long userId          = Long.valueOf(event.get("userId").toString());
        String address       = (String) event.get("deliveryAddress");

        // Evitar duplicados: si ya existe entrega para este pedido, ignorar
        if (deliveryService.existsByOrderId(orderId)) {
            System.out.println("⚠️ Ya existe entrega para orderId=" + orderId + ", ignorando evento duplicado.");
            return;
        }

        // Crear entrega con 30 minutos estimados por defecto
        deliveryService.createDelivery(orderId, userId, address, 30);
        System.out.println("✅ Entrega asignada vía Kafka para orderId=" + orderId);
    }
}

