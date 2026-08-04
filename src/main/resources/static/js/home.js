// Learning Hub - Home Page JavaScript
document.addEventListener('DOMContentLoaded', () => {
    if (typeof lucide !== 'undefined') lucide.createIcons();

    initScrollHeader();
    initScrollAnimations();
    initCategoryCards();
    initPlayButton();
});

// 1. Sticky header shrink on scroll
function initScrollHeader() {
    const header = document.getElementById('site-header');
    if (!header) return;
    window.addEventListener('scroll', () => {
        if (window.scrollY > 40) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
    }, { passive: true });
}

// 2. Scroll-triggered entrance animations using IntersectionObserver
function initScrollAnimations() {
    const targets = document.querySelectorAll('.stat-item, .category-card, .section-header');
    if (!targets.length) return;

    const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry, i) => {
            if (entry.isIntersecting) {
                entry.target.style.animationDelay = `${i * 0.1}s`;
                entry.target.classList.add('animate-fadeInUp');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.15 });

    targets.forEach(el => observer.observe(el));
}

// 3. Category card hover tilt effect
function initCategoryCards() {
    const cards = document.querySelectorAll('.category-card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.willChange = 'transform';
        });
        card.addEventListener('mouseleave', function() {
            this.style.willChange = 'auto';
        });
    });
}

// 4. Play button click - scroll to courses or open modal
function initPlayButton() {
    const playBtn = document.getElementById('btn-play-intro');
    if (!playBtn) return;
    playBtn.addEventListener('click', () => {
        // Scroll smoothly to courses section or alert
        const coursesSection = document.querySelector('.categories-section');
        if (coursesSection) {
            coursesSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    });

    const findBtn = document.getElementById('btn-find-courses');
    if (!findBtn) return;
    findBtn.addEventListener('click', (e) => {
        e.preventDefault();
        const cat = document.querySelector('.categories-section');
        if (cat) cat.scrollIntoView({ behavior: 'smooth', block: 'start' });
    });
}
