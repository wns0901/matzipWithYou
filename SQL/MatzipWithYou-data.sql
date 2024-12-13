-- 기존 데이터 삭제 및 AUTO_INCREMENT 초기화
DELETE
FROM profile_img;
ALTER TABLE profile_img
    AUTO_INCREMENT = 1;

DELETE
FROM member_authorities;
ALTER TABLE member_authorities
    AUTO_INCREMENT = 1;

DELETE
FROM member;
ALTER TABLE member
    AUTO_INCREMENT = 1;

DELETE
FROM authority;
ALTER TABLE authority
    AUTO_INCREMENT = 1;

DELETE
FROM matzip;
ALTER TABLE matzip
    AUTO_INCREMENT = 1;

DELETE
FROM food_kind;
ALTER TABLE food_kind
    AUTO_INCREMENT = 1;

DELETE
FROM wish_list;
ALTER TABLE wish_list
    AUTO_INCREMENT = 1;

DELETE
FROM my_review;
ALTER TABLE my_review
    AUTO_INCREMENT = 1;

DELETE
FROM authority;
ALTER TABLE authority
    AUTO_INCREMENT = 1;

DELETE
FROM matzip_tag;
ALTER TABLE matzip_tag
    AUTO_INCREMENT = 1;

DELETE
FROM user_matzip_tag_status;
ALTER TABLE user_matzip_tag_status
    AUTO_INCREMENT = 1;

DELETE
FROM tag;
ALTER TABLE tag
    AUTO_INCREMENT = 1;

DELETE
FROM friend;
ALTER TABLE friend
    AUTO_INCREMENT = 1;


-- authority 테이블 더미 데이터
INSERT INTO authority (name)
VALUES ('ROLE_MEMBER'),
       ('ROLE_ADMIN');

INSERT INTO member (username, password, email, point, nickname, name)
VALUES ('member_user1', '$2a$10$690GlSAI0trgN6BsyENwOOoB/zjq7h5cUVVr0KpzzgR5Xb5caLC7m', 'member1@email.com', 100,
        'foodie1', '이동희'),
       ('member_user2', '$2a$10$9eSoUc/.K3YyzQq5ZpOxnO01H0.oT8l.hlxgGDLS.mRBShESweioC', 'member2@email.com', 150,
        'matzip2', '이유나'),
       ('admin1_admin1', '$2a$10$csFKAeIbLoeMc.CdW0/AN.ydPVEZstCSLUf6L2st8ExYO4.JakF8.', 'admin@email.com', 500,
        'admin_kim', '장준영'),
       ('member_user3', '$2a$10$Qk.A1REypdS/s1YmvjmiIO2ES/6AfB45asIW7kM/7d3yLbOKp1zS2', 'member3@email.com', 80,
        'gourmet3', '현지윤');

-- member_authorities 테이블 더미 데이터
INSERT INTO member_authorities (authorities_id, member_id)
VALUES (1, 1), -- member1은 일반 사용자
       (1, 2), -- member2는 일반 사용자
       (1, 3), -- admin1은 일반 사용자 권한도 가짐
       (2, 3), -- admin1은 관리자 권한도 가짐
       (1, 4);
-- member3은 일반 사용자

-- profile_img 테이블 더미 데이터
INSERT INTO profile_img (member_id, sourcename, filename)
VALUES (1, 'member1_profile.jpg', 'm1_123456789.jpg'),
       (2, 'member2_avatar.png', 'm2_987654321.png'),
       (3, 'admin_photo.jpg', 'ad_456789123.jpg'),
       (4, 'member3_pic.jpg', 'm3_147258369.jpg');

-- food_kind 테이블 샘플 데이터
INSERT INTO food_kind (kindname)
VALUES ('기타'),
       ('한식'),
       ('중식'),
       ('일식'),
       ('양식'),
       ('요리주점'),
       ('디저트');


-- matzip 테이블 샘플 데이터
INSERT INTO matzip
    (name, kind_id, address, lat, lng, img_url, gu, kakao_map_url)
