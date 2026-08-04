document.addEventListener('DOMContentLoaded', () => {
    // 1. Initialize Lucide Icons
    if (typeof lucide !== 'undefined') {
        lucide.createIcons();
    }

    // 2. Lucide Icons re-render when Bootstrap Collapse / Tabs / Modal are triggered
    const modalEl = document.getElementById('videoModal');
    if (modalEl) {
        modalEl.addEventListener('shown.bs.modal', () => {
            if (typeof lucide !== 'undefined') {
                lucide.createIcons();
            }
        });
    }

    // 3. Review form validation
    const reviewForm = document.getElementById('reviewForm');
    if (reviewForm) {
        reviewForm.addEventListener('submit', (e) => {
            const rating = reviewForm.querySelector('input[name="rating"]:checked');
            if (!rating) {
                e.preventDefault();
                alert('Vui lòng chọn số sao đánh giá!');
            }
        });
    }

    // 4. Load more reviews functionality
    const btnLoadMore = document.getElementById('btn-load-more-reviews');
    if (btnLoadMore) {
        btnLoadMore.addEventListener('click', () => {
            const hiddenReviews = document.querySelectorAll('.feedback-item.d-none');
            const limit = 5;
            let count = 0;

            hiddenReviews.forEach(review => {
                if (count < limit) {
                    review.classList.remove('d-none');
                    count++;
                }
            });

            // Check if there are still any hidden reviews left
            const remainingHidden = document.querySelectorAll('.feedback-item.d-none');
            if (remainingHidden.length === 0) {
                btnLoadMore.style.display = 'none';
            }
        });
    }

    // 5. Edit review modal load setup
    const editReviewModal = document.getElementById('editReviewModal');
    if (editReviewModal) {
        editReviewModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const feedbackId = button.getAttribute('data-feedback-id');
            const comment = button.getAttribute('data-feedback-comment');
            const rating = button.getAttribute('data-feedback-rating');

            const inputFeedbackId = editReviewModal.querySelector('#editFeedbackId');
            const textareaComment = editReviewModal.querySelector('#edit-review-comment');

            inputFeedbackId.value = feedbackId;
            textareaComment.value = comment;

            const radios = editReviewModal.querySelectorAll('input[name="rating"]');
            radios.forEach(radio => {
                if (radio.value === rating) {
                    radio.checked = true;
                } else {
                    radio.checked = false;
                }
            });
        });
    }

    // 6. Preview video modal setup
    const videoModal = document.getElementById('videoModal');
    if (videoModal) {
        videoModal.addEventListener('show.bs.modal', async function (event) {
            const trigger = event.relatedTarget;
            if (!trigger) return;

            const lessonTitle = trigger.getAttribute('data-lesson-title');
            const introVideoUrl = trigger.getAttribute('data-intro-video-url');

            const video = document.getElementById('previewVideo');
            const modalTitle = document.getElementById('videoModalLabel');

            if (video && introVideoUrl) {
                if (lessonTitle && modalTitle) {
                    modalTitle.textContent = "Xem thử khóa học - " + lessonTitle;
                }

                video.onerror = function () {
                    if (typeof showToast === 'function') {
                        showToast('Lỗi: Không thể tải video xem thử.', 'warning');
                    } else {
                        alert('Lỗi: Không thể tải video xem thử.');
                    }
                };

                video.src = introVideoUrl;
                video.load();
                video.play().catch(e => console.log("Autoplay prevented:", e));
            }
        });

        videoModal.addEventListener('hidden.bs.modal', function () {
            const video = document.getElementById('previewVideo');
            if (video) {
                video.pause();
                video.removeAttribute('src');
                video.load();
            }
        });
    }

    // 8. Delete review modal setup
    const deleteReviewModal = document.getElementById('deleteReviewModal');
    if (deleteReviewModal) {
        deleteReviewModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const feedbackId = button.getAttribute('data-feedback-id');
            const confirmBtn = document.getElementById('confirmDeleteReviewBtn');

            // Xóa sự kiện cũ để không gọi hàm nhiều lần
            confirmBtn.onclick = null;

            confirmBtn.onclick = function () {
                deleteReview(button, feedbackId);
                const modalInstance = bootstrap.Modal.getInstance(deleteReviewModal);
                if (modalInstance) {
                    modalInstance.hide();
                }
            };
        });
    }

    // 7. Check for pending toast on load
    const pendingToast = sessionStorage.getItem('pendingToast');
    if (pendingToast) {
        try {
            const data = JSON.parse(pendingToast);
            showToast(data.message, data.type);
        } catch (e) { }
        sessionStorage.removeItem('pendingToast');
    }
});

