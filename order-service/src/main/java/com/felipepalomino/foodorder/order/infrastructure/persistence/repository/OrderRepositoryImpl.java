package com.felipepalomino.foodorder.order.infrastructure.persistence.repository;

import com.felipepalomino.foodorder.order.domain.model.Order;
import com.felipepalomino.foodorder.order.domain.model.OrderItem;
import com.felipepalomino.foodorder.order.domain.model.OrderStatus;
import com.felipepalomino.foodorder.order.domain.repository.OrderRepository;
import com.felipepalomino.foodorder.order.infrastructure.persistence.entity.OrderEntity;
import com.felipepalomino.foodorder.order.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpa;

    public OrderRepositoryImpl(JpaOrderRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Order save(Order order) {
        return toDomain(jpa.save(toEntity(order)));
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return jpa.findByUserId(userId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Order> findAll() {
        return jpa.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    // ============================================================
    // Mapeos
    // ============================================================
    private Order toDomain(OrderEntity e) {
        Order o = new Order();
        o.setId(e.getId());
        o.setUserId(e.getUserId());
        o.setRestaurantId(e.getRestaurantId());
        o.setTotalAmount(e.getTotalAmount());
        o.setStatus(OrderStatus.valueOf(e.getStatus()));
        o.setDeliveryAddress(e.getDeliveryAddress());
        o.setCreatedAt(e.getCreatedAt());
        o.setUpdatedAt(e.getUpdatedAt());
        if (e.getItems() != null) {
            o.setItems(e.getItems().stream().map(this::toItemDomain).collect(Collectors.toList()));
        }
        return o;
    }

    private OrderItem toItemDomain(OrderItemEntity e) {
        OrderItem i = new OrderItem();
        i.setId(e.getId());
        i.setOrderId(e.getOrder() != null ? e.getOrder().getId() : null);
        i.setMenuItemId(e.getMenuItemId());
        i.setMenuItemName(e.getMenuItemName());
        i.setQuantity(e.getQuantity());
        i.setUnitPrice(e.getUnitPrice());
        i.setSubtotal(e.getSubtotal());
        return i;
    }

    private OrderEntity toEntity(Order o) {
        OrderEntity e = new OrderEntity();
        e.setId(o.getId());
        e.setUserId(o.getUserId());
        e.setRestaurantId(o.getRestaurantId());
        e.setTotalAmount(o.getTotalAmount());
        e.setStatus(o.getStatus() != null ? o.getStatus().name() : OrderStatus.PENDING.name());
        e.setDeliveryAddress(o.getDeliveryAddress());
        e.setCreatedAt(o.getCreatedAt());
        e.setUpdatedAt(o.getUpdatedAt());
        if (o.getItems() != null) {
            List<OrderItemEntity> items = o.getItems().stream().map(i -> {
                OrderItemEntity ie = new OrderItemEntity();
                ie.setId(i.getId());
                ie.setOrder(e);
                ie.setMenuItemId(i.getMenuItemId());
                ie.setMenuItemName(i.getMenuItemName());
                ie.setQuantity(i.getQuantity());
                ie.setUnitPrice(i.getUnitPrice());
                ie.setSubtotal(i.getSubtotal());
                return ie;
            }).collect(Collectors.toList());
            e.setItems(items);
        }
        return e;
    }
}

