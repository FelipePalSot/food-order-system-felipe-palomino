CREATE TABLE payments (
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT         NOT NULL,
    user_id         BIGINT         NOT NULL,
    amount          DECIMAL(10, 2) NOT NULL,
    method          VARCHAR(20)    NOT NULL,   -- CASH | CARD | ONLINE
    status          VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    transaction_ref VARCHAR(100),
    created_at      TIMESTAMP      DEFAULT NOW()
);

CREATE INDEX idx_payments_order_id ON payments (order_id);

