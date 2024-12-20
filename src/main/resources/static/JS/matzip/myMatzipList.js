document.addEventListener('DOMContentLoaded', async () => {
    await loadReviewList();
});

async function loadReviewList () {
    const memberId = document.body.dataset.memberId;
    try {
        const response = await fetch(`/matzip/api/reviewList/${memberId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const reviews = await response.json();
        displayReviews(reviews);
        updateReviewCount(reviews.length);

    } catch (error) {
        console.error('Error exists to load data:', error);
        alert('Failed to load data.');
    }
}

function displayReviews (reviews) {
    return reviewsContainer = document.querySelector('.reviews-container');
    reviewsContainer.innerHTML = '';

    reviews.forEach(data => {
        const reviewCard = createReviewCard(data);
        reviewsContainer.appendChild(reviewCard);
    });
}

function createReviewCard (data) {
    const reviewCard = document.createElement('div');
    reviewCard.classList.add('review-item');
    reviewCard.dataset.reviewId = data.review.id;

    reviewCard.innerHTML = `
        <div class="review-image">
            <img src="${data.kakaoImgUrl || '/img/default-restaurant.jpg'}" alt="맛집 이미지">
        </div>
        <div class="review-content">
            <h3 class="store-name">${data.matzipName}</h3>
            <p class="store-address">${data.matzipAddress}</p>
            <div class="review-buttons">
                <button class="detail-btn">
                    <i class="fa-solid fa-plus"></i>
                    상세보기
                </button>
                <div class="visibility-controls">
                    <button class="visibility-btn public ${data.review.visibility === 'PUBLIC' ? 'active' : ''}">공개</button>
                    <button class="visibility-btn hide ${data.review.visibility === 'HIDDEN' ? 'active' : ''}">히든</button>
                    <button class="visibility-btn private ${data.review.visibility === 'PRIVATE' ? 'active' : ''}">비공개</button>
                </div>
                <button class="delete-btn">
                    <i class="fas fa-trash"></i>
                    삭제하기
                </button>
            </div>
        </div>
    `;

    attachEventListeners(reviewCard);

    return reviewCard
}

function attachEventListeners(reviewCard) {
    const detailBtn = reviewCard.querySelector('.detail-btn');
    detailBtn.addEventListener('click', async () => {
        const reviewId = reviewCard.dataset.reviewId;
        const detailSection = reviewCard.querySelector('.review-detail');

        if (!detailSection) {
            await loadReviewDetail(reviewCard, reviewId);
        } else {
            detailSection.classList.toggle('show');
            updateDetailButtonText(detailBtn, detailSection.classList.contains('show'));
        }
    });

    const visibilityButtons = reviewCard.querySelectorAll('.visibility-btn');
    visibilityButtons.forEach(button => {
        button.addEventListener('click', () => handleVisibilityChange(button, reviewCard.dataset.reviewId));
    });

    const deleteBtn = reviewCard.querySelector('.delete-btn');
    detailBtn.addEventListener('click', () => handleReviewDelete(reviewCard.dataset.reviewId, reviewCard));
}

async function loadReviewDetail (reviewCard, reviewId) {
    try {
        const response = await fetch(`/matzip/reviews/${reviewId}/detail`);
        if (!response.ok) {
            throw new Error('Failed to load review details');
        }

        const detailData = await response.json();

        const detailSection = document.createElement('div');
        detailSection.classList.add('review-detail', 'show');
        detailSection.innerHTML = `
            <div class="detail-content">
                    <p class="review-text">${detailData.content}</p>
                    <div class="review-meta">
                        <span class="review-date">${formatDate(detailData.createdAt)}</span>
                        <span class="review-tags">${detailData.tags}</span>
                    </div>
            </div>
        `;

        const contentDiv = reviewCard.querySelector('.review-content');
        contentDiv.appendChild(detailSection);

        const detailBtn = reviewCard.querySelector('.detail-btn');
        updateDetailButtonText(detailBtn, true);

    } catch (error) {
        console.error('Failed to load detail:', error);
        alert('Failed to load details.');
    }
}

async function handleVisibilityChange(button, reviewId) {
    const controls = button.closest('.visibility-controls');
    const allButtons = controls.querySelectorAll('.visibility-btn');

    let visibility;
    if (button.classList.contains('public')) visibility = 'PUBLIC';
    else if (button.classList.contains('hide')) visibility = 'HIDDEN';
    else visibility = 'PRIVATE';

    try {
        const response = await fetch(`/matzip/api/reviews/${reviewId}/visibility`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ visibility })
        });

        if (response.ok) {
            allButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
        } else {
            throw new Error('상태 업데이트 실패');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('상태 업데이트에 실패했습니다.');
    }
}

async function handleReviewDelete(reviewId, reviewCard) {
    if (confirm('정말로 이 리뷰를 삭제하시겠습니까?')) {
        try {
            const response = await fetch(`/matzip/api/reviews/${reviewId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                reviewCard.remove();
                updateReviewCount(document.querySelectorAll('.review-item').length);
                alert('리뷰가 삭제되었습니다.');
            } else {
                throw new Error('삭제 실패');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('리뷰 삭제에 실패했습니다.');
        }
    }
}

function updateDetailButtonText(button, isShow) {
    button.innerHTML = isShow ?
        '<i class="fa-solid fa-minus"></i>접기' :
        '<i class="fa-solid fa-plus"></i>상세보기';
}

function updateReviewCount(count) {
    const countElement = document.querySelector('.review-filter span:nth-child(2)');
    if (countElement) {
        countElement.textContent = `${count}개`;
    }
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}