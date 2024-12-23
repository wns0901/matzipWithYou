document.addEventListener('DOMContentLoaded', () => {
    initializeStarRating();
    loadFoodKinds();
    loadTags();
    setupFormSubmission();
    setupRegistrationButtons();
    setupFoodKindTextHeight();
    const matzipId = document.querySelector('input[name="matzipId"]').value;
    loadStoreInfo(matzipId);
});

async function loadStoreInfo(matzipId) {
    const memberId = document.querySelector('input[name="memberId"]').value;
    const response = await fetch(`/matzip/api/store/${memberId}/${matzipId}`);

    const data = await response.json();

    const storeContainer = document.querySelector('.store-container');
    const imgUrl = data.kakaoImgUrl || "/IMG/defaultStoreImg.png";

    storeContainer.innerHTML = `
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
            </div>
        </div>
    `;
}

function initializeStarRating() {
    const starContainer = document.querySelector(".star-rating");
    const ratingValue = document.querySelector(".rating-value");
    let selectedStar = 1;

    updateStarUI(selectedStar);

    document.querySelectorAll('.star').forEach(($star) => {
        $star.addEventListener('click', ({target}) => {
            selectedStar = Number(target.dataset.value);
            updateStarUI(selectedStar);
            ratingValue.textContent = selectedStar;
        });
    });

    function updateStarUI(starCount) {
        const stars = document.querySelectorAll(".star");
        stars.forEach((star, index) => {
            star.classList.toggle("selected", index < starCount);
            star.classList.toggle("active", index + 1 === starCount);
        });
    }
}


async function loadFoodKinds() {
    try {
        const matzipId = document.querySelector('input[name="matzipId"]')?.value;
        const response = await fetch(`/matzips?matzipId=${matzipId}`);
        const existingMatzip = await response.json();

        const foodKindsResponse = await fetch('/matzips/food-kinds');
        const foodKinds = await foodKindsResponse.json();

        const foodKindContainer = document.querySelector('.select-foodKind');
        foodKindContainer.innerHTML = '';
        const fragment = document.createDocumentFragment();

        // 맛집의 기존 foodKind가 있는 경우
        if (existingMatzip && existingMatzip.kindId) {
            const foodKind = foodKinds.find(foodKind => foodKind.id === existingMatzip.kindId);
            if (foodKind) {
                fragment.appendChild(createFoodKindButton(foodKind, true));
            }
        } else {
            // 첫 리뷰인 경우 모든 foodKind 옵션을 보여줌
            foodKinds.forEach(foodKind => {
                fragment.appendChild(createFoodKindButton(foodKind));
            });
        }

        foodKindContainer.appendChild(fragment);
    } catch (error) {
        console.error('Failed to load Food Kind:', error);
    }
}

function createFoodKindButton(foodKind, isExist = false) {
    const button = document.createElement('button');
    button.type = 'button';
    button.textContent = foodKind.kindName;
    button.classList.add('food-kind-btn');
    button.dataset.foodKindId = foodKind.id;

    const hiddenInput = document.querySelector('input[name="foodKind"]');

    if (isExist) {
        // 기존 리뷰가 있는 경우 - 선택된 상태로 설정하고 수정 불가능하게 만듦
        hiddenInput.value = foodKind.kindName;
        hiddenInput.dataset.foodKindId = foodKind.id;
        button.classList.add('selected');
        button.disabled = true;
    } else {
        // 첫 리뷰인 경우 - 선택 가능하게 설정
        button.addEventListener('click', () => {
            const foodKindContainer = document.querySelector('.select-foodKind');
            foodKindContainer.querySelectorAll('.food-kind-btn').forEach(btn => {
                btn.classList.remove('selected');
            });

            button.classList.add('selected');
            hiddenInput.value = foodKind.kindName;
            hiddenInput.dataset.foodKindId = foodKind.id;
        });
    }

    return button;
}

