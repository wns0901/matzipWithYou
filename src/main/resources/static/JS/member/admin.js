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
});

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
                </tr>
            `).join('');
            break;
        case 'tags':
            tbody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.tagName}</td>
                    <td>${formatDate(item.regdate)}</td>
                </tr>
            `).join('');
            break;
        case 'foodkinds':
            tbody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.kindName}</td>
                    <td>${formatDate(item.regdate)}</td>
                </tr>
            `).join('');
            break;
    }
}

function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR');
}