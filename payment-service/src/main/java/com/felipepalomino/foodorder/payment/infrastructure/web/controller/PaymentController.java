package com.felipepalomino.foodorder.payment.infrastructure.web.controller;

import com.felipepalomino.foodorder.payment.application.service.PaymentApplicationService;
import com.felipepalomino.foodorder.payment.domain.model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentApplicationService paymentService;

    public PaymentController(PaymentApplicationService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody Map<String, Object> body) {
        Payment p = paymentService.processPayment(
                Long.valueOf(body.get("orderId").toString()),
                Long.valueOf(body.get("userId").toString()),
                new BigDecimal(body.get("amount").toString()),
                (String) body.get("method")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrder(orderId));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Payment> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.confirmPayment(id));
    }

    // Cambiar estado libremente (para demo/clase)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Payment> updateStatus(@PathVariable Long id,
                                                @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(paymentService.updateStatus(id, body.get("status")));
    }
}
