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

    fetch('review/')
        .then(response => response.json())
        .then(data => {
            data.forEach(foodKind => {
                const button = document.createElement('input');
                button.type = 'button';
                button.value = foodKind.kind_name;
                button.setAttribute('data-kind-id', matzip)
            })
        })

    async function loadTags() {
        try {
            const response = await fetch('http://localhost:8080/review/');
            if (!response.ok) {
                throw new Error(`Failed to fetch tags: ${response.statusText}`);
            }
            const tags = await response.json();

            const tagsContainer = document.querySelector('.tag-box');
            tags.forEach(tag => {
                const tagButton = document.createElement('input');
                tagButton.type = 'button';
                tagButton.value = tag.name;
                tagButton.classList.add('tag');
                tagsContainer.appendChild(tagButton);
            });
        } catch (error) {
            console.error(error);
        }
    }
    loadTags();
});