async function loadTags() {
    try {
        const response = await fetch('/matzip/reviews/tags');
        const tags = await response.json();

        const tagContainer = document.querySelector('.tag-container');
        tagContainer.innerHTML = '';

        const selectedTagIds = [];

        tags.forEach(tag => {
            const button = document.createElement('button');
            button.type = 'button';
            button.textContent = tag.tagName;
            button.classList.add('tag-btn');
            button.dataset.tagId = tag.id;

            button.addEventListener('click', () => {
                button.classList.toggle('selected');

                const tagId = tag.id;
                const index = selectedTagIds.indexOf(tagId);

                if (index > -1) {
                    selectedTagIds.splice(index, 1);
                } else {
                    selectedTagIds.push(tagId);
                }

                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = 'tagIds';
                hiddenInput.value = selectedTagIds.join(',');

                document.querySelectorAll('input[name="tagIds"]').forEach(el => el.remove());
                document.querySelector('form').appendChild(hiddenInput);
            });
            tagContainer.appendChild(button);
        });
    } catch (error) {
        console.error('Failed to load Tag:', error);
    }
}

async function saveMyMatzip(url, formObject) {
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(formObject),
    }
    console.log(formObject);
    const result = await fetch(url, options).then(res => res.json());
    showCompletionModal(result);
}

async function setupFormSubmission() {
    const form = document.querySelector('.review-write-form');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const starRating = document.querySelector('.rating-value').textContent;
        const memberId = document.querySelector('input[name="memberId"]')?.value;
        const matzipId = document.querySelector('input[name="matzipId"]')?.value;
        const foodKind = document.querySelector('input[name="foodKind"]')?.value;
        const foodKindId = document.querySelector('input[name="foodKind"]')?.dataset.foodKindId; // foodKindId 가져오기
        const content = document.querySelector('textarea[name="content"]')?.value;
        const selectedTags = Array.from(document.querySelectorAll('.tag-btn.selected'));
        const tagIds = document.querySelector('input[name="tagIds"]')?.value.split(',').filter(Boolean);
        const registerBtn = document.querySelector('input[data-name="registerOk"]');
        const unregisterBtn = document.querySelector('input[data-name="Unregister"]');

        if (!registerBtn.classList.contains('active') && !unregisterBtn.classList.contains('active')) {
            alert('등록 여부를 선택해주세요.');
            return;
        }

        if (!memberId) {
            alert('로그인이 필요합니다.');
            window.location.href = '/member/login';
            return;
        }

        if(!foodKind || !foodKindId) {  // foodKindId 체크 추가
            alert('음식 종류를 선택해주세요.');
            return;
        }

        if(selectedTags.length < 3) {
            alert('최소 3개 이상의 태그를 선택해주세요.');
            return;
        }

        if(selectedTags.length > 5) {
            alert('최대 5개의 태그를 선택할 수 있습니다.');
            return;
        }

        if(!content) {
            alert('리뷰를 작성해주세요');
            return;
        }

        const formObject = {
            content: content,
            starRating: starRating,
            matzipId: Number(matzipId),
            memberId: Number(memberId),
            kindName: foodKind,
            foodKindId: Number(foodKindId),  // foodKindId 추가
            tagIds: tagIds
        };

        const isRegistered = document.querySelector('.register-btn input[data-name="registerOk"].active');

        const url = isRegistered
            ? `/matzips/mine/${memberId}/${matzipId}`
            : `/matzip/reviews/${memberId}/${matzipId}`;

        if(isRegistered) {
            formObject.kindId = foodKindId;
            formObject.visibility = document.querySelector('.visibility-btn .active.selected').dataset.name;
            saveMyMatzip(url, formObject)
        } else {
            await saveReview(memberId, matzipId, formObject, url);
        }

    });
}

async function saveReview(memberId, matzipId, formObject, url) {
    try {
        const saveResponse = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(formObject),
        });
    } catch (error) {
        console.error('Error:', error);
        alert('리뷰 저장 중 오류가 발생했습니다.');
    }

    const modalResponse = await fetch(`/matzip/api/reviews/${memberId}/${matzipId}/modal`);
    const isRegistered = document.querySelector('.register-btn input[data-name="registerOk"].active');
    const modalData = await modalResponse.json();
    showCompletionModal(modalData);

    document.getElementById('point-btn').onclick = () => {
        const modal = document.getElementById('completionModal');
        modal.classList.add('hidden');
        window.location.href = isRegistered
            ? `/matzip/myMatzipList/${memberId}`
            : `/matzip/reviewList/${memberId}`;
    };
}

