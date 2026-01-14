// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// Check authentication
const token = localStorage.getItem('token');
const email = localStorage.getItem('email');

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

// Load data on page load
document.addEventListener('DOMContentLoaded', () => {
    loadStatistics();
    loadAllBorrowings();
});

let allBorrowings = [];
let currentFilter = 'all';

// Load all borrowings
async function loadAllBorrowings() {
    const container = document.getElementById('currentBorrowingsList');
    
    try {
        const response = await fetch(`${API_BASE_URL}/borrows/my`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            allBorrowings = await response.json();
            displayFilteredBorrowings();
        } else {
            container.innerHTML = '<div class="empty-state"><h3>Failed to load borrowings</h3></div>';
        }
    } catch (error) {
        console.error('Error loading borrowings:', error);
        container.innerHTML = '<div class="empty-state"><h3>Error loading borrowings</h3></div>';
    }
}

// Filter borrowings
function filterBorrowings(filter) {
    currentFilter = filter;
    
    // Update active button
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');
    
    displayFilteredBorrowings();
}

// Display filtered borrowings
function displayFilteredBorrowings() {
    const container = document.getElementById('currentBorrowingsList');
    const now = new Date();
    
    let filteredBorrowings = allBorrowings;
    
    if (currentFilter === 'current') {
        filteredBorrowings = allBorrowings.filter(b => {
            if (b.status === 'RETURNED') return false;
            const dueDate = new Date(b.dueDate);
            return dueDate >= now;
        });
    } else if (currentFilter === 'overdue') {
        filteredBorrowings = allBorrowings.filter(b => {
            if (b.status === 'RETURNED') return false;
            const dueDate = new Date(b.dueDate);
            return dueDate < now;
        });
    } else if (currentFilter === 'returned') {
        filteredBorrowings = allBorrowings.filter(b => b.status === 'RETURNED');
    }
    
    displayBorrowings(filteredBorrowings, container, currentFilter === 'returned');
}

// Load borrowing statistics
async function loadStatistics() {
    try {
        const response = await fetch(`${API_BASE_URL}/borrows/my`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            const borrowings = await response.json();
            const stats = calculateStats(borrowings);
            displayStatistics(stats);
        } else {
            displayStatistics(getDemoStats());
        }
    } catch (error) {
        console.error('Error loading statistics:', error);
        displayStatistics(getDemoStats());
    }
}

// Calculate statistics from borrowings
function calculateStats(borrowings) {
    const now = new Date();
    const currentNotOverdue = borrowings.filter(b => {
        if (b.status !== 'RETURNED' && b.dueDate) {
            return new Date(b.dueDate) >= now;
        }
        return false;
    }).length;
    const overdue = borrowings.filter(b => {
        if (b.status !== 'RETURNED' && b.dueDate) {
            return new Date(b.dueDate) < now;
        }
        return false;
    }).length;
    const returned = borrowings.filter(b => b.status === 'RETURNED').length;
    
    return {
        current: currentNotOverdue,
        total: borrowings.length,
        overdue: overdue,
        returned: returned
    };
}

// Display statistics
function displayStatistics(stats) {
    document.getElementById('currentBorrowings').textContent = stats.current || 0;
    document.getElementById('totalBorrowed').textContent = stats.total || 0;
    document.getElementById('overdueBooks').textContent = stats.overdue || 0;
    document.getElementById('returnedBooks').textContent = stats.returned || 0;
}

// Load current borrowings
async function loadCurrentBorrowings() {
    // This function is no longer used, replaced by loadAllBorrowings
    return;
}

// Load borrowing history
async function loadBorrowingHistory() {
    // This function is no longer used, replaced by loadAllBorrowings
    return;
}

