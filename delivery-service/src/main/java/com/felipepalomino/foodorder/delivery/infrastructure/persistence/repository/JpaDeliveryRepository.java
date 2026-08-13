package com.felipepalomino.foodorder.delivery.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.delivery.infrastructure.persistence.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaDeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
    Optional<DeliveryEntity> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
}

