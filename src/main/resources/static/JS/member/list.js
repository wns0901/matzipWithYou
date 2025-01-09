document.addEventListener('DOMContentLoaded', function () {
    const memberId = getMemberIdFromUrl();
    if (memberId) {
        loadFriendsList(memberId);
    }

    document.querySelectorAll('.btn-sort').forEach(button => {
        button.addEventListener('click', function () {
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

let friendsList = [];

function sortFriends(sortType) {
    switch (sortType) {
        case 'registration':
            updateFriendList(friendsList);
            break;
        case 'alphabet':
            const sortedByAlphabet = [...friendsList].sort((a, b) =>
                a.nickname.localeCompare(b.nickname, 'ko')
            );
            updateFriendList(sortedByAlphabet);
            break;
        case 'intimacy':
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
        body: JSON.stringify({memberId: memberId})
    })
        .then(response => {
            if (!response.ok) throw new Error('네트워크 응답이 올바르지 않습니다');
            return response.json();
        })
        .then(friends => {
            friendsList = friends;
            updateFriendList(friends);
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

    // TOP 3 친구들의 프로필을 랭킹 섹션에 추가
    const top3Friends = [...friends].sort((a, b) => b.intimacy - a.intimacy).slice(0, 3);
    updateRankingProfiles(top3Friends);

    // TOP 3 리스트와 전체 친구 목록 생성
    createTop3List(top3Friends, container);
    createFullFriendList(friends, container);
}

function updateRankingProfiles(friends) {
    friends.forEach((friend, index) => {
        const rankingSection = document.querySelector(
            `.ranking-section.${index === 0 ? 'first' : index === 1 ? 'second' : 'third'}`
        );

        if (!rankingSection) return;

        const existingProfile = rankingSection.querySelector('.ranking-profile');
        if (existingProfile) {
            existingProfile.remove();
        }

        // 컨테이너 생성
        const rankingProfile = document.createElement('div');
        rankingProfile.className = `ranking-profile rank-${index + 1}`;

        // ranking-bg 내부에 프로필과 정보를 배치
        rankingProfile.innerHTML = `
            <div class="profile-container">
                <img src="${friend.profileImg ? '/upload/' + friend.profileImg : '/IMG/defaultProfileImg.png'}"
                     alt="${friend.nickname}님의 프로필"
                     class="friend-profile-small"
                     onerror="this.src='/IMG/defaultProfileImg.png'">
                <div class="friend-info">
                    <div class="friend-nickname">${friend.nickname}</div>
                    <div class="friend-intimacy">친밀도: ${friend.intimacy}</div>
                </div>
            </div>
        `;

        rankingSection.appendChild(rankingProfile);
    });
}

function createTop3List(top3Friends, container) {
    const top3Container = document.createElement('div');
    top3Container.classList.add('top-3-friends');

    top3Friends.forEach(async (friend, index) => {
        const topFriendItem = document.createElement('div');
        topFriendItem.classList.add('top-friend-item', `rank-${index + 1}`);

        // topFriendItem.innerHTML = `
        //     <div class="rank">${index + 1}위</div>
        //     <div class="friend-content">
        //         <img src="${friend.profileImg || '/IMG/defaultProfileImg.png'}"
        //              alt="${friend.nickname}님의 프로필"
        //              class="friend-profile-small"
        //              onerror="this.src='/IMG/defaultProfileImg.png'">
        //         <span class="friend-nickname">${friend.nickname}</span>
        //     </div>
        // `;

        topFriendItem.style.cursor = 'pointer';
        topFriendItem.addEventListener('click', () => handleFriendClick(friend.friendId));
        top3Container.appendChild(topFriendItem);
    });

    container.appendChild(top3Container);
}

function createFullFriendList(friends, container) {
    const fullListContainer = document.createElement('div');
    fullListContainer.classList.add('full-friend-list');

    friends.forEach(friend => {
        const friendItem = document.createElement('div');
        friendItem.classList.add('friend-item');

        friendItem.innerHTML = `
            <div class="friend-box">
                <img class="friend-background" src="/IMG/friend-box-t.png" alt="friend-box">
                <img src="${friend.profileImg ? '/upload/' + friend.profileImg : '/IMG/defaultProfileImg.png'}"
                     alt="Profile"
                     class="full-friend-profile"
                     onerror="this.src='/IMG/defaultProfileImg.png'">
                <div class="full-friend-nickname">${friend.nickname}</div>
                <div class="full-friend-username">ID : ${friend.username}</div>
                <div class="open-matzip">${friend.publicCount}</div>
                <div class="hidden-matzip">${friend.hiddenCount}</div>
                <img class="btn btn-delete" 
                     src="/IMG/delete_friend.png"
                     alt="delete-friend"
                     data-friend-id="${friend.friendId}">
            </div>
        `;

        friendItem.querySelector('.btn-delete').addEventListener('click', async (event) => {
            event.stopPropagation();
            await deleteFriend(friend.friendId);
        });

        friendItem.style.cursor = 'pointer';
        friendItem.addEventListener('click', () => handleFriendClick(friend.friendId));
        fullListContainer.appendChild(friendItem);
    });

    container.appendChild(fullListContainer);
}

async function handleFriendClick(friendId) {
    try {
        const response = await fetch(`/members/friends/relation/${friendId}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const text = await response.text();
        if (!text) {
            console.error('Empty response received');
            return;
        }

        const friendRelation = JSON.parse(text);
        if (!friendRelation || !friendRelation.senderId || !friendRelation.receiverId) {
            console.error('Invalid friend relation data');
            return;
        }

        const currentMemberId = getMemberIdFromUrl();
        const targetId = friendRelation.senderId == currentMemberId ?
            friendRelation.receiverId : friendRelation.senderId;

        if (targetId) {
            window.location.href = `/matzips/mine/${targetId}`;
        }
    } catch (error) {
        console.error('Error details:', error);
    }
}

// 모달 관련 코드
const requestModal = document.getElementById('friendRequestModal');
const addModal = document.getElementById('addFriendModal');

document.querySelector('.btn-request').addEventListener('click', loadFriendRequests);
document.querySelector('.btn-add').addEventListener('click', () => {
    addModal.style.display = 'block';
});

async function loadFriendRequests() {
    const memberId = getMemberIdFromUrl();
    try {
        const response = await fetch(`/members/${memberId}/friends/requests`);
        const requests = await response.json();

        const container = document.getElementById('pendingRequests');
        container.innerHTML = requests.length === 0 ?
            '<p>친구 요청을 한 사람이 없습니다.</p>' :
            requests.map(request => createRequestCard(request)).join('');

        requestModal.style.display = 'block';
    } catch (error) {
        console.error('친구 요청 목록 로딩 실패:', error);
    }
}

function createRequestCard(request) {
    return `
        <div class="friend-card">
            <div>
                <img src="${request.profileImg || '/IMG/defaultProfileImg.png'}" 
                     onerror="this.src='/IMG/defaultProfileImg.png'">
                <span>${request.nickname}</span>
                <span>${request.username}</span>
                <span>공개: ${request.publicCount}</span>
                <span>비공개: ${request.hiddenCount}</span>
            </div>
            <div>
                <button onclick="respondToRequest(${request.senderId}, true)">수락</button>
                <button onclick="respondToRequest(${request.senderId}, false)">거절</button>
            </div>
        </div>
    `;
}

// 검색 관련 요소들
const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const searchResults = document.getElementById('searchResults');

// 검색 기능
const performSearch = async (searchTerm) => {
    if (!searchTerm.trim()) {
        searchResults.innerHTML = '';
        return;
    }

    try {
        const response = await fetch(`/api/friends/search?term=${encodeURIComponent(searchTerm)}`);
        const results = await response.json();

        searchResults.innerHTML = results.length === 0 ? `
            <div class="no-results">
                <p>결과가 없습니다.</p>
                <p>철자가 정확한지 확인하거나 다시 입력해보세요.</p>
            </div>` :
            results.map(member => `
                <div class="friend-card">
                    <div>
                        <img src="${member.profileImg || '/IMG/defaultProfileImg.png'}"
                             onerror="this.src='/IMG/defaultProfileImg.png'"
                             class="add-pimg">
                        <div class="add-text">
                            <div class="add-nick">${member.nickname}</div>
                            <div class="add-id">ID : ${member.username}</div>
                            <span class="add-open">공개 맛집 : ${member.publicCount} | 숨긴 맛집 : ${member.hiddenCount}</span>
                        </div>
                    </div>
                    <button onclick="sendFriendRequest(${member.id})" 
                            ${member.isAlreadyFriend ? 'disabled' : ''}
                            class="${member.isAlreadyFriend ? 'btn-disabled' : 'btn-request-add'}">
                        ${member.isAlreadyFriend ? '이미 친구입니다' : '친구 요청'}
                    </button>
                </div>
            `).join('');
    } catch (error) {
        console.error('사용자 검색 실패:', error);
        searchResults.innerHTML = '<div class="error">검색 중 오류가 발생했습니다.</div>';
    }
};

// 검색 이벤트 리스너
searchButton.addEventListener('click', () => {
    performSearch(searchInput.value);
});

searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        performSearch(searchInput.value);
    }
});

// 친구 요청 처리
async function respondToRequest(senderId, isAccept) {
    const memberId = getMemberIdFromUrl();
    try {
        const response = await fetch(`/members/${memberId}/friends`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                senderId: senderId,
                isAccept: isAccept
            })
        });

        const affectedRows = await response.json();

        if (affectedRows > 0) {
            requestModal.style.display = 'none';
            await loadFriendsList(memberId);
        }
    } catch (error) {
        console.error('친구 요청 처리 실패:', error);
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
                senderId: memberId,
                receiverId: receiverId,
                intimacy: 0,
                isAccept: false
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

// 친구 삭제
async function deleteFriend(friendId) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    const memberId = getMemberIdFromUrl();
    try {
        const response = await fetch(`/members/${memberId}/friends`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                friendId: friendId
            })
        });

        if (!response.ok) throw new Error('삭제 실패');

        const affectedRows = await response.json();
        if (affectedRows > 0) {
            await loadFriendsList(memberId);
        }
    } catch (error) {
        console.error('친구 삭제 실패:', error);
        alert('친구 삭제에 실패했습니다.');
    }
}

// 모달 닫기 이벤트
document.querySelectorAll('.close').forEach(closeBtn => {
    closeBtn.addEventListener('click', function () {
        this.closest('.modal').style.display = 'none';
    });
});

// 모달 외부 클릭시 닫기
window.onclick = function (event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
    }
};
