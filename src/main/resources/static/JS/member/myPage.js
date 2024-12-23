// 설정버튼 드롭다운
document.querySelector('.settings-button').addEventListener('click', function () {
    const dropdown = this.nextElementSibling;
    dropdown.classList.toggle('show');
});

// 드롭다운 외부 클릭시 닫기
window.addEventListener('click', function (event) {
    if (!event.target.closest('.settings-container')) {
        const dropdowns = document.querySelectorAll('.dropdown-menu');
        dropdowns.forEach(dropdown => {
            dropdown.classList.remove('show');
        });
    }
});

// 친구 버튼 기능
document.addEventListener('DOMContentLoaded', function () {
    if (window.location.pathname.match(/^\/members\/\d+$/)) {
        const friendButton = document.querySelector('.info-button .info-label.friends');
        if (!friendButton) {
            console.error('친구 버튼을 찾을 수 없습니다');
            return;
        }

        friendButton.addEventListener('click', function () {
            const memberId = this.getAttribute('data-member-id');
            if (!memberId) {
                console.error('회원 ID를 찾을 수 없습니다');
                return;
            }
            window.location.href = `/members/${memberId}/friends`;
        });
    }
});

// 닉네임 변경 및 회원 탈퇴 기능
document.addEventListener('DOMContentLoaded', function () {
    const overlay = document.getElementById('overlay');
    const editButton = document.getElementById('editButton');
    const nicknamePopup = document.getElementById('nicknamePopup');
    const cancelButton = document.getElementById('cancelButton');
    const confirmButton = document.getElementById('confirmButton');
    const errorMessage = document.getElementById('errorMessage');
    const newNicknameInput = document.getElementById('newNickname');

    // 회원 탈퇴 관련 요소
    const deleteAccountBtn = document.getElementById('deleteAccountBtn');
    const deleteAccountPopup = document.getElementById('deleteAccountPopup');
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');
    // 회원 탈퇴 기능
    if (deleteAccountBtn && deleteAccountPopup && confirmDeleteBtn && cancelDeleteBtn) {
        deleteAccountBtn.addEventListener('click', function () {
            deleteAccountPopup.style.display = 'block';
            overlay.style.display = 'block';
        });

        cancelDeleteBtn.addEventListener('click', function () {
            deleteAccountPopup.style.display = 'none';
            overlay.style.display = 'none';
        });

        confirmDeleteBtn.addEventListener('click', function () {
            const memberId = parseInt(window.location.pathname.split('/').pop());
            if (isNaN(memberId)) {
                console.error('유효하지 않은 회원 ID입니다.');
                return;
            }

            fetch(`/member/${memberId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert('회원 탈퇴가 완료되었습니다.');
                        window.location.href = '/';
                    } else {
                        throw new Error('회원 탈퇴 처리 중 오류가 발생했습니다.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error.message);
                    deleteAccountPopup.style.display = 'none';
                    overlay.style.display = 'none';
                });
        });
    }

    // 닉네임 수정 기능
    editButton.addEventListener('click', function () {
        nicknamePopup.style.display = 'block';
        errorMessage.textContent = '';
        overlay.style.display = 'block';
    });

    cancelButton.addEventListener('click', function () {
        nicknamePopup.style.display = 'none';
        overlay.style.display = 'none';
    });

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

        const memberId = parseInt(window.location.pathname.split('/').pop());
        fetch(`/members/${memberId}`, {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({newNickname})
        })
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    errorMessage.textContent = data.error;
                } else {
                    document.querySelector('.nickname-text').textContent = newNickname;
                    nicknamePopup.style.display = 'none';
                    overlay.style.display = 'none';
                }
            })
            .catch(error => {
                errorMessage.textContent = '서버와의 통신 중 오류가 발생했습니다.';
                console.error('Error:', error);
            });
    });

    // 별점 렌더링 함수
    function renderStarRating() {
        const reviewCards = document.querySelectorAll(".review-card .starRating");

        reviewCards.forEach(starRatingElement => {
            const ratingText = starRatingElement.closest(".review-card").querySelector(".ratingText");
            const rating = parseInt(starRatingElement.getAttribute("data-rating"));
            starRatingElement.innerHTML = "";

            for (let i = 1; i <= 5; i++) {
                const starImg = document.createElement("img");
                starImg.src = i <= rating ? "/IMG/Star-Yellow.png" : "/IMG/Star-Gray.png";
                starImg.alt = "star";
                starImg.style.width = "16px";
                starImg.style.height = "16px";
                starImg.style.marginRight = "4px";
                starRatingElement.appendChild(starImg);
            }
            ratingText.textContent = `${rating}점`;
        });
    }

// 프사 변경 기능

const profileImageInput = document.getElementById("profileImageInput");
const profileImagePreview = document.getElementById("profileImagePreview");
const deleteImageButton = document.getElementById("deleteImageButton");
const memberId = "{memberId}"; // 서버에서 제공한 memberId 바인딩

// 이미지 변경 시 미리보기 업데이트
profileImageInput.addEventListener("change", (event) => {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            profileImagePreview.src = e.target.result;
        };
        reader.readAsDataURL(file);

        // 자동으로 폼 제출
        document.getElementById("profileImageForm").submit();
    }
});

// 이미지 삭제 버튼 클릭 시
deleteImageButton.addEventListener("click", () => {
    if (confirm("정말로 이미지를 삭제하시겠습니까?")) {
        fetch(`/members/${memberId}/profile-img`, { method: "DELETE" })
            .then((response) => {
                if (response.ok) {
                    location.reload(); // 페이지 새로고침
                } else {
                    alert("이미지 삭제에 실패했습니다.");
                }
            });
    }
});
    // 초기 렌더링
    renderStarRating();
});