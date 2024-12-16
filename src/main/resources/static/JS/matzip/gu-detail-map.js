const centerLatLng = data.centerLatLng;
const mapContainer = document.getElementById("map");
// const mapOption = {
//     center: new kakao.maps.LatLng(centerLatLng.lat, centerLatLng.lng),
//     level: 5
// };
// const pinList = [];
//
// const map = new kakao.maps.Map(mapContainer, mapOption);

console.log(data)

function printPin(data) {
    pinList.length = 0;

    data.forEach(e => {
        const position = new kakao.maps.LatLng(e.lat, e.lng);
        const marker = new kakao.maps.Marker({position});
        const infowindow = new kakao.maps.InfoWindow({
            content: `<div>${e.name}</div>`
        })

        kakao.maps.event.addListener(marker, 'mouseover',)


    })
}

function makeOverListener(map, marker, infowindow) {
    return function () {

        infowindow.open(map, marker)
    }
}