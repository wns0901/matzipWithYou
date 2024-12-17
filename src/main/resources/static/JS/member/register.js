document.addEventListener('DOMContentLoaded', function () {
    console.log('DOM Content Loaded');

    const verifyButton = document.getElementById('verifyButton');
    const authButton = document.getElementById('authButton');
    const verificationCodeGroup = document.getElementById('verificationCodeGroup');
    const timerElement = document.getElementById('timer');
    const form = document.querySelector('form');

    let timerInterval = null;
    const verificationTimeout = 3 * 60 * 1000; // 3분
    let isEmailVerified = false;

    // 폼 제출 이벤트
    if (form) {
        form.addEventListener('submit', function (event) {
            console.log('Form submit triggered');

            if (!isEmailVerified) {
                event.preventDefault();
                alert('이메일 인증이 필요합니다.');
                return;
            }

            console.log('Email verified, submitting form');
            form.submit(); // 인증 완료 시 폼 제출
        });
    }

    // 이메일 인증 요청
    verifyButton.addEventListener('click', function (event) {
        event.preventDefault();

        const email = document.getElementById('email').value.trim();

        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }

        console.log('이메일 인증 요청 시작');

        fetch(`/member/sendEmail?email=${encodeURIComponent(email)}`)
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                return { error: '이메일을 형식에 맞게 입력해주세요' }; // 오류 메시지 반환
            })
            .then(data => {
                if (data.message) {
                    alert(data.message);
                    verificationCodeGroup.style.display = 'block'; // 인증 코드 입력창 표시
                    startTimer(); // 타이머 시작
                } else if (data.error) {
                    alert(data.error);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('이메일을 형식에 맞게 입력해주세요');
            });
    });

    // 인증 코드 확인 요청
    authButton.addEventListener('click', function (event) {
        event.preventDefault();

        const email = document.getElementById('email').value.trim();
        const code = document.getElementById('verificationCode').value.trim();

        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }

        if (!code) {
            alert("인증 코드를 입력해주세요.");
            return;
        }

        console.log('인증 코드 확인 요청');

        fetch(`/member/verify-code?email=${encodeURIComponent(email)}&code=${encodeURIComponent(code)}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(response => {
                if (!response.ok) {
                    return { success: false, error: '서버 응답이 올바르지 않습니다.' }; // 오류 메시지 반환
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    isEmailVerified = true;
                    alert("인증이 성공적으로 완료되었습니다.");
                    document.getElementById('email').readOnly = true;
                    verificationCodeGroup.style.display = 'none'; // 인증 코드 입력창 숨기기
                    clearInterval(timerInterval); // 타이머 종료

                } else {
                    alert(data.error || "인증 코드가 올바르지 않습니다. 다시 시도해주세요.");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('인증 코드 확인 중 문제가 발생했습니다. 다시 시도해주세요.');
            });
    });

    // 타이머 시작 함수
    function startTimer() {
        clearInterval(timerInterval); // 기존 타이머 초기화
        let remainingTime = verificationTimeout;

        timerInterval = setInterval(() => {
            remainingTime -= 1000;
            if (remainingTime <= 0) {
                clearInterval(timerInterval);
                timerElement.textContent = "인증 시간이 초과되었습니다.";
                authButton.disabled = true; // 인증 버튼 비활성화
            } else {
                const minutes = Math.floor(remainingTime / 60000);
                const seconds = Math.floor((remainingTime % 60000) / 1000);
                timerElement.textContent = `남은 시간: ${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
            }
        }, 1000);
    }
});
