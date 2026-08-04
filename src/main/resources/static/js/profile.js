document.addEventListener('DOMContentLoaded', () => {
    // 1. Initialize Lucide Icons
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }

    // 2. Follow Instructor Button Toggle
    const followBtn = document.getElementById('btn-follow-instructor');
    let isFollowing = false;

    if (followBtn) {
        followBtn.addEventListener('click', () => {
            isFollowing = !isFollowing;

            if (isFollowing) {
                // Style as "Đang Theo Dõi"
                followBtn.classList.remove('btn-primary');
                followBtn.classList.add('btn-light', 'border', 'text-muted');
                followBtn.innerHTML = `
                    <i data-lucide="check" class="me-1" style="width:16px; height:16px; vertical-align: text-bottom;"></i>
                    Đang Theo Dõi
                `;
            } else {
                // Revert to "Theo Dõi Giảng Viên"
                followBtn.classList.remove('btn-light', 'border', 'text-muted');
                followBtn.classList.add('btn-primary');
                followBtn.innerHTML = 'Theo Dõi Giảng Viên';
            }

            // Re-render icons inside button
            if (typeof lucide !== 'undefined') {
                lucide.createIcons();
            }
        });
    }

    // 3. Send Message Dialog
    const messageBtn = document.getElementById('btn-send-message');
    if (messageBtn) {
        messageBtn.addEventListener('click', () => {
            const message = prompt('Nhập tin nhắn của bạn gửi tới giảng viên Trang Hiển Khoa:');
            if (message !== null && message.trim() !== '') {
                alert('Tin nhắn của bạn đã được gửi giả lập thành công tới giảng viên!');
            }
        });
    }

    // 4. Sidebar Toggle (Hamburger menu) simulation
    const sidebarToggleBtn = document.getElementById('btn-sidebar-toggle');
    if (sidebarToggleBtn) {
        sidebarToggleBtn.addEventListener('click', () => {
            alert('Trình đơn thanh bên (Sidebar) đang được giả lập. Hệ thống sẽ tích hợp menu trượt khi liên kết với dashboard tổng.');
        });
    }

    // 5. Search bar enter key redirect
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
});