VALUES ('치킨공식', 2, '서울 강남구 역삼로17길 64 역삼동A4스페이스 1층', '37.49882190149589', '127.03662632505247',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F0C4DB8929DDF4EAFA24A56B9EB3BAB97',
        '강남구', 'https://place.map.kakao.com/753087736'),
       ('오늘런치뷔페', 2, '서울 강남구 봉은사로30길 75 2층', '37.50274785411347', '127.03720502093141',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F36E80700682E4557A4F7BF32051C03B6',
        '강남구', 'https://place.map.kakao.com/727693380'),
       ('누나네', 6, '서울 강남구 삼성로100길 23-15', '37.51101353730648', '127.05679629393407',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2FCBE63A0A25FB4C7F9DD8D9C3240725E3',
        '강남구', 'https://place.map.kakao.com/1560343065'),
       ('아부라소바', 4, '서울 강남구 학동로43길 8 B동 1층 아부라소바', '37.51659394797542', '127.03750316407037',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F42F69920ACCA40168D5B071B9C68049B',
        '강남구', 'https://place.map.kakao.com/620324880'),
       ('무탄 압구정 본점', 3, '서울 강남구 논현로176길 22 1층', '37.52729767419066', '127.03030491751514',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2FE705A4F2806045B68BE0E2734D3D3F94',
        '강남구', 'https://place.map.kakao.com/1696571508'),
       ('쇼토 압구정본점', 7, '서울 강남구 압구정로18길 14-6 1층', '37.52384822235571', '127.02430786508214',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocalfiy%2F4D8398A52D7446B78C0CAB2326DAC7D6',
        '강남구', 'https://place.map.kakao.com/1745920539'),
       ('양푼이 김치찌개', 2, '서울특별시 동작구 흑석동 흑석로13마길 65-3', '37.507610', '126.959073',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2FF2826B4071874EFEBF1A363CD4C0C7EA'
           , '동작구', 'https://place.map.kakao.com/948039588'),
       ('푸지미곱창', 2, '서울 강동구 천호옛14길 23 (우)05379', '37.538221', '127.127048',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Ffiy_reboot%2Fplace%2F069AEDBA4F614BD999D07C701F6F984B',
        '강동구', 'https://place.map.kakao.com/13561834'),
       ('과자굽는땅콩누나', 7, '서울 강동구 구천면로35길 16 (우)05325', '37.543861', '127.131027',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F13B3294A33FA41C9A9F50DB6F6E9FD80'
           , '강동구', 'https://place.map.kakao.com/698088212'),
       ('멘야세븐', 4, '서울 강동구 양재대로112길 45 (우)05351', '37.536424', '127.142479',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F8C0F434FF43940F68EA7EEC072A43FB2',
        '강동구', 'https://place.map.kakao.com/167078299'),
       ('쉬즈베이글 덕대점', 5, '서울 강북구 삼양로 526 1층 (우)01044', '37.652956', '127.014603',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F15D9296A3191493A90FBDC569121D11C',
        '강북구', 'https://place.map.kakao.com/2011565641'),
       ('파스타브로', 5, '서울 강북구 도봉로87길 34-19 1-2층 (우)01072', '37.639802', '127.024341',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2FB5BC12F8D9404598AFF6C95F2EDEB34D',
        '강북구', 'https://place.map.kakao.com/1094005149'),
       ('등용문', 6, '서울 강북구 도봉로87길 38 2층', '37.639286', '127.023827',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F39C61DE114D641A7A0F4C30C6D8A5678',
        '강북구', 'https://place.map.kakao.com/2139746664'),
       ('레피큐르', 1, '서울 강서구 공항대로 247 마곡퀸즈파크나인 A동 205호 (우)07803', '37.559485', '126.834946',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocalfiy%2FACB10F3A02404AA6A33ADDF9F211FF0A',
        '강서구', 'https://place.map.kakao.com/27057082'),
       ('옛날한우곱창전문 본점', 2, '서울 강서구 공항대로59길 32 (우)07563', '37.553379', '126.863930',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fcfile%2F2461DA475600A01813',
        '강서구', 'https://place.map.kakao.com/16535922'),
       ('홍린', 3, '서울 강서구 우장산로15길 25 (우)07649', '37.554522', '126.850519',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F83238960D2DF4D6BB5ECB63CC39BF603',
        '강서구', 'https://place.map.kakao.com/16851845'),
       ('정전기하이볼바', 6, '서울 관악구 관악로14길 63 2층 (우)08789', '37.478874', '126.955965',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2FF34F5B24EC96483086974E72CE5452C1',
        '관악구', 'https://place.map.kakao.com/2101280481'),
       ('서라당', 7, '서울 관악구 신사로 120-1 1층 (우)08702', '37.488286', '126.915553',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2FD1D08C20AC0B4F68A0F4C6CD8DFF7596',
        '관악구', 'https://place.map.kakao.com/1605962937'),
       ('문득', 5, '서울 관악구 관악로14길 8 1층 (우)08788', '37.479300', '126.952983',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2FBB368D738FB1481EB8D6305999C253FC',
        '관악구', 'https://place.map.kakao.com/1223991903'),
       ('멕시칼리', 1, '서울 광진구 능동로36길 181 1층 (우)04987', '37.552875', '127.088337',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F79105F3B09B14A2CA83BD7400562754E',
        '광진구', 'https://place.map.kakao.com/1130537615'),
       ('호야초밥참치 본점', 4, '서울 광진구 능동로13길 39 한아름건물 1층 (우)05016', '37.543489', '127.070253',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fcfile%2F2311394D533413D80A',
        '광진구', 'https://place.map.kakao.com/11989881'),
       ('천미향', 3, '서울 광진구 능동로 237 2층 (우)04998', '37.551449', '127.076119',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F45E4E097F9804023AECA441A1AC7D3D9',
        '광진구', 'https://place.map.kakao.com/833758468'),
       ('라꾸긴', 4, '서울 구로구 가마산로 268 (우)08302', '37.495690', '126.890185',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2FEDD3B2D8434E4F019277F9516894CC4E',
        '구로구', 'https://place.map.kakao.com/24529262'),
       ('봉고기 신도림본점', 2, '서울 구로구 경인로 619-3 상가 1층 112~113호 (우)08210', '37.507107', '126.883964',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F07BAEF09BF4D4CECA86A621BF7C01D45',
        '구로구', 'https://place.map.kakao.com/1151964774'),
       ('오비스트로', 5, '서울 구로구 구로중앙로15길 12 1층 101호 (우)08306', '37.494059', '126.889241',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2FA177A68FE4404DC4BA1F3C6F3E3F37EB',
        '구로구', 'https://place.map.kakao.com/1044434257'),
       ('동흥관', 3, '서울 금천구 시흥대로63길 20 B동 지하1층, 1,2층 (우)08614', '37.455349', '126.898930',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Ffiy_reboot%2Fplace%2F3606AE767B9A40DDBEF869A38278DD28',
        '금천구', 'https://place.map.kakao.com/11833906'),
       ('개성손만두요리전문점 가산디지털점', 2, '서울 금천구 가산로9길 22 (우)08515', '37.476842', '126.891243',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fcfile%2F263E054D5812AB5208',
        '금천구', 'https://place.map.kakao.com/1454070757'),
       ('커피라이터', 7, '서울 금천구 독산로 336 아우르하우스 1층 101호 (우)08550', '37.476583', '126.903833',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F62046405C8CB4F01B068669024DC6FB6',
        '금천구', 'https://place.map.kakao.com/63260281'),
       ('딥', 6, '서울 금천구 범안로 1249 1층 (우)08581', '37.466739', '126.901164',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Ffiy_reboot%2Fplace%2F1962F87D025C49E3AD40846C448D81C7',
        '금천구', 'https://place.map.kakao.com/1824138440'),
       ('그래인스쿠키 롯데백화점노원점', 7, '서울 노원구 동일로 1414 지하 1층 (우)01695', '37.666831', '127.058332',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F2EA1B808D32447B8BAC49C37D516B304',
        '노원구', 'https://place.map.kakao.com/21413862'),
       ('푸욱젤라또', 7, '서울 노원구 상계로9가길 23 1층 (우)01685', '37.659221', '127.067543',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F0AC529EF404B4E5092998441E1CFE60E',
        '노원구', 'https://place.map.kakao.com/669418954'),
       ('브로이하우스바네하임', 6, '서울 노원구 공릉로32길 54 고려빌딩 1-2층 (우)01826', '37.621888', '127.082989',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Ffiy_reboot%2Fplace%2FAF5D4D2C0F804AD080B462BB2705EDE0',
        '노원구', 'https://place.map.kakao.com/16999919'),
       ('조이키친', 5, '서울 도봉구 삼양로144길 3-8 (우)01368', '37.651885', '127.013799',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Ffiy_reboot%2Fplace%2F780B864AD79847F3BAB17CB422FD79DC',
        '도봉구', 'https://place.map.kakao.com/554696595'),
       ('양국', 4, '서울 도봉구 삼양로 538-6 1층 102호 (우)01368', '37.651129', '127.013357',
        '#none', '도봉구', 'https://place.map.kakao.com/480094911'),
       ('엘수에뇨', 1, '서울 도봉구 삼양로142길 3 1층 (우)01368', '37.653386', '127.041393',
        '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Ffiy_reboot%2Fplace%2F9D96E2AA879D4F51B029AA127B516E4F',
        '도봉구', 'https://place.map.kakao.com/722569390');
