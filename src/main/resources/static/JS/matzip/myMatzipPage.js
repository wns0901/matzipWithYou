const list = data.list,
    isMine = data.memberId === who;

let wishList;

loadPage(list)

document.querySelectorAll('.filter_btn').forEach(btn => btn.addEventListener('click', filterEvent));

async function loadPage(list) {
    const fragment = document.createDocumentFragment(),
    cardList = document.querySelector('#myMatzip_card_list');

    cardList.innerHTML = '';

    for (const listElement of list) {
        const card  = await makeMyMatzipCard(listElement);
        if(!card) continue;
        fragment.appendChild(card)
    }

    cardList.appendChild(fragment);
}

async function makeMyMatzipCard(myMatzip) {
    const card = document.createElement('div'),
        img = document.createElement('img'),
        textDiv = document.createElement('div'),
        name = document.createElement('h3'),
        address = document.createElement('p'),
        moreDetailBtn = document.createElement('button'),
        moreDetailDiv = document.createElement('div'),
        starRating = document.createElement('div'),
        foodKind = document.createElement('div'),
        content = document.createElement('div'),
        tagList = document.createElement('div'),
        detailTitle = document.createElement('h3')
    ;

    if(isMine) {
        myPage(card, myMatzip);
    } else {
        if(myMatzip.visibility === 'PRIVATE') return;
        await otherPage(card, myMatzip);
        if(myMatzip.visibility === 'HIDDEN') return;
    }

    card.classList.add('card');
    img.classList.add('matzip_img');
    textDiv.classList.add('text_div');
    name.classList.add('name');
    address.classList.add('address');
    moreDetailBtn.classList.add('more_detail_btn');
    moreDetailDiv.classList.add('more_detail_div', 'hidden');
    starRating.classList.add('star_rating');
    foodKind.classList.add('food_kind');
    content.classList.add('content');
    tagList.classList.add('tag_list');

    img.src = myMatzip.imgUrl;
    name.textContent = myMatzip.name;
    address.textContent = myMatzip.address;
    moreDetailBtn.innerHTML = '<span>+ 상세보기</span>'
    moreDetailBtn.dataset.matzipId = myMatzip.matzipId;
    moreDetailBtn.dataset.memberId = myMatzip.memberId;
    content.innerHTML = '<span>' + myMatzip.content + '</span>';
    foodKind.innerHTML = '<span>' + myMatzip.kindName + '</span>';
    detailTitle.textContent = '내가 쓴 리뷰'

    makeStarList(starRating, myMatzip.starRating)
    makeTagList(tagList, myMatzip.tagList);

    moreDetailDiv.appendChild(starRating);
    moreDetailDiv.appendChild(foodKind);
    moreDetailDiv.appendChild(content);
    moreDetailDiv.appendChild(tagList);
    moreDetailDiv.appendChild(detailTitle);

    moreDetailBtn.addEventListener('click', moreDetailEvent);

    textDiv.appendChild(name);
    textDiv.appendChild(address);
    card.appendChild(img);
    card.appendChild(textDiv);
    card.appendChild(moreDetailBtn);
    card.appendChild(moreDetailDiv);

    return card;
}

function myPage(card, myMatzip) {
    const visibilityBtnList = document.createElement('div'),
        publicBtn = document.createElement('button'),
        privateBtn = document.createElement('button'),
        hiddenBtn = document.createElement('button'),
        deleteBtn = document.createElement('button')

    visibilityBtnList.classList.add('visibility_btn_list');
    publicBtn.classList.add('visibility_btn','PUBLIC');
    privateBtn.classList.add('visibility_btn','PRIVATE');
    hiddenBtn.classList.add('visibility_btn','HIDDEN');
    deleteBtn.classList.add('delete_btn');

    deleteBtn.dataset.id = myMatzip.id;

    publicBtn.innerHTML = '<span>공개</span>';
    hiddenBtn.innerHTML = '<span>히든</span>';
    privateBtn.innerHTML = '<span>비공개</span>';

    const visibilityBtn = {
        'PUBLIC': publicBtn,
        'PRIVATE': privateBtn,
        'HIDDEN': hiddenBtn,
    };

    visibilityBtn[myMatzip.visibility].classList.add('active');
    visibilityBtnList.dataset.matzipId = myMatzip.matzipId;

    deleteBtn.addEventListener('click', deleteEvent);
    publicBtn.addEventListener('click', updateVisibilityEvent);
    privateBtn.addEventListener('click', updateVisibilityEvent);
    hiddenBtn.addEventListener('click', updateVisibilityEvent);

    visibilityBtnList.appendChild(publicBtn);
    visibilityBtnList.appendChild(hiddenBtn);
    visibilityBtnList.appendChild(privateBtn);

    card.appendChild(visibilityBtnList);
    card.appendChild(deleteBtn);
}

