-- ============================================
-- V1: Tablas del Order Service
-- ============================================

CREATE TABLE orders (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT         NOT NULL,
    restaurant_id    BIGINT         NOT NULL,
    total_amount     DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    status           VARCHAR(30)    NOT NULL DEFAULT 'PENDING',
    delivery_address VARCHAR(255),
    created_at       TIMESTAMP      DEFAULT NOW(),
    updated_at       TIMESTAMP      DEFAULT NOW(),

    CONSTRAINT chk_order_status CHECK (
        status IN ('PENDING', 'CONFIRMED', 'SHIPPED', 'PREPARING', 'READY', 'DELIVERED', 'CANCELLED')
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
