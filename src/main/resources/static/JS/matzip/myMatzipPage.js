loadPage(data)

function loadPage(data) {
    const list = data.list,
    fragment = document.createDocumentFragment(),
    cardList = document.querySelector('#myMatzip_card_list');

    list.forEach((myMatzip) => {
        fragment.appendChild(makeMyMatzipCard(myMatzip))
    });

    cardList.appendChild(fragment);
}

function makeMyMatzipCard(myMatzip) {
    const card = document.createElement('div'),
        img = document.createElement('img'),
        textDiv = document.createElement('div'),
        name = document.createElement('h3'),
        address = document.createElement('p'),
        visibilityBtnList = document.createElement('div'),
        publicBtn = document.createElement('button'),
        privateBtn = document.createElement('button'),
        hiddenBtn = document.createElement('button'),
        deleteBtn = document.createElement('button'),
        moreDetailBtn = document.createElement('button'),
        moreDetailDiv = document.createElement('div'),
        starRating = document.createElement('div'),
        foodKind = document.createElement('div'),
        content = document.createElement('div'),
        tagList = document.createElement('div'),
        detailTitle = document.createElement('h3')
    ;

    card.classList.add('card');
    img.classList.add('matzip_img');
    textDiv.classList.add('text_div');
    name.classList.add('name');
    address.classList.add('address');
    visibilityBtnList.classList.add('visibility_btn_list');
    publicBtn.classList.add('visibility_btn','PUBLIC');
    privateBtn.classList.add('visibility_btn','PRIVATE');
    hiddenBtn.classList.add('visibility_btn','HIDDEN');
    deleteBtn.classList.add('delete_btn');
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
    moreDetailBtn.addEventListener('click', moreDetailEvent);
    publicBtn.addEventListener('click', updateVisibilityEvent);
    privateBtn.addEventListener('click', updateVisibilityEvent);
    hiddenBtn.addEventListener('click', updateVisibilityEvent);

    visibilityBtnList.appendChild(publicBtn);
    visibilityBtnList.appendChild(hiddenBtn);
    visibilityBtnList.appendChild(privateBtn);

    textDiv.appendChild(name);
    textDiv.appendChild(address);
    card.appendChild(img);
    card.appendChild(textDiv);
    card.appendChild(visibilityBtnList);
    card.appendChild(deleteBtn);
    card.appendChild(moreDetailBtn);
    card.appendChild(moreDetailDiv);

    return card;
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

function sortEvent() {}

function filterEvent() {}