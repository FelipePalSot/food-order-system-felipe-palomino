# 🍕 Frontend - Food Order System

Frontend simple (HTML, CSS, JavaScript puro) para probar el flujo completo del sistema de pedidos de comida.

---

## 📋 Requisitos

1. **Microservicios corriendo:**
   - `user-service` en puerto **8081**
   - `catalog-service` en puerto **8082**
   - `order-service` en puerto **8083**
   - `delivery-service` en puerto **8084**
   - `payment-service` en puerto **8085**

2. **Navegador web moderno** (Chrome, Firefox, Edge, Safari)

3. **Servidor HTTP** (para evitar problemas CORS)

---

## 🚀 Cómo ejecutar

### Opción 1: Usando Python (Recomendado)

```bash
cd front
python3 -m http.server 3000
```

Luego abre: http://localhost:3000

### Opción 2: Usando Node.js

```bash
cd front
npx http-server -p 3000
```

Luego abre: http://localhost:3000

### Opción 3: Usando PHP

```bash
cd front
php -S localhost:3000
```

Luego abre: http://localhost:3000

### Opción 4: Extensión de VS Code

Instala la extensión **Live Server** y haz click derecho en `index.html` → "Open with Live Server"

---

## 🔑 Usuarios de prueba

### Usuario Admin:
```
Email: admin@admin.com
Password: admin123
```

### Usuario Regular:
```
Email: juan@example.com
Password: password123
```

---

## 📱 Flujo de la aplicación

1. **Login** (`index.html`)
   - Autenticación con JWT
   - Guarda el token en localStorage

2. **Catálogo** (`catalog.html`)
   - Ver restaurantes (Pescados, Criolla, Pastas)
   - Ver menús de cada restaurante
   - Agregar items al carrito
   - Ver carrito (modal)

3. **Checkout** (`checkout.html`)
   - Revisar resumen del pedido
   - Ingresar dirección de entrega
   - Seleccionar método de pago (Efectivo, Tarjeta, Online)
   - Confirmar y pagar

4. **Seguimiento** (`tracking.html`)
   - Ver estado del pedido
   - Ver estado del pago
   - Ver estado de la entrega
   - Actualizar en tiempo real

---

## 🗂️ Estructura de archivos

```
front/
├── index.html          # Página de login
├── catalog.html        # Catálogo de restaurantes
├── checkout.html       # Checkout y pago
├── tracking.html       # Seguimiento del pedido
├── css/
│   └── styles.css      # Estilos generales
└── js/
    ├── config.js       # Configuración de APIs
    ├── utils.js        # Utilidades generales
    ├── auth.js         # Lógica de autenticación
    ├── catalog.js      # Lógica del catálogo
    ├── checkout.js     # Lógica del checkout
    └── tracking.js     # Lógica del seguimiento
```

---

## ⚙️ Configuración

Si tus microservicios están en puertos diferentes, edita `js/config.js`:

```javascript
const API_CONFIG = {
    USER_SERVICE: 'http://localhost:8081/api',
    CATALOG_SERVICE: 'http://localhost:8082/api',
    ORDER_SERVICE: 'http://localhost:8083/api',
    PAYMENT_SERVICE: 'http://localhost:8085/api',
    DELIVERY_SERVICE: 'http://localhost:8084/api'
};
```

---

## 🐛 Solución de problemas

### Error: CORS
- Asegúrate de ejecutar el frontend desde un servidor HTTP
- Verifica que los microservicios tengan CORS habilitado

### Error: No se puede conectar a la API
- Verifica que todos los microservicios estén corriendo
- Revisa los puertos en `js/config.js`

### Error: Token inválido
- Cierra sesión y vuelve a iniciar sesión
- Verifica que el endpoint `/auth/login` funcione

---

## 📸 Capturas de pantalla

### Login
Login simple con credenciales predefinidas y validación JWT.

### Catálogo
Visualización de 3 restaurantes con sus menús, precios y botón de agregar al carrito.

### Checkout
Resumen del pedido, dirección de entrega y selección de método de pago.

### Tracking
Estado del pedido en tiempo real con timeline visual.

---

## 🎯 Funcionalidades implementadas

✅ Autenticación JWT  
✅ Visualización de restaurantes  
✅ Visualización de menús  
✅ Carrito de compras  
✅ Creación de pedidos  
✅ Procesamiento de pagos  
✅ Asignación de entregas  
✅ Seguimiento en tiempo real  
✅ Diseño responsive  
✅ Manejo de errores  

---

## 🔐 Seguridad

- Token JWT almacenado en `localStorage`
- Validación de autenticación en cada página
- Redirección automática al login si el token expira
- Headers de autorización en todas las peticiones

---

## 🚀 Para tu clase

Este frontend te permite demostrar:
1. ✅ Autenticación con JWT
2. ✅ Comunicación REST entre servicios
3. ✅ Flujo completo de un pedido
4. ✅ Arquitectura de microservicios en acción
5. ✅ Manejo de estados (pendiente → confirmado → entregado)

---

**¡Listo para probar!** 🎉

