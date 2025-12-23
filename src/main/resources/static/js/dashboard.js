// Check if user is logged in
const token = localStorage.getItem('token');
const email = localStorage.getItem('email');

if (!token) {
    window.location.href = '/';
}

// Display user email
document.getElementById('userEmail').textContent = email;

// Logout functionality
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    window.location.href = '/';
});
