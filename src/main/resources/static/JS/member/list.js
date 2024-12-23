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

    const top3Friends = [...friends].sort((a, b) => b.intimacy - a.intimacy).slice(0, 3);

    const top3Container = document.createElement('div');
    top3Container.classList.add('top-3-friends');
    top3Container.innerHTML = '<h3>친밀도 TOP 3</h3>';

    top3Friends.forEach(async (friend, index) => {
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

        topFriendItem.style.cursor = 'pointer';
        topFriendItem.addEventListener('click', async function () {
            const friendId = friend.friendId;
            try {
                const response = await fetch(`/members/friends/relation/${friendId}`);
                if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

                const text = await response.text();
                if (!text) {
                    console.error('Empty response received');
                    return;
                }

                const friendRelation = JSON.parse(text);
                if (!friendRelation) {
                    console.error('No friend relation found');
                    return;
                }

                const currentMemberId = getMemberIdFromUrl();

                if (friendRelation.senderId == null || friendRelation.receiverId == null) {
                    console.error('Invalid friend relation data');
                    return;
                }

                const targetId = friendRelation.senderId == currentMemberId ?
                    friendRelation.receiverId : friendRelation.senderId;

                if (targetId) {
                    window.location.href = `/matzips/mine/${targetId}`;
                } else {
                    console.error('Could not determine target ID');
                }
            } catch (error) {
                console.error('Error details:', {
                    message: error.message,
                    stack: error.stack
                });
            }
        });

        top3Container.appendChild(topFriendItem);
    });
    const fullListContainer = document.createElement('div');
    fullListContainer.classList.add('full-friend-list');
    fullListContainer.innerHTML = '<h3>전체 친구 목록</h3>';

    friends.forEach(friend => {
        const friendItem = document.createElement('div');
        friendItem.classList.add('friend-item');

        friendItem.innerHTML = `
            <img src="${friend.profileImg || '/IMG/defaultProfileImg.png'}"
                 alt="Profile"
                 class="friend-profile"
                 onerror="this.src='/IMG/defaultProfileImg.png'">
            <p>닉네임: <span class="friend-nickname-hover">${friend.nickname}</span></p>
            <p>공개 맛집 수: ${friend.publicCount}</p>
            <p>비공개 맛집 수: ${friend.hiddenCount}</p>
            <p>친밀도: ${friend.intimacy}</p>
            <button class="btn btn-delete" data-friend-id="${friend.friendId}">삭제</button>
        `;

        friendItem.querySelector('.btn-delete').addEventListener('click', async function (event) {
            event.stopPropagation(); // 이벤트 버블링 방지
            const friendId = this.dataset.friendId;
            await deleteFriend(friendId);
        });

        friendItem.style.cursor = 'pointer';
        friendItem.addEventListener('click', async function () {
            const friendId = friend.friendId;
            try {
                const response = await fetch(`/members/friends/relation/${friendId}`);
                if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

                const text = await response.text();
                if (!text) {
                    console.error('Empty response received');
                    return;
                }

                const friendRelation = JSON.parse(text);
                if (!friendRelation) {
                    console.error('No friend relation found');
                    return;
                }

                const currentMemberId = getMemberIdFromUrl();

                if (friendRelation.senderId == null || friendRelation.receiverId == null) {
                    console.error('Invalid friend relation data');
                    return;
                }

                const targetId = friendRelation.senderId == currentMemberId ?
                    friendRelation.receiverId : friendRelation.senderId;

                if (targetId) {
                    window.location.href = `/matzips/mine/${targetId}`;
                } else {
                    console.error('Could not determine target ID');
                }
            } catch (error) {
                console.error('Error details:', {
                    message: error.message,
                    stack: error.stack
                });
            }
        });

        fullListContainer.appendChild(friendItem);
    });

    container.appendChild(top3Container);
    container.appendChild(fullListContainer);
}

const requestModal = document.getElementById('friendRequestModal');
const addModal = document.getElementById('addFriendModal');

document.querySelector('.btn-request').addEventListener('click', async () => {
    const memberId = getMemberIdFromUrl();
    try {
        const response = await fetch(`/members/${memberId}/friends/requests`);
        const requests = await response.json();

        const container = document.getElementById('pendingRequests');
        container.innerHTML = '';

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

        requestModal.style.display = 'block';
    } catch (error) {
        console.error('친구 요청 목록 로딩 실패:', error);
    }
});

document.querySelector('.btn-add').addEventListener('click', () => {
    addModal.style.display = 'block';
});

const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const searchResults = document.getElementById('searchResults');

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

searchButton.addEventListener('click', () => {
    performSearch(searchInput.value);
});

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
        }
    } catch (error) {
        console.error('친구 요청 처리 실패:', error);
    }
}

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

