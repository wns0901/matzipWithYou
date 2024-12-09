const tooltip = document.getElementById('tooltip');
const paths = document.querySelectorAll('#seoul-map path');

const districtNames = {
    'Dobong-gu': '도봉구',
    'Dongdaemun-gu': '동대문구',
    'Dongjak-gu': '동작구',
    'Eunpyeong-gu': '은평구',
    'Gangbuk-gu': '강북구',
    'Gangdong-gu': '강동구',
    'Gangseo-gu': '강서구',
    'Geumcheon-gu': '금천구',
    'Guro-gu': '구로구',
    'Gwanak-gu': '관악구',
    'Gwangjin-gu': '광진구',
    'Gangnam-gu': '강남구',
    'Jongno-gu': '종로구',
    'Jung-gu': '중구',
    'Jungnang-gu': '중랑구',
    'Mapo-gu': '마포구',
    'Nowon-gu': '노원구',
    'Seocho-gu': '서초구',
    'Seodaemun-gu': '서대문구',
    'Seongbuk-gu': '성북구',
    'Seongdong-gu': '성동구',
    'Songpa-gu': '송파구',
    'Yangcheon-gu': '양천구',
    'Yeongdeungpo-gu_1_': '영등포구',
    'Yongsan-gu': '용산구'
};

paths.forEach(path => {
    path.addEventListener('mousemove', (e) => {
        const districtId = path.id;
        const koreanName = districtNames[districtId] || districtId;

        tooltip.style.display = 'block';
        tooltip.style.left = e.pageX + 10 + 'px';
        tooltip.style.top = e.pageY + 10 + 'px';
        tooltip.textContent = koreanName;
    });

    path.addEventListener('mouseleave', () => {
        tooltip.style.display = 'none';
    });
});