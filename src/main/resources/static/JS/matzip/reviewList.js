document.addEventListener('DOMContentLoaded', async () => {
    const mainElement = document.querySelector('main');
    const memberId = mainElement ? mainElement.dataset.memberId : null;

    await loadReviewList(memberId);
    displayProfile()
});

function displayProfile(data) {
    const friendProfile = document.querySelector('.friend-profile');
    const imgUrl = profile ? '/upload/' + profile.filename : '/IMG/member/default-profile-img.png';

    friendProfile.innerHTML = `
       <img src="${imgUrl}" alt="프로필 이미지">
   `;
}

async function loadReviewList(memberId) {
    try {
        const response = await fetch(`/matzip/api/reviews/${memberId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        displayReviews(data);

    } catch (error) {
        console.error('데이터 로드 중 오류 발생:', error);
        console.error('오류 세부 정보:', error.message);
        alert('리뷰 데이터를 불러오는데 실패했습니다.');
    }
}

function displayReviews(reviewsData) {
    console.log("Received data structure:", reviewsData);

    reviewsData.sort((a, b) => {
        return new Date(b.regdate) - new Date(a.regdate);
    });

    const reviewsContainer = document.querySelector('.reviews-container');
    reviewsContainer.innerHTML = '';

    reviewsData.forEach(data => {
        const reviewCard = document.createElement('div');
        reviewCard.classList.add('review-item');

        reviewCard.dataset.reviewData = JSON.stringify({
            regdate: data.regdate,
            matzipName: data.matzipName,
            starRating: data.starRating
        });


        const reviewId = data.id;
        reviewCard.dataset.reviewId = reviewId;
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
                            <button class="detail-btn" data-review-id="${reviewId}">
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
        detailBtn.addEventListener('click', () => {
            const reviewId = detailBtn.getAttribute('data-review-id')
            console.log('Clicked review ID:', reviewId);

            if (!reviewId) {
                console.error("Review ID is missing");
                return;
            }

            const detailSection = reviewCard.querySelector('.review-detail');
            if (!detailSection) {
                loadReviewDetail(reviewCard, reviewId);
            } else {
                detailSection.classList.toggle('show');
                detailBtn.innerHTML = detailSection.classList.contains('show') ?
                    '<i class="fa-solid fa-minus"></i>상세접기' :
                    '<i class="fa-solid fa-plus"></i>상세보기';
            }
        });

        const deleteBtn = reviewCard.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', async () => {
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
        });

        reviewsContainer.appendChild(reviewCard);
    });

    document.querySelectorAll('.sort-option').forEach(option => {
        option.addEventListener('click', () => {

            document.querySelectorAll('.sort-option').forEach(opt =>
                opt.classList.remove('active'));
            option.classList.add('active');

            const sortType = option.dataset.sort;
            const reviewSection = option.closest('.review-list-section');
            const reviewsContainer = reviewSection.querySelector('.reviews-container');
            const reviews = Array.from(reviewsContainer.children);

            reviews.sort((a, b) => {
                const dataA = JSON.parse(a.dataset.reviewData);
                const dataB = JSON.parse(b.dataset.reviewData);

                switch (sortType) {
                    case 'date':
                        return new Date(dataB.regdate) - new Date(dataA.regdate);
                    case 'name':
                        return dataA.matzipName.localeCompare(dataB.matzipName);
                    case 'rating':
                        return dataB.starRating - dataA.starRating;
                    default:
                        return 0;
                }
            });

            reviewsContainer.innerHTML = '';
            reviews.forEach(review => reviewsContainer.appendChild(review));
        });
    });
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
