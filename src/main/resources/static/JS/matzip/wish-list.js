document.addEventListener('DOMContentLoaded', async () => {
    const mainElement = document.querySelector('.main-container');
    const memberId = mainElement ? mainElement.dataset.memberId : null;

    displayProfile()
});

function displayProfile(data) {
    const friendProfile = document.querySelector('.friend-profile');
    const imgUrl = data.profile
        ? `/upload/${data.profile.filename}?t=${new Date().getTime()}`
        : `/upload/defaultProfileImg.png?t=${new Date().getTime()}`;


    friendProfile.innerHTML = `
       <img src="${imgUrl}" alt="프로필 이미지">
   `;
}


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