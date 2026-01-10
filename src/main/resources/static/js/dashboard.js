// Check if user is logged in
const token = localStorage.getItem('token');
const email = localStorage.getItem('email');
const roles = JSON.parse(localStorage.getItem('roles') || '[]');

if (!token) {
    // Commented out redirect for development - allow access without login
    // window.location.href = '/';
    document.getElementById('userEmail').textContent = 'Guest User';
} else {
    // Display user email
    document.getElementById('userEmail').textContent = email;
}

// Show/hide cards based on user role
function setupRoleBasedUI() {
    const cardsGrid = document.querySelector('.cards-grid');
    
    // Check if user has admin or librarian role
    const isAdmin = roles.includes('ADMIN');
    const isLibrarian = roles.includes('LIBRARIAN');
    const isStudent = roles.includes('STUDENT');
    
    // Clear existing cards
    cardsGrid.innerHTML = '';
    
    if (isAdmin) {
        // Admin sees everything
        cardsGrid.innerHTML = `
            <div class="card" onclick="window.location.href='/createProduct'">
                <div class="card-icon">📚</div>
                <h3>Books</h3>
                <p>Manage library books</p>
            </div>
            <div class="card" onclick="window.location.href='/createUser'">
                <div class="card-icon">👥</div>
                <h3>Users</h3>
                <p>Manage system users</p>
            </div>
            <div class="card" onclick="window.location.href='/createReport'">
                <div class="card-icon">📊</div>
                <h3>Reports</h3>
                <p>View statistics</p>
            </div>
            <div class="card" onclick="window.location.href='/createSetting'">
                <div class="card-icon">⚙️</div>
                <h3>Settings</h3>
                <p>Configure system</p>
            </div>
        `;
    } else if (isLibrarian) {
        // Librarian sees books and reports only
        cardsGrid.innerHTML = `
            <div class="card" onclick="window.location.href='/createProduct'">
                <div class="card-icon">📚</div>
                <h3>Books</h3>
                <p>Manage library books</p>
            </div>
            <div class="card" onclick="window.location.href='/createReport'">
                <div class="card-icon">📊</div>
                <h3>Reports</h3>
                <p>View statistics</p>
            </div>
        `;
    } else {
        // Students see browse books and their profile
        cardsGrid.innerHTML = `
            <div class="card" onclick="window.location.href='/createProduct'">
                <div class="card-icon">📚</div>
                <h3>Browse Books</h3>
                <p>Search and borrow books</p>
            </div>
            <div class="card" onclick="alert('My Borrowings - Coming Soon!')">
                <div class="card-icon">📖</div>
                <h3>My Borrowings</h3>
                <p>View borrowed books</p>
            </div>
            <div class="card" onclick="alert('My Profile - Coming Soon!')">
                <div class="card-icon">👤</div>
                <h3>My Profile</h3>
                <p>View and edit profile</p>
            </div>
        `;
    }
}

// Initialize role-based UI on page load
setupRoleBasedUI();

// Logout functionality
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('roles');
    window.location.href = '/';
});
