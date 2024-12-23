document.addEventListener('DOMContentLoaded', async () => {
    const mainElement = document.querySelector('main');
    const memberId = mainElement ? mainElement.dataset.memberId : null;

    displayProfile()
});

function displayProfile(data) {
    const friendProfile = document.querySelector('.friend-profile');
    const imgUrl = profile ? '/upload/' + profile.filename : '/IMG/member/default-profile-img.png';

    friendProfile.innerHTML = `
       <img src="${imgUrl}" alt="프로필 이미지">
   `;
}