// Global functions for detail page actions
async function addToCart(btn, courseId) {
    btn.disabled = true;
    try {
        const response = await fetch('/api/cart/add?courseId=' + courseId, {
            method: 'POST',
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        });

        if (response.redirected || response.url.includes('/login') || response.url.includes('/login_no')) {
            showToast('Bạn chưa đăng nhập. Vui lòng đăng nhập để thực hiện chức năng này.', 'warning');
            return;
        }

        if (!response.ok) {
            if (response.status === 401) {
                showToast('Bạn chưa đăng nhập. Vui lòng đăng nhập để thực hiện chức năng này.', 'warning');
                return;
            }
            let errMsg = 'Có lỗi xảy ra khi thêm vào giỏ hàng.';
            try {
                const errData = await response.json();
                errMsg = errData.message || errData.error || errMsg;
            } catch (e) { }
            throw new Error(errMsg);
        }

        const contentType = response.headers.get("content-type");
        if (!contentType || contentType.indexOf("application/json") === -1) {
            throw new Error("Lỗi máy chủ: Không trả về JSON.");
        }

        const data = await response.json();

        if (data.success) {
            const badge = document.getElementById('cart-badge-count');
            if (badge) {
                badge.textContent = data.cartSize;
            }
            showToast(data.message, data.newlyAdded ? 'success' : 'warning');
        } else {
            showToast(data.message || 'Không thể thêm vào giỏ hàng.', 'warning');
        }
    } catch (error) {
        console.error('Lỗi khi thêm vào giỏ hàng:', error);
        showToast(error.message, 'warning');
    } finally {
        btn.disabled = false;
    }
}

function showToast(message, type = 'success') {
    const toastEl = document.getElementById('cartToast');
    const messageEl = document.getElementById('toast-message-text');
    if (toastEl && messageEl) {
        messageEl.textContent = message;

        const titleEl = toastEl.querySelector('.toast-title');
        const iconEl = toastEl.querySelector('[data-lucide]');
        const closeBtn = toastEl.querySelector('.btn-close');

        if (type === 'success') {
            toastEl.classList.remove('bg-warning', 'text-dark');
            toastEl.classList.add('bg-success', 'text-white');

            if (titleEl) {
                titleEl.textContent = "Thành công!";
                titleEl.classList.remove('text-dark');
                titleEl.classList.add('text-white');
            }
            if (messageEl) {
                messageEl.style.setProperty('color', 'rgba(255, 255, 255, 0.9)', 'important');
            }
            if (iconEl) {
                iconEl.setAttribute('data-lucide', 'check-circle');
                iconEl.className = 'text-white';
            }
            if (closeBtn) {
                closeBtn.classList.add('btn-close-white');
            }
        } else {
            toastEl.classList.remove('bg-success', 'text-white');
            toastEl.classList.add('bg-warning', 'text-dark');

            if (titleEl) {
                titleEl.textContent = "Thông báo!";
                titleEl.classList.remove('text-white');
                titleEl.classList.add('text-dark');
            }
            if (messageEl) {
                messageEl.style.setProperty('color', 'rgba(0, 0, 0, 0.8)', 'important');
            }
            if (iconEl) {
                iconEl.setAttribute('data-lucide', 'alert-circle');
                iconEl.className = 'text-dark';
            }
            if (closeBtn) {
                closeBtn.classList.remove('btn-close-white');
            }
        }

        if (typeof lucide !== 'undefined') {
            lucide.createIcons();
        }

        const toast = new bootstrap.Toast(toastEl);
        toast.show();
    }
}

