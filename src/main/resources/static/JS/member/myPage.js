// document.addEventListener('DOMContentLoaded', () => {
//     // 하트 버튼 토글 기능
//     const heartButtons = document.querySelectorAll('.heart-button');
//
//     function toggleHeart(element) {
//         element.classList.toggle('clicked');
//     }
// });

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





// // 페이지 로드 시 실행
// document.addEventListener("DOMContentLoaded", () => {
//     const memberId = getMemberIdFromURL(); // URL에서 memberId 가져오는 함수
//
//     fetch(`/members/${memberId}`)
//         .then(response => response.json())
//         .then(data => {
//             // 각 섹션에 데이터 채우기
//             renderWishList(data.wishInfo.preview);
//             renderMyMatzipList(data.matzipInfo.preview);
//             renderMyReviewList(data.reviewInfo.preview);
//         })
//         .catch(error => console.error('데이터 가져오기 실패:', error));
// });
//
// // URL에서 memberId 가져오는 함수
// function getMemberIdFromURL() {
//     const pathSegments = window.location.pathname.split('/');
//     return pathSegments[pathSegments.length - 1]; // URL 맨 끝의 값
// }
//
// // 위시 리스트 렌더링
// function renderWishList(wishPreview) {
//     const container = document.getElementById('wish-list-cards');
//     // container.innerHTML = ''; // 기존 내용 초기화
//
//     wishPreview.forEach(wish => {
//         const card = document.createElement('div');
//         card.classList.add('card');
//
//         card.innerHTML = `
//             <img src="${wish.matzipImage || '/default-image.png'}" class="matzipImage" alt="Wish Image">
//             <div class="matzipName">${wish.matzipName}</div>
//         `;
//         container.appendChild(card);
//     });
// }
//
// // 나의 맛집 리스트 렌더링
// function renderMyMatzipList(matzipPreview) {
//     const container = document.getElementById('my-matzip-cards');
//     // container.innerHTML = ''; // 기존 내용 초기화
//
//     matzipPreview.forEach(matzip => {
//         const card = document.createElement('div');
//         card.classList.add('card');
//
//         card.innerHTML = `
//             <img src="${matzip.matzipImage || '/default-image.png'}" class="matzipImage" alt="Matzip Image">
//             <div class="matzipName">${matzip.matzipName}</div>
//         `;
//         container.appendChild(card);
//     });
// }
//
// // 나의 리뷰 리스트 렌더링
// function renderMyReviewList(reviewPreview) {
//     const container = document.getElementById('review-container');
//     // container.innerHTML = ''; // 기존 내용 초기화
//
//     reviewPreview.forEach(review => {
//         const card = document.createElement('div');
//         card.classList.add('review-card');
//
//         card.innerHTML = `
//             <img src="${review.matzipImage || '/IMG/defaultStoreImgSMALL.png'}" class="matzipImage" alt="Review Image">
//             <div class="starRating">${generateStarRating(review.rating)}</div>
//             <div class="matzipName">${review.matzipName}</div>
//             <div class="reviewContent">${review.reviewContent}</div>
//         `;
//         container.appendChild(card);
//     });
// }
//
// // 별점 생성 함수
// function generateStarRating(rating) {
//     let stars = '';
//     for (let i = 1; i <= 5; i++) {
//         stars += i <= rating ? '★' : '☆';
//     }
//     return `<span style="color: gold;">${stars}</span>`;
// }
