document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('.filter-search-wrapper form');
    const fromDateInput = form.querySelector('input[name="fromDate"]');
    const toDateInput = form.querySelector('input[name="toDate"]');

    form.addEventListener('submit', function(e) {
        fromDateInput.classList.remove('is-invalid');
        toDateInput.classList.remove('is-invalid');
        const existingAlert = document.getElementById('dateValidationError');
        if (existingAlert) existingAlert.remove();

        const fromDateVal = fromDateInput.value;
        const toDateVal = toDateInput.value;
        const today = new Date();

        if (fromDateVal && new Date(fromDateVal) > today) {
            e.preventDefault();
            fromDateInput.classList.add('is-invalid');
            const alertDiv = document.createElement('div');
            alertDiv.id = 'dateValidationError';
            alertDiv.className = 'alert alert-danger d-flex align-items-center mt-3';
            alertDiv.innerHTML = '<i class="ph ph-warning-circle me-2" style="font-size: 1.25rem;"></i><div>"Từ ngày" không được lớn hơn ngày hiện tại.</div>';
            form.parentNode.insertBefore(alertDiv, form);
            alertDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
            return;
        }

        if (fromDateVal && toDateVal && new Date(fromDateVal) > new Date(toDateVal)) {
            e.preventDefault();
            fromDateInput.classList.add('is-invalid');
            toDateInput.classList.add('is-invalid');
            const alertDiv = document.createElement('div');
            alertDiv.id = 'dateValidationError';
            alertDiv.className = 'alert alert-danger d-flex align-items-center mt-3';
            alertDiv.innerHTML = '<i class="ph ph-warning-circle me-2" style="font-size: 1.25rem;"></i><div>"Từ ngày" không được lớn hơn "Đến ngày".</div>';
            form.parentNode.insertBefore(alertDiv, form);
            alertDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    });

    // Fetch modal và mở popup
    document.querySelectorAll('.btn-action[title="Xem chi tiết"]').forEach(btn => {
        btn.addEventListener('click', e => {
            e.preventDefault();
            let container = document.getElementById('transactionContainer');
            
            // Tạo container nếu chưa tồn tại
            if (!container) {
                container = document.createElement('div');
                container.id = 'transactionContainer';
                document.body.appendChild(container);
            }
            
            const paymentId = btn.closest('tr').dataset.paymentId;
            fetch(`/manager/transaction-detail/${paymentId}`)
                .then(r => r.text())
                .then(html => {
                    container.innerHTML = html;
                    document.getElementById('transactionDetailModal').classList.add('active');
                })
                .catch(e => console.error('Lỗi:', e));
        });
    });

    // Close modal khi click outside
    document.addEventListener('click', function(e) {
        const modal = document.getElementById('transactionDetailModal');
        if (modal && e.target === modal) {
            modal.classList.remove('active');
        }
    });

    // Close modal khi nhấn ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            const modal = document.getElementById('transactionDetailModal');
            if (modal) modal.classList.remove('active');
        }
    });
});