async function deleteReview(btn, feedbackId) {
    btn.disabled = true;
    try {
        const params = new URLSearchParams();
        params.append('feedbackId', feedbackId);

        const response = await fetch('/course/review/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params
        });

        if (!response.ok) {
            let errMsg = 'Có lỗi xảy ra khi xóa đánh giá.';
            try {
                const errData = await response.json();
                errMsg = errData.message || errMsg;
            } catch (e) { }
            throw new Error(errMsg);
        }

        const data = await response.json();
        if (data.success) {
            const card = document.getElementById('feedback-item-' + feedbackId);
            if (card) {
                card.remove();
            }
            showToast(data.message || 'Xóa đánh giá thành công!', 'success');

            const form = document.getElementById('reviewForm');
            if (form) {
                form.classList.remove('d-none');
                form.reset();
                const checkedRadio = form.querySelector('input[name="rating"]:checked');
                if (checkedRadio) checkedRadio.checked = false;
            }

            const container = document.getElementById('reviews-list-container');
            const remainingItems = container.querySelectorAll('.feedback-item');
            if (remainingItems.length === 0) {
                const noReviewsAlert = document.createElement('div');
                noReviewsAlert.className = 'alert alert-info py-3 text-center border-0 rounded-3';
                noReviewsAlert.id = 'no-reviews-alert';
                noReviewsAlert.textContent = 'Chưa có đánh giá nào cho khóa học này.';
                container.prepend(noReviewsAlert);
            }
        } else {
            showToast(data.message || 'Không thể xóa đánh giá.', 'warning');
            btn.disabled = false;
        }
    } catch (error) {
        console.error('Lỗi khi xóa đánh giá:', error);
        showToast(error.message, 'warning');
        btn.disabled = false;
    }
}

async function submitEditReview(event) {
    event.preventDefault();
    const form = event.target;
    const submitBtn = form.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.disabled = true;

    const feedbackId = form.querySelector('#editFeedbackId').value;
    const ratingInput = form.querySelector('input[name="rating"]:checked');
    const comment = form.querySelector('#edit-review-comment').value;

    if (!ratingInput) {
        showToast('Vui lòng chọn số sao đánh giá!', 'warning');
        if (submitBtn) submitBtn.disabled = false;
        return;
    }

    if (comment && comment.length > 500) {
        showToast('Nội dung nhận xét không được vượt quá 500 ký tự.', 'warning');
        if (submitBtn) submitBtn.disabled = false;
        return;
    }

    const rating = ratingInput.value;

    try {
        const params = new URLSearchParams();
        params.append('feedbackId', feedbackId);
        params.append('rating', rating);
        params.append('comment', comment);

        const response = await fetch('/course/review/edit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params
        });

        if (!response.ok) {
            let errMsg = 'Có lỗi xảy ra khi cập nhật đánh giá.';
            try {
                const errData = await response.json();
                errMsg = errData.message || errMsg;
            } catch (e) { }
            throw new Error(errMsg);
        }

        const data = await response.json();
        if (data.success) {
            const card = document.getElementById('feedback-item-' + feedbackId);
            if (card) {
                const commentP = card.querySelector('.feedback-comment');
                if (commentP) {
                    commentP.textContent = comment;
                }

                const starsDiv = card.querySelector('.feedback-stars');
                if (starsDiv) {
                    starsDiv.setAttribute('data-rating', rating);
                    starsDiv.innerHTML = '';
                    for (let i = 1; i <= 5; i++) {
                        const isFilled = i <= rating;
                        const icon = document.createElement('i');
                        icon.setAttribute('data-lucide', 'star');
                        icon.className = 'star-icon ' + (isFilled ? 'filled text-warning' : 'text-muted');
                        icon.style.cssText = isFilled ? 'width:14px; height:14px; fill:currentColor;' : 'width:14px; height:14px;';
                        starsDiv.appendChild(icon);
                    }
                    if (typeof lucide !== 'undefined') {
                        lucide.createIcons();
                    }
                }

                const editBtn = card.querySelector('.btn-edit-review');
                if (editBtn) {
                    editBtn.setAttribute('data-feedback-comment', comment);
                    editBtn.setAttribute('data-feedback-rating', rating);
                }
            }

            const modalElement = document.getElementById('editReviewModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            if (modalInstance) {
                modalInstance.hide();
            }

            showToast(data.message || 'Cập nhật đánh giá thành công!', 'success');
        } else {
            showToast(data.message || 'Không thể cập nhật đánh giá.', 'warning');
        }
    } catch (error) {
        console.error('Lỗi khi cập nhật đánh giá:', error);
        showToast(error.message, 'warning');
    } finally {
        if (submitBtn) submitBtn.disabled = false;
    }
}

