// Check if user is logged in
const token = localStorage.getItem('token');
const email = localStorage.getItem('email');

if (!token) {
    // Commented out redirect for development - allow access without login
    // window.location.href = '/';
    document.getElementById('userEmail').textContent = 'Guest User';
} else {
    // Display user email
    document.getElementById('userEmail').textContent = email;
}

// Logout functionality
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    window.location.href = '/';
});
