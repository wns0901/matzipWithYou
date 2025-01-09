document.addEventListener('DOMContentLoaded', async () => {
    const mainElement = document.querySelector('.main-container');
    const memberId = mainElement ? mainElement.dataset.memberId : null;

    displayProfile()
});

// function displayProfile(data) {
//     const friendProfile = document.querySelector('.friend-profile');
//  const imgUrl = profile && profile.filename
//      ? '/upload/' + profile.filename :
//       '/static/IMG/matzip/defaultProfileImg.png';
//  console.log(imgUrl)
//
//
//
//     friendProfile.innerHTML = `
//        <img src="${imgUrl}" >
//    `;
// }

function displayProfile() {
    const friendProfile = document.querySelector('.friend-profile');
    let imgUrl;

    if (profile && profile.filename) {
        // 기본 이미지 파일명인지 확인
        if (profile.filename === 'defaultProfileImg.png') {
            imgUrl = '/IMG/defaultProfileImg.png';
        } else {
            imgUrl = '/upload/' + profile.filename;
        }
    } else {
        // profile 객체가 없거나 filename이 없는 경우
        imgUrl = '/IMG//defaultProfileImg.png';
    }

    console.log('Image URL:', imgUrl); // 디버깅용

    friendProfile.innerHTML = `
        <img src="${imgUrl}" alt="프로필 이미지">
    `;
}





// function displayProfile(data) {
//     const friendProfile = document.querySelector('.friend-profile');
//     let imgUrl;
//
//     if (profile && profile.filename) {
//         imgUrl = '/upload/' + profile.filename;
//         console.log(imgUrl)
//     } else {
//         // 기본 프로필 이미지를 사용하는 경우s
//         imgUrl = 'static/IMG/defaultProfileImg.png';
//         console.log(imgUrl)
//     }
//
//     console.log(imgUrl); // 디버깅용 로그
//
//     friendProfile.innerHTML = `
//        <img src="${imgUrl}" alt="프로필 이미지">
//    `;
// }


// const imgUrl = profile && profile.filename
//     ? '/upload/' + profile.filename
//     : '/static/IMG/defaultProfileImg.png';  // 절대 경로 사용
//

// function displayProfile(data) {
//     const friendProfile = document.querySelector('.friend-profile');
//     const imgUrl = data.profile
//         ? `/upload/${data.profile.filename}?t=${new Date().getTime()}`
//         : '/IMG/member/default-profile-img.png';
//
//     friendProfile.innerHTML = `
//        <img src="${imgUrl}" alt="프로필 이미지">
//    `;
// }