



document.addEventListener('DOMContentLoaded', function () {

    initToastNotifications();
    initSubmitReviewPolicy();

    /* =====================================================
       1. SIDEBAR NAVIGATION
    ===================================================== */
    initSidebarNav();

    /* =====================================================
       2. TABS
    ===================================================== */
    initTabs();

    /* =====================================================
       3. MODALS
    ===================================================== */
    initModals();

    /* =====================================================
       4. RICH TEXT EDITOR
    ===================================================== */
    initRichEditors();

    /* =====================================================
       5. AVATAR PREVIEW
    ===================================================== */
    initAvatarPreview();

    /* =====================================================
       6. THUMBNAIL PREVIEW
    ===================================================== */
    initThumbnailPreview();

    /* =====================================================
       7. PROFILE PREVIEW (live update)
    ===================================================== */
    initProfilePreview();

    /* =====================================================
       8. COURSE PREVIEW (live update)
    ===================================================== */
    initCoursePreview();

    /* =====================================================
       9. CURRICULUM - Add Section & Lesson (JS fallback)
    ===================================================== */
    initCurriculum();

    /* =====================================================
       10. QUIZ - Add Question (JS fallback)
    ===================================================== */
    /* =====================================================
       11. PRICING - Price display & Voucher
    ===================================================== */
    initPricing();

    /* =====================================================
       12. VIDEO PREVIEW
    ===================================================== */
    initVideoUpload();

    initEditVideoUpload();





    /* =====================================================
       13. SUBMIT REQUEST (step 5)
    ===================================================== */
    initSubmitRequest();

    /* =====================================================
       14. WIZARD STEP BUTTONS (náº¿u dÃ¹ng 1 file)
    ===================================================== */
    initWizardButtons();

    /* =====================================================
       15. MATERIAL FILE PREVIEW
    ===================================================== */
    initMaterialPreview();

    initEditMaterialPreview();

    initMaterialFileValidation();
    initLessonUploadSubmitLock();
    initMaterialUploadSubmitLock();

});

/* =====================================================
   FUNCTIONS
===================================================== */

function initSidebarNav() {
    // Highlight active link based on current URL
    const currentPath = window.location.pathname;
    document.querySelectorAll('.sidebar-nav a[href]').forEach(link => {
        if (link.href && currentPath.includes(link.getAttribute('href')) && link.getAttribute('href') !== '#') {
            link.classList.add('active');
        }
    });
}

function initTabs() {
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const group = this.dataset.tabGroup;
            const tab = this.dataset.tab;
            activateTab(group, tab);
        });
    });

    const params = new URLSearchParams(window.location.search);
    const activeTab = params.get('tab');

    if (activeTab) {
        document.querySelectorAll(`.tab-btn[data-tab="${activeTab}"]`).forEach(btn => {
            const group = btn.dataset.tabGroup;
            activateTab(group, activeTab);
        });
    }



    document.querySelectorAll('.media-tab').forEach(tab => {
        tab.addEventListener('click', function () {
            const group = this.dataset.group;
            const type = this.dataset.type;

            document.querySelectorAll(`.media-tab[data-group="${group}"]`)
                .forEach(t => t.classList.toggle('active', t === this));


            document.querySelectorAll(`.media-content[data-group="${group}"]`)
                .forEach(c => {
                    c.style.display = c.dataset.type === type ? '' : 'none';
                });
        });
    });


    document.querySelectorAll('.media-tab.active').forEach(tab => {
        const group = tab.dataset.group;
        const type = tab.dataset.type;
        document.querySelectorAll(`.media-content[data-group="${group}"]`)
            .forEach(c => {
                c.style.display = c.dataset.type === type ? '' : 'none';
            });
    });

}

function activateTab(group, tab) {
    document.querySelectorAll(`.tab-btn[data-tab-group="${group}"]`)
        .forEach(b => b.classList.toggle('active', b.dataset.tab === tab));

    document.querySelectorAll(`.tab-content[data-tab-group="${group}"]`)
        .forEach(c => {
            c.style.display = c.dataset.tab === tab ? '' : 'none';
        });
}




function initModals() {

    document.querySelectorAll('[data-open-modal]').forEach(btn => {
        btn.addEventListener('click', function () {
            const id = this.dataset.openModal;
            openModal(id);
        });
    });

    // Close modal via data-close-modal attribute
    document.querySelectorAll('[data-close-modal]').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.dataset.closeModal;
            closeModal(id);
        });
    });

    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', e => {
            if (e.target === overlay) closeModal(overlay.id);
        });
    });


    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            document.querySelectorAll('.modal-overlay.active').forEach(m => closeModal(m.id));
        }
    });
}

function openModal(id) {
    const el = document.getElementById(id);
    if (el) el.classList.add('active');
}

function closeModal(id) {
    const el = document.getElementById(id);
    if (!el) return;
    if (el.dataset.uploadLocked === 'true') return;
    el.classList.remove('active');
}

function initRichEditors() {
    document.querySelectorAll('.rich-toolbar button[data-cmd]').forEach(btn => {
        btn.addEventListener('click', function () {
            const cmd = this.dataset.cmd;
            const val = this.dataset.val || null;
            document.execCommand(cmd, false, val);
        });
    });

    // Sync hidden inputs before form submit
    document.querySelectorAll('form').forEach(form => {
        form.addEventListener('submit', function () {
            syncRichEditor('bioEditor', 'bioHidden');
            syncRichEditor('courseDescEditor', 'descHidden');
            syncRichEditor('outcomeEditor', 'outcomeHidden');
            syncRichEditor('requirementEditor', 'requirementHidden');
        });
    });
}

