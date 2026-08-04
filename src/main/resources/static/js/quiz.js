document.addEventListener("click", function (e) {
    // If learning.js is active (embedded in the learning page), let it handle the clicks
    if (document.getElementById("quiz-content-container")) {
        return;
    }
    const toggleBtn = e.target.closest("#toggleQuizSidebarBtn, .quiz-toggle-btn");
    if (toggleBtn) {
        const container = toggleBtn.closest(".quiz-container-layout");
        if (container) {
            container.classList.toggle("is-collapsed");
            const isCollapsed = container.classList.contains("is-collapsed");
            toggleBtn.setAttribute("title", isCollapsed ? "Mở rộng danh sách" : "Thu gọn danh sách");
            toggleBtn.innerHTML = isCollapsed ? '&#10095;' : '&#10094;';
        }
        return;
    }


    const toggleAttemptsBtn = e.target.closest(".btn-toggle-attempts");
    if (toggleAttemptsBtn) {
        const list = toggleAttemptsBtn.closest(".quiz-attempt-list");
        if (list) {
            const olderItems = list.querySelectorAll(".older-attempt");
            const isExpanded = toggleAttemptsBtn.classList.contains("expanded");
            const count = toggleAttemptsBtn.getAttribute("data-count");

            olderItems.forEach(item => {
                item.style.display = isExpanded ? "none" : "flex";
            });

            if (isExpanded) {
                toggleAttemptsBtn.classList.remove("expanded");
                toggleAttemptsBtn.innerHTML = `Xem thêm ${parseInt(count) - 1} lần làm bài trước &#10095;`;
            } else {
                toggleAttemptsBtn.classList.add("expanded");
                toggleAttemptsBtn.innerHTML = `Thu gọn lịch sử &#10094;`;
            }
        }
        return;
    }

    const link = e.target.closest(".quiz-option-link");
    if (!link) return;

    const href = link.getAttribute("href");
    if (href) {
        e.preventDefault();
        if (typeof loadQuizContent === "function") {
            loadQuizContent(href);
        } else {
            window.location.href = href;
        }
    }
});

let quizCountdownTimer = null;

function initQuizCountdown() {
    if (quizCountdownTimer) {
        clearInterval(quizCountdownTimer);
        quizCountdownTimer = null;
    }

    const form = document.querySelector("form[data-time-limit-minutes]");
    const countdown = document.querySelector(".quiz-countdown[data-time-limit-minutes]");
    if (!form || !countdown) {
        return;
    }

    const limitMinutes = Number(form.dataset.timeLimitMinutes || countdown.dataset.timeLimitMinutes);
    if (!Number.isFinite(limitMinutes) || limitMinutes <= 0) {
        return;
    }

    let remainingSeconds = Math.round(limitMinutes * 60);

    function renderCountdown() {
        const minutes = Math.floor(remainingSeconds / 60);
        const seconds = remainingSeconds % 60;
        countdown.textContent = `${minutes}:${String(seconds).padStart(2, "0")}`;
    }

    renderCountdown();
    quizCountdownTimer = setInterval(function () {
        remainingSeconds -= 1;
        renderCountdown();

        if (remainingSeconds <= 0) {
            clearInterval(quizCountdownTimer);
            quizCountdownTimer = null;
            if (typeof form.requestSubmit === "function") {
                form.requestSubmit();
            } else {
                form.submit();
            }
        }
    }, 1000);
}

document.addEventListener("DOMContentLoaded", initQuizCountdown);
