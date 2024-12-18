document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.tab-button').forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();

            document.querySelectorAll('.tab-button').forEach(b => b.classList.remove('active'));
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

            this.classList.add('active');
            document.getElementById(this.dataset.tab).classList.add('active');

            if (this.dataset.tab !== 'members') {
                loadData(this.dataset.tab);
            }
        });
    });

    attachDeleteListeners();
});

function attachDeleteListeners() {
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            const id = this.dataset.id;
            const type = this.dataset.type;
            if (id && type) {
                deleteItem(type, id);
            }
        });
    });
}

function loadData(type) {
    fetch(`/admin/api/${type}`)
        .then(response => response.json())
        .then(data => {
            updateTable(type, data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('데이터를 불러오는데 실패했습니다.');
        });
}

function updateTable(type, data) {
    const tbody = document.getElementById(`${type}List`);
    switch (type) {
        case 'matzips':
            tbody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.name}</td>
                    <td>${item.address}</td>
                    <td>${item.kindId}</td>
                    <td>${formatDate(item.regdate)}</td>
                    <td><button class="delete-btn" data-id="${item.id}" data-type="matzips">삭제</button></td>
                </tr>
            `).join('');
            break;
        case 'tags':
            tbody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.tagName}</td>
                    <td>${formatDate(item.regdate)}</td>
                    <td><button class="delete-btn" data-id="${item.id}" data-type="tags">삭제</button></td>
                </tr>
            `).join('');
            break;
        case 'foodkinds':
            tbody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.kindName}</td>
                    <td>${formatDate(item.regdate)}</td>
                    <td><button class="delete-btn" data-id="${item.id}" data-type="foodkinds">삭제</button></td>
                </tr>
            `).join('');
            break;
    }

    // 삭제 버튼 이벤트 리스너 추가
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            const id = this.dataset.id;
            const type = this.dataset.type;
            deleteItem(type, id);
        });
    });
}

function deleteItem(type, id) {

    if (confirm('정말 삭제하시겠습니까?')) {
        fetch(`/admin/api/${type}/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type':'application/json'
            }
        })
            .then(response => response.text())
            .then(result => {
                if (result === '삭제 성공') {
                    alert('삭제되었습니다.');
                    if (type === 'members') {
                        window.location.reload();
                    } else {
                        loadData(type);
                    }
                } else {
                    alert(result);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('삭제 중 오류가 발생했습니다.');
            });
    }
}
function formatDate(dateArray) {
    if (!Array.isArray(dateArray) || dateArray.length < 3) {
        console.warn('Invalid date array:', dateArray);
        return '';
    }

    const [year, month, day, hour = 0, minute = 0, second = 0] = dateArray;
    const date = new Date(year, month - 1, day, hour, minute, second);

    if (isNaN(date.getTime())) {
        console.warn('Invalid date:', dateArray);
        return '';
    }

    return date.toLocaleDateString('ko-KR');
}

