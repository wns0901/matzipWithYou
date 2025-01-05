document.addEventListener('DOMContentLoaded', async () => {
    const mainElement = document.querySelector('main');
    const memberId = mainElement ? mainElement.dataset.memberId : null;

    await loadReviewList(memberId);
    setupSortOptions();
    displayProfile();
});

function displayProfile(data) {
    const friendProfile = document.querySelector('.friend-profile');
    const imgUrl = profile ? '/upload/' + profile.filename : '/IMG/member/default-profile-img.png';

    friendProfile.innerHTML = `
       <img src="${imgUrl}" alt="프로필 이미지">
   `;
}

let originalReviewData = [];

async function loadReviewList(memberId) {
    try {
        const response = await fetch(`/matzip/api/reviews/${memberId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        originalReviewData = [...data];
        displayReviews(data);

    } catch (error) {
        console.error('데이터 로드 중 오류 발생:', error);
        alert('리뷰 데이터를 불러오는데 실패했습니다.');
    }
}

function displayReviews(reviewsData) {
    const reviewsContainer = document.querySelector('.reviews-container');
    reviewsContainer.innerHTML = ''; // 기존 내용을 제거하여 중복 로딩 방지

    reviewsData.forEach(data => {
        const reviewCard = document.createElement('div');
        reviewCard.classList.add('review-item');

        reviewCard.dataset.reviewId = data.id;
        const imgUrl = data.kakaoImgUrl !== '#none' ? data.kakaoImgUrl : "/IMG/defaultStoreImg.png";

        reviewCard.innerHTML = `
            <div class="review-card">
                <section class="review-card-container">
                    <div class="left-info">
                        <div class="review-image">
                            <img src="${imgUrl}" alt="맛집 이미지">
                        </div>
                    </div>    
                    <div class="right-info">
                        <div class="store-info">
                            <div class="store-details">
                                <h3>${data.matzipName}</h3>
                                <p>${data.matzipAddress}</p>
                            </div>
                            <button class="detail-btn" data-review-id="${data.id}">
                                <i class="fa-solid fa-plus"></i>
                                상세보기
                            </button>
                        </div>
                        <button class="delete-btn">
                            <i class="fas fa-trash"></i>
                            삭제하기
                        </button>
                    </div>
                </section>
                <div class="review-content"></div>
            </div>
        `;

        const detailBtn = reviewCard.querySelector('.detail-btn');
        detailBtn.addEventListener('click', () => toggleDetail(reviewCard, data.id, detailBtn));

        const deleteBtn = reviewCard.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', async () => deleteReview(reviewCard, data.id));

        reviewsContainer.appendChild(reviewCard);
    });
}

function setupSortOptions() {
    document.querySelectorAll('.sort-option').forEach(option => {
        option.addEventListener('click', () => {
            document.querySelectorAll('.sort-option').forEach(opt =>
                opt.classList.remove('active')); // 기존 active 클래스 제거
            option.classList.add('active'); // 클릭한 버튼에 active 추가

            const sortType = option.dataset.sort;
            let sortedReviews = [];

            if (sortType === 'date') {
                sortedReviews = [...originalReviewData].sort((a, b) =>
                    new Date(b.regdate) - new Date(a.regdate)
                );
            } else if (sortType === 'name') {
                sortedReviews = [...originalReviewData].sort((a, b) =>
                    a.matzipName.localeCompare(b.matzipName)
                );
            } else if (sortType === 'rating') {
                sortedReviews = [...originalReviewData].sort((a, b) =>
                    b.starRating - a.starRating
                );
            } else if (sortType === 'default') { // 등록순
                sortedReviews = [...originalReviewData]; // 원본 데이터
            }

            displayReviews(sortedReviews); // 정렬된 데이터 표시
        });
    });
}

function toggleDetail(reviewCard, reviewId, detailBtn) {
    const detailSection = reviewCard.querySelector('.review-detail');
    if (!detailSection) {
        loadReviewDetail(reviewCard, reviewId);
    } else {
        detailSection.classList.toggle('show');
        detailBtn.innerHTML = detailSection.classList.contains('show') ?
            '<i class="fa-solid fa-minus"></i>상세접기' :
            '<i class="fa-solid fa-plus"></i>상세보기';
    }
}

async function deleteReview(reviewCard, reviewId) {
    if (confirm('이 리뷰를 삭제하시겠습니까?')) {
        try {
            const response = await fetch(`/matzip/reviews/delete/${reviewId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || '삭제 실패');
            }

            reviewCard.remove();
            alert('리뷰가 삭제되었습니다.');
        } catch (error) {
            console.error('Error:', error);
            alert(error.message || '리뷰 삭제에 실패했습니다.');
        }
    }
}

async function loadReviewDetail(reviewCard, reviewId) {
    try {
        const response = await fetch(`/matzip/api/reviews/${reviewId}/detail`);
        if (!response.ok) {
            throw new Error('Failed to load review details');
        }

        const detailData = await response.json();

        if (!detailData || Object.keys(detailData).length === 0) {
            throw new Error('No detail data found for the review.');
        }

        const detailSection = document.createElement('div');
        detailSection.classList.add('review-detail', 'show');
        detailSection.innerHTML = `
        <div class="detail-content">
            <p>${detailData.content}</p>
        </div>
        `;

        const starRatingHtml = `
            <div class="star-rating-container">
                <div class="star-rating">
                    ${Array(5).fill(0).map((_, i) => `
                        <div class="star ${i < detailData.starRating ? 'active' : ''}" 
                             data-value="${i + 1}"></div>
                    `).join('')}
                </div>
                <div class="star-rating-text">평점 <span class="rating-value">
                    ${detailData.starRating}</span>점</div>
            </div>
        `;

        const tagsHtml = detailData.reviewTags && detailData.reviewTags.length > 0 ? `
            <div class="tags-container">
                ${detailData.reviewTagName.map(tagName =>
            `<div class="tag">#${tagName}</div>`
        ).join('')}
            </div>
        ` : '';

        detailSection.innerHTML = `
            <div class="detail-content">
                <section class="detail-left-section">
                    <div class="detail-title">
                        <h3>내가 쓴 리뷰</h3>
                    </div>
                    <div class="rating-section">
                        ${starRatingHtml}
                    </div>
                    ${detailData.kindName ? `
                        <div class="food-kind">
                            <span class="food-kind-text">${detailData.kindName}</span>
                        </div>
                    ` : ''}
                    ${tagsHtml}
                </section>
                <section class="detail-right-section">
                    <div class="review-text">
                        <span class="review-text-content">${detailData.content}</span>
                    </div>
                </section>
            </div>
        `;

        const contentDiv = reviewCard.querySelector('.review-content') ||
            reviewCard.appendChild(document.createElement('div'));
        contentDiv.classList.add('review-content');
        contentDiv.appendChild(detailSection);

    } catch (error) {
        console.error('Failed to load review details:', error);
        alert('상세 정보를 불러오는데 실패했습니다.');
    }
}
