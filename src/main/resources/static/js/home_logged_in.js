document.addEventListener('DOMContentLoaded', () => {
    // 1. Initialize Lucide Icons
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }

    // 2. Simulated Log Out action (Redirects to home.html)
    const logoutBtn = document.querySelector('.dropdown-item.text-danger');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            // Show toast/alert first or redirect directly
            alert('Đăng xuất thành công! Đang chuyển hướng bạn quay lại trang chủ khách...');
            window.location.href = '../../index.html';
        });
    }

    // 3. CTA Discover action
    const btnDiscover = document.getElementById('btn-discover');
    const statsSection = document.querySelector('.stats-section');

    if (btnDiscover && statsSection) {
        btnDiscover.addEventListener('click', (e) => {
            e.preventDefault();
            statsSection.scrollIntoView({
                behavior: 'smooth'
            });
        });
    }

    // 4. Search bar enter key redirect
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
