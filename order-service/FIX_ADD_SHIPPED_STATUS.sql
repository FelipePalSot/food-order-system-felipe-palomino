-- ============================================
-- FIX: Agregar SHIPPED al constraint de orders
-- Ejecuta esto en DBeaver en la BD orderdb
-- ============================================
ALTER TABLE orders DROP CONSTRAINT IF EXISTS chk_order_status;

ALTER TABLE orders ADD CONSTRAINT chk_order_status CHECK (
    status IN ('PENDING', 'CONFIRMED', 'SHIPPED', 'PREPARING', 'READY', 'DELIVERED', 'CANCELLED')
);

-- Verificar que se aplicó
SELECT constraint_name, check_clause
FROM information_schema.check_constraints
WHERE constraint_name = 'chk_order_status';