async function otherPage(card, myMatzip) {
    await getWishList()
    const wishBtn = document.createElement('input'),
        matzipId = myMatzip.matzipId,
        fillHeartImgUrl = '/IMG/matzip/fill_heart.png',
        emptyHeartImgUrl = '/IMG/matzip/empty_heart.png';

    wishBtn.className = 'wish_heart';
    wishBtn.type = 'button';
    wishBtn.dataset.matzipId = matzipId;
    wishBtn.addEventListener('click', (e) => updateWishListEvent(e));
    if (wishList.includes(matzipId)) {
        wishBtn.style.backgroundImage = 'url(' + fillHeartImgUrl + ')';
        wishBtn.dataset.status = 'fill';
    } else {
        wishBtn.style.backgroundImage = 'url(' + emptyHeartImgUrl + ')';
        wishBtn.dataset.status = 'empty';
    }

    card.appendChild(wishBtn);
}

function updateWishListEvent(e) {
    e.stopPropagation();
    const fillHeartImgUrl = '/IMG/matzip/fill_heart.png',
        emptyHeartImgUrl = '/IMG/matzip/empty_heart.png';

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

function makeTagList(tagListDiv, tagListData) {
    const fragment = document.createDocumentFragment();
    tagListData.forEach(tag => {
        const tagDiv = document.createElement('div');
        tagDiv.classList.add('tag');
        tagDiv.innerHTML = '<span>' + tag.tagName + '</span>';
        fragment.appendChild(tagDiv);
    })

    tagListDiv.appendChild(fragment);
}

function makeStarList(item, starRating) {
    const filledStar = document.createElement('img'),
        emptyStar = document.createElement('img'),
        text = document.createElement('span'),
        fragment = document.createDocumentFragment();

    item.innerHTML = '';

    filledStar.className = 'star';
    emptyStar.className = 'star';

    filledStar.src = '/IMG/matzip/filled_star.png';
    emptyStar.src = '/IMG/matzip/empty_star.png';

    for (let i = 1; i <= 5; i++) {
        fragment.appendChild((i <= starRating) ? filledStar.cloneNode() : emptyStar.cloneNode());
    }

    text.textContent = '평점 ' + starRating;
    fragment.appendChild(text);

    item.appendChild(fragment);
}

async function deleteEvent(e) {
    const id = e.currentTarget.dataset.id,
        card = e.currentTarget.closest('.card'),
    url = '/matzips/mine/' + id,
    options = {
        method: 'DELETE',
    };

    const res = await fetch(url, options).then(res => res.json());
    console.log(res);
    if (res.status !== 'SUCCESS') {
        alert(res.msg);
        return;
    }

    card.remove();
}

function moreDetailEvent(e) {
    const card = e.currentTarget.closest('.card'),
        detailDiv = card.querySelector('.more_detail_div');
    detailDiv.classList.toggle('hidden');
}

async function updateVisibilityEvent(e) {
    if(e.currentTarget.classList.contains('active')) return;

    const btnList = e.currentTarget.closest('.visibility_btn_list'),
        id = btnList.dataset.matzipId,
        btn = e.currentTarget,
    url = '/matzips/mine/' + id,
    options = {
        method: 'PATCH',
        headers: {
            'Content-Type':'application/json'
        },
        body: JSON.stringify({
            visibility: btn.classList[1]
        })
    };

    const res = await fetch(url, options).then(res => res.json());

    if(res.status !== 'SUCCESS') {
        alert(res.msg);
        return;
    }

    const btnArr = btnList.querySelectorAll('.visibility_btn');

    btnArr.forEach(btn => btn.classList.remove('active'));
    btn.classList.add('active');
}

function sortEvent() {
    const item = event.currentTarget,
        type = item.dataset.type;

    if(item.classList.contains('active')) return;

    item.classList.toggle('active');

    if(type === 'reg') {
        loadPage(list);
        return;
    }
    
    const sortedList = [...list].sort((a, b) => a.name.localeCompare(b.name));
    loadPage(sortedList);
}

function filterEvent(e) {
    const filter = e.currentTarget.dataset.type;
    let filterModal;
    if(filter === 'kind') {
        filterModal = document.querySelector('#all_kind_list');
    } else if(filter === 'tag') {
        filterModal = document.querySelector('#all_tag_list');
    }
    filterModal.classList.toggle('hidden');
}

function tagFilterEvent() {
    const item = event.currentTarget,
        tagId = Number(item.dataset.tagId);
    if(item.classList.contains('active')) {
        loadPage(list);
        return;
    }

    const filteredList = list.filter(myMatzip => myMatzip.tagList.some(tag => tag.id === tagId));

    item.classList.toggle('active');

    loadPage(filteredList);
}

function kindFilterEvent() {
    const item = event.currentTarget,
        kind = item.dataset.kindName;

    if(item.classList.contains('active')) {
        loadPage(list);
        return;
    }

    const filteredList = list.filter(myMatzip => myMatzip.kindName === kind);

    item.classList.toggle('active');

    loadPage(filteredList);
}

async function getWishList() {
    const url = '/matzips/wish-list/' + who;
    wishList = await fetch(url).then(res => res.json()).then(res => res.data.map(wish => wish.id));
    console.log(wishList);
}