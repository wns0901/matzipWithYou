let currentPage = 0; // 현재 페이지 인덱스
let isScrolling = false; // 스크롤 중인지 확인하는 플래그

function scrollToPage(pageIndex) {
    const sections = document.querySelectorAll(".section");
    const totalPages = sections.length;

    if (pageIndex < 0 || pageIndex >= totalPages) return;

    currentPage = pageIndex;
    const offset = -currentPage * 100;

    sections.forEach((section) => {
        section.style.transform = `translateY(${offset}vh)`;
    });

    const radioButton = document.querySelector(`input[name="page"][id="page${pageIndex + 1}-radio"]`);
    if (radioButton) {
        radioButton.checked = true;
    }

    setTimeout(() => {
        isScrolling = false;
    }, 1000); // 애니메이션 시간 조정
}

document.addEventListener("DOMContentLoaded", () => {
    const sections = document.querySelectorAll(".section");
    sections.forEach((section, index) => {
        if (index === 0) {
            section.style.transform = "translateY(0)";
        } else {
            section.style.transform = "translateY(100vh)";
        }
    });

    let debounceTimeout;
    window.addEventListener("wheel", (event) => {
        if (isScrolling) return;
        if (debounceTimeout) return;

        debounceTimeout = setTimeout(() => {
            debounceTimeout = null;

            isScrolling = true;

            if (event.deltaY > 0) {
                scrollToPage(currentPage + 1);
            } else {
                scrollToPage(currentPage - 1);
            }
        }, 300); // 디바운싱 시간
    });

    const radioButtons = document.querySelectorAll('input[name="page"]');
    radioButtons.forEach((radio, index) => {
        radio.addEventListener("click", () => {
            scrollToPage(index);
        });
    });
});


document.addEventListener("DOMContentLoaded", () => {
        const sections = document.querySelectorAll(".section");
        sections.forEach((section, index) => {
            if (index === 0) {
                section.style.transform = "translateY(0)";
            } else {
                section.style.transform = "translateY(100vh)";
            }
        });
    });
