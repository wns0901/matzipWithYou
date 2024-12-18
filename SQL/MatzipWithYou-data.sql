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
FROM my_matzip;
ALTER TABLE my_matzip
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

DELETE
FROM my_matzip;
ALTER TABLE my_matzip
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
        'gourmet3', '현지윤'),
       ('member_user4', '$2a$10$690GlSAI0trgN6BsyENwOOoB/zjq7h5cUVVr0KpzzgR5Xb5caLC7m', 'member4@email.com', 300,
        'test1', '테스트')
;

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
       (4, 'member3_pic.jpg', 'm3_147258369.jpg')
;

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
        '강남구', 'https://place.map.kakao.com/1745920539');

-- wish_list 테이블 샘플 데이터
INSERT INTO wish_list (member_id, matzip_id)
VALUES (1, 1)
     , (1, 2)

     , (2, 3)
     , (2, 6)

     , (3, 1)
     , (3, 5)

     , (4, 3)
     , (4, 6);

-- my_matzip 나의 맛집 테이블 샘플 데이터
INSERT INTO my_matzip (matzip_id, member_id, visibility, content, star_rating)
VALUES (1, 3, 'PRIVATE', '나만 알고 싶은 비공개 맛집', 5),
       (2, 2, 'PUBLIC', '누구에게나 알려주고 싶은 맛없는 맛집', 2),
       (5, 4, 'HIDDEN', '누군가 찾아줬으면 하는  맛집', 4),
       (4, 1, 'HIDDEN', '힌트 구매해줬으면 히든맛집', 1),
       (4, 3, 'PUBLIC', '좋아요', 1),
       (3, 2, 'PRIVATE', '개인맛집', 5),
       (1, 4, 'HIDDEN', '히든맛집', 4),
       (6, 1, 'PUBLIC', '공개맛집', 5),
       (1, 1, 'HIDDEN', '테스트', 5),
       (6, 3, 'PUBLIC', '테스트', 5),
       (3, 5, 'PUBLIC', '리뷰 테스트', 4)
;

-- friend 테이블 샘플 데이터
INSERT INTO friend(sender_id, receiver_id, intimacy, is_accept)
VALUES (1, 2, 200, TRUE),
       (1, 3, 100, TRUE),
       (1, 4, 0, FALSE),
       (2, 4, 500, TRUE),
       (2, 3, 300, TRUE),
       (3, 4, 0, FALSE),
       (2, 5, 10, TRUE)
;


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

       (2, 2, '친구들과 오기 좋은 맛집!', '2024-01-25 20:00:00', 4),
       (2, 4, '특별한 맛은 없지만 무난합니다', '2024-02-05 13:20:00', 3),
       (2, 1, '완벽한 맛집!', '2024-03-05 20:15:00', 5),

       (3, 2, '매운 음식 좋아하는 분들 추천!', '2024-01-10 11:45:00', 4),
       (3, 6, '양이 너무 많아요', '2024-03-15 14:00:00', 3),
       (3, 6, '최고의 맛집!!!', '2024-04-20 19:45:00', 5),

       (4, 1, '가성비 최고', '2024-02-15 12:10:00', 5),
       (4, 2, '분위기 좋은 레스토랑', '2024-03-25 20:30:00', 4),
       (4, 5, '실망스러운 경험', '2024-01-05 19:00:00', 2);

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
       (6, 9);

INSERT INTO matzip_tag (tag_id, my_matzip_id)
VALUES (1, 1),
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

       (1, 8),
       (2, 8),
       (3, 8),

       (1, 11),
       (2, 11),
       (3, 11)
;

--  친구관계 && 히든
INSERT INTO user_matzip_tag_status (member_id, my_matzip_id, tag_id)
VALUES (3, 2, 15),
       (4, 5, 7),
       (2, 4, 1);
