// ========================================
// AUTENTICACIÓN Y LOGIN
// ========================================

document.addEventListener('DOMContentLoaded', () => {
    // Si ya está autenticado, redirigir al catálogo
    if (isAuthenticated()) {
        window.location.href = 'catalog.html';
        return;
    }

    const loginForm = document.getElementById('login-form');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        try {
            // Llamar al endpoint de login
            const response = await fetch(`${API_CONFIG.USER_SERVICE}/users/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });

            if (!response.ok) {
                throw new Error('Credenciales inválidas');
            }

            const data = await response.json();

            // Guardar token y datos del usuario
            saveToken(data.token);
            saveUser({
                id: data.userId,
                name: data.name,
                email: data.email,
                role: data.role
            });

            // Redirigir al catálogo
            window.location.href = 'catalog.html';

        } catch (error) {
            showError('error-message', error.message || 'Error al iniciar sesión');
        }
    });
});

