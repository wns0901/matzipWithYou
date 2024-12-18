const centerLatLng = data.centerLatLng,
    totalList = data.totalMatzipList,
    friendListLenght = data.friendList.length,

    mapContainer = document.getElementById("map"),
    mapOption = {
        center: new kakao.maps.LatLng(centerLatLng.lat, centerLatLng.lng - 0.02),
        level: 6
    },
    ps = new kakao.maps.services.Places(),
    map = new kakao.maps.Map(mapContainer, mapOption),
    overlay = new kakao.maps.CustomOverlay(),

    markers = [],
    closeBtn = document.getElementById('close_detail_btn'),
    detailInfo = document.getElementById('detail-info'),
    searchBtn = document.getElementById('search_btn'),
    searchSelectBtn = document.getElementById('search_select_btn'),
    addBtn = document.getElementById('add_btn'),
    closeSearchBtn = document.getElementById('close_search_btn'),
    searchWindow = document.getElementById('search_window'),
    leftBtn = document.getElementById('left'),
    rightBtn = document.getElementById('right'),
    hiddenDetial = document.getElementById('hidden_detail'),
    closeHiddenDetailBtn = document.getElementById('close_hidden_detail_btn'),
    inputKeyWord = document.getElementById('keyword')
;

let searchResultList,
    wishList = data.wishList,
    friendStart = 0,
    friendEnd = 2
;

console.log(data);

displayPlaces(totalList);
displayFriendProfile();

searchBtn.addEventListener('click', searchPlaces);
searchSelectBtn.addEventListener('click', postMatzipData);
addBtn.addEventListener('click', () => searchWindow.classList.remove('hidden'));
closeSearchBtn.addEventListener('click', () => {
    searchWindow.className += ' hidden'
    Array.from(searchWindow.childNodes).forEach(node => {
        if (node.id === 'keyword') node.value = '';
        else if (node.id === 'result_list') node.innerHTML = '';
    })
});
closeBtn.addEventListener('click', closeDetailEnvent);
leftBtn.addEventListener('click', moveLeftFriendListEvent);
rightBtn.addEventListener('click', moveRightFriendListEvent);
closeHiddenDetailBtn.addEventListener('click', closeHiddenDetailEvent);
inputKeyWord.addEventListener('keyup', enterSearchEnvet)

async function displayPlaces(places, isFriendList = false) {
    const matzipWrap = document.getElementById('matzip_wrap'),
        bounds = new kakao.maps.LatLngBounds(),
        fragment = document.createDocumentFragment();

    removeAllChildNods(matzipWrap);
    removeMarker();

    for (const place of places) {
        const placePosition = new kakao.maps.LatLng(place.lat, place.lng),
            marker = place.visibility === 'PUBLIC' ? addMakers(placePosition) : addCircle(placePosition),
            item = await getDivItem(place, wishList);

        bounds.extend(placePosition);

        (function (marker, title, position) {
            if (title) {
                kakao.maps.event.addListener(marker, 'mouseover', function () {
                    displayInfowindow(title, position);
                    item.className += ' selected'
                });

                kakao.maps.event.addListener(marker, 'mouseout', function () {
                    overlay.setMap(null);
                    item.classList.remove('selected')
                });
            }

            item.onmouseover = function () {
                displayInfowindow(title, position);
            };

            item.onclick = cardClickedEvent;

            item.onmouseout = function () {
                overlay.setMap(null);
            };
        })(marker, place.name, placePosition);

        fragment.appendChild(item);
    }

    matzipWrap.appendChild(fragment);

    if (!isFriendList) map.setBounds(bounds);
}

function addMakers(position) {
    const marker = new kakao.maps.Marker({position})

    marker.setMap(map);
    markers.push(marker);

    return marker;
}

function addCircle(center) {
    const circle = new kakao.maps.Circle({
        center,
        radius: 200,
        strokeWeight: 1, // 선의 두께입니다
        strokeColor: '#FF7327', // 선의 색깔입니다
        strokeOpacity: 0.5, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: 'solid', // 선의 스타일 입니다
        fillColor: '#FF7327',
        fillOpacity: 0.5
    })
    circle.setMap(map);
    markers.push(circle)
    return circle;
}

async function getNickNameByMemberIds(memberId) {
    const memberIds = Array.isArray(memberId) ? memberId : [memberId];
    let url = '/member/nicknames?';

    memberIds.forEach(e => url += 'memberId=' + e + '&');
    url = url.slice(0, -1);
    return await fetch(url).then(res => res.json()).then(data => JSON.stringify(data.nickname));
}