function syncRichEditor(editorId, hiddenId) {
    const editor = document.getElementById(editorId);
    const hidden = document.getElementById(hiddenId);
    if (editor && hidden) hidden.value = editor.innerHTML;
}

function setUploadModalLock(form, locked, fallbackLoadingText) {
    const overlay = form.closest('.modal-overlay');
    if (overlay) {
        overlay.dataset.uploadLocked = locked ? 'true' : 'false';
        overlay.classList.toggle('modal-upload-locked', locked);
        overlay.querySelectorAll('[data-close-modal]').forEach(button => {
            button.disabled = locked;
        });
    }

    form.querySelectorAll('input, textarea, select, button').forEach(control => {
        if (control.type === 'hidden' || control.type === 'submit' || control.matches('[data-close-modal]')) {
            return;
        }

        if (locked) {
            control.dataset.uploadLockPointerEvents = control.style.pointerEvents || '';
            control.dataset.uploadLockTabIndex = control.getAttribute('tabindex') || '';
            control.style.pointerEvents = 'none';
            control.setAttribute('tabindex', '-1');
            if (control.matches('input:not([type="checkbox"]):not([type="radio"]):not([type="file"]), textarea')) {
                control.dataset.uploadLockReadOnly = control.readOnly ? 'true' : 'false';
                control.readOnly = true;
            }
            if (document.activeElement === control) {
                control.blur();
            }
            return;
        }

        control.style.pointerEvents = control.dataset.uploadLockPointerEvents || '';
        if (control.dataset.uploadLockTabIndex) {
            control.setAttribute('tabindex', control.dataset.uploadLockTabIndex);
        } else {
            control.removeAttribute('tabindex');
        }
        if (control.dataset.uploadLockReadOnly !== undefined) {
            control.readOnly = control.dataset.uploadLockReadOnly === 'true';
        }
        delete control.dataset.uploadLockPointerEvents;
        delete control.dataset.uploadLockTabIndex;
        delete control.dataset.uploadLockReadOnly;
    });

    const submitButton = form.querySelector('button[type="submit"]');
    if (!submitButton) return;

    if (locked) {
        if (!submitButton.dataset.originalText) {
            submitButton.dataset.originalText = submitButton.textContent;
        }
        submitButton.disabled = true;
        submitButton.classList.add('btn-loading');
        submitButton.textContent = submitButton.dataset.loadingText || fallbackLoadingText || 'Đang Lưu...';
        return;
    }

    submitButton.disabled = false;
    submitButton.classList.remove('btn-loading');
    submitButton.textContent = submitButton.dataset.originalText || 'Lưu';
}

function initLessonUploadSubmitLock() {
    document.querySelectorAll('form[data-lesson-upload-form]').forEach(form => {
        form.addEventListener('submit', async function (event) {
            if (form.dataset.submitting === 'true') {
                event.preventDefault();
                return;
            }

            const videoInput = form.querySelector('input[type="file"][name="videoFile"]');
            const blobInput = form.querySelector('input[name="videoBlobName"]');
            const selectedVideo = videoInput?.files?.[0];

            form.dataset.submitting = 'true';
            setUploadModalLock(form, true);
            if (selectedVideo && blobInput && form.dataset.directUploaded !== 'true') {
                event.preventDefault();

                const MAX_VIDEO_SIZE_MB = 500;
                const MAX_VIDEO_SIZE_BYTES = MAX_VIDEO_SIZE_MB * 1024 * 1024;
                if (selectedVideo.size > MAX_VIDEO_SIZE_BYTES) {
                    const actualSizeMB = (selectedVideo.size / 1024 / 1024).toFixed(1);
                    showAppToast(`Video vượt quá giới hạn cho phép! Kích thước tối đa: ${MAX_VIDEO_SIZE_MB} MB. File của bạn: ${actualSizeMB} MB. Vui lòng chọn video nhỏ hơn.`, 'error');
                    form.dataset.submitting = 'false';
                    setUploadModalLock(form, false);
                    return;
                }

                try {
                    const sectionId = resolveLessonFormSectionId(form);
                    const progress = createUploadProgressController(form);
                    progress.update(0);
                    const blobName = await uploadLessonVideoDirectToAzure(selectedVideo, sectionId, progress.update);
                    progress.update(100);
                    blobInput.value = blobName;
                    videoInput.disabled = true;
                    form.dataset.directUploaded = 'true';
                    form.submit();
                } catch (error) {
                    console.error('Tải video lên thất bại', error);
                    alert(error.message || 'Tải video lên thất bại');
                    form.dataset.submitting = 'false';
                    setUploadModalLock(form, false);
                }
            }
        });
    });
}

function createUploadProgressController(form) {
    const container = form.querySelector('[data-upload-progress]');
    const bar = form.querySelector('[data-upload-progress-bar]');
    const text = form.querySelector('[data-upload-progress-text]');

    return {
        update(percent) {
            const safePercent = Math.max(0, Math.min(100, Math.round(percent)));
            if (container) {
                container.style.display = 'block';
            }
            if (bar) {
                bar.style.width = `${safePercent}%`;
            }
            if (text) {
                text.textContent = `${safePercent}%`;
            }
        }
    };
}

