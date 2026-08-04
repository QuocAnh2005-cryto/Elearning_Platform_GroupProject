// Shopping Cart JS Logic (Server-driven AJAX reloads)
document.addEventListener("DOMContentLoaded", () => {
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }

    // Khôi phục trạng thái lửng lơ (indeterminate) cho checkbox từ thuộc tính server truyền xuống
    document.querySelectorAll('[data-indeterminate="true"]').forEach(cb => {
        cb.indeterminate = true;
    });

    initializeCheckboxes();
    initializeRemoveButtons();
    initializeCheckoutButton();
});

// Hàm hiển thị Toast thông báo 
function displayMessage(message, type = 'warning') {
    const toastEl = document.getElementById('cartToast');
    const messageEl = document.getElementById('toast-message-text');

    if (toastEl && messageEl) {
        messageEl.textContent = message; // Gán nội dung thông báo
        const titleEl = toastEl.querySelector('.toast-title');

        if (type === 'success') {
            toastEl.classList.remove('toast-warning', 'bg-warning', 'text-dark');
            toastEl.classList.add('toast-success', 'bg-success', 'text-white');
            if (titleEl) titleEl.textContent = "Thành công!";
        } else {
            toastEl.classList.remove('toast-success', 'bg-success', 'text-white');
            toastEl.classList.add('toast-warning', 'bg-warning', 'text-dark');
            if (titleEl) titleEl.textContent = "Thông báo!";
        }

        // Khởi tạo Bootstrap Toast và hiển thị
        if (typeof bootstrap !== 'undefined') {
            const toast = new bootstrap.Toast(toastEl);
            toast.show();
        } else {
            alert(message);
        }
    } else {
        // Fallback: Nếu trên trang cart.html chưa có sẵn cục HTML id="cartToast" thì dùng alert 
        alert(message);
    }
}

// Helper gọi API chuẩn hóa bắt lỗi
async function handleApiCall(url, options = {}) {
    try {
        const fetchOptions = {
            ...options,
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                ...options.headers
            }
        };

        const response = await fetch(url, fetchOptions);

        // Phát hiện bị redirect về trang đăng nhập do Spring Security (302 -> 200 HTML)
        if (response.redirected || response.url.includes('/login') || response.url.includes('/login_no')) {
            displayMessage('Bạn chưa đăng nhập. Vui lòng đăng nhập!', 'warning');
            return null;
        }

        if (!response.ok) {
            if (response.status === 401) {
                displayMessage('Bạn chưa đăng nhập. Vui lòng đăng nhập!', 'warning');
                return null;
            }
            let errMsg = 'Có lỗi xảy ra từ máy chủ.';
            try {
                const errData = await response.json();
                errMsg = errData.message || errData.error || errMsg;
            } catch (e) { }
            throw new Error(errMsg);
        }

        const contentType = response.headers.get("content-type");
        if (!contentType || contentType.indexOf("application/json") === -1) {
            throw new Error("Lỗi máy chủ: Dữ liệu trả về không phải JSON hợp lệ.");
        }

        return await response.json();
    } catch (error) {
        console.error('Lỗi call API:', error);
        displayMessage(error.message, 'error');
        return null;
    }
}

// 1. Logic Checkbox (Gửi API thay đổi selected rồi reload trang)
function initializeCheckboxes() {
    const selectAllCb = document.getElementById("select-all-checkout");
    const instCbs = document.querySelectorAll(".instructor-checkbox");
    const itemCbs = document.querySelectorAll(".item-checkbox");

    // A. Chọn tất cả
    if (selectAllCb) {
        selectAllCb.addEventListener("change", async (e) => {
            const selected = e.target.checked;
            const data = await handleApiCall(`/api/cart/toggle-select-all?selected=${selected}`, { method: 'POST' });
            if (data) {
                if (data.success) window.location.reload();
                else displayMessage(data.message || "Có lỗi xảy ra.");
            }
        });
    }

    // B. Chọn theo giảng viên
    instCbs.forEach(cb => {
        cb.addEventListener("change", async (e) => {
            const instructorId = cb.getAttribute("data-instructor-id");
            const selected = e.target.checked;
            const data = await handleApiCall(`/api/cart/toggle-select-instructor?instructorId=${instructorId}&selected=${selected}`, { method: 'POST' });
            if (data) {
                if (data.success) window.location.reload();
                else displayMessage(data.message || "Có lỗi xảy ra.");
            }
        });
    });

    // C. Chọn từng khóa học
    itemCbs.forEach(cb => {
        cb.addEventListener("change", async (e) => {
            const cartItemId = cb.getAttribute("data-id");
            const selected = e.target.checked;
            const data = await handleApiCall(`/api/cart/toggle-select?cartItemId=${cartItemId}&selected=${selected}`, { method: 'POST' });
            if (data) {
                if (data.success) window.location.reload();
                else displayMessage(data.message || "Có lỗi xảy ra.");
            }
        });
    });
}

// 2. Logic xóa khóa học
function initializeRemoveButtons() {
    const removeBtns = document.querySelectorAll(".btn-remove-item");
    removeBtns.forEach(btn => {
        btn.addEventListener("click", async () => {
            const cartItemId = btn.getAttribute("data-item-id");
            if (!cartItemId) return;

            btn.disabled = true;
            const data = await handleApiCall(`/api/cart/remove?cartItemId=${cartItemId}`, { method: 'POST' });
            if (data) {
                if (data.success) window.location.reload();
                else {
                    btn.disabled = false;
                    displayMessage(data.message || "Không thể xóa khóa học.");
                }
            } else {
                btn.disabled = false;
            }
        });
    });
}

function initializeCheckoutButton() {
    const checkoutBtn = document.getElementById("btn-checkout");
    if (checkoutBtn) {
        checkoutBtn.addEventListener("click", async () => {
            checkoutBtn.disabled = true;
            const data = await handleApiCall('/api/payments/checkout', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });
            if (data) {
                if (data.status === 'PAID') {
                    const myModal = new bootstrap.Modal(document.getElementById('freeSuccessModal'), {
                        backdrop: 'static',
                        keyboard: false
                    });
                    document.getElementById('btn-go-learning').addEventListener('click', () => {
                        window.location.href = '/student/my-learning';
                    });
                    myModal.show();
                } else {
                    window.location.href = `/payment?id=${data.id}`;
                }
            } else {
                checkoutBtn.disabled = false;
            }
        });
    }
}

