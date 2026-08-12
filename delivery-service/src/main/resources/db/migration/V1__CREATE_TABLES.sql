-- ============================================
-- V1: Tablas del Delivery Service
-- ============================================

CREATE TABLE deliveries (
    id                BIGSERIAL PRIMARY KEY,
    order_id          BIGINT       NOT NULL,
    user_id           BIGINT       NOT NULL,
    delivery_address  VARCHAR(255) NOT NULL,
    status            VARCHAR(30)  NOT NULL DEFAULT 'PENDING',
    estimated_minutes INTEGER      CHECK (estimated_minutes > 0),
    created_at        TIMESTAMP    DEFAULT NOW(),
    updated_at        TIMESTAMP    DEFAULT NOW(),

    -- Validación: solo permite estados definidos en DeliveryStatus.java
    CONSTRAINT chk_delivery_status CHECK (
        status IN ('PENDING', 'ASSIGNED', 'IN_TRANSIT', 'DELIVERED')
    )
);

-- Índices para optimizar consultas
CREATE INDEX idx_deliveries_order_id ON deliveries (order_id);
CREATE INDEX idx_deliveries_user_id ON deliveries (user_id);
CREATE INDEX idx_deliveries_status ON deliveries (status);


