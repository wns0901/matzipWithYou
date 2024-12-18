document.addEventListener('DOMContentLoaded', () => {
    // 하트 버튼 토글 기능
    const heartButtons = document.querySelectorAll('.heart-button');

    function toggleHeart(element) {
        element.classList.toggle('clicked');
    }
});

// 닉 변경 기능
document.addEventListener('DOMContentLoaded', function () {
    const overlay = document.getElementById('overlay');
    const editButton = document.getElementById('editButton');
    const nicknamePopup = document.getElementById('nicknamePopup');
    const cancelButton = document.getElementById('cancelButton');
    const confirmButton = document.getElementById('confirmButton');
    const errorMessage = document.getElementById('errorMessage');
    const newNicknameInput = document.getElementById('newNickname');

    // 수정 버튼 클릭 시 팝업 열기
    editButton.addEventListener('click', function () {
        nicknamePopup.style.display = 'block';
        errorMessage.textContent = ''; // 에러 메시지 초기화
        overlay.style.display = 'block'; // 배경 레이어 보이기

    });

    // 취소 버튼 클릭 시 팝업 닫기
    cancelButton.addEventListener('click', function () {
        nicknamePopup.style.display = 'none';
        overlay.style.display = 'none'; // 배경 레이어 숨기기

    });

    // 확인 버튼 클릭 시 닉네임 검증
    confirmButton.addEventListener('click', function () {
        const newNickname = newNicknameInput.value.trim();
        if (newNickname === '') {
            errorMessage.textContent = '닉네임을 입력해주세요.';
            return;
        }
        if (newNickname.length > 20) {
            errorMessage.textContent = '닉네임은 최대 20자까지 입력 가능합니다.';
            return;
        }

        const memberId= parseInt(window.location.pathname.split('/').pop());
        // 서버에 변경된 닉 전송
        fetch(`/members/${memberId}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ newNickname })
        })
            .then(response => response.json())
            .then(data => {
                const errorMessage = document.getElementById('errorMessage');
                if (data.error) {
                    errorMessage.textContent = data.error;
                } else {
                    // 성공 시 팝업 닫기 및 닉네임 갱신
                    document.querySelector('.nickname-text').textContent = newNickname;
                    document.getElementById('nicknamePopup').style.display = 'none';
                    document.getElementById('overlay').style.display = 'none';
                }
            })
            .catch(error => {
                errorMessage.textContent = '서버와의 통신 중 오류가 발생했습니다.';
                console.error('Error:', error);
            });
    });
});