function resetUploadProgress(form) {
    const container = form?.querySelector('[data-upload-progress]');
    const bar = form?.querySelector('[data-upload-progress-bar]');
    const text = form?.querySelector('[data-upload-progress-text]');

    if (container) {
        container.style.display = 'none';
    }
    if (bar) {
        bar.style.width = '0%';
    }
    if (text) {
        text.textContent = '0%';
    }
}
function initMaterialUploadSubmitLock() {
    const form = document.getElementById('addMaterialForm');
    if (!form) return;

    form.addEventListener('submit', async function (event) {
        const materialInput = document.getElementById('addMaterialFile');
        if (!validateMaterialFiles(materialInput)) {
            event.preventDefault();
            return;
        }

        const files = materialInput?.files;
        if (!files || files.length === 0 || form.dataset.uploaded === 'true') {
            return;
        }

        event.preventDefault();

        if (form.dataset.submitting === 'true') {
            return;
        }

        form.dataset.submitting = 'true';
        setUploadModalLock(form, true, 'Đang tải tài liệu lên...');

        try {
            form.querySelectorAll('.js-material-blob-input').forEach(el => el.remove());
            const progress = createUploadProgressController(form);
            progress.update(0);

            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                if (file.size > 50 * 1024 * 1024) {
                    throw new Error(`Dung lượng tệp '${file.name}' vượt quá 50MB (BR-35). Vui lòng chọn tệp nhỏ hơn.`);
                }
                const requestBody = new FormData();
                requestBody.append('fileName', file.name);

                const sasResponse = await fetch('/instructor/materials/upload-url', {
                    method: 'POST',
                    body: requestBody
                });
                if (!sasResponse.ok) {
                    throw new Error(`Không lấy được URL tải lên cho tệp: ${file.name}`);
                }

                const uploadInfo = await sasResponse.json();

                await uploadBlockBlobToAzure(uploadInfo.uploadUrl, file, function(percent) {
                    const overallPercent = ((i + (percent / 100)) / files.length) * 100;
                    progress.update(overallPercent);
                });

                const blobInput = document.createElement('input');
                blobInput.type = 'hidden';
                blobInput.name = 'materialBlobNames';
                blobInput.value = uploadInfo.blobName;
                blobInput.className = 'js-material-blob-input';
                form.appendChild(blobInput);

                const nameInput = document.createElement('input');
                nameInput.type = 'hidden';
                nameInput.name = 'materialFileNames';
                nameInput.value = file.name;
                nameInput.className = 'js-material-blob-input';
                form.appendChild(nameInput);

                const sizeInput = document.createElement('input');
                sizeInput.type = 'hidden';
                sizeInput.name = 'materialFileSizes';
                sizeInput.value = file.size;
                sizeInput.className = 'js-material-blob-input';
                form.appendChild(sizeInput);
            }

            progress.update(100);
            form.dataset.uploaded = 'true';
            setUploadModalLock(form, false);
            form.submit();
        } catch (error) {
            showAppToast('Lỗi tải tài liệu: ' + error.message, 'error');
            form.dataset.submitting = 'false';
            setUploadModalLock(form, false);
        }
    });
}

function validateMaterialFiles(input) {
    if (!input || !input.files || input.files.length === 0) {
        return true;
    }

    const allowedExtensions = ['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx'];
    const maxSizeBytes = 150 * 1024 * 1024; // 150MB

    const invalidFiles = Array.from(input.files).filter(file => {
        const extension = file.name.includes('.')
            ? file.name.split('.').pop().toLowerCase()
            : '';
        return !allowedExtensions.includes(extension);
    });

    if (invalidFiles.length > 0) {
        showAppToast('Chỉ chấp nhận tài liệu định dạng: pdf, doc, docx, xls, xlsx, ppt, pptx.', 'error');
        input.value = '';
        return false;
    }

    const oversizedFiles = Array.from(input.files).filter(file => file.size > maxSizeBytes);
    if (oversizedFiles.length > 0) {
        showAppToast('Kích thước tài liệu tải lên không được vượt quá 150MB.', 'error');
        input.value = '';
        return false;
    }

    return true;
}

function initMaterialFileValidation() {
    const input = document.getElementById('addMaterialFile');
    if (!input) return;

    input.addEventListener('change', function () {
        validateMaterialFiles(input);
    });
}

function resolveLessonFormSectionId(form) {
    const sectionInput = form.querySelector('input[name="sectionId"]');
    if (sectionInput?.value) {
        return sectionInput.value;
    }

    const match = form.action.match(/\/sections\/(\d+)\/lessons/);
    if (match) {
        return match[1];
    }

    throw new Error('Không tìm thấy chương để tải video lên.');
}

async function uploadLessonVideoDirectToAzure(file, sectionId, onProgress) {
    const requestBody = new FormData();
    requestBody.append('fileName', file.name);
    requestBody.append('sectionId', sectionId);

    let sasResponse;
    try {
        sasResponse = await fetch('/instructor/video-upload-url', {
            method: 'POST',
            body: requestBody
        });
    } catch (error) {
        throw new Error('Không lấy được đường dẫn để tải video lên.');
    }

    if (!sasResponse.ok) {
        throw new Error('Không lấy được đường dẫn để tải video lên.');
    }

    const uploadInfo = await sasResponse.json();
    await uploadBlockBlobToAzure(uploadInfo.uploadUrl, file, onProgress);

    return uploadInfo.blobName;
}

