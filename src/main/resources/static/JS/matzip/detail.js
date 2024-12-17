document.querySelectorAll('.star').forEach(($star) => {
    $star.addEventListener('click', ({ target }) => {
        // 모든 .star에서 .active 삭제
        document.querySelectorAll('.star').forEach(($star) => {
            $star.classList.remove('active');
        });

        // 클릭한 .star에 .active 추가
        target.classList.add('active');
    });
});