async function getDivItem(place, wishList) {
    const card = document.createElement('div');
    card.className = 'matzip_card';
    card.dataset.memberId = place.memberIds ? JSON.stringify(place.memberIds) : place.memberId;
    card.dataset.matzipId = place.matzipId;
    card.dataset.visibility = place.visibility;

    if (place.visibility === 'PUBLIC') {
        const matzipImg = document.createElement('img');
        const matzipName = document.createElement('div');
        const wishListBtn = document.createElement('input');

        matzipImg.className = 'matzip_img';
        matzipImg.src = place.imgUrl;

        matzipName.className = 'matzip_name';
        matzipName.textContent = place.name;

        wishListBtn.className = 'wish_heart';
        wishListBtn.type = 'button';
        wishListBtn.dataset.matzipId = place.matzipId;
        wishListBtn.onclick = updateWishListEvent;
        if (wishList.includes(place.matzipId)) {
            wishListBtn.style.backgroundImage = 'url(' + fillHeartImgUrl + ')';
            wishListBtn.dataset.status = 'fill';
        } else {
            wishListBtn.style.backgroundImage = 'url(' + emptyHeartImgUrl + ')';
            wishListBtn.dataset.status = 'empty';
        }

        card.appendChild(matzipImg);
        card.appendChild(matzipName);
        card.appendChild(wishListBtn);
    } else {
        card.className += ' hidden_card';
        card.dataset.nickname = await getNickNameByMemberIds(JSON.parse(card.dataset.memberId));
    }

    return card;
}

function removeMarker() {
    markers.forEach(m => m.setMap(null))
    markers.length = 0;
}

function removeAllChildNods(div) {
    while (div.hasChildNodes()) {
        div.removeChild(div.lastChild);
    }
}

function displayInfowindow(title, position) {
    let content;
    if (!title) {
        content = '<span class="infowindow">' + '히든!' + '</span>';
    } else {
        content = '<span class="infowindow">' + title + '</span>';
    }
    ;
    overlay.setContent(content);
    overlay.setPosition(position);
    overlay.setMap(map);
}

async function getMatzipDetail(matzipId, friendId) {
    const url = '/matzips/detail/' + matzipId + '?friendId=' + friendId;
    return await fetch(url).then(res => res.json())
}

function openHiddenDetail(e) {
    const card = e.currentTarget,
        nickname = JSON.parse(card.dataset.nickname),
        text1 = hiddenDetial.querySelector('#hidden_detail_text1');
    text2 = hiddenDetial.querySelector('#hidden_detail_text2');

    text1.textContent = nickname.join(', ') + '님의 숨겨진 맛집입니다.'
    text2.textContent = '힌트를 통해 맛집을 찾아보세요.'

    hiddenDetial.classList.remove('hidden')
}

async function cardClickedEvent(e) {
    const card = e.currentTarget,
        selectedCard = document.querySelector('.selected');

    closeHiddenDetailBtn.addEventListener('click', function closerBtnEvent() {
        card.classList.remove('selected');
        closeBtn.removeEventListener('click', closerBtnEvent);
    })

    if (card.dataset.visibility === 'HIDDEN') {
        if (card.classList.contains('selected')) {
            card.classList.remove('selected');
            closeHiddenDetailEvent();
            return;
        } else {
            if (selectedCard) selectedCard.classList.remove('selected');
            card.className += ' selected'
            closeDetailEnvent();
            openHiddenDetail(e);
            return;
        }
    }

    closeBtn.addEventListener('click', function closerBtnEvent() {
        card.classList.remove('selected');
        closeBtn.removeEventListener('click', closerBtnEvent)
    })

    if (card.classList.contains('selected')) {
        card.classList.remove('selected');
        closeDetailEnvent();
    } else {
        const memberIdData = JSON.parse(card.dataset.memberId);
        const memberId = Array.isArray(memberIdData) ? memberIdData[0] : memberIdData;
        const matzipId = Number(card.dataset.matzipId);

        const result = await getMatzipDetail(matzipId, memberId);
        if (selectedCard) selectedCard.classList.remove('selected');
        card.className += ' selected';
        detailInfo.classList.remove('hidden')
        closeHiddenDetailEvent();
        dataIntoDetailCard(result);
    }
}