async function uploadBlockBlobToAzure(uploadUrl, file, onProgress) {
    const blockSize = 8 * 1024 * 1024;
    const concurrency = 4;
    const blockIds = [];
    const blockLoadedBytes = [];
    const totalBlocks = Math.ceil(file.size / blockSize);
    let nextBlockIndex = 0;

    function reportProgress() {
        if (typeof onProgress !== 'function') {
            return;
        }
        const uploadedBytes = blockLoadedBytes.reduce((sum, loaded) => sum + (loaded || 0), 0);
        onProgress(Math.min(99, uploadedBytes * 100 / file.size));
    }

    async function uploadNextBlock() {
        const blockIndex = nextBlockIndex++;
        if (blockIndex >= totalBlocks) {
            return;
        }

        const start = blockIndex * blockSize;
        const end = Math.min(start + blockSize, file.size);
        const block = file.slice(start, end);
        const blockId = btoa(String(blockIndex).padStart(6, '0'));
        blockIds[blockIndex] = blockId;

        await uploadBlockWithProgress(appendAzureSasParams(uploadUrl, {
            comp: 'block',
            blockid: blockId
        }), block, function (loaded) {
            blockLoadedBytes[blockIndex] = loaded;
            reportProgress();
        });

        await uploadNextBlock();
    }

    try {
        const workers = Array.from(
            { length: Math.min(concurrency, totalBlocks) },
            uploadNextBlock
        );
        await Promise.all(workers);

        const blockListXml = `<?xml version="1.0" encoding="utf-8"?><BlockList>${blockIds
            .map(blockId => `<Latest>${blockId}</Latest>`)
            .join('')}</BlockList>`;

        const commitResponse = await fetch(appendAzureSasParams(uploadUrl, {
            comp: 'blocklist'
        }), {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/xml',
                'x-ms-blob-content-type': file.type || 'application/octet-stream'
            },
            body: blockListXml
        });

        if (!commitResponse.ok) {
            throw new Error('Không thể hoàn tất tải tài liệu lên.');
        }
        if (typeof onProgress === 'function') {
            onProgress(100);
        }
    } catch (error) {
        throw new Error(error.message || 'Khong upload truc tiep len Azure duoc. Kiem tra CORS cua Azure Blob.');
    }
}

function uploadBlockWithProgress(url, block, onBlockProgress) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open('PUT', url, true);
        xhr.setRequestHeader('Content-Type', 'application/octet-stream');

        xhr.upload.onprogress = function (event) {
            if (event.lengthComputable && typeof onBlockProgress === 'function') {
                onBlockProgress(event.loaded);
            }
        };

        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 300) {
                if (typeof onBlockProgress === 'function') {
                    onBlockProgress(block.size);
                }
                resolve();
                return;
            }
            reject(new Error('Tải video lên thất bại'));
        };

        xhr.onerror = function () {
            reject(new Error('Tải video lên thất bại.'));
        };

        xhr.send(block);
    });
}

function appendAzureSasParams(uploadUrl, params) {
    const url = new URL(uploadUrl);
    Object.entries(params).forEach(([key, value]) => {
        url.searchParams.set(key, value);
    });
    return url.toString();
}

function initAvatarPreview() {
    const input = document.getElementById('avatarInput');
    const preview = document.getElementById('avatarPreview');
    if (!input || !preview) return;

    input.addEventListener('change', function () {
        const file = this.files[0];
        if (!file) return;
        const url = URL.createObjectURL(file);


        preview.innerHTML = `<img src="${url}" alt="Avatar" style="width:100%;height:100%;object-fit:cover;border-radius:50%;"/>`;

        const sidebarAvatar = document.getElementById('sidebarAvatar');
        if (sidebarAvatar) sidebarAvatar.innerHTML = `<img src="${url}" alt=""/>`;


        const ppAvatar = document.querySelector('.profile-preview-avatar img');
        if (ppAvatar) ppAvatar.src = url;
    });
}

function initThumbnailPreview() {
    const input = document.getElementById('thumbnailInput');
    if (!input) return;

    input.addEventListener('change', function () {
        const file = this.files[0];
        if (!file) return;
        const url = URL.createObjectURL(file);
        const banner = document.getElementById('courseBanner');
        if (banner) banner.style.backgroundImage = `url(${url})`;
        const uploadArea = input.closest('.form-group')?.querySelector('.file-upload-area');
        if (uploadArea) {
            uploadArea.innerHTML = `<img src="${url}" style="max-height:120px;border-radius:6px;" alt="Thumbnail"/>`;
        }
    });
}

function initProfilePreview() {
    const firstNameInput = document.getElementById('firstName') || document.querySelector('[name="firstName"]');
    const lastNameInput = document.getElementById('lastName') || document.querySelector('[name="lastName"]');
    const previewName = document.getElementById('previewName');
    const bioEditor = document.getElementById('bioEditor');
    const previewBio = document.getElementById('previewBio');

    if (firstNameInput && lastNameInput && previewName) {
        const updateName = () => {
            previewName.textContent = (firstNameInput.value + ' ' + lastNameInput.value).trim();
        };
        firstNameInput.addEventListener('input', updateName);
        lastNameInput.addEventListener('input', updateName);
    }

    if (bioEditor && previewBio) {
        bioEditor.addEventListener('input', function () {
            previewBio.innerHTML = this.innerHTML || 'Giới thiệu bản thân của bạn sẽ hiện ở đây...';
        });
    }
}

function initCoursePreview() {
    const titleInput = document.getElementById('courseTitleInput');
    const shortInput = document.getElementById('courseShortInput');
    const previewTitle = document.getElementById('previewCourseTitle');
    const previewShort = document.getElementById('previewCourseShort');

    if (titleInput && previewTitle) {
        titleInput.addEventListener('input', function () {
            previewTitle.textContent = this.value || 'Tiêu đề khóa học sẽ hiện ở đây';
        });
    }
    if (shortInput && previewShort) {
        shortInput.addEventListener('input', function () {
            previewShort.textContent = this.value || 'Mô tả ngắn về khóa học...';
        });
    }
}

function initPricing() {
    const priceInput = document.getElementById('priceInput');
    const priceDisplay = document.getElementById('priceDisplay');
    if (!priceInput || !priceDisplay) return;

    priceInput.addEventListener('input', function () {
        const val = parseInt(this.value) || 0;
        priceDisplay.textContent = val.toLocaleString('vi-VN') + ' VNĐ';
    });
}

