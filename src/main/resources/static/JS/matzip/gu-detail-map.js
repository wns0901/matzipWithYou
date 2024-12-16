const centerLatLng = data.centerLatLng;
const mapContainer = document.getElementById("map");
const mapOption = {
    center: new kakao.maps.LatLng(centerLatLng.lat, centerLatLng.lng - 0.02),
    level: 6
};
const markers = [];

const ps = new kakao.maps.services.Places();

const map = new kakao.maps.Map(mapContainer, mapOption);

const infowindow = new kakao.maps.InfoWindow({zIndex: 1});

console.log(data)

const totalList = data.totalMatzipList;

displayPlaces(totalList)

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

        (function (marker, title) {
            kakao.maps.event.addListener(marker, 'mouseover', function () {
                displayInfowindow(marker, title);
            });

            kakao.maps.event.addListener(marker, 'mouseout', function () {
                infowindow.close();
            });

            item.onmouseover = function () {
                displayInfowindow(marker, title);
            };

            item.onmouseout = function () {
                infowindow.close();
            };
        })(marker, place.name);

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
    console.log(circle);
    circle.setMap(map);
    markers.push(circle)
    return circle;
}

function getDivItem(place, wishList) {
    const card = document.createElement('div');

    card.className = 'matzip_card';

    if (place.visibility === 'PUBLIC') {
        const matzipImg = document.createElement('img');
        const matzipName = document.createElement('div');
        const wishListBtn = document.createElement('div');

        matzipImg.className = 'matzip_img';
        matzipImg.src = place.imgUrl;

        matzipName.className = 'matzip_name';
        matzipName.textContent = place.name;

        if (wishList.includes(place.matzipId)) {
            wishListBtn.textContent = '누름';
        } else {
            wishListBtn.textContent = '안 누름';
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

function displayInfowindow(marker, title) {
    if (!title) return;
    const content = '<div class="infowindow">' + title + '</div>';
    infowindow.setContent(content);
    infowindow.open(map, marker);
}