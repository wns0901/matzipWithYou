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
    document.querySelectorAll('.district-count').forEach(el => el.remove());

    Object.keys(districtNames).forEach(engName => {
        const korName = districtNames[engName];
        const path = document.getElementById(engName);

        if (path) {
            const publicCount = data.publicGu[korName] || 0;
            const hiddenCount = data.hiddenGu[korName] || 0;

            if (publicCount > 0 || hiddenCount > 0) {
                // Center
                const bbox = path.getBBox();
                const centerX = bbox.x + bbox.width / 2;
                const centerY = bbox.y + bbox.height / 2;

                let textClass, textContent;

                if (publicCount > 0 && hiddenCount > 0) {
                    textClass = 'mixed';
                    textContent = publicCount + hiddenCount;
                } else if (hiddenCount > 0) {
                    textClass = 'hidden';
                    textContent = hiddenCount;
                } else {
                    textClass = 'public';
                    textContent = publicCount;
                }

                addCountText(centerX, centerY, textContent, textClass);
            }
        }
    });
}

function addCountText(x, y, count, className) {
    const svg = document.getElementById('seoul-map');
    const text = document.createElementNS("http://www.w3.org/2000/svg", "text");
    text.setAttribute('x', x);
    text.setAttribute('y', y);
    text.setAttribute('class', `district-count ${className}`);
    text.setAttribute('text-anchor', 'middle');
    text.setAttribute('dominant-baseline', 'middle');
    text.setAttribute('font-size', '24');
    text.setAttribute('font-weight', 'bold');
    text.textContent = count;
    svg.appendChild(text);
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
    document.querySelectorAll('.friend-card').forEach(card => {
        card.addEventListener('click', () => {
            // 선택 효과 처리
            document.querySelectorAll('.friend-card').forEach(c =>
                c.classList.remove('selected'));
            card.classList.add('selected');

            // 데이터 업데이트
            const friendId = parseInt(card.dataset.friendId);
            showFriendData(friendId);
        });
    });
}

function showFriendData(friendId) {
    const friend = serverData.friendData.find(f => f.friendId === friendId);
    if (friend) {
        currentData = {
            publicGu: friend.publicGu,
            hiddenGu: friend.hiddenGu
        };
        updateMap(currentData);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const paths = document.querySelectorAll('#seoul-map path');
    paths.forEach(path => {
        path.addEventListener('mousemove', (e) => showTooltip(e, path.id));
        path.addEventListener('mouseleave', hideTooltip);
    });

    initializeFriendCards();

    updateMap(currentData);
});