function initVideoUpload() {
    initVideoUploadById(
        'videoFileInput',
        'videoPreviewContainer',
        'durationSecondsInput'
    );
}


function initEditVideoUpload() {
    initVideoUploadById(
        'editVideoFileInput',
        'editVideoPreviewContainer',
        'editDurationSecondsInput'
    );
}


function initVideoUploadById(inputId, containerId, durationId) {
    const input = document.getElementById(inputId);
    const container = document.getElementById(containerId);
    const durationInput = document.getElementById(durationId);

    if (!input) return;

    input.addEventListener('change', function () {
        const file = this.files[0];
        if (!file) return;

        // Preview
        if (container) {
            const url = URL.createObjectURL(file);

            container.innerHTML = `
                <video src="${url}" controls
                       style="width:100%;margin-top:10px;border-radius:6px;max-height:220px;">
                </video>
                <p>${file.name}</p>
            `;
        }

        // Duration
        if (durationInput) {
            const video = document.createElement('video');
            video.preload = 'metadata';

            video.onloadedmetadata = function () {
                URL.revokeObjectURL(video.src);
                durationInput.value = Math.round(video.duration);
            };

            video.src = URL.createObjectURL(file);
        }
    });
}



/* =====================
   CURRICULUM
===================== */
let sectionCounter = 0;

function initCurriculum() {
    const addSectionBtn = document.getElementById('addSectionBtn');
    if (!addSectionBtn) return;

    addSectionBtn.addEventListener('click', function () {
        openModal('modal-add-section');
    });

    // Confirm add section (JS only mode)
    const confirmBtn = document.getElementById('confirmAddSection');
    if (confirmBtn) {
        confirmBtn.addEventListener('click', function () {
            const name = document.getElementById('sectionNameInput')?.value.trim();
            if (!name) { alert('Vui lòng nhập tên chương.'); return; }
            addSectionToUI(name);
            document.getElementById('sectionNameInput').value = '';
            closeModal('modal-add-section');
        });
    }
}

function addSectionToUI(title) {
    sectionCounter++;
    const list = document.getElementById('curriculumList');
    if (!list) return;

    const sectionEl = document.createElement('div');
    sectionEl.className = 'section-item';
    sectionEl.dataset.sectionIndex = sectionCounter;
    sectionEl.innerHTML = `
    <div class="section-header">
      <span class="drag-handle">⠿</span>
      <span class="section-number">Chương ${sectionCounter}</span>
      <span class="section-title">${escapeHtml(title)}</span>
      <div class="section-actions">
        <button type="button" class="btn btn-sm btn-outline add-lesson-btn" data-section-index="${sectionCounter}">
          + Thêm Bài Giảng
        </button>
        <button type="button" class="btn btn-sm btn-danger delete-section-btn">🗑</button>
      </div>
    </div>
    <div class="lesson-list" id="lessons-${sectionCounter}">
      <div class="table-empty" style="padding:10px 16px;font-size:.85rem;">Chưa có bài giảng</div>
    </div>
  `;

    sectionEl.querySelector('.add-lesson-btn').addEventListener('click', function () {
        currentSectionIndex = parseInt(this.dataset.sectionIndex);
        openModal('modal-add-lesson');
    });
    sectionEl.querySelector('.delete-section-btn').addEventListener('click', function () {
        if (confirm('Xóa chương này?')) sectionEl.remove();
    });

    list.appendChild(sectionEl);
    updateCurriculumPreview();
}


let currentSectionIndex = null;


document.addEventListener('click', function (e) {
    const confirmLesson = e.target.closest('#confirmAddLesson');
    if (!confirmLesson) return;
    const name = document.getElementById('lessonNameInput')?.value.trim();
    if (!name) { alert('Vui lòng nhập tên bài giảng.'); return; }
    addLessonToSection(currentSectionIndex, name);
    document.getElementById('lessonNameInput').value = '';
    closeModal('modal-add-lesson');
});





function addLessonToSection(sectionIndex, title) {
    const lessonList = document.getElementById(`lessons-${sectionIndex}`);
    if (!lessonList) return;

    // Remove empty state message
    const empty = lessonList.querySelector('.table-empty');
    if (empty) empty.remove();

    const lessonEl = document.createElement('div');
    lessonEl.className = 'lesson-item';
    lessonEl.innerHTML = `
    <span class="drag-handle">⠿</span>
    <span class="lesson-icon">🎬</span>
    <span class="lesson-title">${escapeHtml(title)}</span>
    <div class="lesson-actions">
      <button type="button" class="btn btn-sm btn-danger delete-lesson-btn">🗑</button>
    </div>
  `;
    lessonEl.querySelector('.delete-lesson-btn').addEventListener('click', function () {
        if (confirm('Xóa bài giảng này?')) lessonEl.remove();
    });
    lessonList.appendChild(lessonEl);
    updateCurriculumPreview();
}

function updateCurriculumPreview() {
    const preview = document.getElementById('curriculumPreview');
    if (!preview) return;
    const sections = document.querySelectorAll('#curriculumList .section-item');
    if (!sections.length) {
        preview.innerHTML = '<p style="color:var(--text-muted);font-size:.85rem;">Các chương và bài giảng sẽ hiện ở đây...</p>';
        return;
    }
    let html = '';
    sections.forEach((sec, i) => {
        const title = sec.querySelector('.section-title')?.textContent || '';
        const lessons = sec.querySelectorAll('.lesson-title');
        html += `<div style="margin-bottom:10px;"><strong>Chương ${i + 1}: ${title}</strong>`;
        if (lessons.length) {
            html += '<ul style="margin:4px 0 0 16px;">';
            lessons.forEach(l => { html += `<li style="font-size:13px;color:var(--text-muted);">${l.textContent}</li>`; });
            html += '</ul>';
        }
        html += '</div>';
    });
    preview.innerHTML = html;
}