;


-- wish_list 테이블 샘플 데이터
INSERT INTO wish_list (member_id, matzip_id)
VALUES (1, 1)
     , (1, 2)
     , (1, 8)
     , (1, 9)

     , (2, 3)
     , (2, 6)
     , (2, 10)
     , (2, 11)

     , (3, 1)
     , (3, 5)
     , (3, 12)
     , (3, 13)

     , (4, 3)
     , (4, 6)
     , (4, 14)
     , (4, 7)
;


-- my_matzip 나의 맛집 테이블 샘플 데이터
INSERT INTO my_matzip (matzip_id, member_id, visibility, content, star_rating)
VALUES (1, 3, 'PRIVATE', '나만 알고 싶은 비공개 맛집', 5),
       (2, 2, 'PUBLIC', '누구에게나 알려주고 싶은 맛없는 맛집', 2),
       (5, 4, 'HIDDEN', '누군가 찾아줬으면 하는  맛집', 4),
       (6, 1, 'HIDDEN', '힌트 구매해줬으면 히든맛집', 1),
       (4, 3, 'PUBLIC', '좋아요', 1),
       (3, 2, 'PRIVATE', '개인맛집', 5),
       (1, 4, 'HIDDEN', '히든맛집', 4),
       (6, 1, 'PUBLIC', '공개맛집', 5),
       (7, 4, 'HIDDEN', '현지윤의 비밀맛집', 5),
       (7, 1, 'PUBLIC', '양푼이 김치찌개 맛있어요!', 4),
       (8, 2, 'PRIVATE', '푸지미곱창 강추합니다', 5),
       (9, 3, 'PUBLIC', '디저트가 정말 맛있네요', 5),
       (10, 4, 'HIDDEN', '일식 맛집 발견!', 4),
       (11, 1, 'PUBLIC', '베이글이 너무 부드러워요', 5),
       (12, 2, 'PRIVATE', '파스타가 일품입니다', 4),
       (13, 3, 'HIDDEN', '분위기가 좋아요', 3),
       (14, 4, 'PUBLIC', '레피큐르 강추!', 5);
