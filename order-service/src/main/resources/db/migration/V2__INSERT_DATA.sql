-- ============================================
-- V2: Datos de prueba
-- ============================================
INSERT INTO orders (user_id, restaurant_id, total_amount, status, delivery_address)
VALUES (2, 1, 73.00, 'DELIVERED', 'Miraflores, Lima'),
       (3, 2, 60.00, 'CONFIRMED', 'San Isidro, Lima');

INSERT INTO order_items (order_id, menu_item_id, menu_item_name, quantity, unit_price, subtotal)
VALUES (1, 1, 'Ceviche Clásico', 1, 35.00, 35.00),
       (1, 2, 'Tiradito', 1, 38.00, 38.00),
       (2, 5, 'Lomo Saltado', 1, 32.00, 32.00),
       (2, 6, 'Ají de Gallina', 1, 28.00, 28.00);