function openEditSectionModal(dataset) {
    const source = dataset.source || 'create';
    const form = document.getElementById("editSectionForm");
    if (!form) {
        alert('Không tìm thấy nơi nhập dữ liệu chương!');
        return;
    }

    form.action =
        `/instructor/${dataset.courseId}/sections/${dataset.sectionId}/edit?source=${source}`;

    const sourceInput = form.querySelector('input[name="source"]');
    if (sourceInput) {
        sourceInput.value = source;
    }

    const titleInput = form.querySelector('[name="title"]');
    if (titleInput) {
        titleInput.value = dataset.sectionTitle || '';
    }

    openModal("modal-edit-section");
}



function openEditLessonModal(dataset) {
    const form = document.getElementById('editLessonForm');
    const source = dataset.source || 'create';
    const courseId = dataset.courseId;
    const sectionId = dataset.sectionId;
    const lessonId = dataset.lessonId;

    form.action = `/instructor/sections/${sectionId}/lessons/${lessonId}/edit?source=${source}`;

    document.getElementById('editCourseid').value = courseId;
    document.getElementById('editSectionId').value = sectionId;
    document.getElementById('editLessonId').value = lessonId;

    document.getElementById('editLessonName').value = dataset.lessonTitle || '';
    const videoPreviewContainer = document.getElementById('editVideoPreviewContainer');
    videoPreviewContainer.innerHTML = '';
    const editVideoInput = document.getElementById('editVideoFileInput');
    editVideoInput.value = '';
    editVideoInput.disabled = false;
    document.getElementById('editVideoBlobNameInput').value = '';
    form.dataset.submitting = 'false';
    form.dataset.directUploaded = 'false';
    setUploadModalLock(form, false);
    resetUploadProgress(form);


    const oldVideoUrl = dataset.lessonVideoUrl || dataset.videoUrl;
    if (oldVideoUrl && oldVideoUrl !== 'null' && oldVideoUrl !== '') {
        videoPreviewContainer.innerHTML = `
            <div class="old-video-info" style="margin-top: 10px; padding: 10px; background: #f9f9f9; border: 1px dashed #ccc; border-radius: 6px;">
                <p style="font-size: 13px; color: #555; margin-bottom: 5px;">🎬 <strong>Video hiện tại của bài giảng:</strong></p>
                <video src="${oldVideoUrl}" controls style="width:100%; border-radius:4px; max-height:180px;"></video>
            </div>
        `;
    }

    // Reset media tabs
    document.querySelectorAll('.media-tab[data-group="edit-lesson-media"]').forEach(tab => {
        tab.classList.toggle('active', tab.dataset.type === 'video');
    });
    document.querySelectorAll('.media-content[data-group="edit-lesson-media"]').forEach(content => {
        content.style.display = content.dataset.type === 'video' ? '' : 'none';
    });

    openModal('modal-edit-lesson');
}



function deleteLesson(lessonId, courseId, source) {
    source = source || 'create';
    if (!lessonId || !courseId) {
        alert('ID không hợp lệ!');
        return;
    }

    if (!confirm('Xóa bài giảng này? Tất cả tài liệu và bài tập sẽ bị xóa theo.')) {
        return;
    }


    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/instructor/lessons/${lessonId}/delete?courseId=${courseId}&source=${source}`;
    document.body.appendChild(form);
    form.submit();
}

function deleteMaterial(courseId, materialId, source) {
    source = source || 'create';
    if (!materialId || !courseId) {
        alert(' ID không hợp lệ!');
        return;
    }

    if (!confirm('Xóa tài liệu này?')) {
        return;
    }


    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/instructor/materials/${materialId}/delete?source=${source}`;
    const courseInput = document.createElement('input');
    courseInput.type = 'hidden';
    courseInput.name = 'courseId';
    courseInput.value = courseId;
    form.appendChild(courseInput);
    document.body.appendChild(form);
    form.submit();
}



function renderExistingMaterials(materials, lessonId, courseId) {
    const container = document.getElementById('existingMaterialsContainer');
    if (!materials || materials.length === 0) {
        container.innerHTML = '<p style="color:#999; font-size:0.9rem;">📭 Chưa có tài liệu nào</p>';
        return;
    }

    const iconMap = {
        pdf: { icon: 'ti-file-type-pdf', color: '#E24B4A' },
        docx: { icon: 'ti-file-type-doc', color: '#185FA5' },
        pptx: { icon: 'ti-file-type-ppt', color: '#D85A30' },
        xlsx: { icon: 'ti-file-type-xls', color: '#3B6D11' },
    };

    container.innerHTML = materials.map(material => {
        const ext = material.fileName.split('.').pop().toLowerCase();
        const m = iconMap[ext] || { icon: 'ti-file', color: '#888' };
        const mb = (material.fileSize / 1024 / 1024).toFixed(1);

        return `
        <div style="display:flex; align-items:center; gap:10px; margin-top:8px; padding:10px 12px; 
                    border:0.5px solid var(--border-color); border-radius:8px; background:var(--bg-light);">
            <i class="ti ${m.icon}" style="font-size:20px; color:${m.color}; flex-shrink:0;"></i>
            <div style="flex:1; min-width:0;">
                <div style="font-size:13px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
                    ${material.fileName}
                </div>
                <div style="font-size:12px; color:var(--text-muted);">${mb} MB</div>
            </div>
            <form method="POST" 
                  action="/instructor/materials/${material.id}/delete?lessonId=${lessonId}&courseId=${courseId}"
                  style="display:inline;">
                <button type="submit" 
                        onclick="return confirm('Xóa tài liệu này?');"
                        style="background:none; border:none; cursor:pointer; color:var(--text-danger); 
                               padding:4px; font-size:16px;">
                    ✕
                </button>
            </form>
        </div>`;
    }).join('');
}


