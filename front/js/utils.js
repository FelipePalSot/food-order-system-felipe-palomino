// ========================================
// UTILIDADES GENERALES
// ========================================

// Obtener token del localStorage
function getToken() {
    return localStorage.getItem(STORAGE_KEYS.TOKEN);
}

// Guardar token en localStorage
function saveToken(token) {
    localStorage.setItem(STORAGE_KEYS.TOKEN, token);
}

// Eliminar token
function removeToken() {
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
}

// Obtener usuario del localStorage
function getUser() {
    const user = localStorage.getItem(STORAGE_KEYS.USER);
    return user ? JSON.parse(user) : null;
}

// Guardar usuario
function saveUser(user) {
    localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(user));
}

// Verificar si el usuario está autenticado
function isAuthenticated() {
    return getToken() !== null;
}

// Redirigir a login si no está autenticado
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = 'index.html';
        return false;
    }
    return true;
}

// Logout
function logout() {
    removeToken();
    localStorage.removeItem(STORAGE_KEYS.USER);
    localStorage.removeItem(STORAGE_KEYS.CART);
    localStorage.removeItem(STORAGE_KEYS.CURRENT_ORDER);
    window.location.href = 'index.html';
}

// Hacer petición HTTP con autenticación
async function fetchWithAuth(url, options = {}) {
    const token = getToken();

    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, {
        ...options,
        headers
    });

    // Si el token expiró, redirigir al login
    if (response.status === 401 || response.status === 403) {
        logout();
        return;
    }

    return response;
}

// Mostrar mensaje de error
function showError(elementId, message) {
    const errorElement = document.getElementById(elementId);
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.classList.remove('hidden');
        setTimeout(() => {
            errorElement.classList.add('hidden');
        }, 5000);
    }
}

// Mostrar mensaje de éxito
function showSuccess(elementId, message) {
    const successElement = document.getElementById(elementId);
    if (successElement) {
        successElement.textContent = message;
        successElement.classList.remove('hidden');
        setTimeout(() => {
            successElement.classList.add('hidden');
        }, 5000);
    }
}

// Formatear precio
function formatPrice(price) {
    return parseFloat(price).toFixed(2);
}

// Obtener carrito del localStorage
function getCart() {
    const cart = localStorage.getItem(STORAGE_KEYS.CART);
    return cart ? JSON.parse(cart) : [];
}

// Guardar carrito
function saveCart(cart) {
    localStorage.setItem(STORAGE_KEYS.CART, JSON.stringify(cart));
}

// Agregar item al carrito
function addToCart(menuItem, restaurantId, restaurantName) {
    const cart = getCart();

    const existingItem = cart.find(item => item.menuItemId === menuItem.id);

    if (existingItem) {
        existingItem.quantity++;
    } else {
        cart.push({
            menuItemId: menuItem.id,
            menuItemName: menuItem.name,
            unitPrice: menuItem.price,
            quantity: 1,
            restaurantId: restaurantId,
            restaurantName: restaurantName
        });
    }

    saveCart(cart);
    return cart;
}

// Remover item del carrito
function removeFromCart(menuItemId) {
    let cart = getCart();
    cart = cart.filter(item => item.menuItemId !== menuItemId);
    saveCart(cart);
    return cart;
}

// Actualizar cantidad del item
function updateCartItemQuantity(menuItemId, quantity) {
    const cart = getCart();
    const item = cart.find(item => item.menuItemId === menuItemId);

    if (item) {
        if (quantity <= 0) {
            return removeFromCart(menuItemId);
        }
        item.quantity = quantity;
        saveCart(cart);
    }

    return cart;
}

// Calcular total del carrito
function calculateCartTotal() {
    const cart = getCart();
    return cart.reduce((total, item) => {
        return total + (item.unitPrice * item.quantity);
    }, 0);
}

// Limpiar carrito
function clearCart() {
    localStorage.removeItem(STORAGE_KEYS.CART);
}

// Event listener para logout (común en todas las páginas)
document.addEventListener('DOMContentLoaded', () => {
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            logout();
        });
    }
});

