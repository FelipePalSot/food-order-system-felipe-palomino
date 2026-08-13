package com.felipepalomino.foodorder.delivery.domain.repository;

import com.felipepalomino.foodorder.delivery.domain.model.Delivery;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);
    Optional<Delivery> findById(Long id);
    Optional<Delivery> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
    List<Delivery> findAll();
    void deleteById(Long id);
}

