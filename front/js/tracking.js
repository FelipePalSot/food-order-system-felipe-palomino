// ========================================
// SEGUIMIENTO DE PEDIDO
// Variables globales
// ========================================
let currentOrderId  = null;
let currentPaymentId = null;
let currentDeliveryId = null;

document.addEventListener('DOMContentLoaded', async () => {
    if (!requireAuth()) return;
    currentOrderId = localStorage.getItem(STORAGE_KEYS.CURRENT_ORDER);
    if (!currentOrderId) {
        showError('error-message', 'No hay pedido para hacer seguimiento');
        setTimeout(() => { window.location.href = 'catalog.html'; }, 2000);
        return;
    }
    await loadOrderTracking();
    document.getElementById('refresh-btn').addEventListener('click', loadOrderTracking);
});

// ── Cargar todo el tracking ─────────────────────────────────────
async function loadOrderTracking() {
    try {
        // 1. Pedido
        const orderRes = await fetchWithAuth(`${API_CONFIG.ORDER_SERVICE}/orders/${currentOrderId}`);
        if (!orderRes.ok) throw new Error('Error al obtener el pedido');
        const order = await orderRes.json();

        document.getElementById('order-id').textContent        = order.id;
        document.getElementById('order-amount').textContent    = formatPrice(order.totalAmount);
        document.getElementById('delivery-address').textContent = order.deliveryAddress || '-';
        highlightOrderStatus(order.status);

        // 2. Pago
        try {
            const payRes = await fetchWithAuth(`${API_CONFIG.PAYMENT_SERVICE}/payments/order/${currentOrderId}`);
            if (payRes.ok) {
                const payments = await payRes.json();
                if (payments && payments.length > 0) {
                    const pay = payments[0];
                    currentPaymentId = pay.id;
                    document.getElementById('payment-method').textContent  = getPayMethodText(pay.method);
                    document.getElementById('payment-status').textContent  = pay.status;
                    document.getElementById('payment-status').className    = `badge badge-${pay.status.toLowerCase()}`;
                    document.getElementById('transaction-ref').textContent = pay.transactionRef || 'N/A';
                    highlightPaymentStatus(pay.status);
                }
            }
        } catch (e) { console.warn('Sin pago aún', e); }

        // 3. Entrega
        try {
            const delRes = await fetchWithAuth(`${API_CONFIG.DELIVERY_SERVICE}/deliveries/order/${currentOrderId}`);
            if (delRes.ok) {
                const del = await delRes.json();
                currentDeliveryId = del.id;
                document.getElementById('delivery-status').textContent    = del.status;
                document.getElementById('delivery-status').className      = `badge badge-${getDelBadge(del.status)}`;
                document.getElementById('estimated-minutes').textContent  = del.estimatedMinutes || '-';
                highlightDeliveryStatus(del.status);
            }
        } catch (e) { console.warn('Sin entrega aún', e); }

    } catch (err) {
        showError('error-message', err.message);
    }
}

// ── CAMBIAR ESTADO DEL PEDIDO ──────────────────────────────────
// Endpoint: PATCH /api/orders/{id}/status  → body: {status}
async function changeOrderStatus(newStatus) {
    try {
        const res = await fetchWithAuth(
            `${API_CONFIG.ORDER_SERVICE}/orders/${currentOrderId}/status`,
            { method: 'PATCH', body: JSON.stringify({ status: newStatus }) }
        );
        const data = await res.json();
        if (!res.ok) throw new Error(data.error || 'Error al cambiar estado del pedido');
        highlightOrderStatus(data.status);
        showSuccess('success-message', `✅ Pedido: ${data.status}`);
    } catch (e) { showError('error-message', e.message); }
}

