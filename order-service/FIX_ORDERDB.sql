-- ============================================
-- FIX: Recrear tablas de orderdb
-- Ejecuta esto en DBeaver en la BD orderdb
-- ============================================

-- 1. Borrar tablas existentes
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;

-- 2. Volver a crear tablas con estructura correcta
CREATE TABLE orders (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT         NOT NULL,
    restaurant_id    BIGINT         NOT NULL,
    total_amount     DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    status           VARCHAR(30)    NOT NULL DEFAULT 'PENDING',
    delivery_address VARCHAR(255),
    created_at       TIMESTAMP      DEFAULT NOW(),
    updated_at       TIMESTAMP      DEFAULT NOW(),

    -- Validación: solo permite estados definidos en OrderStatus.java
    CONSTRAINT chk_order_status CHECK (
        status IN ('PENDING', 'CONFIRMED', 'PREPARING', 'READY', 'DELIVERED', 'CANCELLED')
    )
);

CREATE TABLE order_items (
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT         NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    menu_item_id   BIGINT         NOT NULL,
    menu_item_name VARCHAR(100)   NOT NULL,
    quantity       INTEGER        NOT NULL CHECK (quantity > 0),
    unit_price     DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    subtotal       DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0)
);

-- Índices para optimizar consultas
CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_orders_restaurant_id ON orders (restaurant_id);
CREATE INDEX idx_orders_status ON orders (status);
CREATE INDEX idx_order_items_order_id ON order_items (order_id);

-- 3. Insertar datos de prueba
INSERT INTO orders (user_id, restaurant_id, total_amount, status, delivery_address)
VALUES
    (2, 1, 33.00, 'CONFIRMED', 'Av. Arequipa 1234, Lima'),
    (3, 2, 26.00, 'PENDING', 'Calle Los Pinos 567, Lima');

INSERT INTO order_items (order_id, menu_item_id, menu_item_name, quantity, unit_price, subtotal)
VALUES
    -- Pedido 1: Ceviche (2) + Tiradito (1)
    (1, 1, 'Ceviche de Pescado', 2, 15.00, 30.00),
    (1, 2, 'Tiradito', 1, 18.00, 18.00),

    -- Pedido 2: Lomo Saltado (2)
    (2, 7, 'Lomo Saltado', 2, 14.00, 28.00);

