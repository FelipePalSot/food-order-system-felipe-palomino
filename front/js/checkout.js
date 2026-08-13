// ========================================
// CHECKOUT Y PAGO
// ========================================

document.addEventListener('DOMContentLoaded', () => {
    // Verificar autenticación
    if (!requireAuth()) return;

    // Verificar que haya items en el carrito
    const cart = getCart();
    if (cart.length === 0) {
        alert('El carrito está vacío');
        window.location.href = 'catalog.html';
        return;
    }

    // Renderizar resumen del pedido
    renderOrderSummary();

    // Event listener para el formulario
    const checkoutForm = document.getElementById('checkout-form');
    checkoutForm.addEventListener('submit', handleCheckout);
});

// Renderizar resumen del pedido
function renderOrderSummary() {
    const cart = getCart();
    const summaryContainer = document.getElementById('order-summary');
    const totalElement = document.getElementById('order-total');

    summaryContainer.innerHTML = '';

    cart.forEach(item => {
        const itemElement = document.createElement('div');
        itemElement.className = 'order-summary-item';
        itemElement.innerHTML = `
            <div>
                <strong>${item.menuItemName}</strong> (x${item.quantity})
                <br>
                <small>${item.restaurantName}</small>
            </div>
            <div>$${formatPrice(item.unitPrice * item.quantity)}</div>
        `;
        summaryContainer.appendChild(itemElement);
    });

    const total = calculateCartTotal();
    totalElement.textContent = formatPrice(total);
}

// Manejar el checkout
async function handleCheckout(e) {
    e.preventDefault();

    const deliveryAddress = document.getElementById('delivery-address').value;
    const paymentMethod = document.querySelector('input[name="payment-method"]:checked').value;
    const cart = getCart();
    const user = getUser();

    // Validar que todos los items sean del mismo restaurante
    const restaurantIds = [...new Set(cart.map(item => item.restaurantId))];
    if (restaurantIds.length > 1) {
        showError('error-message', 'Error: Solo puedes pedir de un restaurante a la vez');
        return;
    }

    const restaurantId = restaurantIds[0];
    const totalAmount = calculateCartTotal();

    try {
        // 1. Crear el pedido
        const orderData = {
            userId: user.id,
            restaurantId: restaurantId,
            totalAmount: totalAmount,
            deliveryAddress: deliveryAddress,
            items: cart.map(item => ({
                menuItemId: item.menuItemId,
                menuItemName: item.menuItemName,
                quantity: item.quantity,
                unitPrice: item.unitPrice
            }))
        };

        const orderResponse = await fetchWithAuth(`${API_CONFIG.ORDER_SERVICE}/orders`, {
            method: 'POST',
            body: JSON.stringify(orderData)
        });

        if (!orderResponse.ok) {
            throw new Error('Error al crear el pedido');
        }

        const order = await orderResponse.json();
        const orderId = order.id;

        // 2. Procesar el pago
        const paymentData = {
            orderId: orderId,
            userId: user.id,
            amount: totalAmount,
            method: paymentMethod
        };

        const paymentResponse = await fetchWithAuth(`${API_CONFIG.PAYMENT_SERVICE}/payments`, {
            method: 'POST',
            body: JSON.stringify(paymentData)
        });

        if (!paymentResponse.ok) {
            throw new Error('Error al procesar el pago');
        }

        const payment = await paymentResponse.json();

        // 3. Crear la entrega
        const deliveryData = {
            orderId: orderId,
            userId: user.id,
            deliveryAddress: deliveryAddress
        };

        const deliveryResponse = await fetchWithAuth(`${API_CONFIG.DELIVERY_SERVICE}/deliveries`, {
            method: 'POST',
            body: JSON.stringify(deliveryData)
        });

        if (!deliveryResponse.ok) {
            console.warn('Error al crear la entrega, pero el pedido fue procesado');
        }

        // Guardar el ID del pedido
        localStorage.setItem(STORAGE_KEYS.CURRENT_ORDER, orderId);

        // Limpiar carrito
        clearCart();

        // Mostrar mensaje de éxito
        showSuccess('success-message', '✅ ¡Pedido procesado exitosamente!');

        // Redirigir a tracking después de 2 segundos
        setTimeout(() => {
            window.location.href = 'tracking.html';
        }, 2000);

    } catch (error) {
        showError('error-message', error.message || 'Error al procesar el pedido');
    }
}

