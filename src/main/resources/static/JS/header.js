let lastScrollTop = 0;
const navbar = document.querySelector('.navbar');

window.addEventListener('scroll', () => {
    let scrollTop = window.pageYOffset || document.documentElement.scrollTop;

    if (scrollTop > lastScrollTop) {
        // 아래로 스크롤
        navbar.classList.add('hidden');
    } else {
        // 위로 스크롤
        navbar.classList.remove('hidden');
    }

    lastScrollTop = scrollTop;
});