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

const lockPositions = {
    'Gangnam-gu': {x: 950, y: 1020},
    'Gangdong-gu': {x: 1220, y: 770},
    'Gangbuk-gu': {x: 824, y: 385},
    'Gangseo-gu': {x: 200, y: 720},
    'Gwanak-gu': {x: 600, y: 1100},
    'Gwangjin-gu': {x: 950, y: 700},
    'Guro-gu': {x: 400, y: 900},
    'Geumcheon-gu': {x: 450, y: 1050},
    'Nowon-gu': {x: 1000, y: 300},
    'Dobong-gu': {x: 885, y: 275},
    'Dongdaemun-gu': {x: 945, y: 620},
    'Dongjak-gu': {x: 600, y: 965},
    'Mapo-gu': {x: 490, y: 735},
    'Seodaemun-gu': {x: 570, y: 630},
    'Seocho-gu': {x: 810, y: 1100},
    'Seongdong-gu': {x: 895, y: 755},
    'Seongbuk-gu': {x: 800, y: 450},
    'Songpa-gu': {x: 1000, y: 850},
    'Yangcheon-gu': {x: 350, y: 750},
    'Yeongdeungpo-gu': {x: 500, y: 800},
    'Yongsan-gu': {x: 700, y: 750},
    'Eunpyeong-gu': {x: 550, y: 400},
    'Jongno-gu': {x: 700, y: 500},
    'Jung-gu': {x: 750, y: 650},
    'Jungnang-gu': {x: 1000, y: 500}
};

let currentData = serverData.toTalData;
let selectedFriendId = null;

const colorRanges = [
    {threshold: 0, color: '#FFFFFF'},
    {threshold: 0.0001, color: '#FFE2BA'},
    {threshold: 0.25, color: '#FF9345'},
    {threshold: 0.50, color: '#FF7327'},
    {threshold: 0.75, color: '#D25800'}
];

function getMaxCount(data) {
    let maxCount = 0;
    Object.keys(districtNames).forEach(engName => {
        const korName = districtNames[engName];
        const publicCount = data.publicGu[korName] || 0;
        const hiddenCount = data.hiddenGu[korName] || 0;
        const totalCount = publicCount + hiddenCount;
        maxCount = Math.max(maxCount, totalCount);
    });
    return maxCount;
}

function getColorByPercentage(percentage) {
    if (percentage > 0 && percentage <= 0.25) {
        return '#FFE2BA';
    }

    for (let i = colorRanges.length - 1; i >= 0; i--) {
        if (percentage > colorRanges[i].threshold) {
            return colorRanges[i].color;
        }
    }
    return colorRanges[0].color;
}

function updateMap(data) {
    document.querySelectorAll('.district-lock').forEach(el => el.remove());
    const maxCount = getMaxCount(data);

    Object.keys(districtNames).forEach(engName => {
        const korName = districtNames[engName];
        const path = document.getElementById(engName);

        if (path) {
            const publicCount = data.publicGu[korName] || 0;
            const hiddenCount = data.hiddenGu[korName] || 0;
            const totalCount = publicCount + hiddenCount;

            if (totalCount > 0) {
                const percentage = maxCount > 0 ? totalCount / maxCount : 0;
                const color = getColorByPercentage(percentage);
                path.style.fill = color;

                if (hiddenCount > 0 && lockPositions[engName]) {
                    addLockText(lockPositions[engName].x, lockPositions[engName].y);
                }
            } else {
                path.style.fill = '#FFFFFF';
            }
        }
    });
}

function addLockText(x, y) {
    const svg = document.getElementById('seoul-map');
    const image = document.createElementNS("http://www.w3.org/2000/svg", "image");

    image.setAttribute('x', x - 10);
    image.setAttribute('y', y - 10);
    image.setAttribute('width', '50');
    image.setAttribute('height', '50');
    image.setAttribute('href', '/IMG/lock.png');
    image.setAttribute('class', 'district-lock');

    svg.appendChild(image);
}

function showTooltip(e, districtId) {
    const tooltip = document.getElementById('tooltip');
    const koreanName = districtNames[districtId];
    tooltip.textContent = koreanName;

    const x = e.clientX + 10;
    const y = e.clientY + 10;
    const tooltipWidth = tooltip.offsetWidth;
    const windowWidth = window.innerWidth;

    if (x + tooltipWidth > windowWidth) {
        tooltip.style.left = (x - tooltipWidth - 20) + 'px';
    } else {
        tooltip.style.left = x + 'px';
    }

    tooltip.style.top = y + 'px';
    tooltip.style.display = 'block';
}

function hideTooltip() {
    document.getElementById('tooltip').style.display = 'none';
}

function initializeFriendCards() {
    const hasFriends = serverData.friendData && serverData.friendData.length > 0;
    const friendsSidebar = document.querySelector('.friends-sidebar');

    if (!hasFriends) {
        friendsSidebar.innerHTML = `
            <div class="no-friends-message">
                등록된 친구가 없습니다
            </div>
            <button class="add-friend-button" onclick="location.href='#'">
                + 친구 등록하기
            </button>
            
        `;
        return;
    }

    document.querySelectorAll('.friend-card').forEach(card => {
        card.addEventListener('click', () => {
            const friendId = parseInt(card.dataset.friendId);

            if (selectedFriendId === friendId) {
                card.classList.remove('selected');
                selectedFriendId = null;
                currentData = serverData.toTalData;
                updateMap(currentData);
                return;
            }

            document.querySelectorAll('.friend-card').forEach(c =>
                c.classList.remove('selected'));
            card.classList.add('selected');
            selectedFriendId = friendId;
            showFriendData(friendId);
        });
    });
}

function showFriendData(friendId) {
    const friend = serverData.friendData.find(f => f.friendId === friendId);
    if (friend) {
        currentData = {
            publicGu: friend.publicGu || {},
            hiddenGu: friend.hiddenGu || {}
        };
        updateMap(currentData);
    }
}

// DOM 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
    const paths = document.querySelectorAll('#seoul-map path');
    paths.forEach(path => {
        path.addEventListener('mousemove', (e) => showTooltip(e, path.id));
        path.addEventListener('mouseleave', hideTooltip);
        path.addEventListener('click', guClickEvent);
    });

    initializeFriendCards();
    updateMap(currentData);
});

function guClickEvent(e) {
    const target = e.target,
        memberId = location.href.split('/').pop(),
        gu = districtNames[target.id],
        url = '/matzips/' + memberId + '/' + gu;
    location.href = url;
}

