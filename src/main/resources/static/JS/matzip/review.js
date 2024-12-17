document.querySelectorAll('.star').forEach(($star) => {
    $star.addEventListener('click', ({target}) => {
        document.querySelectorAll('.star').forEach(($star) => {
            $star.classList.remove('active');
        });

        target.classList.add('active');
    });
})

document.addEventListener('DOMContentLoaded', () => {
    const foodKindContainer = document.querySelector('.select-foodKind');

    async function loadFoodKinds() {
        try {
            const response = await fetch('http://localhost:8080/matzip/review');
            if (!response.ok) {
                throw new Error(`Failed to fetch food kinds: ${response.statusText}`);
            }
            const foodKinds = await response.json();

            foodKinds.forEach(foodKind => {
                const kindButton = document.createElement('input');
                kindButton.type = 'button';
                kindButton.value = foodKind.name;
                kindButton.setAttribute('data-kind-name', foodKind.name);
                kindButton.classList.add('food-kind-btn');

                kindButton.addEventListener('click', async () => {
                    document.querySelectorAll('.food-kind-btn').forEach(btn => btn.disabled = true);

                    const kindName = kindButton.getAttribute('data-kind-name');
                    console.log(`클릭된 kind_name: ${kindName}`);

                    try {
                        const submitResponse = await fetch('matzip/review', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify({ kindName: kindName }),
                        });

                        if (!submitResponse.ok) {
                            throw new Error(`Failed to submit kind_name: ${submitResponse.statusText}`);
                        }
                        const submitData = await submitResponse.json();
                        console.log('서버 응답:', submitData);
                        alert('Choose successful');
                    } catch (submitError) {
                        console.error('전송 중 에러', submitError);
                        alert('Error please re-try.')
                        document.querySelectorAll('.food-kind-btn').forEach(btn => btn.disabled = false);
                    }
                });
                foodKindContainer.appendChild(kindButton);
            });
        } catch (error) {
            console.error('Error loading food-kind:', error);
            alert('Failed to load food-kind.');
        }
    }

    let selectedTagIds = [];

    async function loadTags() {
        try {
            const response = await fetch('http://localhost:8080/matzip/review');
            if (!response.ok) {
                throw new Error(`Failed to fetch tags: ${response.statusText}`);
            }
            const tags = await response.json();

            const tagContainer = document.querySelector('.tag-box');

            tags.forEach(tag => {
                const tagButton = document.createElement('input');
                tagButton.type = 'button';
                tagButton.value = tag.name;
                tagButton.classList.add('tag');
                tagButton.setAttribute('data-tag-id', tag.id);

                tagButton.addEventListener('click', () => toggleSelection(tagButton, tag.id));

                tagContainer.appendChild(tagButton);
            });
        } catch (error) {
            console.error('Error loading tags:', error);
        }
    }

    function toggleSelection(tagButton, tagId) {
        if (selectedTagIds.includes(tagId)) {
            selectedTagIds = selectedTagIds.filter(id => id !== tagId);
            tagButton.classList.remove('selected');
        } else {
            if (selectedTagIds.length >= 5) {
                alert('최대 5개의 태그만 선택할 수 있습니다.');
                return;
            }
            if (selectedTagIds.length < 3) {
                alert('최소 3개의 태그를 선택해야 합니다.');
                return;
            }
            selectedTagIds.push(tagId);
            tagButton.classList.add('selected');
        }
        console.log('현재 선택된 tagIds:', selectedTagIds);
    }

    async function submitTags() {
        try {
            const response = await fetch('matzip/review', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ tagIds: selectedTagIds }),
            });

            if (!response.ok) {
                throw new Error(`Failed to submit tagIds: ${response.statusText}`);
            }

            const result = await response.json();
            console.log('서버 응답:', result);
            alert('태그가 성공적으로 전송되었습니다!');
        } catch (error) {
            console.error('태그 전송 중 오류 발생:', error);
            alert('태그 전송 중 오류가 발생했습니다.');
        }
    }
    loadFoodKinds();
    loadTags();

    document.querySelector('.submit-button').addEventListener('click', submitTags);
});