function makeTagList(node, tagList) {
    if (!tagList) return;

    node.innerHTML = '';

    const tag = document.createElement('div'),
        fragment = document.createDocumentFragment();

    tag.className = 'tag';

    tagList.forEach(t => {
        let cloneTag = tag.cloneNode(true);
        cloneTag.textContent = t;
        fragment.appendChild(cloneTag)
    });

    node.appendChild(fragment);
}

function dataIntoDetailCard(result) {
    const img = detailInfo.querySelector('#matzip_detail_img'),
        name = detailInfo.querySelector('#matzip_name'),
        address = detailInfo.querySelector('#matzip_address'),
        starRating = detailInfo.querySelector('#star_rating_list'),
        tagList = detailInfo.querySelector('#tags_wrap'),
        reviewBtn = detailInfo.querySelector('#write_review_btn'),
        kindName = detailInfo.querySelector('#kind_name');

    img.src = result.imgUrl;

    name.textContent = result.name;

    address.textContent = result.address;

    reviewBtn.dataset.matzipId = result.id;

    kindName.textContent = result.kindName;

    makeTagList(tagList, result.tagList);

    makeStarList(starRating, result.starRating);

}

function searchPlaces() {
    const keyword = document.getElementById('keyword').value;

    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    ps.keywordSearch((data.gu + ' ' + keyword), placesSearchCB);
}

function placesSearchCB(result, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        makeResultCards(result);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
        return;
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 결과 중 오류가 발생했습니다.');
        return;
    }
}

function makeResultCards(result) {
    const searchWrap = document.getElementById('result_list'),
        fragment = document.createDocumentFragment()
    ;
    searchWrap.innerHTML = '';
    searchResultList = result;
    for (let i = 0; i < result.length; i++) {
        const e = result[i],
            searchCard = document.createElement('div'),
            name = document.createElement('span'),
            address = document.createElement('span'),
            br = document.createElement('br')
        ;

        searchCard.className = 'search_card';
        name.className = 'search_name';
        address.className = 'search_address';

        searchCard.dataset.searchIndex = i;

        name.textContent = e['place_name'];
        address.textContent = e['road_address_name'];

        searchCard.appendChild(name);
        searchCard.appendChild(br)
        searchCard.appendChild(address);

        searchCard.onclick = (e) => {
            const card = e.currentTarget,
                selectedList = document.getElementsByClassName('selected');
            for (const e of selectedList) e.classList.remove('selected');
            card.className += ' selected';
        }

        fragment.appendChild(searchCard);
    }
    searchWrap.appendChild(fragment);
}

function postMatzipData() {
    const selectedItem = document.getElementsByClassName('selected');
    if (!selectedItem[0]) return;

    const selectedIndex = Number(selectedItem[0].dataset.searchIndex),
        data = searchResultList[selectedIndex],
        url = '/matzips',
        method = 'POST',
        headers = {
            'Content-Type': 'application/json'
        },
        body = JSON.stringify(data)
    ;

    fetch(url, {method, headers, body})
        .then(res => res.json())
        .then(res => {
            if (res.status !== 'SUCCESS') {
                alert('맛집 불어오기 실패')
                return;
            }

            const searchWindow = document.getElementById('search_window');
            searchWindow.className += ' hidden'
            dataIntoDetailCard(res.data)
            detailInfo.querySelector('#star_rating_list').innerHTML = '';
            detailInfo.querySelector('#tags_wrap').innerHTML = '';
            detailInfo.classList.remove('hidden')
            searchWindow.childNodes[0].value = '';
            searchWindow.childNodes[2].innerHTML = '';
        })
}

function updateWishListEvent(e) {
    e.stopPropagation();

    const item = e.currentTarget,
        isFilled = item.dataset.status === 'fill',
        matzipId = Number(item.dataset.matzipId),
        memberId = data.memberId,
        imgUrl = isFilled ? emptyHeartImgUrl : fillHeartImgUrl,

        method = isFilled ? 'DELETE' : 'POST',
        url = '/matzips/wish-list/' + memberId + (isFilled ? '/' + matzipId : ''),
        headers = {'Content-Type': 'application/json'},
        body = JSON.stringify({matzipId}),
        option = isFilled ? {method, headers} : {method, headers, body}
    ;

    if (isFilled) {
        wishList = wishList.filter(e => e !== matzipId);
    } else {
        wishList.push(matzipId);
    }

    fetch(url, option)
        .then(res => res.json())
        .then(res => {
            if (res.status !== 'SUCCESS') {
                alert('위시리스트 업데이트 실패')
                return;
            }

            item.style.backgroundImage = 'url(' + imgUrl + ')';
            item.dataset.status = isFilled ? 'empty' : 'fill';
        })

}

