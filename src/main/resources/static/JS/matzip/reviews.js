document.addEventListener('DOMContentLoaded', () => {
    initializeStarRating();
    loadFoodKinds();
    loadTags();
    setupFormSubmission();
    setupRegistrationButtons();
    setupFoodKindTextHeight();
});

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
        const response = await fetch('/matzip/reviews/food-kinds');
        if(!response.ok) {
            throw new Error('Failed to fetch food kinds');
        }
        const foodKinds = await response.json();

        const foodKindContainer = document.querySelector('.select-foodKind');
        foodKindContainer.innerHTML = '';

        foodKinds.forEach(kindName => {
            const button = document.createElement('button');
            button.type = 'button';
            button.textContent = kindName;
            button.classList.add('food-kind-btn');
            button.dataset.kindName = kindName;

            button.addEventListener('click', () => {

                foodKindContainer.querySelectorAll('.food-kind-btn').forEach(btn => {
                    btn.classList.remove('selected');
                });

                button.classList.add('selected');
                document.querySelector('input[name="foodKind"]').value = kindName;
            });

            foodKindContainer.appendChild(button);
        });
    } catch (error) {
        console.error('Failed to load Food Kind:', error);
    }
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
            button.textContent = tag.name;
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

function setupFormSubmission() {
    const form = document.querySelector('.review-write-form');
    form.addEventListener('submit', (e) => {
        const foodKind = document.querySelector('input[name="foodKind"]').value;
        if(!foodKind) {
            alert('음식 종류를 선택해주세요.');
            e.preventDefault();
            return;
        }

        const selectedTags = document.querySelectorAll('.tag-btn.selected');
        if(selectedTags.length < 3) {
            alert('최소 3개 이상의 태그를 선택해주세요.');
            e.preventDefault();
            return;
        }

        if(selectedTags.length > 5) {
            alert('최대 5개의 태그를 선택할 수 있습니다.');
            e.preventDefault();
            return;
        }

        const isRegistered = document.querySelector('.register-btn input[data-name="registerOk"].active');
        const visibilityButton = document.querySelector('.visibility-btn input.active');

        if (isRegistered && !visibilityButton) {
            alert('맛집 공개 설정을 선택해주세요.');
            e.preventDefault();
            return;
        }
    });
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