;

-- friend 테이블 샘플 데이터
INSERT INTO friend(sender_id, receiver_id, intimacy, is_accept)
VALUES (1, 2, 200, TRUE),
       (1, 3, 100, TRUE),
       (1, 4, 0, FALSE),
       (2, 4, 500, TRUE),
       (2, 3, 300, TRUE),
       (3, 4, 0, FALSE);


-- tag 데이터 생성
INSERT INTO tag (tagname)
VALUES ('혼밥'),         -- 1
       ('느낌 좋은'),      -- 2
       ('JMT'),        -- 3
       ('맛있다'),        -- 4
       ('깔끔하다'),       -- 5
       ('친절하다'),       -- 6
       ('양많다'),        -- 7
       ('분위기좋다'),      -- 8
       ('가성비굿'),       -- 9
       ('음식이 빨리 나와요'), -- 10
       ('카공'),         -- 11
       ('친구와 함께'),     -- 12
       ('연인과 함께'),     -- 13
       ('매장이 청결해여'),   -- 14
       ('인테리어가 멋져요');
-- 15

-- my_review 테이블 샘플 데이터
INSERT INTO my_review (member_id, matzip_id, content, regdate, star_rating)
VALUES (1, 3, '분위기 좋고 음식이 정말 맛있어요!', '2024-01-15 12:30:00', 5),
       (1, 4, '조금 비싸지만 퀄리티는 좋네요', '2024-02-20 18:45:00', 4),
       (1, 6, '서비스가 별로였어요', '2024-03-10 19:15:00', 2),
       (1, 7, '김치찌개가 정말 맛있어요', '2024-03-01 14:30:00', 4),
       (1, 11, '베이글이 부드럽고 맛있어요', '2024-03-05 11:30:00', 5),

       (2, 2, '친구들과 오기 좋은 맛집!', '2024-01-25 20:00:00', 4),
       (2, 4, '특별한 맛은 없지만 무난합니다', '2024-02-05 13:20:00', 3),
       (2, 1, '완벽한 맛집!', '2024-03-05 20:15:00', 5),
       (2, 8, '곱창의 신선도가 최고!', '2024-03-02 18:20:00', 5),
       (2, 12, '파스타가 알덴테!', '2024-03-06 20:15:00', 4),

       (3, 2, '매운 음식 좋아하는 분들 추천!', '2024-01-10 11:45:00', 4),
       (3, 6, '양이 너무 많아요', '2024-03-15 14:00:00', 3),
       (3, 6, '최고의 맛집!!!', '2024-04-20 19:45:00', 5),
       (3, 9, '디저트 맛집 발견!', '2024-03-03 15:45:00', 5),
       (3, 13, '분위기도 좋고 음식도 맛있어요', '2024-03-07 17:40:00', 4),

       (4, 1, '가성비 최고', '2024-02-15 12:10:00', 5),
       (4, 2, '분위기 좋은 레스토랑', '2024-03-25 20:30:00', 4),
       (4, 7, '굿굿', '2024-01-05', 5),
       (4, 5, '실망스러운 경험', '2024-01-05 19:00:00', 2),
       (4, 10, '라멘이 일품이에요', '2024-03-04 19:10:00', 4),
       (4, 14, '레피큐르 또 방문하고 싶어요', '2024-03-08 13:25:00', 5);