async function submitAddReview(event) {
    event.preventDefault();
    const form = event.target;
    const submitBtn = form.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.disabled = true;

    const courseId = form.querySelector('input[name="courseId"]').value;
    const ratingInput = form.querySelector('input[name="rating"]:checked');
    const comment = form.querySelector('#course-review-comment').value;

    if (!ratingInput) {
        showToast('Vui lòng chọn số sao đánh giá!', 'warning');
        if (submitBtn) submitBtn.disabled = false;
        return;
    }

    if (comment && comment.length > 500) {
        showToast('Nội dung nhận xét không được vượt quá 500 ký tự.', 'warning');
        if (submitBtn) submitBtn.disabled = false;
        return;
    }

    const rating = ratingInput.value;

    try {
        const params = new URLSearchParams();
        params.append('courseId', courseId);
        params.append('rating', rating);
        params.append('comment', comment);

        const response = await fetch('/course/review/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params
        });

        if (!response.ok) {
            let errMsg = 'Có lỗi xảy ra khi gửi đánh giá.';
            try {
                const errData = await response.json();
                errMsg = errData.message || errMsg;
            } catch (e) { }
            throw new Error(errMsg);
        }

        const data = await response.json();
        if (data.success) {
            showToast(data.message || 'Gửi đánh giá thành công!', 'success');

            const noReviewsAlert = document.getElementById('no-reviews-alert');
            if (noReviewsAlert) {
                noReviewsAlert.remove();
            }

            form.classList.add('d-none');

            const container = document.getElementById('reviews-list-container');
            const fb = data.feedback;

            let starsHtml = '';
            for (let i = 1; i <= 5; i++) {
                const isFilled = i <= fb.rating;
                starsHtml += `
                    <i data-lucide="star" class="star-icon ${isFilled ? 'filled text-warning' : 'text-muted'}" 
                       style="${isFilled ? 'width:14px; height:14px; fill:currentColor;' : 'width:14px; height:14px;'}"></i>
                `;
            }

            const avatarUrl = fb.user.avatarUrl ? fb.user.avatarUrl : '/images/student2.png';

            const reviewHtml = `
                <div id="feedback-item-${fb.id}" class="feedback-item border rounded-3 p-4 bg-white shadow-sm">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <div class="d-flex align-items-center gap-3">
                            <div class="avatar-ring-wrapper d-flex align-items-center justify-content-center rounded-circle p-0.5"
                                 style="border: 2px solid #e2e8f0; width: 44px; height: 44px;">
                                <img src="${avatarUrl}"
                                     alt="Learner Avatar" class="rounded-circle"
                                     style="width: 100%; height: 100%; object-fit: cover;"
                                     onerror="this.onerror=null; this.src='/images/student2.png';">
                            </div>
                            <div class="d-flex flex-column">
                                <span class="fw-semibold text-dark fs-7">${fb.user.lastName} ${fb.user.firstName}</span>
                                <div class="feedback-stars text-warning d-flex gap-1 mt-1" data-rating="${fb.rating}">
                                    ${starsHtml}
                                </div>
                            </div>
                        </div>
                        <div class="d-flex align-items-center gap-2">
                            <button class="btn btn-link btn-sm text-primary p-0 me-2 text-decoration-none fs-8 fw-semibold btn-edit-review"
                                    data-feedback-id="${fb.id}"
                                    data-feedback-comment="${fb.comment}"
                                    data-feedback-rating="${fb.rating}"
                                    data-bs-toggle="modal" data-bs-target="#editReviewModal">
                                <i class="fa-regular fa-pen-to-square me-1"></i>Sửa
                            </button>
                            <button class="btn btn-link btn-sm text-danger p-0 me-2 text-decoration-none fs-8 fw-semibold btn-delete-review"
                                    data-feedback-id="${fb.id}"
                                    data-bs-toggle="modal" data-bs-target="#deleteReviewModal">
                                <i class="fa-regular fa-trash-can me-1"></i>Xóa
                            </button>
                            <span class="text-muted fs-8">Vừa xong</span>
                        </div>
                    </div>
                    <p class="feedback-comment text-muted fs-7 mb-0 text-justify">${fb.comment}</p>
                </div>
            `;

            container.insertAdjacentHTML('afterbegin', reviewHtml);

            if (typeof lucide !== 'undefined') {
                lucide.createIcons();
            }
        } else {
            showToast(data.message || 'Không thể gửi đánh giá.', 'warning');
        }
    } catch (error) {
        console.error('Lỗi khi gửi đánh giá:', error);
        showToast(error.message, 'warning');
    } finally {
        if (submitBtn) submitBtn.disabled = false;
    }
}
