// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Check authentication
const token = localStorage.getItem('token');
const email = localStorage.getItem('email');
const roles = JSON.parse(localStorage.getItem('roles') || '[]');

if (!token) {
    document.getElementById('userEmail').textContent = 'Guest User';
} else {
    document.getElementById('userEmail').textContent = email;
}

// Logout functionality
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('roles');
    window.location.href = '/';
});

// User profile data
let userProfile = null;

// Load profile on page load
document.addEventListener('DOMContentLoaded', () => {
    loadProfile();
    loadBorrowingStats();
    loadCurrentBorrowings();
});

// Load user profile
async function loadProfile() {
    try {
        const response = await fetch(`${API_BASE_URL}/users/profile`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            userProfile = await response.json();
            displayProfile(userProfile);
        } else {
            // Use demo data
            userProfile = getDemoProfile();
            displayProfile(userProfile);
        }
    } catch (error) {
        console.error('Error loading profile:', error);
        userProfile = getDemoProfile();
        displayProfile(userProfile);
    }
}

// Display profile information
function displayProfile(profile) {
    // Set avatar initial
    const initial = profile.name ? profile.name.charAt(0).toUpperCase() : '👤';
    document.getElementById('avatarInitial').textContent = initial;
    
    // Set profile details
    document.getElementById('profileName').textContent = profile.name || 'User Name';
    document.getElementById('profileRole').textContent = profile.role || 'STUDENT';
    document.getElementById('profileEmail').textContent = profile.email || email;
    document.getElementById('profilePhone').textContent = profile.phone || 'Not provided';
    
    // Format join date
    const joinDate = profile.createdAt ? new Date(profile.createdAt).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    }) : 'January 2025';
    document.getElementById('profileJoined').textContent = joinDate;
    
    // Set account status
    document.getElementById('profileStatus').textContent = profile.active ? 'Active' : 'Inactive';
}

// Load borrowing statistics
async function loadBorrowingStats() {
    try {
        const response = await fetch(`${API_BASE_URL}/borrowings/stats`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            const stats = await response.json();
            displayStats(stats);
        } else {
            // Use demo stats
            displayStats(getDemoStats());
        }
    } catch (error) {
        console.error('Error loading stats:', error);
        displayStats(getDemoStats());
    }
}

// Display borrowing statistics
function displayStats(stats) {
    document.getElementById('currentBorrowings').textContent = stats.current || 0;
    document.getElementById('totalBorrowed').textContent = stats.total || 0;
    document.getElementById('overdueBooks').textContent = stats.overdue || 0;
}

// Load current borrowings
async function loadCurrentBorrowings() {
    try {
        const response = await fetch(`${API_BASE_URL}/borrowings/current`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            const borrowings = await response.json();
            displayBorrowings(borrowings);
        } else {
            // Use demo borrowings
            displayBorrowings(getDemoBorrowings());
        }
    } catch (error) {
        console.error('Error loading borrowings:', error);
        displayBorrowings(getDemoBorrowings());
    }
}

// Display borrowed books
function displayBorrowings(borrowings) {
    const container = document.getElementById('borrowedBooksList');
    
    if (!borrowings || borrowings.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <p>You haven't borrowed any books yet.</p>
                <p>Visit the <a href="/borrowBooks.html" style="color: var(--primary-color);">Browse Books</a> page to start borrowing!</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = '';
    
    borrowings.forEach(borrowing => {
        const item = createBorrowedBookItem(borrowing);
        container.appendChild(item);
    });
}

// Create borrowed book item element
function createBorrowedBookItem(borrowing) {
    const item = document.createElement('div');
    item.className = 'borrowed-book-item';
    
    const dueDate = new Date(borrowing.dueDate);
    const today = new Date();
    const daysUntilDue = Math.ceil((dueDate - today) / (1000 * 60 * 60 * 24));
    const isDueSoon = daysUntilDue <= 3 && daysUntilDue >= 0;
    const isOverdue = daysUntilDue < 0;
    
    const dueDateClass = isOverdue || isDueSoon ? 'due-date due-soon' : 'due-date';
    const dueDateText = isOverdue 
        ? `Overdue by ${Math.abs(daysUntilDue)} days` 
        : `Due in ${daysUntilDue} days`;
    
    item.innerHTML = `
        <div class="book-icon">📚</div>
        <div class="book-details">
            <div class="book-title-small">${borrowing.bookTitle}</div>
            <div class="book-meta">
                Borrowed on ${new Date(borrowing.borrowDate).toLocaleDateString()}
            </div>
        </div>
        <div class="${dueDateClass}">
            ${dueDateText}
        </div>
    `;
    
    return item;
}

// Toggle between view and edit mode
function toggleEditMode() {
    const profileView = document.getElementById('profileView');
    const editView = document.getElementById('editView');
    
    if (editView.classList.contains('hidden')) {
        // Switch to edit mode
        populateEditForm();
        profileView.classList.add('hidden');
        editView.classList.remove('hidden');
    } else {
        // Switch to view mode
        profileView.classList.remove('hidden');
        editView.classList.add('hidden');
    }
}

// Populate edit form with current data
function populateEditForm() {
    document.getElementById('editName').value = userProfile?.name || '';
    document.getElementById('editEmail').value = userProfile?.email || email;
    document.getElementById('editPhone').value = userProfile?.phone || '';
}

// Handle profile edit form submission
document.getElementById('editProfileForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const updatedProfile = {
        name: document.getElementById('editName').value,
        phone: document.getElementById('editPhone').value
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/users/profile`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(updatedProfile)
        });
        
        if (response.ok) {
            userProfile = await response.json();
            displayProfile(userProfile);
            toggleEditMode();
            alert('Profile updated successfully!');
        } else {
            alert('Failed to update profile. Please try again.');
        }
    } catch (error) {
        console.error('Error updating profile:', error);
        // In demo mode, just update local data
        userProfile.name = updatedProfile.name;
        userProfile.phone = updatedProfile.phone;
        displayProfile(userProfile);
        toggleEditMode();
        alert('Profile updated successfully! (Demo mode)');
    }
});

// Demo data functions
function getDemoProfile() {
    return {
        name: email ? email.split('@')[0].charAt(0).toUpperCase() + email.split('@')[0].slice(1) : 'Student User',
        email: email || 'student@example.com',
        role: roles[0] || 'STUDENT',
        phone: '+1 (555) 123-4567',
        createdAt: '2025-01-01T00:00:00',
        active: true
    };
}

function getDemoStats() {
    return {
        current: 0,
        total: 0,
        overdue: 0
    };
}

function getDemoBorrowings() {
    const today = new Date();
    const borrowDate1 = new Date(today);
    borrowDate1.setDate(borrowDate1.getDate() - 7);
    
    const borrowDate2 = new Date(today);
    borrowDate2.setDate(borrowDate2.getDate() - 14);
    
    const dueDate1 = new Date(today);
    dueDate1.setDate(dueDate1.getDate() + 7);
    
    const dueDate2 = new Date(today);
    dueDate2.setDate(dueDate2.getDate() + 2); // Due soon
    
    return [
        {
            
        },
        {
            
        }
    ];
}