function displayFriendProfile() {
    const friendListData = data.friendList,
        friendCardList = document.getElementById('friend_card_list'),
        fragment = document.createDocumentFragment()
    ;

    friendCardList.innerHTML = '';

    for (let i = friendStart; i <= friendEnd; i++) {
        const fData = friendListData[i];

        if (!fData) break;

        const friendCard = document.createElement('div'),
            profileImg = document.createElement('img'),
            overlay = document.createElement('div')
        ;

        friendCard.className = 'friend_card';
        friendCard.dataset.index = i;
        friendCard.onclick = friendClickEvent;

        profileImg.className = 'profile_img';
        // profileImg.src = profileImgUrl + fData.profileImg;
        profileImg.src = profileImgUrl + 'defaultProfileImg.png';

        overlay.className = 'overlay' + (fData.isSelected ? '' : ' hidden');

        friendCard.appendChild(profileImg);
        friendCard.appendChild(overlay);

        fragment.appendChild(friendCard);
    }
    friendCardList.appendChild(fragment);
}

function moveLeftFriendListEvent(e) {
    if (friendStart === 0) return;

    friendStart -= 3;
    friendEnd -= 3;

    displayFriendProfile();
}

function moveRightFriendListEvent(e) {
    if ((friendListLenght - (friendEnd + 3)) < -1) return;

    friendStart += 3;
    friendEnd += 3;

    displayFriendProfile();
}

function friendClickEvent(e) {
    const item = e.currentTarget,

        overlay = item.querySelector('.overlay'),
        overlayList = document.getElementsByClassName('overlay'),

        index = item.dataset.index,
        friendList = data.friendList,
        friendData = friendList[index],
        friendMatzipList = friendData.matzipList;

    if (!overlay.classList.contains('hidden')) {
        overlay.className += ' hidden';
        displayPlaces(totalList);
        return;
    }

    for (const e of overlayList) e.className += ' hidden';
    friendList.forEach(f => {
        delete f.isSelected;
    })
    friendData.isSelected = true;

    overlay.classList.remove('hidden');
    displayPlaces(friendMatzipList, true);

}

function makeStarList(node, starRating) {
    const filledStar = document.createElement('img'),
        emptyStar = document.createElement('img'),
        text = document.createElement('span'),
        fragment = document.createDocumentFragment();

    node.innerHTML = '';

    filledStar.className = 'star';
    emptyStar.className = 'star';

    filledStar.src = '/IMG/matzip/filled_star.png';
    emptyStar.src = '/IMG/matzip/empty_star.png';

    for (let i = 1; i <= 5; i++) {
        fragment.appendChild((i <= starRating) ? filledStar.cloneNode() : emptyStar.cloneNode());
    }

    text.textContent = '평점 ' + starRating;
    fragment.appendChild(text);

    node.appendChild(fragment);
}

function closeDetailEnvent(e) {
    const img = detailInfo.querySelector('#matzip_detail_img'),
        name = detailInfo.querySelector('#matzip_name'),
        address = detailInfo.querySelector('#matzip_address'),
        starRating = detailInfo.querySelector('#star_rating_list'),
        tagList = detailInfo.querySelector('#tags_wrap'),
        reviewBtn = detailInfo.querySelector('#write_review_btn'),
        kindName = detailInfo.querySelector('#kind_name');

    detailInfo.className += ' hidden';

    img.src = '#';

    kindName.textContent = '';

    name.textContent = '';

    address.textContent = '';

    delete reviewBtn.dataset.matzipId;

    tagList.innerHTML = '';

    starRating.innerHTML = '';
}

function closeHiddenDetailEvent(e) {
    const text1 = hiddenDetial.querySelector('#hidden_detail_text1'),
        text2 = hiddenDetial.querySelector('#hidden_detail_text2');

    hiddenDetial.className += ' hidden';

    text1.textContent = '';
    text2.textContent = '';
}

function enterSearchEnvet(e) {
    if (e.key === 'Enter') {
        const searchWrap = document.getElementById('result_list');
        searchPlaces();
        searchWrap.scrollTop = 0;
    }
}