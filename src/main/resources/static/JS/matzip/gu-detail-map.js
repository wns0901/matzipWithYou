const centerLatLng = data.centerLatLng,
    totalList = data.totalMatzipList,
    friendListLenght = data.friendList.length,
    TIMEOUT = 1000,

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
    inputKeyWord = document.getElementById('keyword'),
    purchasingCancelBtn = document.querySelector('#cancel'),
    hintResultColseBtn = document.querySelector('#stop'),
    oneMoreBtn = document.querySelector('#one_more'),
    backBtn = document.querySelector('#back'),
    reviewBtn = detailInfo.querySelector('#write_review_btn')
;

let searchResultList,
    wishList = data.wishList,
    friendStart = 0,
    friendEnd = 2
;

if (totalList[0]) displayPlaces(totalList);
displayFriendProfile();

searchBtn.addEventListener('click', searchPlaces);
searchSelectBtn.addEventListener('click', postMatzipData);
addBtn.addEventListener('click', () => searchWindow.classList.remove('hidden'));
closeSearchBtn.addEventListener('click', () => {
    searchWindow.classList.add('hidden')
    Array.from(searchWindow.childNodes).forEach(node => {
        if (node.id === 'keyword') node.value = '';
        else if (node.id === 'result_list') node.innerHTML = '';
    })
});
closeBtn.addEventListener('click', closeDetailEnvent);
leftBtn.addEventListener('click', moveLeftFriendListEvent);
rightBtn.addEventListener('click', moveRightFriendListEvent);
closeHiddenDetailBtn.addEventListener('click', closeHiddenDetailEvent);
inputKeyWord.addEventListener('keyup', enterSearchEnvet);
purchasingCancelBtn.addEventListener('click', cancelBtnEvent);
hintResultColseBtn.addEventListener('click', cancelBtnEvent);
backBtn.addEventListener('click', cancelBtnEvent);
reviewBtn.addEventListener('click', reviewBtnClickedEvent);

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
            } else {
                kakao.maps.event.addListener(marker, 'click', function () {
                    const myMatzipIds = place.myMatzipIds ? place.myMatzipIds : [place.myMatzipId];
                    clickHiddenEvent(place.myMatzipIds)
                })
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

function getMemberPoint(id) {
    const url = '/member/' + id + '/points';
    return fetch(url).then(res => res.json());
}

async function clickHiddenEvent(myMatzipIds) {
    const point = (await getMemberPoint(data.memberId)).point;
    myMatzipId = myMatzipIds[0];
    const pointSpan = document.querySelector('#purchasing_hint_info span'),
        hintInfoWindow = document.querySelector('#purchasing_hint_info'),
        allOverlay = document.getElementById('all_overlay');

    allOverlay.classList.remove('hidden');
    hintInfoWindow.classList.remove('hidden');

    pointSpan.textContent = '소지 포인트 ' + point + 'pt';

    const purchaseBtn = document.querySelector('#purchase');
    purchaseBtn.addEventListener('click', (e) => {
        purchasingHintEvent(e, myMatzipId, hintInfoWindow)
    });
}

async function getHintList(myMatzipId) {
    const url = '/matzip/tags/' + myMatzipId;
    return await fetch(url).then(res => res.json());
}

async function purchasingHintEvent(e, myMatzipId, hintInfoWindow) {
    hintInfoWindow.className = 'fade_out';
    const hintTagWindow = document.querySelector('#hint_tags');
    hintTagWindow.className = 'fade_in_ready';
    setTimeout(() => {
        hintInfoWindow.className = 'hidden';
        document.querySelector('#hint_result').className = 'hidden'
        hintTagWindow.className = 'fade_in';
    }, TIMEOUT)
    const hintTagList = document.querySelectorAll('.hint_tag'),
        hintPopup = document.querySelector('#hint_popup'),
        fragment = document.createDocumentFragment(),
        hintData = await getHintList(myMatzipId),
        openHint = hintData.purchased,
        unopenedHint = hintData.unpurchased,
        isFinished = unopenedHint.length === 1;

    if (unopenedHint.length) {
        hintPopup.classList.remove('hidden');
    } else {
        hintPopup.classList.add('hidden');
    }


    oneMoreBtn.addEventListener('click', function oneMoreBtnEvent(event) {
        document.querySelector('#hint_result').className = 'fade_out';
        purchasingHintEvent(event, myMatzipId, hintInfoWindow);
        oneMoreBtn.removeEventListener('click', oneMoreBtnEvent);
    });

    unopenedHint.forEach((tag, i) => {
        const tagElement = document.createElement('div');
        tagElement.className = 'hint_tag';
        tagElement.id = 'hint_tag' + (i + 1);
        tagElement.innerHTML = '<span class="hint_text">HINT</span>'

        const reqData = {tag, hintTagWindow, hintTagList, myMatzipId, memberId: data.memberId};
        tagElement.addEventListener('click', (event) => openingHintEvent(event, reqData, isFinished));

        fragment.appendChild(tagElement);

    })


    openHint.forEach((tag, i) => {
        const tagElement = document.createElement('div');
        tagElement.className = 'result_tag';
        tagElement.id = 'result_tag' + (i + 1);
        tagElement.innerHTML = '<span class="result_text">' + tag.tagName + '</span>'

        fragment.appendChild(tagElement);
    })

    hintTagWindow.appendChild(fragment);
}

async function openingHintEvent(e, {tag, hintTagWindow, hintTagList, memberId, myMatzipId}, isFinished) {

    e.stopPropagation();
    hintTagWindow.className = 'fade_out';
    const hintResultWindow = document.querySelector('#hint_result');
    hintResultWindow.className = 'fade_in_ready';
    setTimeout(() => {
        hintTagWindow.className = 'hidden';
        hintTagWindow.innerHTML = '<img id="hint_popup" src="/IMG/matzip/hint_popup.png" class="hidden">';
        hintTagWindow.appendChild(backBtn);
        hintResultWindow.className = 'fade_in';
    }, TIMEOUT)
    if (isFinished) {
        oneMoreBtn.classList.add('hidden');
    } else {
        oneMoreBtn.classList.remove('hidden');
    }
    hintResultWindow.classList.remove('hidden');
    if (!(await saveHintStatus(tag.tagId, memberId, myMatzipId))) {
        alert('힌트 구매 실패');
        location.reload();
        return;
    }
    const hintSpan = document.querySelector('#hint_result span');
    hintSpan.textContent = tag.tagName;
}

async function saveHintStatus(tagId, memberId, myMatzipId) {
    const method = 'POST',
        headers = {'Content-Type': 'application/json'},
        body = JSON.stringify({tagId, memberId, myMatzipId}),
        url = '/matzip/saveTag';

    const result = await fetch(url, {method, headers, body});
    return result.status === 200;
}

function cancelBtnEvent(e) {
    const allOverlay = document.getElementById('all_overlay');
    allOverlay.classList.add('hidden');
    const hintInfoWindow = document.querySelector('#purchasing_hint_info');
    hintInfoWindow.classList.add('hidden');
    const hintResultWindow = document.querySelector('#hint_result');
    hintResultWindow.classList.add('hidden');
    const hintTagsWindow = document.querySelector('#hint_tags');
    hintTagsWindow.innerHTML = '<img id="hint_popup" src="/IMG/matzip/hint_popup.png" class="hidden">' +
        '<button id="back" onclick="cancelBtnEvent()"><span>돌아가기</span></button>';
    hintTagsWindow.classList.add('hidden');
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

        makeWishListBtn(wishListBtn, place.matzipId);

        card.appendChild(matzipImg);
        card.appendChild(matzipName);
        card.appendChild(wishListBtn);
    } else {
        card.className += ' hidden_card';
        card.dataset.nickname = await getNickNameByMemberIds(JSON.parse(card.dataset.memberId));
    }

    return card;
}

