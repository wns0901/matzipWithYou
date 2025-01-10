document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('matzipModal');
    const addBtn = document.getElementById('addMatzipBtn');
    const closeBtn = document.querySelector('.close');
    const searchBtn = document.getElementById('search_btn');
    const searchSelectBtn = document.getElementById('search_select_btn');
    const inputKeyword = document.getElementById('keyword');

    // 태그 모달 관련 변수들
    const tagModal = document.getElementById('tagModal');
    const addTagBtn = document.getElementById('addTagBtn');
    const tagForm = document.getElementById('tagForm');
    const closeTagBtn = tagModal.querySelector('.close');

    // 음식 종류 모달 관련 변수들
    const foodKindModal = document.getElementById('foodKindModal');
    const addFoodKindBtn = document.getElementById('addFoodKindBtn');
    const foodKindForm = document.getElementById('foodKindForm');
    const closeFoodKindBtn = foodKindModal.querySelector('.close');

    // 탭 이벤트 리스너
    document.querySelectorAll('.tab-button').forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();

            document.querySelectorAll('.tab-button').forEach(b => b.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

            this.classList.add('active');
            document.getElementById(this.dataset.tab).classList.add('active');

            if (this.dataset.tab !== 'members') {
                loadData(this.dataset.tab);
            }
        });
    });

    let searchResultList;

    const ps = new kakao.maps.services.Places();

    // 모달 관련 이벤트 리스너
    addBtn.onclick = () => {
        modal.style.display = "block";
    };

    closeBtn.onclick = () => {
        modal.style.display = "none";
        clearSearchResults();
    };

    addTagBtn.onclick = () => {
        tagModal.style.display = "block";
    };

    closeTagBtn.onclick = () => {
        tagModal.style.display = "none";
        tagForm.reset();
    };

    addFoodKindBtn.onclick = () => {
        foodKindModal.style.display = "block";
    };

    closeFoodKindBtn.onclick = () => {
        foodKindModal.style.display = "none";
        foodKindForm.reset();
    };

    window.onclick = (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
            clearSearchResults();
        }
        if (event.target === tagModal) {
            tagModal.style.display = "none";
            tagForm.reset();
        }
        if (event.target === foodKindModal) {
            foodKindModal.style.display = "none";
            foodKindForm.reset();
        }
    };

    function loadData(type) {
        fetch(`/admin/api/${type}`)
            .then(response => response.json())
            .then(data => {
                updateTable(type, data);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('데이터를 불러오는데 실패했습니다.');
            });
    }

    function updateTable(type, data) {
        const tbody = document.getElementById(`${type}List`);
        switch (type) {
            case 'matzips':
                tbody.innerHTML = data.map(item => `
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td>${item.address}</td>
                        <td>${item.kindId || '미지정'}</td>
                        <td>
                            <button class="delete-btn" data-id="${item.id}" data-type="matzips">삭제</button>
                        </td>
                    </tr>
                `).join('');
                break;
            case 'tags':
                tbody.innerHTML = data.map(item => `
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.tagName}</td>
                        <td><button class="delete-btn" data-id="${item.id}" data-type="tags">삭제</button></td>
                    </tr>
                `).join('');
                break;
            case 'foodkinds':
                tbody.innerHTML = data.map(item => `
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.kindName}</td>
                        <td><button class="delete-btn" data-id="${item.id}" data-type="foodkinds">삭제</button></td>
                    </tr>
                `).join('');
                break;
        }
        attachDeleteListeners();
    }

    // 검색 기능
    searchBtn.addEventListener('click', searchPlaces);
    inputKeyword.addEventListener('keyup', (e) => {
        if (e.key === 'Enter') searchPlaces();
    });

    function searchPlaces() {
        const keyword = document.getElementById('keyword').value;
        if (!keyword.replace(/^\s+|\s+$/g, '')) {
            alert('키워드를 입력해주세요!');
            return false;
        }
        ps.keywordSearch(keyword, placesSearchCB);
    }

    function placesSearchCB(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            makeResultCards(result);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            alert('검색 결과가 존재하지 않습니다.');
        } else if (status === kakao.maps.services.Status.ERROR) {
            alert('검색 결과 중 오류가 발생했습니다.');
        }
    }

    function makeResultCards(result) {
        const searchWrap = document.getElementById('result_list');
        searchWrap.innerHTML = '';
        searchResultList = result;

        const fragment = document.createDocumentFragment();
        result.forEach((place, i) => {
            const searchCard = document.createElement('div');
            searchCard.className = 'search_card';
            searchCard.dataset.searchIndex = i;
            searchCard.innerHTML = `
                <span class="search_name">${place.place_name}</span><br>
                <span class="search_address">${place.road_address_name}</span>
            `;
            searchCard.onclick = (e) => {
                const selectedList = document.getElementsByClassName('selected');
                Array.from(selectedList).forEach(el => el.classList.remove('selected'));
                e.currentTarget.classList.add('selected');
            };

            fragment.appendChild(searchCard);
        });
        searchWrap.appendChild(fragment);
    }

    searchSelectBtn.onclick = function () {
        const selectedItem = document.querySelector('.search_card.selected');
        if (!selectedItem) return;

        const selectedIndex = Number(selectedItem.dataset.searchIndex);
        const placeData = searchResultList[selectedIndex];
        const matzipData = {
            name: placeData.place_name,
            address: placeData.road_address_name,
            lat: placeData.y,
            lng: placeData.x,
            kakaoMapUrl: placeData.place_url
        };

        fetch('/matzips', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(matzipData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    alert('맛집이 추가되었습니다.');
                    modal.style.display = "none";
                    clearSearchResults();
                    loadData('matzips');
                } else {
                    alert(data.msg || '맛집 추가에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('맛집 추가 중 오류가 발생했습니다.');
            });
    };

    function clearSearchResults() {
        document.getElementById('keyword').value = '';
        document.getElementById('result_list').innerHTML = '';
        searchResultList = null;
    }

    function deleteItem(type, id) {
        if (confirm('정말 삭제하시겠습니까?')) {
            fetch(`/admin/api/${type}/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.text())
                .then(result => {
                    if (result === '삭제 성공') {
                        alert('삭제되었습니다.');
                        if (type === 'members') {
                            window.location.reload();
                        } else {
                            loadData(type);
                        }
                    } else {
                        alert(result);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('삭제 중 오류가 발생했습니다.');
                });
        }
    }

    function attachDeleteListeners() {
        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', function () {
                const id = this.dataset.id;
                const type = this.dataset.type;
                if (id && type) {
                    deleteItem(type, id);
                }
            });
        });
    }

    function formatDate(dateArray) {
        if (!Array.isArray(dateArray) || dateArray.length < 3) {
            console.warn('Invalid date array:', dateArray);
            return '';
        }

        const [year, month, day, hour = 0, minute = 0, second = 0] = dateArray;
        const date = new Date(year, month - 1, day, hour, minute, second);

        if (isNaN(date.getTime())) {
            console.warn('Invalid date:', dateArray);
            return '';
        }

        return date.toLocaleDateString('ko-KR');
    }

    // 태그 추가 제출
    tagForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = {
            tagName: document.getElementById('tagName').value
        };

        fetch('/tags/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'SUCCESS') {
                    tagModal.style.display = "none";
                    tagForm.reset();
                    loadData('tags');
                } else {
                    alert('태그 추가에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('태그 추가 중 오류가 발생했습니다.');
            });
    });

    // 음식 종류 추가 제출
    foodKindForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const formData = {
            kindName: document.getElementById('kindName').value
        };

        fetch('/foodkinds/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })
            .then(response => response.text())
            .then(result => {
                if (result === '저장 성공') {
                    foodKindModal.style.display = "none";
                    foodKindForm.reset();
                    loadData('foodkinds');
                } else {
                    alert('음식 종류 추가에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('음식 종류 추가 중 오류가 발생했습니다.');
            });
    });

    // 초기 실행
    attachDeleteListeners();
});