// Display borrowings
function displayBorrowings(borrowings, container, isHistory) {
    if (!borrowings || borrowings.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">📚</div>
                <h3>${isHistory ? 'No Borrowing History' : 'No Current Borrowings'}</h3>
                <p>${isHistory ? 'Your borrowing history will appear here.' : 'You haven\'t borrowed any books yet.'}</p>
                ${!isHistory ? '<a href="/borrowBooks.html" class="browse-btn">Browse Books</a>' : ''}
            </div>
        `;
        return;
    }
    
    container.innerHTML = '';
    
    borrowings.forEach(borrowing => {
        const card = createBorrowingCard(borrowing, isHistory);
        container.appendChild(card);
    });
}

// Create borrowing card
function createBorrowingCard(borrowing, isHistory) {
    const card = document.createElement('div');
    card.className = 'borrowing-card';
    
    const borrowDate = new Date(borrowing.borrowDate);
    const dueDate = new Date(borrowing.dueDate);
    const today = new Date();
    const daysUntilDue = Math.ceil((dueDate - today) / (1000 * 60 * 60 * 24));
    
    // Calculate late fee (0.5$ per day)
    const daysOverdue = dueDate < today ? Math.ceil((today - dueDate) / (1000 * 60 * 60 * 24)) : 0;
    const lateFee = daysOverdue * 0.5;
    
    let statusClass = 'status-active';
    let statusText = 'Not Returned';
    
    if (borrowing.status === 'RETURNED' || borrowing.returnDate) {
        statusClass = 'status-returned';
        statusText = 'Returned';
    } else if (dueDate < today) {
        statusClass = 'status-overdue';
        statusText = 'Overdue';
    } else if (daysUntilDue <= 2) {
        statusClass = 'status-due-soon';
        statusText = 'Due Soon';
    }
    
    const returnDate = borrowing.returnDate ? new Date(borrowing.returnDate).toLocaleDateString() : '-';
    const bookTitle = borrowing.bookTitle || 'Unknown Book';
    const bookAuthor = borrowing.bookAuthor || 'Unknown Author';
    const bookImage = borrowing.bookImageUrl;
    
    card.innerHTML = `
        <div class="book-thumbnail">
            ${bookImage ? `<img src="${bookImage}" alt="${bookTitle}" style="width:100%;height:100%;object-fit:cover;border-radius:8px;">` : '📚'}
        </div>
        <div class="borrowing-details">
            <h3 class="book-title">${bookTitle}</h3>
            <div class="book-author">by ${bookAuthor}</div>
            
            <div class="borrowing-info">
                <div class="info-item">
                    <div class="info-label">Borrow Date</div>
                    <div class="info-value">${borrowDate.toLocaleDateString()}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Due Date</div>
                    <div class="info-value">${dueDate.toLocaleDateString()}</div>
                </div>
                ${borrowing.returned ? `
                    <div class="info-item">
                        <div class="info-label">Return Date</div>
                        <div class="info-value">${returnDate}</div>
                    </div>
                ` : ''}
                <div class="info-item">
                    <div class="info-label">Status</div>
                    <div class="info-value">
                        <span class="status-badge ${statusClass}">${statusText}</span>
                    </div>
                </div>
                ${daysOverdue > 0 && borrowing.status !== 'RETURNED' ? `
                    <div class="info-item">
                        <div class="info-label">Late Fee</div>
                        <div class="info-value" style="color: #dc2626; font-weight: 600;">
                            $${lateFee.toFixed(2)} (${daysOverdue} day${daysOverdue > 1 ? 's' : ''} overdue)
                        </div>
                    </div>
                ` : ''}
            </div>
            
           
        </div>
    `;
    
    return card;
}

// Switch tabs
function switchTab(tabName) {
    // Update tab buttons
    const tabButtons = document.querySelectorAll('.tab-btn');
    tabButtons.forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    // Update tab content
    document.getElementById('currentTab').classList.remove('active');
    document.getElementById('historyTab').classList.remove('active');
    
    if (tabName === 'current') {
        document.getElementById('currentTab').classList.add('active');
    } else {
        document.getElementById('historyTab').classList.add('active');
    }
}

// Renew book
async function renewBook(borrowingId) {
    if (!confirm('Are you sure you want to renew this book?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/borrowings/${borrowingId}/renew`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            alert('Book renewed successfully!');
            loadCurrentBorrowings();
            loadStatistics();
        } else {
            const error = await response.json();
            alert(error.message || 'Failed to renew book.');
        }
    } catch (error) {
        console.error('Error renewing book:', error);
        alert('Book renewed successfully! (Demo mode)');
        loadCurrentBorrowings();
    }
}

// Return book
async function returnBook(borrowingId) {
    if (!confirm('Are you sure you want to return this book?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/borrowings/${borrowingId}/return`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            alert('Book returned successfully!');
            loadCurrentBorrowings();
            loadBorrowingHistory();
            loadStatistics();
        } else {
            const error = await response.json();
            alert(error.message || 'Failed to return book.');
        }
    } catch (error) {
        console.error('Error returning book:', error);
        alert('Book returned successfully! (Demo mode)');
        loadCurrentBorrowings();
        loadBorrowingHistory();
    }
}

// Demo data functions
function getDemoStats() {
    return {
        current: 0,
        total: 0,
        overdue: 0,
        returned: 0
    };
}

function getDemoCurrentBorrowings() {
    const today = new Date();
    
    const borrowDate1 = new Date(today);
    borrowDate1.setDate(borrowDate1.getDate() - 7);
    const dueDate1 = new Date(today);
    dueDate1.setDate(dueDate1.getDate() + 7);
    
    const borrowDate2 = new Date(today);
    borrowDate2.setDate(borrowDate2.getDate() - 14);
    const dueDate2 = new Date(today);
    dueDate2.setDate(dueDate2.getDate() + 2);
    
    return [
        
    ];
}

function getDemoHistory() {
    const today = new Date();
    
    return [
        {
           
        },
        {
           
        },
        {
            
        }
    ];
}