function showCompletionModal(data) {
    const point = document.getElementById('point-btn');
    const modal = document.getElementById('completionModal');
    const messageContainer = document.getElementById('messageContainer');
    const friendsContainer = document.getElementById('friendProfiles');

    let messages = [];

    if (data.topFriendName && data.friendCount > 0) {
        const unlockMessage = `${data.topFriendName}외 ${data.friendCount}명의 hidden 맛집을 unlock 했습니다!`;
        messages.push(
            `<p class="unlock-message">
                <i class="fas fa-unlock"></i> ${unlockMessage}
            </p>`
        );
    } else {
        const unlockMessage = `리뷰를 등록했습니다.<br>${data.rewardPoints}포인트가 지급됩니다.`
        messages.push(
            `<p class="unlock-message">
                <i class="fa-solid fa-envelope-circle-check"></i> ${unlockMessage}
            </p>`
        );
    }

    if (data.friendCount > 0) {
        const intimacyMessage = `${data.topFriendName}${data.friendCount > 0 ? `외 ${data.friendCount}명` : ''}의 
            친밀도가 +${data.intimacyIncrease} 되었습니다!`;
        messages.push(
            `<p class="intimacy-message">
                <i class="fas fa-heart"></i> ${intimacyMessage}
            </p>`
        );
    }

    point.innerHTML = `
        <i class="fa-solid fa-plus"></i> ${data.rewardPoints}pt
    `;

    messageContainer.innerHTML = `
        <div class="message-box">
            ${messages.join('')}
        </div>
    `;

    friendsContainer.innerHTML = data.hiddenFriends
        .map(friend => `
            <div class="friend-profile">
                <img src="${friend.profileImg ? '/upload/' + friend.profileImg : '/images/default-profile.png'}" 
                     alt="${friend.nickname}">
            </div>
        `)
        .join('');

    modal.classList.remove('hidden');
}

function setupRegistrationButtons() {
    const registerButtons = document.querySelectorAll('.register-btn input');
    const visibilityButtons = document.querySelectorAll('.visibility-btn input');
    const visibilityContainer = document.querySelector('.matzip-visibility');

    visibilityButtons.forEach(btn => {
        btn.disabled = true;
        btn.classList.add('disabled');
    });
    visibilityContainer.style.pointerEvents = 'none';
    visibilityContainer.style.opacity = '0.5';

    registerButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            registerButtons.forEach(b => b.classList.remove('active', 'selected'));
            btn.classList.add('active');
            btn.classList.add('selected')

            const isRegistered = btn.getAttribute('data-name') === 'registerOk';

            if (isRegistered) {
                visibilityButtons.forEach(visibilityBtn => {
                    visibilityBtn.disabled = false;
                    visibilityBtn.classList.remove('disabled');
                });
                visibilityContainer.style.pointerEvents = 'auto';
                visibilityContainer.style.opacity = '1';
            } else {
                visibilityButtons.forEach(visibilityBtn => {
                    visibilityBtn.disabled = true;
                    visibilityBtn.classList.add('disabled');
                    visibilityBtn.classList.remove('active');
                });
                visibilityContainer.style.pointerEvents = 'none';
                visibilityContainer.style.opacity = '0.5';
            }
        });
    });

    visibilityButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            visibilityButtons.forEach(b => b.classList.remove('active', 'selected'));

            btn.classList.add('active');
            btn.classList.add('selected');
        })
    })
}

function setupFoodKindTextHeight() {
    function adjustHeight() {
        const selectFoodKind = document.querySelector('.select-foodKind');
        const foodKindText = document.querySelector('.food-kind-text');

        if (selectFoodKind && foodKindText) {
            const selectHeight = selectFoodKind.offsetHeight;
            foodKindText.style.height = `${selectHeight}px`;
            foodKindText.style.display = 'flex';
            foodKindText.style.alignItems = 'center';
        }
    }

    adjustHeight();

    window.addEventListener('resize', adjustHeight);

    const selectFoodKind = document.querySelector('.select-foodKind');
    if (selectFoodKind) {
        const observer = new MutationObserver(adjustHeight);
        observer.observe(selectFoodKind, {
            childList: true,
            subtree: true
        });
    }
}

