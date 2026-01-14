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

// Store all books
let allBooks = [];
let filteredBooks = [];
let allCategories = [];

// Load books on page load
document.addEventListener('DOMContentLoaded', () => {
    loadCategories();
    loadBooks();
});

// Load categories from API
async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            allCategories = await response.json();
            populateCategoryFilter();
        }
    } catch (error) {
        console.error('Error loading categories:', error);
        // Add some default categories if API fails
        allCategories = [
            { id: 1, name: 'Fiction' },
            { id: 2, name: 'Technology' },
            { id: 3, name: 'Science' }
        ];
        populateCategoryFilter();
    }
}

// Populate category filter dropdown
function populateCategoryFilter() {
    const categoryFilter = document.getElementById('categoryFilter');
    allCategories.forEach(cat => {
        const option = document.createElement('option');
        option.value = cat.name;
        option.textContent = cat.name;
        categoryFilter.appendChild(option);
    });
}

// Load books from API
async function loadBooks() {
    const container = document.getElementById('booksContainer');
    
    try {
        const response = await fetch(`${API_BASE_URL}/books`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            allBooks = await response.json();
            filteredBooks = [...allBooks];
            displayBooks(filteredBooks);
        } else {
            container.innerHTML = '<div class="empty-state"><h3>Failed to load books</h3><p>Please try again later.</p></div>';
        }
    } catch (error) {
        console.error('Error loading books:', error);
        // Display sample books for demonstration
        allBooks = getSampleBooks();
        filteredBooks = [...allBooks];
        displayBooks(filteredBooks);
    }
}

// Display books in grid
function displayBooks(books) {
    const container = document.getElementById('booksContainer');
    
    if (books.length === 0) {
        container.innerHTML = '<div class="empty-state"><h3>No books found</h3><p>Try adjusting your search or filters.</p></div>';
        return;
    }
    
    const booksGrid = document.createElement('div');
    booksGrid.className = 'books-grid';
    
    books.forEach(book => {
        const bookCard = createBookCard(book);
        booksGrid.appendChild(bookCard);
    });
    
    container.innerHTML = '';
    container.appendChild(booksGrid);
}

// Create book card element
function createBookCard(book) {
    const card = document.createElement('div');
    card.className = 'book-card';
    
    const available = (book.availableCopies ?? 0) > 0;
    const statusClass = available ? 'status-available' : 'status-unavailable';
    const statusText = available ? `${book.availableCopies} Available` : 'Unavailable';
    
    card.innerHTML = `
        <div class="book-image">
            ${book.imageUrl ? `<img src="${book.imageUrl}" alt="${book.title}" style="width:100%;height:100%;object-fit:cover;border-radius:8px;">` : '📚'}
        </div>
        <h3 class="book-title">${book.title}</h3>
        <div class="book-author">by ${book.author}</div>
        <span class="book-category">${book.category || 'General'}</span>
        <div class="book-status">
            <span class="status-badge ${statusClass}">${statusText}</span>
        </div>
        <div class="book-description">${book.description || 'No description available.'}</div>
        <button class="borrow-btn" onclick="borrowBook(${book.id})" ${!available ? 'disabled' : ''}>
            ${available ? '📖 Borrow Book' : 'Unavailable'}
        </button>
    `;
    
    return card;
}

// Search books
function searchBooks() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase().trim();
    
    if (searchTerm === '') {
        filteredBooks = [...allBooks];
    } else {
        filteredBooks = allBooks.filter(book => 
            book.title.toLowerCase().includes(searchTerm) ||
            book.author.toLowerCase().includes(searchTerm) ||
            (book.isbn && book.isbn.toLowerCase().includes(searchTerm))
        );
    }
    
    filterBooks();
}

// Filter books by category and status
function filterBooks() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase().trim();
    const categoryFilter = document.getElementById('categoryFilter').value;
    const statusFilter = document.getElementById('statusFilter').value;
    
    // Start from all books and apply all filters
    let results = [...allBooks];
    
    // Apply search filter
    if (searchTerm !== '') {
        results = results.filter(book => 
            book.title.toLowerCase().includes(searchTerm) ||
            book.author.toLowerCase().includes(searchTerm) ||
            (book.isbn && book.isbn.toLowerCase().includes(searchTerm))
        );
    }
    
    // Apply category filter
    if (categoryFilter) {
        results = results.filter(book => {
            const bookCategory = book.category || '';
            return bookCategory.toLowerCase() === categoryFilter.toLowerCase();
        });
    }
    
    // Apply status filter
    if (statusFilter === 'available') {
        results = results.filter(book => book.availableCopies > 0);
    } else if (statusFilter === 'newest') {
        // Sort by ID descending (newest first) and limit to recent books
        results = [...results].sort((a, b) => b.id - a.id);
    }
    
    displayBooks(results);
}

// Store current book being borrowed
let currentBorrowBook = null;