-- review_tag
INSERT INTO review_tag (my_review_id, tag_id)
VALUES (1, 1),
       (1, 5),
       (1, 2),
       (1, 8),
       (1, 9),
       (2, 7),
       (2, 5),
       (2, 12),
       (2, 11),
       (2, 9),
       (3, 1),
       (3, 5),
       (3, 2),
       (4, 8),
       (4, 9),
       (4, 10),
       (4, 3),
       (5, 12),
       (5, 15),
       (5, 14),
       (6, 1),
       (6, 15),
       (6, 7),
       (6, 3),
       (6, 9),
       (7, 6),
       (7, 8),
       (7, 2),
       (7, 7),
       (8, 4),
       (8, 9),
       (9, 7),
       (9, 6),
       (10, 8),
       (10, 13),
       (11, 5),
       (11, 14),
       (12, 12),
       (12, 8),
       (13, 13),
       (13, 15),
       (14, 4),
       (14, 9)
;

insert into matzip_tag (tag_id, my_matzip_id)
values (1, 1),
       (12, 1),
       (11, 1),

       (15, 2),
       (6, 2),
       (8, 2),

       (9, 3),
       (11, 3),
       (3, 3),

       (3, 4),
       (1, 4),
       (2, 4),
       (8, 4),
       (9, 4),

       (2, 5),
       (9, 5),
       (7, 5),

       (7, 6),
       (2, 6),
       (1, 6),

       (1, 7),
       (2, 7),
       (3, 7),
       (4, 7),

       (1, 8),
       (2, 8),
       (3, 8),

       (4, 9),
       (8, 9),

       (9, 10),
       (3, 10),

       (2, 11),
       (15, 11),

       (7, 12),
       (8, 12),

       (13, 13),
       (8, 13),

       (9, 14),
       (4, 14)
;

--  친구관계 && 히든
insert into user_matzip_tag_status (member_id, my_matzip_id, tag_id)
values (3, 2, 15),
       (4, 5, 7),
       (2, 4, 1);
