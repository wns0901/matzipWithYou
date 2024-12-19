document.addEventListener('DOMContentLoaded', function() {
    const memberId = getMemberIdFromUrl();
    if (memberId) {
        loadFriendsList(memberId);
    }

    // 정렬 버튼에 이벤트 리스너 추가
    document.querySelectorAll('.btn-sort').forEach(button => {
        button.addEventListener('click', function() {
            const sortType = this.dataset.sort;
            sortFriends(sortType);
        });
    });
});

function getMemberIdFromUrl() {
    const pathParts = window.location.pathname.split('/');
    const memberIndex = pathParts.indexOf('members');
    return memberIndex !== -1 ? pathParts[memberIndex + 1] : null;
}

let friendsList = []; // 전역 변수로 친구 목록 저장

function sortFriends(sortType) {
    switch(sortType) {
        case 'registration':
            // 등록순 (원본 배열 순서)
            updateFriendList(friendsList);
            break;
        case 'alphabet':
            // 가나다순
            const sortedByAlphabet = [...friendsList].sort((a, b) =>
                a.nickname.localeCompare(b.nickname, 'ko')
            );
            updateFriendList(sortedByAlphabet);
            break;
        case 'intimacy':
            // 친밀도순
            const sortedByIntimacy = [...friendsList].sort((a, b) =>
                b.intimacy - a.intimacy
            );
            updateFriendList(sortedByIntimacy);
            break;
    }
}

function loadFriendsList(memberId) {
    fetch(`/members/${memberId}/friends/list`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ memberId: memberId })
    })
        .then(response => {
            if (!response.ok) throw new Error('네트워크 응답이 올바르지 않습니다');
            return response.json();
        })
        .then(friends => {
            friendsList = friends; // 전역 변수에 저장
            updateFriendList(friends); // 초기 표시는 등록순
        })
        .catch(error => {
            console.error('친구 목록 로딩 오류:', error);
        });
}

function updateFriendList(friends) {
    const container = document.querySelector('#friends-container');
    if (!container) {
        console.error('친구 목록 컨테이너를 찾을 수 없습니다');
        return;
    }

    container.innerHTML = '';

    if (!Array.isArray(friends) || friends.length === 0) {
        container.innerHTML = `
            <div class="no-friends">
                <p>아직 등록된 친구가 없습니다.</p>
            </div>`;
        return;
    }

    // TOP 3 섹션은 항상 친밀도 순으로 정렬
    const top3Friends = [...friends].sort((a, b) => b.intimacy - a.intimacy).slice(0, 3);

    // 상위 3명을 위한 섹션 추가
    const top3Container = document.createElement('div');
    top3Container.classList.add('top-3-friends');
    top3Container.innerHTML = '<h3>친밀도 TOP 3</h3>';

    // 상위 3명 표시
    top3Friends.forEach((friend, index) => {
        const topFriendItem = document.createElement('div');
        topFriendItem.classList.add('top-friend-item');

        topFriendItem.innerHTML = `
            <div class="rank">${index + 1}위</div>
            <div class="friend-content">
                <img src="${friend.profileImg || '/IMG/defaultProfileImg.png'}"
                     alt="${friend.nickname}님의 프로필"
                     class="friend-profile-small"
                     onerror="this.src='/IMG/defaultProfileImg.png'">
                <span class="friend-nickname">${friend.nickname}</span>
            </div>
        `;
        top3Container.appendChild(topFriendItem);
    });

    // 전체 친구 목록 섹션
    const fullListContainer = document.createElement('div');
    fullListContainer.classList.add('full-friend-list');
    fullListContainer.innerHTML = '<h3>전체 친구 목록</h3>';

    // 전체 친구 목록 표시
    friends.forEach(friend => {
        const friendItem = document.createElement('div');
        friendItem.classList.add('friend-item');

        friendItem.innerHTML = `
            <img src="${friend.profileImg || '/IMG/defaultProfileImg.png'}"
                 alt="Profile"
                 class="friend-profile"
                 onerror="this.src='/IMG/defaultProfileImg.png'">
            <p>닉네임: ${friend.nickname}</p>
            <p>공개 맛집 수: ${friend.publicCount}</p>
            <p>비공개 맛집 수: ${friend.hiddenCount}</p>
            <p>친밀도: ${friend.intimacy}</p>
            <button class="btn btn-delete" data-friend-id="${friend.friendId}">삭제</button>
        `;

        fullListContainer.appendChild(friendItem);
    });

    // 컨테이너에 추가
    container.appendChild(top3Container);
    container.appendChild(fullListContainer);
}


