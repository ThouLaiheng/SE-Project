// Expose modal helpers globally and resolve DOM elements at call time
        function openModal(isUpdate = false) {
            const modal = document.getElementById('userModal');
            const modalTitle = document.getElementById('modalTitle');
            const submitBtn = document.getElementById('submitBtn');
            const passwordInput = document.getElementById('password');
            const passwordRequired = document.getElementById('passwordRequired');
            const form = document.getElementById('createUserForm');
            
            if (modal) modal.classList.add('show');
            if (modalTitle) modalTitle.textContent = isUpdate ? 'Update User' : 'Create New User';
            if (submitBtn) submitBtn.textContent = isUpdate ? 'Update User' : 'Create User';
            
            // Make password optional for updates
            if (passwordInput) {
                passwordInput.required = !isUpdate;
            }
            if (passwordRequired) {
                passwordRequired.style.display = isUpdate ? 'none' : 'inline';
            }

            // Ensure form action resets for create mode
            if (!isUpdate && form) {
                form.action = '/createUser';
            }
        }

        function closeModal() {
            const modal = document.getElementById('userModal');
            const form = document.getElementById('createUserForm');
            if (modal) modal.classList.remove('show');
            if (form) {
                form.reset();
                // Reset to create mode
                document.getElementById('userId').value = '';
                document.getElementById('password').required = true;
                document.getElementById('passwordRequired').style.display = 'inline';
            }
        }

        // Update user function
        function updateUser(userId, userName, userEmail, userRole) {
            const form = document.getElementById('createUserForm');
            const userIdInput = document.getElementById('userId');
            const nameInput = document.getElementById('name');
            const emailInput = document.getElementById('email');
            const passwordInput = document.getElementById('password');
            const roleInput = document.getElementById('role');
            
            if (form && userIdInput && nameInput && emailInput && roleInput) {
                // Set form action for update
                form.action = '/createUser/updateUser/' + userId;
                
                // Populate form fields
                userIdInput.value = userId;
                nameInput.value = userName;
                emailInput.value = userEmail;
                passwordInput.value = ''; // Leave password empty
                roleInput.value = userRole || 'USER';
                
                // Open modal in update mode
                openModal(true);
            }
        }

        // Delete user function
        function deleteUser(userId, userName) {
            if (confirm(`Are you sure you want to delete user "${userName}"?\n\nThis action cannot be undone.`)) {
                const form = document.createElement('form');
                form.method = 'POST';
                // Match controller mapping: @RequestMapping("/createUser") + @PostMapping("/deleteUser/{id}")
                form.action = '/createUser/deleteUser/' + userId;
                // CSRF is disabled in SecurityConfig; include token only if present
                const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
                if (csrfToken) {
                    const input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = '_csrf';
                    input.value = csrfToken;
                    form.appendChild(input);
                }
                document.body.appendChild(form);
                form.submit();
            }
        }

        // Attach globals
        window.openModal = openModal;
        window.closeModal = closeModal;
        window.updateUser = updateUser;
        window.deleteUser = deleteUser;

        // DOM-dependent wiring after content loads
        document.addEventListener('DOMContentLoaded', () => {
            const modal = document.getElementById('userModal');
            const form = document.getElementById('createUserForm');
            const nameInput = document.getElementById('name');
            const emailInput = document.getElementById('email');
            const passwordInput = document.getElementById('password');
            const submitBtn = document.getElementById('submitBtn');
            const deleteButtons = document.querySelectorAll('.btn-delete[data-user-id]');
            const updateButtons = document.querySelectorAll('.btn-update[data-user-id]');

            // Close modal when clicking outside
            window.onclick = function(event) {
                if (event.target === modal) {
                    closeModal();
                }
            };

            // Real-time validation
            function validateField(input, errorId) {
                const errorElement = document.getElementById(errorId);
                if (!input || !errorElement) return false;

                if (!input.validity.valid) {
                    input.classList.add('error');
                    errorElement.classList.add('show');
                    return false;
                } else {
                    input.classList.remove('error');
                    errorElement.classList.remove('show');
                    return true;
                }
            }

            if (nameInput) nameInput.addEventListener('input', () => validateField(nameInput, 'nameError'));
            if (emailInput) emailInput.addEventListener('input', () => validateField(emailInput, 'emailError'));
            if (passwordInput) passwordInput.addEventListener('input', () => validateField(passwordInput, 'passwordError'));

            if (form && submitBtn) {
                form.addEventListener('submit', (e) => {
                    let isValid = true;
                    isValid = validateField(nameInput, 'nameError') && isValid;
                    isValid = validateField(emailInput, 'emailError') && isValid;
                    isValid = validateField(passwordInput, 'passwordError') && isValid;

                    if (!isValid) {
                        e.preventDefault();
                    } else {
                        submitBtn.disabled = true;
                        // Detect create vs update by action
                        const isUpdateAction = form.action.includes('/updateUser/');
                        submitBtn.textContent = isUpdateAction ? 'Updating...' : 'Creating...';
                    }
                });
            }

            // Auto-hide alerts after 5 seconds
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.display = 'none';
                }, 5000);
            });

            // Auto-open modal if there's an error
            const hasError = document.querySelector('.alert-error');
if (hasError) {
    openModal();
}
// update buttons using data attributes
updateButtons.forEach(btn => {
                btn.addEventListener('click', () => {
                    const id = btn.getAttribute('data-user-id');
                    const name = btn.getAttribute('data-user-name');
                    const email = btn.getAttribute('data-user-email');
                    const role = btn.getAttribute('data-user-role');
                    updateUser(id, name, email, role);
                });
            });

            // Wire 
            // Wire delete buttons using data attributes
            deleteButtons.forEach(btn => {
                btn.addEventListener('click', () => {
                    const id = btn.getAttribute('data-user-id');
                    const name = btn.getAttribute('data-user-name');
                    deleteUser(id, name);
                });
            });
        });