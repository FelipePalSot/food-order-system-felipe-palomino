package com.felipepalomino.foodorder.payment.infrastructure.messaging;

import com.felipepalomino.foodorder.payment.application.service.PaymentApplicationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * INFRAESTRUCTURA - Mensajería: Escucha eventos de pedidos desde Kafka.
 *
 * Cuando order-service publica "order.created",
 * payment-service lo recibe y procesa el pago automáticamente.
 *
 * Ventaja sobre REST síncrono:
 *  - Si payment-service estaba caído, el mensaje queda en Kafka
 *    y se procesa cuando vuelve. ¡No se pierde el pedido!
 */
@Component
public class PaymentEventConsumer {

    private final PaymentApplicationService paymentService;

    public PaymentEventConsumer(PaymentApplicationService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Escucha el topic "order.created" y procesa el pago.
     * groupId = "payment-service-group" → cada instancia del servicio
     * procesa el mensaje UNA sola vez (idempotencia).
     */
    @KafkaListener(topics = "order.created", groupId = "payment-service-group")
    public void handleOrderCreated(Map<String, Object> event) {
        System.out.println("📥 Kafka ← order.created recibido: " + event);

        Long orderId = Long.valueOf(event.get("orderId").toString());
        Long userId  = Long.valueOf(event.get("userId").toString());
        double amount = Double.parseDouble(event.get("totalAmount").toString());

        // Procesar pago automáticamente con método ONLINE por defecto
        paymentService.processPayment(orderId, userId, new BigDecimal(amount), "ONLINE");
        System.out.println("✅ Pago procesado vía Kafka para orderId=" + orderId);
    }
}