function deleteSection(sectionId, courseId, source) {
    source = source || 'create';
    if (!confirm('Xóa chương này và tất cả bài giảng bên trong?')) return;
    fetch(`/instructor/${courseId}/sections/${sectionId}/delete?source=${source}`, { method: 'POST' })
        .then(r => { if (r.ok) location.reload(); });
}


function openAddLessonModal(sectionId, source, courseId) {
    const form = document.getElementById('addLessonForm');
    source = source || 'create';
    form.action = `/instructor/sections/${sectionId}/lessons?source=${source}`;

    document.getElementById('addLessonSectionId').value = sectionId;
    const courseInput = document.getElementById('addLessonCourseId');
    if (courseInput && courseId) {
        courseInput.value = courseId;
    }
    document.getElementById('videoBlobNameInput').value = '';
    const videoInput = document.getElementById('videoFileInput');
    if (videoInput) {
        videoInput.value = '';
        videoInput.disabled = false;
    }
    const preview = document.getElementById('videoPreviewContainer');
    if (preview) {
        preview.innerHTML = '';
    }
    form.dataset.submitting = 'false';
    form.dataset.directUploaded = 'false';
    setUploadModalLock(form, false);
    resetUploadProgress(form);

    document.getElementById('modal-add-lesson').classList.add('active');
}



/* =====================
   SUBMIT REQUEST (step 5)
===================== */
function initSubmitRequest() {
    const btn = document.getElementById('submitRequestBtn');
    const msg = document.getElementById('submitSuccessMsg');
    if (!btn || !msg) return;
    btn.addEventListener('click', function () {
        msg.style.display = 'flex';
        btn.disabled = true;
        btn.textContent = '✅ Đã Gửi';
    });
}

function initSubmitReviewPolicy() {
    const form = document.getElementById('submitReviewForm');
    const checkbox = document.getElementById('acceptPolicyInput');
    const button = document.getElementById('submitReviewBtn');
    if (!form || !checkbox || !button) return;

    const readyExceptPolicy = form.dataset.readyExceptPolicy === 'true';
    const policyItem = document.querySelector('[data-policy-item="true"]');
    const policyStatusIcon = policyItem?.querySelector('.result-icon i');
    const policyTrailingIcon = policyItem?.querySelector('.item-check, .item-empty');
    const policyMissing = document.querySelector('[data-policy-missing="true"]');
    const missingBox = policyMissing?.closest('.missing-box');
    const progressCard = document.querySelector('.progress-card[data-completed-count][data-total-count]');
    const percentText = document.getElementById('submitReviewPercentText');
    const ratioText = document.getElementById('submitReviewRatioText');
    const progressNumber = document.getElementById('submitReviewProgressNumber');
    const progressBar = document.getElementById('submitReviewProgressBar');
    const progressRing = progressCard?.querySelector('.progress-ring');

    const baseCompleted = progressCard ? Number(progressCard.dataset.completedCount || 0) : 0;
    const totalCount = progressCard ? Number(progressCard.dataset.totalCount || 0) : 0;
    const policyWasCompletedOnLoad = policyItem?.classList.contains('is-ok') === true;

    const updateState = () => {
        const accepted = checkbox.checked;
        button.disabled = !(readyExceptPolicy && accepted);
        const completedCount = baseCompleted + (accepted && !policyWasCompletedOnLoad ? 1 : 0) - (!accepted && policyWasCompletedOnLoad ? 1 : 0);
        const percent = totalCount === 0 ? 0 : Math.round(completedCount * 100 / totalCount);

        if (policyItem) {
            policyItem.classList.toggle('is-ok', accepted);
            policyItem.classList.toggle('is-missing', !accepted);
        }

        if (policyStatusIcon) {
            policyStatusIcon.className = accepted
                ? 'bi bi-check-lg'
                : 'bi bi-exclamation-triangle-fill';
        }

        if (policyTrailingIcon) {
            policyTrailingIcon.className = accepted
                ? 'bi bi-check-square-fill item-check'
                : 'bi bi-square item-empty';
        }

        if (policyMissing) {
            policyMissing.style.display = accepted ? 'none' : '';
        }

        if (missingBox) {
            const hasVisibleMissing = Array.from(missingBox.querySelectorAll('li'))
                .some(item => item.style.display !== 'none');
            missingBox.style.display = hasVisibleMissing ? '' : 'none';
        }

        if (progressRing) {
            progressRing.style.setProperty('--percent', percent);
        }
        if (percentText) {
            percentText.textContent = `${percent}%`;
        }
        if (ratioText) {
            ratioText.textContent = `${completedCount}/${totalCount}`;
        }
        if (progressNumber) {
            progressNumber.textContent = `${completedCount} / ${totalCount}`;
        }
        if (progressBar) {
            progressBar.style.width = `${percent}%`;
        }
    };

    checkbox.addEventListener('change', updateState);
    updateState();
}

