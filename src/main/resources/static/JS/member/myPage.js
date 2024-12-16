document.addEventListener('DOMContentLoaded', function () {
    const memberId = [[${memberId}]];

    const updateNicknameForm = document.getElementById('updateNicknameForm');
    const messageContainer = document.getElementById('messageContainer');
    const myPageContainer = document.getElementById('myPageContainer');

    updateNicknameForm.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 폼 동작 방지

        const newNickname = document.getElementById('newNickname').value;

        // 닉네임 변경 요청
        fetch(`/members/${memberId}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ newNickname })
        })
            .then(response => response.json())
            .then(data => {
                // 응답 메시지를 표시
                if (data.error) {
                    messageContainer.innerText = `Error: ${data.error}`;
                    messageContainer.style.color = 'red';
                } else {
                    messageContainer.innerText = data.message;
                    messageContainer.style.color = 'green';

                    // 마이페이지 정보 갱신
                    if (data.myPage) {
                        const { nickname, friendCnt, point, profileImage } = data.myPage;

                        // 화면에 갱신된 정보 반영
                        myPageContainer.innerHTML = `
                            <p><strong>닉네임:</strong> ${nickname}</p>
                            <p><strong>친구 수:</strong> ${friendCnt}</p>
                            <p><strong>포인트:</strong> ${point}</p>
                            <p><strong>프로필 이미지:</strong> <img src="${profileImage}" alt="프로필 이미지" width="100" /></p>
                        `;
                    }
                }
            })
            .catch(error => {
                messageContainer.innerText = `Error: ${error}`;
                messageContainer.style.color = 'red';
            });
    });
});