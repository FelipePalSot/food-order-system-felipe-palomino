package com.felipepalomino.foodorder.delivery.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.delivery.domain.model.Delivery;
import com.felipepalomino.foodorder.delivery.domain.model.DeliveryStatus;
import com.felipepalomino.foodorder.delivery.domain.repository.DeliveryRepository;
import com.felipepalomino.foodorder.delivery.infrastructure.persistence.entity.DeliveryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final JpaDeliveryRepository jpa;

    public DeliveryRepositoryImpl(JpaDeliveryRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Delivery save(Delivery d) { return toDomain(jpa.save(toEntity(d))); }

    @Override
    public Optional<Delivery> findById(Long id) { return jpa.findById(id).map(this::toDomain); }

    @Override
    public Optional<Delivery> findByOrderId(Long orderId) { return jpa.findByOrderId(orderId).map(this::toDomain); }

    @Override
    public List<Delivery> findAll() { return jpa.findAll().stream().map(this::toDomain).collect(Collectors.toList()); }

    @Override
    public void deleteById(Long id) { jpa.deleteById(id); }

    private Delivery toDomain(DeliveryEntity e) {
        Delivery d = new Delivery();
        d.setId(e.getId());
        d.setOrderId(e.getOrderId());
        d.setUserId(e.getUserId());
        d.setDeliveryAddress(e.getDeliveryAddress());
        d.setStatus(DeliveryStatus.valueOf(e.getStatus()));
        d.setEstimatedMinutes(e.getEstimatedMinutes());
        d.setCreatedAt(e.getCreatedAt());
        d.setUpdatedAt(e.getUpdatedAt());
        return d;
    }

    private DeliveryEntity toEntity(Delivery d) {
        DeliveryEntity e = new DeliveryEntity();
        e.setId(d.getId());
        e.setOrderId(d.getOrderId());
        e.setUserId(d.getUserId());
        e.setDeliveryAddress(d.getDeliveryAddress());
        e.setStatus(d.getStatus() != null ? d.getStatus().name() : DeliveryStatus.PENDING.name());
        e.setEstimatedMinutes(d.getEstimatedMinutes());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}

