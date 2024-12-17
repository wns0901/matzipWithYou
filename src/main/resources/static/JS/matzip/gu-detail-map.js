const centerLatLng = data.centerLatLng,
    totalList = data.totalMatzipList,

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
    searchWindow = document.getElementById('search_window')
;

let searchResultList;

console.log(data)

displayPlaces(totalList)

searchBtn.addEventListener('click', searchPlaces);
searchSelectBtn.addEventListener('click', postMatzipData);
addBtn.addEventListener('click', () => searchWindow.classList.remove('hidden'));
closeSearchBtn.addEventListener('click', () => searchWindow.className += ' hidden');
closeBtn.addEventListener('click', () => detailInfo.className += ' hidden')

function displayPlaces(places) {
    const matzipWrap = document.getElementById('matzip_wrap'),
        bounds = new kakao.maps.LatLngBounds(),
        fragment = document.createDocumentFragment();

    removeAllChildNods(matzipWrap);
    removeMarker();

    places.forEach(place => {
        const placePosition = new kakao.maps.LatLng(place.lat, place.lng),
            marker = place.visibility === 'PUBLIC' ? addMakers(placePosition) : addCircle(placePosition),
            item = getDivItem(place, data.wishList);

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

            item.onclick = cardClickedEvent();

            item.onmouseout = function () {
                overlay.setMap(null);
            };
        })(marker, place.name, placePosition);

        fragment.appendChild(item);
    })

    matzipWrap.appendChild(fragment);

    map.setBounds(bounds);
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

function getDivItem(place, wishList) {
    const card = document.createElement('div');
    card.className = 'matzip_card';
    card.dataset.memberId = place.memberIds[0];
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
        wishListBtn.type = 'button'
        if (wishList.includes(place.matzipId)) {
            wishListBtn.style.backgroundImage = 'url(' + fillHeartImgUrl + ')';
        } else {
            wishListBtn.style.backgroundImage = 'url(' + emptyHeartImgUrl + ')';
        }

        card.appendChild(matzipImg);
        card.appendChild(matzipName);
        card.appendChild(wishListBtn);
    } else {
        const hiddenImg = document.createElement('img')
        hiddenImg.className = 'hidden_img';
        hiddenImg.src = hiddenImgUrl;

        card.appendChild(hiddenImg);
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

function cardClickedEvent() {
    return async function (e) {
        const card = e.currentTarget,
            cardList = document.getElementsByClassName('matzip_card');

        if (card.dataset.visibility === 'HIDDEN') {
            for (const card of cardList) card.classList.remove('selected');
            detailInfo.className += ' hidden';
            return;
        }

        closeBtn.addEventListener('click', function closerBtnEvent() {
            card.classList.remove('selected');
            closeBtn.removeEventListener('click', closerBtnEvent)
        })

        if (card.classList.contains('selected')) {
            card.classList.remove('selected');
            detailInfo.className += ' hidden';
        } else {
            const memberId = Number(card.dataset.memberId);
            const matzipId = Number(card.dataset.matzipId);

            const result = await getMatzipDetail(matzipId, memberId);
            for (const card of cardList) card.classList.remove('selected');
            card.className += ' selected';
            detailInfo.classList.remove('hidden')

            fillDataIntoCard(result);
        }

    }
}

function fillDataIntoCard(result) {
    Array.from(detailInfo.childNodes).forEach(node => {
        switch (node.id) {
            case 'matzip_img':
                node.src = result.imgUrl;
                break;
            case 'matzip_name':
                node.textContent = result.name;
                break;
            case 'matzip_address':
                node.textContent = result.address;
                break;
            case 'star_rating':
                node.textContent = result.starRating;
                break;
            case 'tags_wrap':
                node.textContent = result.tagList;
                break;
            case 'write_review_btn':
                node.textContent = result.id;
                break;
            default:
                break;
        }
    })
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
    searchResultList = result;
    for (let i = 0; i < result.length; i++) {
        const e = result[i],
            searchCard = document.createElement('div'),
            name = document.createElement('span'),
            address = document.createElement('span')
        ;

        searchCard.className = 'search_card';
        name.className = 'search_name';
        address.className = 'search_address';

        searchCard.dataset.searchIndex = i;

        name.textContent = e['place_name'];
        address.textContent = e['road_address_name'];

        searchCard.appendChild(name);
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
        body = JSON.stringify({data})
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
            console.log(res.data)
            fillDataIntoCard(res.data);
            detailInfo.classList.remove('hidden')

        })
}