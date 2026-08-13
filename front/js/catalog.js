// ========================================
// CATÁLOGO DE RESTAURANTES
// ========================================

let restaurants = [];
let menuItems = [];

document.addEventListener('DOMContentLoaded', async () => {
    // Verificar autenticación
    if (!requireAuth()) return;

    // Mostrar nombre del usuario
    const user = getUser();
    if (user) {
        document.getElementById('user-name').textContent = user.name;
    }

    // Actualizar contador del carrito
    updateCartCount();

    // Cargar restaurantes
    await loadRestaurants();

    // Event listener para el carrito
    document.getElementById('cart-link').addEventListener('click', (e) => {
        e.preventDefault();
        showCartModal();
    });

    // Event listener para cerrar modal
    const closeBtn = document.querySelector('.close');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            hideCartModal();
        });
    }

    // Cerrar modal al hacer click fuera
    window.addEventListener('click', (e) => {
        const modal = document.getElementById('cart-modal');
        if (e.target === modal) {
            hideCartModal();
        }
    });

    // Event listener para checkout
    document.getElementById('checkout-btn').addEventListener('click', () => {
        const cart = getCart();
        if (cart.length === 0) {
            alert('El carrito está vacío');
            return;
        }
        window.location.href = 'checkout.html';
    });
});

// Cargar restaurantes y menús
async function loadRestaurants() {
    const loadingElement = document.getElementById('loading');
    const errorElement = document.getElementById('error-message');

    try {
        loadingElement.classList.remove('hidden');

        // Cargar restaurantes
        const restaurantsResponse = await fetchWithAuth(`${API_CONFIG.CATALOG_SERVICE}/restaurants`);

        if (!restaurantsResponse.ok) {
            throw new Error('Error al cargar restaurantes');
        }

        restaurants = await restaurantsResponse.json();

        // Cargar menú de cada restaurante
        menuItems = [];
        for (const restaurant of restaurants) {
            const menuResponse = await fetchWithAuth(`${API_CONFIG.CATALOG_SERVICE}/restaurants/${restaurant.id}/menu`);
            if (menuResponse.ok) {
                const items = await menuResponse.json();
                // Agregar restaurantId a cada item
                items.forEach(item => item.restaurantId = restaurant.id);
                menuItems = menuItems.concat(items);
            }
        }

        // Renderizar restaurantes
        renderRestaurants();

        loadingElement.classList.add('hidden');

    } catch (error) {
        loadingElement.classList.add('hidden');
        errorElement.textContent = error.message;
        errorElement.classList.remove('hidden');
    }
}

// Renderizar restaurantes
function renderRestaurants() {
    const container = document.getElementById('restaurants-container');
    container.innerHTML = '';

    restaurants.forEach(restaurant => {
        const restaurantCard = createRestaurantCard(restaurant);
        container.appendChild(restaurantCard);
    });
}

// Crear tarjeta de restaurante
function createRestaurantCard(restaurant) {
    const card = document.createElement('div');
    card.className = 'restaurant-card';

    // Filtrar items del menú por restaurante
    const restaurantMenuItems = menuItems.filter(item =>
        item.restaurantId === restaurant.id && item.available
    );

    let menuItemsHTML = '';
    restaurantMenuItems.forEach(item => {
        menuItemsHTML += `
            <div class="menu-item">
                <div class="menu-item-info">
                    <h4>${item.name}</h4>
                    <p>${item.description || 'Delicioso platillo'}</p>
                    <p><strong>Categoría:</strong> ${item.category}</p>
                </div>
                <div style="text-align: right;">
                    <div class="menu-item-price">$${formatPrice(item.price)}</div>
                    <button class="btn btn-add" onclick="addItemToCart(${item.id}, ${restaurant.id}, '${restaurant.name}', '${item.name}', ${item.price})">
                        + Agregar
                    </button>
                </div>
            </div>
        `;
    });

    card.innerHTML = `
        <h3>${getRestaurantIcon(restaurant.type)} ${restaurant.name}</h3>
        <span class="restaurant-type">${restaurant.type}</span>
        <div class="menu-items">
            ${menuItemsHTML || '<p>No hay items disponibles</p>'}
        </div>
    `;

    return card;
}

// Obtener icono según tipo de restaurante
function getRestaurantIcon(type) {
    const icons = {
        'PESCADOS': '🐟',
        'CRIOLLA': '🥘',
        'PASTAS': '🍝'
    };
    return icons[type] || '🍽️';
}

// Agregar item al carrito
function addItemToCart(menuItemId, restaurantId, restaurantName, menuItemName, price) {
    const menuItem = {
        id: menuItemId,
        name: menuItemName,
        price: price
    };

    addToCart(menuItem, restaurantId, restaurantName);
    updateCartCount();

    // Mostrar notificación
    alert(`✅ ${menuItemName} agregado al carrito`);
}

// Actualizar contador del carrito
function updateCartCount() {
    const cart = getCart();
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    document.getElementById('cart-count').textContent = totalItems;
}

// Mostrar modal del carrito
function showCartModal() {
    const modal = document.getElementById('cart-modal');
    const cartItemsContainer = document.getElementById('cart-items');
    const cart = getCart();

    if (cart.length === 0) {
        cartItemsContainer.innerHTML = '<p style="text-align: center; color: #999;">El carrito está vacío</p>';
    } else {
        cartItemsContainer.innerHTML = '';

        cart.forEach(item => {
            const cartItem = document.createElement('div');
            cartItem.className = 'cart-item';
            cartItem.innerHTML = `
                <div class="cart-item-info">
                    <h4>${item.menuItemName}</h4>
                    <p>Restaurante: ${item.restaurantName}</p>
                    <p>Precio unitario: $${formatPrice(item.unitPrice)}</p>
                </div>
                <div class="cart-item-actions">
                    <div class="quantity-control">
                        <button class="quantity-btn" onclick="changeQuantity(${item.menuItemId}, ${item.quantity - 1})">-</button>
                        <span>${item.quantity}</span>
                        <button class="quantity-btn" onclick="changeQuantity(${item.menuItemId}, ${item.quantity + 1})">+</button>
                    </div>
                    <div class="menu-item-price">$${formatPrice(item.unitPrice * item.quantity)}</div>
                    <button class="remove-btn" onclick="removeItem(${item.menuItemId})">Eliminar</button>
                </div>
            `;
            cartItemsContainer.appendChild(cartItem);
        });
    }

    // Actualizar total
    const total = calculateCartTotal();
    document.getElementById('total-amount').textContent = formatPrice(total);

    modal.classList.remove('hidden');
}

// Ocultar modal del carrito
function hideCartModal() {
    const modal = document.getElementById('cart-modal');
    modal.classList.add('hidden');
}

// Cambiar cantidad de un item
function changeQuantity(menuItemId, newQuantity) {
    updateCartItemQuantity(menuItemId, newQuantity);
    updateCartCount();
    showCartModal(); // Refrescar modal
}

// Eliminar item del carrito
function removeItem(menuItemId) {
    removeFromCart(menuItemId);
    updateCartCount();
    showCartModal(); // Refrescar modal
}

