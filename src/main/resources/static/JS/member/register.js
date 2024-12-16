document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM Content Loaded');

    const verifyButton = document.getElementById('verifyButton');
    const authButton = document.getElementById('authButton');
    const verificationCodeGroup = document.getElementById('verificationCodeGroup');
    const timerElement = document.getElementById('timer');
    let timerInterval = null;
    let verificationTimeout = 3 * 60 * 1000;  // 3분
    let isEmailVerified = false;

    const form = document.querySelector('form');
    console.log('Form found:', form);

    if(form) {
        form.addEventListener('submit', function(event) {
            console.log('Form submit triggered');
            console.log('Email verified status:', isEmailVerified);

            event.preventDefault();  // 항상 기본 동작을 막고

            if (!isEmailVerified) {
                console.log('Email not verified, preventing form submission');
                alert('이메일 인증이 필요합니다.');
                return;
            }

            console.log('Email verified, submitting form');
            form.submit();  // 인증되었을 때만 수동으로 submit
        });
        console.log('Submit event listener added');
    }

    verifyButton.addEventListener('click', function(event) {
        event.stopPropagation();
        event.preventDefault();
        const email = document.getElementById('email').value;

        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }

        console.log('이메일 인증 요청');

        fetch('/member/sendEmail?email=' + encodeURIComponent(email))
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('서버에서 오류가 발생했습니다.');
                }
            })
            .then(data => {
                if (data.message) {
                    alert(data.message);
                    verificationCodeGroup.style.display = 'block';
                    startTimer();
                } else if (data.error) {
                    alert(data.error);
                }
            })
            .catch(error => {
                alert('에러 발생: ' + error.message);
            });
    });

    authButton.addEventListener('click', function(event) {
        event.preventDefault();
        event.stopPropagation();

        const email = document.getElementById('email').value;
        const code = document.getElementById('verificationCode').value;

        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }

        if (!code) {
            alert("인증 코드를 입력해주세요.");
            return;
        }

        console.log('인증 코드 확인');

        fetch('/member/verify-code?email=' + encodeURIComponent(email) + '&code=' + encodeURIComponent(code), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(response => {
                if (!response.ok) {
                    alert('서버 응답이 올바르지 않습니다.');
                    return Promise.reject('서버 응답이 올바르지 않습니다.');
                }
                return response.json();
            })
            .then(data => {
                if (data && typeof data.success !== 'undefined') {
                    if (data.success) {
                        isEmailVerified = true;
                        console.log('Email verification successful. Status set to:', isEmailVerified);
                        alert("인증이 성공적으로 완료되었습니다.");
                        document.getElementById('email').readOnly = true;
                        document.getElementById('verificationCodeGroup').style.display='none';
                        clearInterval(timerInterval);
                    } else {
                        alert("인증 코드가 잘못되었습니다. 다시 시도해주세요.");
                    }
                } else {
                    alert("서버 응답 형식이 올바르지 않습니다.");
                }
            })
            .catch(error => {
                alert('에러 발생: ' + error);
            });
    });

    function startTimer() {
        clearInterval(timerInterval);
        let remainingTime = verificationTimeout;

        timerInterval = setInterval(() => {
            remainingTime -= 1000;
            if (remainingTime <= 0) {
                clearInterval(timerInterval);
                timerElement.textContent = "인증 시간이 초과되었습니다.";
                authButton.disabled = true;
            } else {
                const minutes = Math.floor(remainingTime / 60000);
                const seconds = Math.floor((remainingTime % 60000) / 1000);
                timerElement.textContent = `남은 시간: ${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
            }
        }, 1000);
    }
});