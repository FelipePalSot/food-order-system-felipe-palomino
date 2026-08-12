-- ============================================
-- V2: Datos iniciales (passwords = "password123" en BCrypt)
-- ============================================
INSERT INTO users (name, email, password, phone, address, role)
VALUES ('Admin', 'admin@foodorder.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        '+51999000001', 'Lima, Peru', 'ADMIN'),
       ('Juan Perez', 'juan@example.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        '+51999000002', 'Miraflores, Lima', 'CUSTOMER'),
       ('Maria Garcia', 'maria@example.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        '+51999000003', 'San Isidro, Lima', 'CUSTOMER');

