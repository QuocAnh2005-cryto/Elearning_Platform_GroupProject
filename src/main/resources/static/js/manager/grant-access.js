async function openGrantModal(button) {
    const userId = button.getAttribute('data-user-id');
    const userName = button.getAttribute('data-user-name');
    const userEmail = button.getAttribute('data-user-email');
    
    document.getElementById('modalUserId').value = userId;
    document.getElementById('modalUserName').textContent = userName;
    document.getElementById('modalUserEmail').textContent = userEmail;

    // Reset custom course dropdown
    const searchInput = document.getElementById('courseSearchInput');
    const container = document.getElementById('hiddenCourseInputsContainer');
    const badgesContainer = document.getElementById('selectedCoursesBadges');
    const optionsContainer = document.getElementById('courseDropdownOptions');
    
    if (searchInput) {
        searchInput.value = '';
        searchInput.placeholder = "Đang tải danh sách khóa học...";
        searchInput.disabled = true;
    }
    if (container) container.innerHTML = '';
    if (badgesContainer) badgesContainer.innerHTML = '';
    if (optionsContainer) {
        optionsContainer.innerHTML = '<div class="option-item" data-value="" style="display: none;">-- Chọn khóa học --</div>';
    }
    
    try {
        const response = await fetch(`/manager/grant-access/available-courses?userId=${userId}`);
        if (!response.ok) {
            throw new Error("Không thể tải danh sách khóa học khả dụng");
        }
        const courses = await response.json();
        
        // Rebuild option items
        if (optionsContainer) {
            courses.forEach(course => {
                const opt = document.createElement('div');
                opt.className = 'option-item';
                opt.setAttribute('data-value', course.id);
                opt.style.display = 'block';
                opt.innerHTML = `<span>${course.courseName}</span>`;
                optionsContainer.appendChild(opt);
            });
        }
    } catch (error) {
        console.error(error);
        showToast("Lỗi khi tải danh sách khóa học.", "danger");
    } finally {
        if (searchInput) {
            searchInput.placeholder = "Tìm kiếm và chọn khóa học...";
            searchInput.disabled = false;
        }
    }
    
    const noResult = optionsContainer ? optionsContainer.querySelector('.no-result') : null;
    if (noResult) noResult.remove();
    
    const modal = new bootstrap.Modal(document.getElementById('grantAccessModal'));
    modal.show();
}

window.deselectCourse = function(id) {
    console.log("Deselecting course ID:", id);
    const option = document.querySelector(`#courseDropdownOptions .option-item[data-value="${id}"]`);
    if (option) {
        option.classList.remove('selected');
        option.style.display = 'block';
        updateSelectedCoursesDisplay();
    }
};

const form = document.querySelector("#formSubmit");
const button = document.querySelector("#btnSubmitForm");

if (form && button) {
    button.addEventListener("click", async (e) => {
        e.preventDefault();
        const courseIdInputs = document.querySelectorAll('#hiddenCourseInputsContainer input[name="courseId"]');
        const reason = document.getElementById('modalReasonSelect').value;
        const note = document.getElementById('modalNote') ? document.getElementById('modalNote').value.trim() : "";
        if (courseIdInputs.length === 0) {
            showToast("Vui lòng chọn ít nhất một khóa học!", "warning");
            return;
        }
        if (!reason) {
            showToast("Vui lòng chọn lý do cấp quyền!", "warning");
            return;
        }
        if (reason === 'OTHER' && !note) {
            showToast("Vui lòng nhập ghi chú khi chọn lý do khác!", "warning");
            const noteTextarea = document.getElementById('modalNote');
            if (noteTextarea) noteTextarea.focus();
            return;
        }
        button.disabled = true;
        const originalText = button.innerHTML;
        button.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-2"></i>Đang xử lý...';

        try {
            const formData = new FormData(form);
            const sendEmailCheckbox = document.getElementById('modalSendEmail');
            formData.set('sendEmail', sendEmailCheckbox ? sendEmailCheckbox.checked : false);

            const response = await fetch('/manager/grant-access', {
                method: 'POST',
                body: formData
            });
            if (!response.ok) {
                throw new Error("Lỗi hệ thống (HTTP " + response.status + ")");
            }
            const result = await response.json();
            if (result.success) {
                const modalEl = document.getElementById('grantAccessModal');
                const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
                if (modal) {
                    modal.hide();
                    const backdrop = document.querySelector('.modal-backdrop');
                    if (backdrop) backdrop.remove();
                }

                showToast(result.message || "Cấp quyền truy cập khóa học thành công!", "success");
                
                // Reload trang sau 1.5s để cập nhật danh sách
                setTimeout(() => {
                    window.location.reload();
                }, 1500);
            } else {
                showToast(result.message || "Đã xảy ra lỗi khi cấp quyền.", "danger");
            }
        } catch (error) {
            showToast(error.message || "Không thể kết nối đến máy chủ.", "danger");
        } finally {
            button.disabled = false;
            button.innerHTML = originalText;
        }
    });
}

function showToast(message, type) {
    const toastEl = document.getElementById('resultToast');
    const toastMessage = document.getElementById('toastMessage');
    
    toastEl.classList.remove('bg-success', 'bg-danger', 'bg-warning');
    
    if (type === 'success') {
        toastEl.classList.add('bg-success');
    } else if (type === 'danger') {
        toastEl.classList.add('bg-danger');
    } else {
        toastEl.classList.add('bg-warning');
    }
    
    toastMessage.textContent = message;
    const toast = new bootstrap.Toast(toastEl, { delay: 4000 });
    toast.show();
}

