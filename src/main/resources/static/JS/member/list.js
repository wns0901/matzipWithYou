document.addEventListener('DOMContentLoaded', function () {
    const memberId = getMemberIdFromUrl();
    if (memberId) {
        loadFriendsList(memberId);
    }

    // 정렬 버튼에 이벤트 리스너 추가
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

    // TOP 3 섹션은 항상 친밀도 순으로 정렬
    const top3Friends = [...friends].sort((a, b) => b.intimacy - a.intimacy).slice(0, 3);

    // 상위 3명을 위한 섹션 추가
    const top3Container = document.createElement('div');
    top3Container.classList.add('top-3-friends');
    // top3Container.innerHTML = '<h3>친밀도 TOP 3</h3>';

    // 상위 3명 표시
    top3Friends.forEach((friend, index) => {
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
        top3Container.appendChild(topFriendItem);
    });

    // 전체 친구 목록 섹션
    const fullListContainer = document.createElement('div');
    fullListContainer.classList.add('full-friend-list');
    // fullListContainer.innerHTML = '<h3>전체 친구 목록</h3>';


    // 전체 친구 목록 표시
    friends.forEach(friend => {
        const friendItem = document.createElement('div');
        friendItem.classList.add('friend-item');

        friendItem.innerHTML = `
<div class="friend-box">
    <img class="friend-background" src="/IMG/friend-box-t.png" alt="friend-box">
            <img src="${friend.profileImg || '/IMG/defaultProfileImg.png'}"
                 alt="Profile"
                 class="full-friend-profile"
                 onerror="this.src='/IMG/defaultProfileImg.png'">
            <div class="full-friend-nickname">${friend.nickname}</div>
            <div class="full-friend-username">ID : ${friend.username}</div>
            <div class="open-matzip">${friend.publicCount}
<!--            <img class="open-matzip" src="/IMG/open_matzip.png" alt="openMZ Background">-->
            </div>
            <div class="hidden-matzip">${friend.hiddenCount}
<!--             <img class="hidden-matzip" src="/IMG/hidden_matzip.png" alt="hiddenMZ Background">-->
            </div>
            <img class="btn btn-delete" 
                src="/IMG/delete_friend.png"
                alt="delete-friend"
                data-friend-id="${friend.friendId}"></img>
</div>
            
        `;

        friendItem.querySelector('.btn-delete').addEventListener('click', async function () {
            const friendId = this.dataset.friendId;
            await deleteFriend(friendId);
        });

        fullListContainer.appendChild(friendItem);
    });

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
        const requests = await response.json();

        const container = document.getElementById('pendingRequests');
        container.innerHTML = '';

        // 여기가 문제였던 부분입니다.
        // return을 제거하고, 모달은 항상 표시되도록 수정
        if (requests.length === 0) {
            container.innerHTML = '<p>친구 요청을 한 사람이 없습니다.</p>';
        } else {
            requests.forEach(request => {
                const card = document.createElement('div');
                card.className = 'friend-card';
                card.innerHTML = `
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
                `;
                container.appendChild(card);
            });
        }

        // 모달 표시 (requests가 비어있어도 항상 실행)
        requestModal.style.display = 'block';

    } catch (error) {
        console.error('친구 요청 목록 로딩 실패:', error);
    }
});

// 친구 추가 모달 열기
document.querySelector('.btn-add').addEventListener('click', () => {
    addModal.style.display = 'block';
});

// 기존 검색 함수를 아래와 같이 수정
const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const searchResults = document.getElementById('searchResults');

// 검색 함수
const performSearch = async (searchTerm) => {
    if (!searchTerm.trim()) {
        searchResults.innerHTML = '';
        return;
    }

    try {
        const response = await fetch(`/api/friends/search?term=${encodeURIComponent(searchTerm)}`);
        const results = await response.json();

        searchResults.innerHTML = '';

        if (results.length === 0) {
            searchResults.innerHTML = `
                <div class="no-results">
                    <p>결과가 없습니다.</p>
                    <p>철자가 정확한지 확인하거나 다시 입력해보세요.</p>
                </div>`;
            return;
        }

        results.forEach(member => {
            const card = document.createElement('div');
            card.className = 'friend-card';
            card.innerHTML = `
                <div>
                    <img src="${member.profileImg || '/IMG/defaultProfileImg.png'}"
                         onerror="this.src='/IMG/defaultProfileImg.png'">
                    <span>${member.nickname}</span>
                    <span>@${member.username}</span>
                    <span>공개: ${member.publicCount}</span>
                    <span>비공개: ${member.hiddenCount}</span>
                </div>
                <button onclick="sendFriendRequest(${member.id})" ${member.isAlreadyFriend ? 'disabled' : ''}
                class="${member.isAlreadyFriend ? 'btn-disabled' : 'btn-request'}">
                    ${member.isAlreadyFriend ? '이미 친구입니다' : '친구 요청'}
                </button>
            `;
            searchResults.appendChild(card);
        });
    } catch (error) {
        console.error('사용자 검색 실패:', error);
        searchResults.innerHTML = '<div class="error">검색 중 오류가 발생했습니다.</div>';
    }
};

// 검색 버튼 클릭 이벤트
searchButton.addEventListener('click', () => {
    performSearch(searchInput.value);
});

// 입력창 이벤트는 검색 버튼을 눌렀을 때만 작동하도록 제거
searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        performSearch(searchInput.value);
    }
});

async function respondToRequest(senderId, isAccept) {
    const memberId = getMemberIdFromUrl();

    try {
        const requestData = {
            senderId: senderId,
            isAccept: isAccept
        };

        const response = await fetch(`/members/${memberId}/friends`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        const affectedRows = await response.json();

        if (affectedRows > 0) {
            const modal = document.getElementById('friendRequestModal');
            if (modal) {
                modal.style.display = 'none';
            }

            await loadFriendsList(memberId);
        } else {
        }
    } catch (error) {
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
    const memberId = getMemberIdFromUrl();

    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

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

// 모달 닫기 기능
document.querySelectorAll('.close').forEach(closeBtn => {
    closeBtn.addEventListener('click', function () {
        this.closest('.modal').style.display = 'none';
    });
});

window.onclick = function (event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
    }
};