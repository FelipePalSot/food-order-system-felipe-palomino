-- ============================================
-- V1: Crear tablas del User Service
-- ============================================

CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(150) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    phone      VARCHAR(20),
    address    VARCHAR(255),
    role       VARCHAR(20)         NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP           DEFAULT NOW(),

    -- Validación: solo permite roles definidos en UserRole.java
    CONSTRAINT chk_user_role CHECK (
        role IN ('CUSTOMER', 'ADMIN')
    )
);

-- Índices para optimizar consultas
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role ON users (role);


