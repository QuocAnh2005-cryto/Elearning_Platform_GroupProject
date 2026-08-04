document.addEventListener('DOMContentLoaded', () => {
    // 1. Initialize Lucide Icons
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }

    // 2. Avatar Selection and Operations (Visual Previews before Form Submission)
    const btnUploadAvatar = document.getElementById('btn-upload-avatar');
    const btnDeleteAvatar = document.getElementById('btn-delete-avatar');
    const btnSaveAvatar = document.getElementById('btn-save-avatar');
    const avatarFileInput = document.getElementById('avatarFileInput');
    const deleteAvatarInput = document.getElementById('deleteAvatarInput');
    const avatarContainer = document.getElementById('avatarContainer');

    const enableSaveAvatarButton = () => {
        if (btnSaveAvatar) {
            btnSaveAvatar.removeAttribute('disabled');
            btnSaveAvatar.classList.remove('bg-opacity-25', 'cursor-not-allowed');
            btnSaveAvatar.classList.add('bg-primary');
        }
    };

    if (btnUploadAvatar && avatarFileInput) {
        btnUploadAvatar.addEventListener('click', () => {
            avatarFileInput.click();
        });

        avatarFileInput.addEventListener('change', (e) => {
            const file = e.target.files[0];
            if (file) {
                // Check size limit (1MB)
                if (file.size > 1024 * 1024) {
                    alert('Kích thước ảnh đại diện không được vượt quá 1MB!');
                    avatarFileInput.value = '';
                    return;
                }

                const reader = new FileReader();
                reader.onload = (event) => {
                    avatarContainer.innerHTML = `<img src="${event.target.result}" alt="Avatar" class="w-100 h-100 object-fit-cover" id="studentAvatarImg"/>`;
                    deleteAvatarInput.value = "false";
                    enableSaveAvatarButton();
                };
                reader.readAsDataURL(file);
            }
        });
    }

    if (btnDeleteAvatar && deleteAvatarInput) {
        btnDeleteAvatar.addEventListener('click', () => {
            if (confirm('Bạn có chắc chắn muốn xóa ảnh đại diện hiện tại?')) {
                avatarContainer.innerHTML = `<span id="studentAvatarInitial">H</span>`;
                deleteAvatarInput.value = "true";
                if (avatarFileInput) {
                    avatarFileInput.value = '';
                }
                enableSaveAvatarButton();
            }
        });
    }

    // 3. Persistent Global Theme Switcher
    const interfaceMode = document.getElementById('interface-mode');
    if (interfaceMode) {
        // Initialize state from localStorage
        const currentTheme = localStorage.getItem('theme');
        if (currentTheme === 'dark') {
            interfaceMode.value = 'Chế độ tối';
        } else {
            interfaceMode.value = 'Chế độ sáng';
        }

        interfaceMode.addEventListener('change', (e) => {
            const selectedMode = e.target.value;
            if (selectedMode === 'Chế độ tối') {
                localStorage.setItem('theme', 'dark');
                document.documentElement.classList.add('dark-theme');
            } else {
                localStorage.setItem('theme', 'light');
                document.documentElement.classList.remove('dark-theme');
            }
        });
    }

    // 4. Search Bar Redirect
    const searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                const query = searchInput.value.trim();
                if (query) {
                    window.location.href = `/courses?search=${encodeURIComponent(query)}`;
                }
            }
        });
    }

    // 5. Delete Lesson Note
    const deleteNoteButtons = document.querySelectorAll('.delete-note-btn');
    deleteNoteButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            const noteId = btn.getAttribute('data-note-id');
            fetch(`/api/lesson-note/remove?noteId=${noteId}`, {
                method: 'POST'
            })
            .then(response => {
                if (response.ok) {
                    const noteCard = document.getElementById(`note-card-${noteId}`);
                    if (noteCard) {
                        // Fade out transition
                        noteCard.style.transition = 'all 0.3s ease';
                        noteCard.style.opacity = '0';
                        noteCard.style.transform = 'scale(0.95)';
                        setTimeout(() => {
                            const courseCard = noteCard.closest('.course-notes-card');
                            noteCard.remove();
                            
                            // Update note count badge or remove course card if empty
                            if (courseCard) {
                                const remainingNotes = courseCard.querySelectorAll('.note-item');
                                const badge = courseCard.querySelector('.badge');
                                if (remainingNotes.length === 0) {
                                    courseCard.style.transition = 'all 0.3s ease';
                                    courseCard.style.opacity = '0';
                                    setTimeout(() => {
                                        courseCard.remove();
                                        // Check if all course cards are removed
                                        const allCourseCards = document.querySelectorAll('.course-notes-card');
                                        if (allCourseCards.length === 0) {
                                            location.reload(); // Reload to show fallback empty state
                                        }
                                    }, 300);
                                } else if (badge) {
                                    badge.textContent = `${remainingNotes.length} ghi chú`;
                                }
                            }
                        }, 300);
                    }
                } else {
                    console.error('Xóa ghi chú thất bại.');
                }
            })
            .catch(error => {
                console.error('Error deleting note:', error);
            });
        });
    });
});