// Open borrow modal
function borrowBook(bookId) {
    if (!token) {
        alert('Please log in to borrow books');
        window.location.href = '/';
        return;
    }
    
    // Find the book
    const book = allBooks.find(b => b.id === bookId);
    if (!book) {
        alert('Book not found');
        return;
    }
    
    // Store current book
    currentBorrowBook = book;
    
    // Update modal with book info
    const modalBookInfo = document.getElementById('modalBookInfo');
    modalBookInfo.innerHTML = `
        <h3>${book.title}</h3>
        <p><strong>Author:</strong> ${book.author}</p>
        <p><strong>Category:</strong> ${book.category || 'General'}</p>
        <p><strong>Available Copies:</strong> ${book.availableCopies}</p>
    `;
    
    // Show modal
    document.getElementById('borrowModal').classList.add('active');
}

// Close borrow modal
function closeBorrowModal() {
    document.getElementById('borrowModal').classList.remove('active');
    document.getElementById('borrowForm').reset();
    currentBorrowBook = null;
}

// Submit borrow request
async function submitBorrow(event) {
    event.preventDefault();
    
    if (!currentBorrowBook) {
        alert('No book selected');
        return;
    }
    
    const borrowDays = document.getElementById('borrowDays').value;
    const notes = document.getElementById('borrowNotes').value;
    
    // Store book info before the request
    const bookTitle = currentBorrowBook.title;
    const bookId = currentBorrowBook.id;
    
    try {
        console.log('Attempting to borrow book:', bookId);
        const response = await fetch(`${API_BASE_URL}/borrows/book/${bookId}?borrowDays=${borrowDays}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        console.log('Response status:', response.status);
        
        if (response.ok) {
            const result = await response.json();
            console.log('Borrow result:', result);
            closeBorrowModal();
            alert(`Book borrowed successfully!\n\nBook: ${bookTitle}\nBorrow Duration: ${borrowDays} days\n${notes ? 'Notes: ' + notes : ''}\n\nPlease return on time to avoid fines.`);
            loadBooks(); // Reload to update availability
        } else {
            let errorMessage = 'Failed to borrow book. Please try again.';
            try {
                const errorData = await response.json();
                console.error('Error response:', errorData);
                errorMessage = errorData.message || errorData.error || errorMessage;
            } catch (e) {
                const errorText = await response.text();
                console.error('Error text:', errorText);
                if (errorText) errorMessage = errorText;
            }
            alert(errorMessage);
        }
    } catch (error) {
        console.error('Error borrowing book:', error);
        alert('Failed to borrow book. Error: ' + error.message);
    }
}

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('borrowModal');
    if (event.target === modal) {
        closeBorrowModal();
    }
}

// Sample books for demonstration
function getSampleBooks() {
    return [
        {
            id: 1,
            title: "The Great Gatsby",
            author: "F. Scott Fitzgerald",
            isbn: "978-0743273565",
            category: "FICTION",
            description: "A classic American novel set in the Jazz Age, exploring themes of wealth, love, and the American Dream.",
            availableCopies: 3,
            totalCopies: 5
        },
        {
            id: 2,
            title: "To Kill a Mockingbird",
            author: "Harper Lee",
            isbn: "978-0061120084",
            category: "FICTION",
            description: "A gripping tale of racial injustice and childhood innocence in the American South.",
            availableCopies: 2,
            totalCopies: 4
        },
        {
            id: 3,
            title: "1984",
            author: "George Orwell",
            isbn: "978-0451524935",
            category: "FICTION",
            description: "A dystopian social science fiction novel exploring the dangers of totalitarianism.",
            availableCopies: 0,
            totalCopies: 3
        },
        {
            id: 4,
            title: "Clean Code",
            author: "Robert C. Martin",
            isbn: "978-0132350884",
            category: "TECHNOLOGY",
            description: "A handbook of agile software craftsmanship, teaching how to write clean, maintainable code.",
            availableCopies: 5,
            totalCopies: 5
        },
        {
            id: 5,
            title: "Sapiens",
            author: "Yuval Noah Harari",
            isbn: "978-0062316110",
            category: "HISTORY",
            description: "A brief history of humankind, from the Stone Age to the modern age.",
            availableCopies: 4,
            totalCopies: 6
        },
        {
            id: 6,
            title: "The Lean Startup",
            author: "Eric Ries",
            isbn: "978-0307887894",
            category: "NON_FICTION",
            description: "How today's entrepreneurs use continuous innovation to create radically successful businesses.",
            availableCopies: 3,
            totalCopies: 4
        },
        {
            id: 7,
            title: "A Brief History of Time",
            author: "Stephen Hawking",
            isbn: "978-0553380163",
            category: "SCIENCE",
            description: "From the Big Bang to black holes, a landmark volume in science writing.",
            availableCopies: 2,
            totalCopies: 3
        },
        {
            id: 8,
            title: "Steve Jobs",
            author: "Walter Isaacson",
            isbn: "978-1451648539",
            category: "BIOGRAPHY",
            description: "The exclusive biography of Steve Jobs, based on extensive interviews.",
            availableCopies: 1,
            totalCopies: 2
        }
    ];
}

// Add auto-search on input (triggers on every keystroke)
document.getElementById('searchInput').addEventListener('input', () => {
    searchBooks();
});

// Add enter key support for search
document.getElementById('searchInput').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        searchBooks();
    }
});
