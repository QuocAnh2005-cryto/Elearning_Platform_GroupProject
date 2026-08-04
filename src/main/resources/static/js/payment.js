let paymentId = null;
let expiredAtStr = null;

document.addEventListener('DOMContentLoaded', function () {
    const qrBox = document.querySelector('.qr-box');
    if (qrBox) {
        paymentId = qrBox.dataset.paymentId;
        expiredAtStr = qrBox.dataset.expiredAt;
    }

    setupEventListeners();

    if (expiredAtStr) {
        startCountdown(new Date(expiredAtStr));
    }

    checkPaymentStatus();
});


function setupEventListeners() {
    document.addEventListener('click', function (e) {
        if (e.target.dataset.action === 'copy') {
            const targetId = e.target.dataset.target;
            const label = e.target.dataset.label;
            const text = document.querySelector(targetId).textContent.trim();

            if (!text || text.includes('Chờ tải...')) return;

            copyToClipboard(text, label);
        }
    });

    const proceedCancelBtn = document.getElementById('proceedCancelBtn');
    if (proceedCancelBtn) {
        proceedCancelBtn.addEventListener('click', proceedCancelTransaction);
    }
}


function startCountdown(expirationDate) {
    const timerElement = document.getElementById('expireTimer');

    const countdown = setInterval(function () {
        const now = new Date().getTime();
        const expireTime = expirationDate.getTime();
        const distance = expireTime - now;

        if (distance < 0) {
            clearInterval(countdown);
            if (timerElement) timerElement.textContent = '00:00';
            window.location.href = '/cart';
            return;
        }

        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((distance % (1000 * 60)) / 1000);

        const formattedTime = (minutes < 10 ? '0' : '') + minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
        if (timerElement) timerElement.textContent = formattedTime;
    }, 1000);
}


function copyToClipboard(text, label) {
    navigator.clipboard.writeText(text).then(() => {
        showCopyToast('Đã sao chép ' + label + '!');
    });
}


function checkPaymentStatus() {
    if (!paymentId) return;

    const interval = setInterval(async () => {
        try {
            const response = await fetch(`/api/payments/${paymentId}/status`);
            const data = await response.json();

            if (data.status === 'PAID') {
                clearInterval(interval);
                showNotification('Thanh toán thành công!', 'Hệ thống đang chuyển bạn đến trang bài học...');
                setTimeout(() => {
                    window.location.href = '/student/my-learning';
                }, 3000);
            } else if (data.status === 'CANCELLED' || data.status === 'EXPIRED') {
                clearInterval(interval);
                showError('Đơn hàng đã bị hủy hoặc hết hạn.');
                setTimeout(() => {
                    window.location.href = '/cart';
                }, 3000);
            }
        } catch (error) {
            // Silent fail - allow user to manually cancel if needed
        }
    }, 3000);
}


async function proceedCancelTransaction() {
    if (!paymentId) return;

    const proceedBtn = document.getElementById('proceedCancelBtn');
    const originalText = proceedBtn.textContent;

    proceedBtn.disabled = true;
    proceedBtn.textContent = 'Đang thực hiện hủy...';

    try {
        const response = await fetch(`/api/payments/${paymentId}/cancel`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const data = await response.json();

        if (response.ok && data.success) {
            // Close modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('confirmCancelModal'));
            if (modal) {
                modal.hide();
            }

            showNotification('Hủy giao dịch thành công!', 'Quay lại giỏ hàng.');
            setTimeout(() => {
                window.location.href = '/cart';
            }, 1000);
        } else {
            showError(data.error || 'Không thể hủy giao dịch.');
            proceedBtn.disabled = false;
            proceedBtn.textContent = originalText;
        }
    } catch (error) {
        showError('Lỗi kết nối hệ thống khi hủy giao dịch.');
        proceedBtn.disabled = false;
        proceedBtn.textContent = originalText;
    }
}


function showCopyToast(message) {
    const toastEl = document.getElementById('toastCopy');
    toastEl.querySelector('.toast-body').textContent = '✔ ' + message;
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}

function showNotification(title, message) {
    const toastEl = document.getElementById('statusToast');
    document.getElementById('toastMessage').textContent = message;
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}

function showError(message) {
    const toastEl = document.getElementById('errorToast');
    document.getElementById('errorMessage').textContent = message;
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}