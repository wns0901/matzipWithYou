let currentPage = 0; // 현재 페이지 인덱스
let isScrolling = false; // 스크롤 중인지 확인하는 플래그

function scrollToPage(pageIndex) {
    const sections = document.querySelectorAll(".section");
    const totalPages = sections.length;

    // 페이지 인덱스가 범위를 벗어나지 않도록 조정
    if (pageIndex < 0 || pageIndex >= totalPages) return;

    currentPage = pageIndex; // 현재 페이지 업데이트
    const offset = -currentPage * 100; // 스크롤할 위치 계산

    // 각 섹션을 이동
    sections.forEach((section) => {
        section.style.transform = `translateY(${offset}vh)`;
    });

    // 라디오 버튼 상태 업데이트
    const radioButton = document.querySelector(`input[name="page"][id="page${pageIndex + 1}-radio"]`);
    if (radioButton) {
        radioButton.checked = true;
    }

    // 스크롤 중 플래그 초기화
    setTimeout(() => {
        isScrolling = false;
    }, 700); // 스크롤 완료 후 플래그 초기화 (애니메이션 시간과 일치)
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

    // 스크롤 이벤트 추가
    window.addEventListener("wheel", (event) => {
        if (isScrolling) return; // 스크롤 중일 때 중복 처리 방지

        isScrolling = true; // 스크롤 중 플래그 설정

        if (event.deltaY > 0) {
            // 스크롤 아래로 (다음 페이지)
            scrollToPage(currentPage + 1);
        } else {
            // 스크롤 위로 (이전 페이지)
            scrollToPage(currentPage - 1);
        }
    });

    // 라디오 버튼 클릭 이벤트 추가
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