// 추가 구현
// 모달 관련 함수들
const requestModal = document.getElementById('friendRequestModal');
const addModal = document.getElementById('addFriendModal');

// 친구 요청 모달 열기
document.querySelector('.btn-request').addEventListener('click', async () => {
    const memberId = getMemberIdFromUrl();
    try {
        const response = await fetch(`/members/${memberId}/friends/requests`);
        console.log(response);
        const requests = await response.json();
        console.log(requests);


        const container = document.getElementById('pendingRequests');
        container.innerHTML = '';

        if (requests.length === 0) {
            container.innerHTML = '<p>친구 요청을 한 사람이 없습니다.</p>';
            return;
        }

        requests.forEach(request => {
            const card = document.createElement('div');
            card.className = 'friend-card';
            card.innerHTML = `
                <div>
                    <img src="${request.profileImg || '/IMG/defaultProfileImg.png'}" 
                         onerror="this.src='/IMG/defaultProfileImg.png'">
                    <span>${request.nickname}</span>
                    <span>@${request.username}</span>
                    <span>공개: ${request.publicCount}</span>
                    <span>비공개: ${request.hiddenCount}</span>
                </div>
                <div>
                    <button onclick="respondToRequest(${request.senderId}, 'ACCEPTED')">수락</button>
                    <button onclick="respondToRequest(${request.senderId}, 'REJECTED')">거절</button>
                </div>
            `;
            container.appendChild(card);
        });

        requestModal.style.display = 'block';
    } catch (error) {
        console.error('친구 요청 목록 로딩 실패:', error);
    }
});

// 친구 추가 모달 열기
document.querySelector('.btn-add').addEventListener('click', () => {
    addModal.style.display = 'block';
});

// 친구 검색 기능
let searchTimeout;
document.getElementById('searchInput').addEventListener('input', (e) => {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(async () => {
        const searchTerm = e.target.value;
        if (!searchTerm) return;

        try {
            const response = await fetch(`/api/members/search?term=${searchTerm}`);
            const results = await response.json();

            const container = document.getElementById('searchResults');
            container.innerHTML = '';

            results.forEach(user => {
                const card = document.createElement('div');
                card.className = 'friend-card';
                card.innerHTML = `
                    <div>
                        <img src="${user.profileImg || '/IMG/defaultProfileImg.png'}"
                             onerror="this.src='/IMG/defaultProfileImg.png'">
                        <span>${user.nickname}</span>
                        <span>@${user.username}</span>
                        <span>공개: ${user.publicCount}</span>
                        <span>비공개: ${user.hiddenCount}</span>
                    </div>
                    <button onclick="sendFriendRequest(${user.id})">친구 요청</button>
                `;
                container.appendChild(card);
            });
        } catch (error) {
            console.error('사용자 검색 실패:', error);
        }
    }, 300);
});

// 친구 요청 응답 처리
async function respondToRequest(senderId, status) {
    const memberId = getMemberIdFromUrl();
    try {
        const response = await fetch(`/members/${memberId}/friends`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                senderId: senderId,
                status: status
            })
        });

        if (response.ok) {
            // 요청 목록 새로고침
            document.querySelector('.btn-request').click();
            // 친구 목록 새로고침
            loadFriendsList(memberId);
        }
    } catch (error) {
        console.error('친구 요청 응답 실패:', error);
    }
}

// 친구 요청 보내기
async function sendFriendRequest(receiverId) {
    const memberId = getMemberIdFromUrl();
    try {
        const response = await fetch(`/members/${memberId}/friends`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                receiverId: receiverId
            })
        });

        if (response.ok) {
            alert('친구 요청을 보냈습니다.');
            addModal.style.display = 'none';
        } else {
            const errorMsg = await response.text();
            alert(errorMsg);
        }
    } catch (error) {
        console.error('친구 요청 실패:', error);
    }
}

// 모달 닫기 기능
document.querySelectorAll('.close').forEach(closeBtn => {
    closeBtn.addEventListener('click', function() {
        this.closest('.modal').style.display = 'none';
    });
});

window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
    }
};