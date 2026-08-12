-- ============================================
-- V1: Tablas del Payment Service
-- ============================================

CREATE TABLE payments (
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT         NOT NULL,
    user_id         BIGINT         NOT NULL,
    amount          DECIMAL(10, 2) NOT NULL CHECK (amount > 0),
    method          VARCHAR(20)    NOT NULL,
    status          VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    transaction_ref VARCHAR(100),
    created_at      TIMESTAMP      DEFAULT NOW(),

    -- Validación: métodos de pago permitidos (PaymentMethod.java)
    CONSTRAINT chk_payment_method CHECK (
        method IN ('CASH', 'CARD', 'ONLINE')
    ),

    -- Validación: estados permitidos (PaymentStatus.java)
    CONSTRAINT chk_payment_status CHECK (
        status IN ('PENDING', 'COMPLETED', 'FAILED')
    )
);

-- Índices para optimizar consultas
CREATE INDEX idx_payments_order_id ON payments (order_id);
CREATE INDEX idx_payments_user_id ON payments (user_id);
CREATE INDEX idx_payments_status ON payments (status);