// ── CAMBIAR ESTADO DEL PAGO ────────────────────────────────────
// Endpoint: PATCH /api/payments/{id}/status  → body: {status}
async function changePaymentStatus(newStatus) {
    if (!currentPaymentId) {
        showError('error-message', 'No hay pago registrado para este pedido');
        return;
    }
    try {
        const res = await fetchWithAuth(
            `${API_CONFIG.PAYMENT_SERVICE}/payments/${currentPaymentId}/status`,
            { method: 'PATCH', body: JSON.stringify({ status: newStatus }) }
        );
        const data = await res.json();
        if (!res.ok) throw new Error(data.error || 'Error al cambiar estado del pago');
        document.getElementById('payment-status').textContent = data.status;
        document.getElementById('payment-status').className   = `badge badge-${data.status.toLowerCase()}`;
        highlightPaymentStatus(data.status);
        showSuccess('success-message', `✅ Pago: ${data.status}`);
    } catch (e) { showError('error-message', e.message); }
}

// ── CAMBIAR ESTADO DE ENTREGA ──────────────────────────────────
// Endpoint: PATCH /api/deliveries/{id}/status  → body: {status}
async function changeDeliveryStatus(newStatus) {
    if (!currentDeliveryId) {
        showError('error-message', 'No hay entrega registrada para este pedido');
        return;
    }
    try {
        const res = await fetchWithAuth(
            `${API_CONFIG.DELIVERY_SERVICE}/deliveries/${currentDeliveryId}/status`,
            { method: 'PATCH', body: JSON.stringify({ status: newStatus }) }
        );
        const data = await res.json();
        if (!res.ok) throw new Error(data.error || 'Error al cambiar estado de entrega');
        document.getElementById('delivery-status').textContent = data.status;
        document.getElementById('delivery-status').className   = `badge badge-${getDelBadge(data.status)}`;
        highlightDeliveryStatus(data.status);
        showSuccess('success-message', `✅ Entrega: ${data.status}`);
    } catch (e) { showError('error-message', e.message); }
}

// ── Resaltar botón activo PEDIDO ────────────────────────────────
function highlightOrderStatus(status) {
    const colors = { PENDING:'#ffc107', CONFIRMED:'#28a745', SHIPPED:'#667eea' };
    const lbl = document.getElementById('current-order-status');
    lbl.textContent = status;
    lbl.style.color = colors[status] || '#333';
    const map = { PENDING:'btn-pending', CONFIRMED:'btn-confirmed', SHIPPED:'btn-shipped' };
    ['btn-pending','btn-confirmed','btn-shipped'].forEach(id => {
        document.getElementById(id)?.classList.remove('active-state');
    });
    if (map[status]) document.getElementById(map[status])?.classList.add('active-state');
}

// ── Resaltar botón activo PAGO ──────────────────────────────────
function highlightPaymentStatus(status) {
    const map = { PENDING:'btn-pay-pending', COMPLETED:'btn-pay-completed', FAILED:'btn-pay-failed' };
    ['btn-pay-pending','btn-pay-completed','btn-pay-failed'].forEach(id => {
        document.getElementById(id)?.classList.remove('active-state');
    });
    if (map[status]) document.getElementById(map[status])?.classList.add('active-state');
}

// ── Resaltar botón activo ENTREGA ───────────────────────────────
function highlightDeliveryStatus(status) {
    const map = { PENDING:'btn-del-pending', ASSIGNED:'btn-del-assigned', IN_TRANSIT:'btn-del-transit', DELIVERED:'btn-del-delivered' };
    ['btn-del-pending','btn-del-assigned','btn-del-transit','btn-del-delivered'].forEach(id => {
        document.getElementById(id)?.classList.remove('active-state');
    });
    if (map[status]) document.getElementById(map[status])?.classList.add('active-state');
}

// ── Helpers ─────────────────────────────────────────────────────
function getPayMethodText(m) {
    return { CASH:'💵 Efectivo', CARD:'💳 Tarjeta', ONLINE:'🌐 Online' }[m] || m;
}
function getDelBadge(s) {
    return { PENDING:'pending', ASSIGNED:'pending', IN_TRANSIT:'pending', DELIVERED:'completed' }[s] || 'pending';
}
