package com.felipepalomino.foodorder.order.application.service;

import com.felipepalomino.foodorder.order.domain.exception.OrderNotFoundException;
import com.felipepalomino.foodorder.order.domain.model.Order;
import com.felipepalomino.foodorder.order.domain.model.OrderItem;
import com.felipepalomino.foodorder.order.domain.model.OrderStatus;
import com.felipepalomino.foodorder.order.domain.repository.OrderRepository;
import com.felipepalomino.foodorder.order.infrastructure.client.CatalogServiceClient;
import com.felipepalomino.foodorder.order.infrastructure.messaging.OrderEventProducer;
import com.felipepalomino.foodorder.order.infrastructure.web.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final CatalogServiceClient catalogClient;
    private final OrderEventProducer eventProducer;

    public OrderApplicationService(OrderRepository orderRepository,
                                   CatalogServiceClient catalogClient,
                                   OrderEventProducer eventProducer) {
        this.orderRepository = orderRepository;
        this.catalogClient   = catalogClient;
        this.eventProducer   = eventProducer;
    }

    // ============================================================
    // CASO DE USO: Crear pedido + publicar evento Kafka
    // ============================================================
    public Order createOrder(CreateOrderRequest request) {
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderRequest.ItemRequest itemReq : request.getItems()) {
            // Circuit Breaker activo aquí: si catalog-service cae → fallback
            Map<String, Object> menuItem = catalogClient.getMenuItemById(itemReq.getMenuItemId());

            String name       = (String) menuItem.get("name");
            BigDecimal unitPrice = new BigDecimal(menuItem.get("price").toString());
            BigDecimal subtotal  = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            OrderItem item = new OrderItem();
            item.setMenuItemId(itemReq.getMenuItemId());
            item.setMenuItemName(name);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setSubtotal(subtotal);
            items.add(item);
            total = total.add(subtotal);
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setItems(items);
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        // 📤 Publicar evento Kafka → payment-service escuchará esto
        eventProducer.publishOrderCreated(
                saved.getId(),
                saved.getUserId(),
                saved.getTotalAmount().doubleValue(),
                saved.getDeliveryAddress()
        );

        return saved;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateStatus(Long id, String newStatus) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.valueOf(newStatus.toUpperCase()));
        order.setUpdatedAt(LocalDateTime.now());
        Order updated = orderRepository.save(order);

        // Si el pedido se confirma → publicar evento para delivery-service
        if (OrderStatus.CONFIRMED.name().equalsIgnoreCase(newStatus)) {
            eventProducer.publishOrderConfirmed(
                    updated.getId(),
                    updated.getUserId(),
                    updated.getDeliveryAddress()
            );
        }

        return updated;
    }

    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
