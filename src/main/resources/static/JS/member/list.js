// list.js 수정
// 먼저 함수들을 정의
function updateFriendList(friends) {
    const container = document.querySelector('#friends-container');
    if (!container) {
        console.error('Friends container not found');
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

    friends.forEach(friend => {
        const friendItem = document.createElement('div');
        friendItem.classList.add('friend-item');

        friendItem.innerHTML = `
            <img src="${friend.profileImg || '/IMG/defaultProfileImg.png'}"
                 alt="Profile"
                 class="friend-profile"
                 onerror="this.src='/IMG/defaultProfileImg.png'">
            <p>Nickname: ${friend.nickname}</p>
            <p>Public Count: ${friend.publicCount}</p>
            <p>Hidden Count: ${friend.hiddenCount}</p>
            <p>Intimacy: ${friend.intimacy}</p>
            <button class="btn btn-delete" data-friend-id="${friend.friendId}">삭제</button>
        `;

        container.appendChild(friendItem);
    });
}

function getCurrentMemberId() {
    const path = window.location.pathname;
    const matches = path.match(/\/members\/(\d+)/);
    return matches ? matches[1] : null;
}

// 그 다음 이벤트 리스너에서 함수 사용
document.addEventListener('DOMContentLoaded', async function() {
    const memberId = getCurrentMemberId();
    if (!memberId) return;

    try {
        const response = await fetch(`/members/${memberId}/friends/list`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({ memberId: memberId })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const friends = await response.json();
        updateFriendList(friends);
    } catch (error) {
        console.error('Error fetching friend list:', error);
    }
});