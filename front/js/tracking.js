// ========================================
// SEGUIMIENTO DE PEDIDO
// ========================================

let currentOrderId = null;

document.addEventListener('DOMContentLoaded', async () => {
    // Verificar autenticación
    if (!requireAuth()) return;

    // Obtener ID del pedido
    currentOrderId = localStorage.getItem(STORAGE_KEYS.CURRENT_ORDER);

    if (!currentOrderId) {
        showError('error-message', 'No hay pedido para hacer seguimiento');
        setTimeout(() => {
            window.location.href = 'catalog.html';
        }, 2000);
        return;
    }

    // Cargar información del pedido
    await loadOrderTracking();

    // Event listener para actualizar
    document.getElementById('refresh-btn').addEventListener('click', loadOrderTracking);
});

// Cargar información de seguimiento
async function loadOrderTracking() {
    try {
        // 1. Obtener información del pedido
        const orderResponse = await fetchWithAuth(`${API_CONFIG.ORDER_SERVICE}/orders/${currentOrderId}`);

        if (!orderResponse.ok) {
            throw new Error('Error al obtener el pedido');
        }

        const order = await orderResponse.json();

        // Renderizar información del pedido
        document.getElementById('order-id').textContent = order.id;
        document.getElementById('order-amount').textContent = formatPrice(order.totalAmount);
        document.getElementById('delivery-address').textContent = order.deliveryAddress || 'No especificada';

        // Actualizar estado del pedido
        updateOrderStatus(order.status);

        // 2. Obtener información del pago
        try {
            const paymentResponse = await fetchWithAuth(`${API_CONFIG.PAYMENT_SERVICE}/payments/order/${currentOrderId}`);

            if (paymentResponse.ok) {
                const payments = await paymentResponse.json();
                if (payments && payments.length > 0) {
                    const payment = payments[0];
                    document.getElementById('payment-method').textContent = getPaymentMethodText(payment.method);
                    document.getElementById('payment-status').textContent = payment.status;
                    document.getElementById('payment-status').className = `badge badge-${payment.status.toLowerCase()}`;
                    document.getElementById('transaction-ref').textContent = payment.transactionRef || 'N/A';
                }
            }
        } catch (error) {
            console.warn('Error al obtener información del pago:', error);
        }

        // 3. Obtener información de la entrega
        try {
            const deliveryResponse = await fetchWithAuth(`${API_CONFIG.DELIVERY_SERVICE}/deliveries/order/${currentOrderId}`);

            if (deliveryResponse.ok) {
                const delivery = await deliveryResponse.json();
                document.getElementById('delivery-status').textContent = delivery.status;
                document.getElementById('delivery-status').className = `badge badge-${getDeliveryBadgeClass(delivery.status)}`;
                document.getElementById('estimated-minutes').textContent = delivery.estimatedMinutes || 'No disponible';
            }
        } catch (error) {
            console.warn('Error al obtener información de la entrega:', error);
        }

    } catch (error) {
        showError('error-message', error.message || 'Error al cargar información del pedido');
    }
}

// Actualizar visualización del estado del pedido
function updateOrderStatus(status) {
    const statusSteps = document.querySelectorAll('.status-step');

    const statusOrder = ['PENDING', 'CONFIRMED', 'PREPARING', 'READY', 'DELIVERED'];
    const currentIndex = statusOrder.indexOf(status);

    statusSteps.forEach((step, index) => {
        if (index < currentIndex) {
            step.classList.add('completed');
            step.classList.remove('active');
        } else if (index === currentIndex) {
            step.classList.add('active');
            step.classList.remove('completed');
        } else {
            step.classList.remove('active', 'completed');
        }
    });
}

// Obtener texto del método de pago
function getPaymentMethodText(method) {
    const methods = {
        'CASH': '💵 Efectivo',
        'CARD': '💳 Tarjeta',
        'ONLINE': '🌐 Pago Online'
    };
    return methods[method] || method;
}

// Obtener clase del badge según estado de entrega
function getDeliveryBadgeClass(status) {
    const classes = {
        'PENDING': 'pending',
        'ASSIGNED': 'pending',
        'IN_TRANSIT': 'pending',
        'DELIVERED': 'completed'
    };
    return classes[status] || 'pending';
}

