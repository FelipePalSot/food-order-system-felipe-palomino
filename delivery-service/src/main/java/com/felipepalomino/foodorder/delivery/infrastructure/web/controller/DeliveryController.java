package com.felipepalomino.foodorder.delivery.infrastructure.web.controller;

import com.felipepalomino.foodorder.delivery.application.service.DeliveryApplicationService;
import com.felipepalomino.foodorder.delivery.domain.model.Delivery;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryApplicationService deliveryService;

    public DeliveryController(DeliveryApplicationService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Map<String, Object> body) {
        Delivery d = deliveryService.createDelivery(
                Long.valueOf(body.get("orderId").toString()),
                Long.valueOf(body.get("userId").toString()),
                (String) body.get("deliveryAddress"),
                body.get("estimatedMinutes") != null ? Integer.valueOf(body.get("estimatedMinutes").toString()) : null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(d);
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> getAll() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Delivery> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(deliveryService.getDeliveryByOrderId(orderId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Delivery> updateStatus(@PathVariable Long id,
                                                 @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(deliveryService.updateStatus(id, body.get("status")));
    }
}