/* =====================
   WIZARD BUTTONS (single-file SPA mode)
===================== */
function initWizardButtons() {
    const prevBtn = document.getElementById('stepPrevBtn');
    const nextBtn = document.getElementById('stepNextBtn');
    if (!prevBtn || !nextBtn) return;

    let currentStep = 1;
    const totalSteps = 5;

    function showStep(step) {
        for (let i = 1; i <= totalSteps; i++) {
            const el = document.getElementById(`step-${i}`);
            if (el) el.style.display = i === step ? '' : 'none';
        }
        document.querySelectorAll('.wizard-step').forEach((s, i) => {
            s.classList.toggle('active', i + 1 === step);
        });
        prevBtn.disabled = step === 1;
        nextBtn.textContent = step === totalSteps ? 'Gửi Duyệt' : 'Tiếp Theo →';
    }

    prevBtn.addEventListener('click', () => { if (currentStep > 1) showStep(--currentStep); });
    nextBtn.addEventListener('click', () => {
        if (currentStep < totalSteps) showStep(++currentStep);
        else document.getElementById('submitRequestBtn')?.click();
    });

    document.querySelectorAll('.wizard-step[data-step]').forEach(s => {
        s.addEventListener('click', function () {
            const step = parseInt(this.dataset.step);
            if (step) { currentStep = step; showStep(step); }
        });
    });
}

/* =====================
   MATERIAL FILE PREVIEW
===================== */
function initMaterialPreview() {
    initMaterialPreviewByID(
        "MaterialInput",
        "materialList"
    );
}

function initEditMaterialPreview() {
    initMaterialPreviewByID(
        "editMaterialInput",
        "editMaterialList"
    );
}
function initMaterialPreviewByID(inputId, listId) {
    const input = document.getElementById(inputId);
    const list = document.getElementById(listId);
    const dt = new DataTransfer();

    if (!input || !list) return;

    input.addEventListener('change', function () {
        Array.from(this.files).forEach(file => {
            if (![...dt.files].find(f => f.name === file.name)) {
                dt.items.add(file);
            }
        });
        input.files = dt.files;
        renderList();
    });

    function renderList() {
        if (!dt.files.length) { list.innerHTML = ''; return; }

        const iconMap = {
            pdf: { icon: 'ti-file-type-pdf', color: '#E24B4A' },
            docx: { icon: 'ti-file-type-doc', color: '#185FA5' },
            pptx: { icon: 'ti-file-type-ppt', color: '#D85A30' },
            xlsx: { icon: 'ti-file-type-xls', color: '#3B6D11' },
        };

        list.innerHTML = Array.from(dt.files).map((file, i) => {
            const ext = file.name.split('.').pop().toLowerCase();
            const m = iconMap[ext] || { icon: 'ti-file', color: '#888' };
            const mb = (file.size / 1024 / 1024).toFixed(1);
            return `
            <div style="display:flex; align-items:center; gap:10px; margin-top:8px; padding:10px 12px; border:0.5px solid var(--border-color); border-radius:8px;">
                <i class="ti ${m.icon}" style="font-size:20px; color:${m.color}; flex-shrink:0;"></i>
                <div style="flex:1; min-width:0;">
                    <div style="font-size:13px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${file.name}</div>
                    <div style="font-size:12px; color:var(--text-muted);">${mb} MB</div>
                </div>
                <button type="button" onclick="removeMaterial(${i})"
                        style="background:none; border:none; cursor:pointer; color:var(--text-muted); padding:4px;">
                    <i class="ti ti-x" style="font-size:15px;"></i>
                </button>
            </div>`;
        }).join('');
    }

    window.removeMaterial = function (index) {
        dt.items.remove(index);
        input.files = dt.files;
        renderList();
    };
}

function appendSourceParam(url, source) {
    const sep = url.includes('?') ? '&' : '?';
    return url + sep + 'source=' + encodeURIComponent(source || 'create');
}
function openAddMaterialModal(dataset) {
    const form = document.getElementById('addMaterialForm');
    const source = dataset.source || 'create';
    if (!form || !dataset.lessonId) {
        alert('Không tìm thấy thông tin bài giảng!');
        return;
    }

    form.action = appendSourceParam(dataset.actionUrl, source);

    document.getElementById('addMaterialLessonId').value =
        dataset.lessonId;

    document.getElementById('addMaterialCourseId').value =
        dataset.courseId;

    document.getElementById('addMaterialFile').value = '';
    form.dataset.submitting = 'false';
    setUploadModalLock(form, false);

    const preview =
        document.getElementById('addMaterialPreview');

    if (preview) {
        preview.innerHTML = '';
    }

    openModal('modal-add-material');
}

function initToastNotifications() {
    document.querySelectorAll('.app-toast').forEach(toast => {
        const close = toast.querySelector('.toast-close');
        const hide = () => {
            toast.classList.add('toast-hide');
            window.setTimeout(() => toast.remove(), 180);
        };

        if (close) {
            close.addEventListener('click', hide);
        }

        window.setTimeout(hide, 4200);
    });
}


function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

function showAppToast(message, type = 'error') {
    let stack = document.querySelector('.toast-stack');
    if (!stack) {
        stack = document.createElement('div');
        stack.className = 'toast-stack';
        stack.setAttribute('aria-live', 'polite');
        stack.setAttribute('aria-atomic', 'true');
        document.body.appendChild(stack);
    }

    const toast = document.createElement('div');
    toast.className = `app-toast toast-${type}`;
    
    const iconClass = type === 'success' ? 'bi-check-circle' : (type === 'warning' ? 'bi-exclamation-triangle' : 'bi-exclamation-circle');
    
    toast.innerHTML = `
        <i class="bi ${iconClass}"></i>
        <span>${escapeHtml(message)}</span>
        <button type="button" class="toast-close" aria-label="Đóng">&times;</button>
    `;

    stack.appendChild(toast);

    const closeBtn = toast.querySelector('.toast-close');
    const hide = () => {
        toast.classList.add('toast-hide');
        window.setTimeout(() => toast.remove(), 180);
    };

    if (closeBtn) {
        closeBtn.addEventListener('click', hide);
    }
    window.setTimeout(hide, 4200);
}
