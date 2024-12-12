const districtNames = {
    'Gangnam-gu': '강남구',
    'Gangdong-gu': '강동구',
    'Gangbuk-gu': '강북구',
    'Gangseo-gu': '강서구',
    'Gwanak-gu': '관악구',
    'Gwangjin-gu': '광진구',
    'Guro-gu': '구로구',
    'Geumcheon-gu': '금천구',
    'Nowon-gu': '노원구',
    'Dobong-gu': '도봉구',
    'Dongdaemun-gu': '동대문구',
    'Dongjak-gu': '동작구',
    'Mapo-gu': '마포구',
    'Seodaemun-gu': '서대문구',
    'Seocho-gu': '서초구',
    'Seongdong-gu': '성동구',
    'Seongbuk-gu': '성북구',
    'Songpa-gu': '송파구',
    'Yangcheon-gu': '양천구',
    'Yeongdeungpo-gu': '영등포구',
    'Yongsan-gu': '용산구',
    'Eunpyeong-gu': '은평구',
    'Jongno-gu': '종로구',
    'Jung-gu': '중구',
    'Jungnang-gu': '중랑구'
};

let currentData = serverData.toTalData;

function updateMap(data) {
    // 모든 path의 클래스와 기존 텍스트 초기화
    document.querySelectorAll('#seoul-map path').forEach(path => {
        path.style.fill = '#eead84';
    });

    // 기존 텍스트 삭제
    document.querySelectorAll('.district-count').forEach(el => el.remove());

    // 각 구별로 처리
    Object.keys(districtNames).forEach(engName => {
        const korName = districtNames[engName];
        const path = document.getElementById(engName);

        if (path) {
            const publicCount = data.publicGu[korName] || 0;
            const hiddenCount = data.hiddenGu[korName] || 0;

            // bbox를 사용하여 중심점 계산
            const bbox = path.getBBox();
            const centerX = bbox.x + bbox.width / 2;
            const centerY = bbox.y + bbox.height / 2;

            if (hiddenCount > 0) {
                // 히든 맛집이 있으면 빨간색으로 표시하고 전체 합계 표시
                path.style.fill = '#E74C3C';
                if (publicCount > 0) {
                    addCountText(centerX, centerY, hiddenCount + publicCount);
                } else {
                    addCountText(centerX, centerY, hiddenCount);
                }
            } else if (publicCount > 0) {
                // 공개 맛집만 있는 경우
                path.style.fill = '#4B89DC';
                addCountText(centerX, centerY, publicCount);
            }
        }
    });
}

function addCountText(x, y, count) {
    const svg = document.getElementById('seoul-map');
    const text = document.createElementNS("http://www.w3.org/2000/svg", "text");
    text.setAttribute('x', x);
    text.setAttribute('y', y);
    text.setAttribute('class', 'district-count');
    text.setAttribute('fill', 'white');
    text.setAttribute('text-anchor', 'middle');
    text.setAttribute('dominant-baseline', 'middle');
    text.setAttribute('font-size', '24');
    text.textContent = count;
    svg.appendChild(text);
}

function showTooltip(e, districtId) {
    const tooltip = document.getElementById('tooltip');
    const koreanName = districtNames[districtId];
    tooltip.style.display = 'block';
    tooltip.style.left = e.pageX + 10 + 'px';
    tooltip.style.top = e.pageY + 10 + 'px';
    tooltip.textContent = koreanName;
}

function hideTooltip() {
    document.getElementById('tooltip').style.display = 'none';
}

function displayFriendList() {
    const friendListEl = document.getElementById('friendList');
    friendListEl.innerHTML = serverData.friendData
        .map(friend => `
            <div class="friend-item" data-friend-id="${friend.firendId}">
                <img src="${friend.profileImg}" alt="${friend.nickname}" class="friend-img">
                <div>
                    <div>${friend.nickname}</div>
                    <div>ID: ${friend.firendId}</div>
                </div>
            </div>
        `)
        .join('');

    document.querySelectorAll('.friend-item').forEach(item => {
        item.addEventListener('click', () => {
            const friendId = parseInt(item.dataset.friendId);
            showFriendData(friendId);
        });
    });
}

function showFriendData(friendId) {
    const friend = serverData.friendData.find(f => f.firendId === friendId);
    if (friend) {
        currentData = {
            publicGu: friend.publicGu,
            hiddenGu: friend.hiddenGu
        };
        updateMap(currentData);
    }
    closeModal();
}

function resetToTotalData() {
    currentData = serverData.toTalData;
    updateMap(currentData);
}

function openModal() {
    document.getElementById('friendModal').style.display = 'block';
    displayFriendList();
}

function closeModal() {
    document.getElementById('friendModal').style.display = 'none';
}

// DOM 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    // 지도 이벤트 설정
    const paths = document.querySelectorAll('#seoul-map path');
    paths.forEach(path => {
        path.addEventListener('mousemove', (e) => showTooltip(e, path.id));
        path.addEventListener('mouseleave', hideTooltip);
    });

    // 모달 버튼 이벤트 설정
    const showFriendsBtn = document.getElementById('showFriendsBtn');
    const closeModalBtn = document.getElementById('closeModalBtn');

    showFriendsBtn.addEventListener('click', openModal);
    closeModalBtn.addEventListener('click', closeModal);

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', (event) => {
        const modal = document.getElementById('friendModal');
        if (event.target === modal) {
            closeModal();
        }
    });

    // 초기 데이터로 지도 업데이트
    updateMap(currentData);
});