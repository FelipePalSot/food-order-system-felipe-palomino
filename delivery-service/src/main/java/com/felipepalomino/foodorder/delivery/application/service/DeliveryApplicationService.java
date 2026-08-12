package com.felipepalomino.foodorder.delivery.application.service;

import com.felipepalomino.foodorder.delivery.domain.exception.DeliveryNotFoundException;
import com.felipepalomino.foodorder.delivery.domain.model.Delivery;
import com.felipepalomino.foodorder.delivery.domain.model.DeliveryStatus;
import com.felipepalomino.foodorder.delivery.domain.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryApplicationService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery createDelivery(Long orderId, Long userId, String deliveryAddress, Integer estimatedMinutes) {
        Delivery d = new Delivery();
        d.setOrderId(orderId);
        d.setUserId(userId);
        d.setDeliveryAddress(deliveryAddress);
        d.setEstimatedMinutes(estimatedMinutes);
        d.setStatus(DeliveryStatus.PENDING);
        d.setCreatedAt(LocalDateTime.now());
        d.setUpdatedAt(LocalDateTime.now());
        return deliveryRepository.save(d);
    }

    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Delivery getDeliveryByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DeliveryNotFoundException(orderId));
    }

    @Transactional(readOnly = true)
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery updateStatus(Long id, String newStatus) {
        Delivery d = getDeliveryById(id);
        d.setStatus(DeliveryStatus.valueOf(newStatus.toUpperCase()));
        d.setUpdatedAt(LocalDateTime.now());
        return deliveryRepository.save(d);
    }
}

