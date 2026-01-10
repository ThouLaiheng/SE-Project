// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// DOM Elements
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const showRegisterLink = document.getElementById('showRegister');
const showLoginLink = document.getElementById('showLogin');
const loginFormElement = document.getElementById('loginFormElement');
const registerFormElement = document.getElementById('registerFormElement');
const loginError = document.getElementById('loginError');
const registerError = document.getElementById('registerError');
const registerSuccess = document.getElementById('registerSuccess');

// Toggle between login and register forms
showRegisterLink.addEventListener('click', (e) => {
    e.preventDefault();
    loginForm.classList.remove('active');
    registerForm.classList.add('active');
    clearMessages();
});

showLoginLink.addEventListener('click', (e) => {
    e.preventDefault();
    registerForm.classList.remove('active');
    loginForm.classList.add('active');
    clearMessages();
});

// Clear all error and success messages
function clearMessages() {
    loginError.classList.remove('show');
    registerError.classList.remove('show');
    registerSuccess.classList.remove('show');
    loginError.textContent = '';
    registerError.textContent = '';
    registerSuccess.textContent = '';
}

// Show error message
function showError(element, message) {
    element.textContent = message;
    element.classList.add('show');
    setTimeout(() => {
        element.classList.remove('show');
    }, 5000);
}

// Show success message
function showSuccess(element, message) {
    element.textContent = message;
    element.classList.add('show');
    setTimeout(() => {
        element.classList.remove('show');
    }, 5000);
}

// Toggle button loading state
function toggleButtonLoading(button, isLoading) {
    const btnText = button.querySelector('.btn-text');
    const spinner = button.querySelector('.spinner');
    
    if (isLoading) {
        button.disabled = true;
        btnText.style.display = 'none';
        spinner.style.display = 'inline-block';
    } else {
        button.disabled = false;
        btnText.style.display = 'inline';
        spinner.style.display = 'none';
    }
}

// Handle Login
loginFormElement.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearMessages();
    
    const loginBtn = document.getElementById('loginBtn');
    const email = document.getElementById('loginEmail').value.trim();
    const password = document.getElementById('loginPassword').value;
    
    if (!email || !password) {
        showError(loginError, 'Please fill in all fields');
        return;
    }
    
    toggleButtonLoading(loginBtn, true);
    
    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });
        
        const data = await response.json();
        
        if (response.ok) {
            // Store token and user info in localStorage
            localStorage.setItem('token', data.token);
            localStorage.setItem('email', data.email);
            localStorage.setItem('roles', JSON.stringify(data.roles || []));
            
            // Show success message
            showSuccess(loginError, '✓ Login successful! Redirecting...');
            loginError.style.background = '#d1fae5';
            loginError.style.borderColor = '#a7f3d0';
            loginError.style.color = '#10b981';
            
            // Redirect to dashboard after 1.5 seconds
            setTimeout(() => {
                window.location.href = '/dashboard.html';
            }, 1500);
        } else {
            showError(loginError, data.error || 'Invalid email or password');
        }
    } catch (error) {
        console.error('Login error:', error);
        showError(loginError, 'Connection error. Please try again.');
    } finally {
        toggleButtonLoading(loginBtn, false);
    }
});

// Handle Register
registerFormElement.addEventListener('submit', async (e) => {
    e.preventDefault();
    clearMessages();
    
    const registerBtn = document.getElementById('registerBtn');
    const email = document.getElementById('registerEmail').value.trim();
    const password = document.getElementById('registerPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    // Validation
    if (!email || !password || !confirmPassword) {
        showError(registerError, 'Please fill in all fields');
        return;
    }
    
    if (password !== confirmPassword) {
        showError(registerError, 'Passwords do not match');
        return;
    }
    
    if (password.length < 6) {
        showError(registerError, 'Password must be at least 6 characters');
        return;
    }
    
    toggleButtonLoading(registerBtn, true);
    
    try {
        const response = await fetch(`${API_BASE_URL}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });
        
        const data = await response.json();
        
        if (response.ok || response.status === 201) {
            // Registration successful
            showSuccess(registerSuccess, '✓ Registration successful! You can now sign in.');
            
            // Clear form
            registerFormElement.reset();
            
            // Switch to login form after 2 seconds
            setTimeout(() => {
                registerForm.classList.remove('active');
                loginForm.classList.add('active');
                document.getElementById('loginEmail').value = email;
            }, 2000);
        } else {
            showError(registerError, data.error || 'Registration failed. Please try again.');
        }
    } catch (error) {
        console.error('Registration error:', error);
        showError(registerError, 'Connection error. Please try again.');
    } finally {
        toggleButtonLoading(registerBtn, false);
    }
});

// Check if user is already logged in
window.addEventListener('load', () => {
    const token = localStorage.getItem('token');
    if (token) {
        // Optionally verify token and redirect to dashboard
        window.location.href = '/dashboard.html';
    }
});