function updateSelectedCoursesDisplay() {
    const container = document.getElementById('hiddenCourseInputsContainer');
    const badgesContainer = document.getElementById('selectedCoursesBadges');
    const selectedOptions = document.querySelectorAll('#courseDropdownOptions .option-item.selected');
    
    console.log("Selected options count:", selectedOptions.length);
    if (container) container.innerHTML = '';
    if (badgesContainer) badgesContainer.innerHTML = '';
    
    selectedOptions.forEach(opt => {
        const id = opt.getAttribute('data-value');
        const nameSpan = opt.querySelector('span');
        const name = nameSpan ? nameSpan.textContent : opt.textContent.trim();
        
        console.log("Rendering selected item ID:", id, "Name:", name);
        if (container) {
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'courseId';
            hiddenInput.value = id;
            container.appendChild(hiddenInput);
        }
        
        if (badgesContainer) {
            const itemDiv = document.createElement('div');
            itemDiv.className = 'd-flex justify-content-between align-items-center p-2 mb-2 border rounded bg-light w-100';
            itemDiv.style.fontSize = '0.9rem';
            itemDiv.innerHTML = `
                <span class="fw-semibold text-dark">${name}</span>
                <button type="button" class="btn btn-sm btn-outline-danger border-0 p-1 lh-1" onclick="deselectCourse(${id})">
                    <i class="ph ph-trash" style="font-size: 1.15rem; cursor: pointer;"></i>
                </button>
            `;
            badgesContainer.appendChild(itemDiv);
        }
    });
}

function initializeSelector() {
    const searchInput = document.getElementById('courseSearchInput');
    const optionsContainer = document.getElementById('courseDropdownOptions');
    const wrapper = searchInput ? searchInput.closest('.custom-search-select') : null;
    
    console.log("Initializing selector components:", { searchInput, optionsContainer, wrapper });
    
    if (searchInput && optionsContainer && wrapper) {
        
        searchInput.addEventListener('click', function (e) {
            e.stopPropagation();
            wrapper.classList.add('active');
        });
        
        searchInput.addEventListener('input', function () {
            const query = searchInput.value.toLowerCase().trim();
            wrapper.classList.add('active');
            let matches = 0;
            const existingNoResult = optionsContainer.querySelector('.no-result');
            if (existingNoResult) existingNoResult.remove();

            // Query dynamic items inside input handler to support dynamic elements
            const optionItems = Array.from(optionsContainer.querySelectorAll('.option-item')).filter(item => item.getAttribute('data-value') !== '');

            optionItems.forEach(item => {
                if (!item.classList.contains('selected')) {
                    const span = item.querySelector('span');
                    const text = span ? span.textContent.toLowerCase() : item.textContent.toLowerCase();
                    if (text.includes(query)) {
                        item.style.display = 'block';
                        matches++;
                    } else {
                        item.style.display = 'none';
                    }
                } else {
                    item.style.display = 'none';
                }
            });

            if (matches === 0) {
                const noResult = document.createElement('div');
                noResult.className = 'option-item no-result';
                noResult.textContent = 'Không tìm thấy khóa học nào phù hợp';
                optionsContainer.appendChild(noResult);
            }
        });
        
        optionsContainer.addEventListener('click', function (e) {
            const target = e.target.closest('.option-item');
            console.log("Option clicked:", target);
            if (target && !target.classList.contains('no-result')) {
                const id = target.getAttribute('data-value');
                if (id) {
                    target.classList.add('selected');
                    target.style.display = 'none';
                    updateSelectedCoursesDisplay();
                    
                    searchInput.value = '';
                    wrapper.classList.remove('active');
                    
                    // Reset visibility on other items dynamically
                    const optionItems = Array.from(optionsContainer.querySelectorAll('.option-item')).filter(item => item.getAttribute('data-value') !== '');
                    optionItems.forEach(item => {
                        if (!item.classList.contains('selected')) {
                            item.style.display = 'block';
                        }
                    });
                    const existingNoResult = optionsContainer.querySelector('.no-result');
                    if (existingNoResult) existingNoResult.remove();
                }
            }
        });
        
        document.addEventListener('click', function (e) {
            if (!wrapper.contains(e.target)) {
                wrapper.classList.remove('active');
                searchInput.value = '';
                const optionItems = Array.from(optionsContainer.querySelectorAll('.option-item')).filter(item => item.getAttribute('data-value') !== '');
                optionItems.forEach(item => {
                    if (!item.classList.contains('selected')) {
                        item.style.display = 'block';
                    }
                });
                const existingNoResult = optionsContainer.querySelector('.no-result');
                if (existingNoResult) existingNoResult.remove();
            }
        });
    }
}

function initializeReasonValidation() {
    const reasonSelect = document.getElementById('modalReasonSelect');
    const noteTextarea = document.getElementById('modalNote');
    const noteLabel = noteTextarea ? noteTextarea.previousElementSibling : null;
    
    if (reasonSelect && noteTextarea && noteLabel) {
        reasonSelect.addEventListener('change', function() {
            if (reasonSelect.value === 'OTHER') {
                noteLabel.innerHTML = 'Ghi chú <span class="required-star" style="color: #ef4444;">*</span>';
                noteTextarea.setAttribute('required', 'required');
                noteTextarea.placeholder = "Vui lòng nhập lý do chi tiết ở đây (bắt buộc)...";
            } else {
                noteLabel.innerHTML = 'Ghi chú (Không bắt buộc)';
                noteTextarea.removeAttribute('required');
                noteTextarea.placeholder = "Học viên liên hệ hỗ trợ do...";
            }
        });
    }
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        initializeSelector();
        initializeReasonValidation();
    });
} else {
    initializeSelector();
    initializeReasonValidation();
}
