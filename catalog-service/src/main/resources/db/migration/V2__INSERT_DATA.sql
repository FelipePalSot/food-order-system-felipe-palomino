-- ============================================
-- V2: Datos iniciales (3 restaurantes del sistema)
-- ============================================
INSERT INTO restaurants (name, type, description, address)
VALUES ('El Cevichero', 'PESCADOS', 'Los mejores ceviches y mariscos de Lima', 'Miraflores, Lima'),
       ('La Criollita', 'CRIOLLA', 'Comida criolla tradicional peruana', 'Barranco, Lima'),
       ('Pasta e Basta', 'PASTAS', 'Pastas artesanales al estilo italiano', 'San Isidro, Lima');

INSERT INTO menu_items (restaurant_id, name, description, price, category)
VALUES
    -- El Cevichero (id=1)
    (1, 'Ceviche Clásico', 'Ceviche de pescado con leche de tigre y choclo', 35.00, 'CEVICHE'),
    (1, 'Tiradito', 'Tiradito de lenguado con ají amarillo', 38.00, 'TIRADITO'),
    (1, 'Leche de Tigre', 'Jugo de ceviche con mariscos', 20.00, 'BEBIDA'),
    (1, 'Jalea Mixta', 'Mixtura frita de mariscos con yuca y salsa criolla', 45.00, 'FRITO'),
    -- La Criollita (id=2)
    (2, 'Lomo Saltado', 'Lomo de res salteado con papas fritas y arroz', 32.00, 'SEGUNDO'),
    (2, 'Ají de Gallina', 'Pollo desmenuzado en salsa de ají amarillo con arroz', 28.00, 'SEGUNDO'),
    (2, 'Cau Cau', 'Mondongo guisado con papas y hierbabuena', 25.00, 'SEGUNDO'),
    (2, 'Anticuchos', 'Corazón de res a la parrilla con papas doradas', 22.00, 'ENTRADA'),
    -- Pasta e Basta (id=3)
    (3, 'Spaghetti Carbonara', 'Pasta con huevo, queso parmesano y panceta', 30.00, 'PASTA'),
    (3, 'Fettuccine Alfredo', 'Pasta con salsa de crema y queso parmesano', 28.00, 'PASTA'),
    (3, 'Lasaña Bolognesa', 'Lasaña con salsa boloñesa casera', 35.00, 'PASTA'),
    (3, 'Risotto de Hongos', 'Arroz cremoso con hongos porcini', 32.00, 'ARROZ');

