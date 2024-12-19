document.addEventListener('DOMContentLoaded', () => {
    // 하트 버튼 토글 기능
    const heartButtons = document.querySelectorAll('.heart-button');

    function toggleHeart(element) {
        element.classList.toggle('clicked');
    }
});


// myPage.js
document.addEventListener('DOMContentLoaded', function() {
    const friendButton = document.querySelector('span.info-label.friends');
    if (!friendButton) {
        console.error('Friend button not found');
        return;
    }

    friendButton.addEventListener('click', function() {
        const memberId = this.dataset.memberId;
        if (memberId) {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = `/members/${memberId}/friends`;
            document.body.appendChild(form);
            form.submit();
        } else {
            console.error('Member ID not found');
        }
    });
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

        // 서버에서 로그인된 사용자 ID를 가져옴
        fetch('/members')
            .then(response => response.json())
            .then(memberId => {
                const newNickname = document.getElementById('newNickname').value;

                // 서버로 닉네임 전송
                fetch(`/members/${memberId}`, {
                    method: 'PATCH',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({newNickname})
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
            })
            .catch(error => {
                console.error('로그인 사용자 정보를 가져오는 중 오류 발생:', error);
            });
    });
});




