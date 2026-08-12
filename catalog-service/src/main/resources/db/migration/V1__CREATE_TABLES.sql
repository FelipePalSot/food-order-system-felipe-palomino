-- ============================================
-- V1: Tablas del Catalog Service
-- ============================================

CREATE TABLE restaurants (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    type        VARCHAR(30)  NOT NULL,
    description VARCHAR(255),
    address     VARCHAR(255),
    active      BOOLEAN      DEFAULT TRUE,

    -- Validación: solo 3 tipos de restaurantes permitidos
    CONSTRAINT chk_restaurant_type CHECK (
        type IN ('PESCADOS', 'CRIOLLA', 'PASTAS')
    )
);

CREATE TABLE menu_items (
    id            BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT          NOT NULL REFERENCES restaurants (id) ON DELETE CASCADE,
    name          VARCHAR(100)    NOT NULL,
    description   VARCHAR(255),
    price         DECIMAL(10, 2)  NOT NULL CHECK (price >= 0),
    category      VARCHAR(50),
    available     BOOLEAN         DEFAULT TRUE
);

-- Índices para optimizar consultas
CREATE INDEX idx_restaurants_type ON restaurants (type);
CREATE INDEX idx_restaurants_active ON restaurants (active);
CREATE INDEX idx_menu_items_restaurant ON menu_items (restaurant_id);
CREATE INDEX idx_menu_items_available ON menu_items (available);