function makeWishListBtn(wishListBtn, matzipId, isSearch = false) {
    wishListBtn.className = 'wish_heart';
    wishListBtn.type = 'button';
    wishListBtn.dataset.matzipId = matzipId;
    wishListBtn.addEventListener('click', (e) => updateWishListEvent(e, isSearch));
    if (wishList.includes(matzipId)) {
        wishListBtn.style.backgroundImage = 'url(' + fillHeartImgUrl + ')';
        wishListBtn.dataset.status = 'fill';
    } else if (isSearch) {
        wishListBtn.style.backgroundImage = 'url(/IMG/matzip/gray_heart.png)';
        wishListBtn.dataset.status = 'empty';
    } else {
        wishListBtn.style.backgroundImage = 'url(' + emptyHeartImgUrl + ')';
        wishListBtn.dataset.status = 'empty';
    }
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

function dataIntoDetailCard(result, isSearch = false) {
    const img = detailInfo.querySelector('#matzip_detail_img'),
        name = detailInfo.querySelector('#matzip_name'),
        address = detailInfo.querySelector('#matzip_address'),
        starRating = detailInfo.querySelector('#star_rating_list'),
        tagList = detailInfo.querySelector('#tags_wrap'),
        reviewBtn = detailInfo.querySelector('#write_review_btn'),
        kindName = detailInfo.querySelector('#kind_name'),
        wishListBtn = detailInfo.querySelector('#detail_wish_list_btn')
    ;

    img.src = result.imgUrl;

    name.textContent = result.name;

    address.textContent = result.address;

    reviewBtn.dataset.matzipId = result.id;
    reviewBtn.dataset.memberId = data.memberId;

    kindName.textContent = result.kindName;

    makeTagList(tagList, result.tagList);

    makeStarList(starRating, result.starRating);

    if (isSearch) {
        makeWishListBtn(wishListBtn, result.id, isSearch);
    } else {
        wishListBtn.style.backgroundImage = null;
        wishListBtn.dataset.status = null;
    }
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
    const searchWindow = document.getElementById('search_window');
    searchWindow.classList.add('hidden');

    const loadingImg = document.querySelector('#loading_img');
    hiddenDetial.classList.add('hidden');
    loadingImg.classList.remove('hidden');
    detailInfo.classList.remove('hidden');
    fetch(url, {method, headers, body})
        .then(res => res.json())
        .then(res => {
            if (res.status !== 'SUCCESS') {
                alert('맛집 불어오기 실패')
                return;
            }


            dataIntoDetailCard(res.data, true);
            detailInfo.querySelector('#star_rating_list').innerHTML = '';
            detailInfo.querySelector('#tags_wrap').innerHTML = '';
            loadingImg.classList.add('hidden');
            searchWindow.childNodes[0].value = '';
            searchWindow.childNodes[2].innerHTML = '';
        })
}

function updateWishListEvent(e, isSearch = false) {
    e.stopPropagation();

    const item = e.currentTarget,
        isFilled = item.dataset.status === 'fill',
        isEmpty = !isFilled,
        matzipId = Number(item.dataset.matzipId),
        memberId = data.memberId,
        searchImgUrl = '/IMG/matzip/gray_heart.png',
        imgUrl = isEmpty ? fillHeartImgUrl : isSearch ? searchImgUrl : emptyHeartImgUrl,

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

        if (fData.profileImg) {
            profileImg.src = '/upload/' + fData.profileImg;
        } else {
            profileImg.src = '/IMG/defaultProfileImg.png';
        }

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
        overlay.classList.add('hidden');
        delete friendData.isSelected;
        displayPlaces(totalList);
        return;
    }

    for (const e of overlayList) e.classList.add('hidden');
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
        kindName = detailInfo.querySelector('#kind_name'),
        wishListBtn = detailInfo.querySelector('#detail_wish_list_btn');

    detailInfo.classList.add('hidden');

    img.src = '#';

    kindName.textContent = '';

    name.textContent = '';

    address.textContent = '';

    delete reviewBtn.dataset.matzipId;

    tagList.innerHTML = '';

    starRating.innerHTML = '';

    delete wishListBtn.style.backgroundImage;
    delete wishListBtn.dataset.status;
}

function closeHiddenDetailEvent(e) {
    const text1 = hiddenDetial.querySelector('#hidden_detail_text1'),
        text2 = hiddenDetial.querySelector('#hidden_detail_text2');

    hiddenDetial.classList.add('hidden');

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

function reviewBtnClickedEvent(e) {
    const matzipId = e.currentTarget.dataset.matzipId,
        memberId = e.currentTarget.dataset.memberId;
    window.location.href = '/matzip/reviews/' + memberId + '/' + matzipId;
}