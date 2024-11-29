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

-- authority 테이블 더미 데이터
INSERT INTO authority (name)
VALUES ('ROLE_MEMBER'),
       ('ROLE_ADMIN');

-- member 테이블 더미 데이터
INSERT INTO member (username, password, email, point, nickname, name)
VALUES ('member1', 'password123', 'member1@email.com', 100, 'foodie1', '이동희'),
       ('member2', 'password456', 'member2@email.com', 150, 'matzip2', '이유나'),
       ('admin1', 'admin123', 'admin@email.com', 500, 'admin_kim', '장준영'),
       ('member3', 'password789', 'member3@email.com', 80, 'gourmet3', '현지윤');

-- member_authorities 테이블 더미 데이터
INSERT INTO member_authorities (authorities_id, member_id)
VALUES (1, 1), -- member1은 일반 사용자
       (1, 2), -- member2는 일반 사용자
       (1, 3), -- admin1은 일반 사용자 권한도 가짐
       (2, 3), -- admin1은 관리자 권한도 가짐
       (1, 4);-- member3은 일반 사용자

-- profile_img 테이블 더미 데이터
INSERT INTO profile_img (member_id, sourcename, filename)
VALUES (1, 'member1_profile.jpg', 'm1_123456789.jpg'),
       (2, 'member2_avatar.png', 'm2_987654321.png'),
       (3, 'admin_photo.jpg', 'ad_456789123.jpg'),
       (4, 'member3_pic.jpg', 'm3_147258369.jpg');

-- food_kind 테이블 샘플 데이터
INSERT INTO food_kind (kindname) VALUES ('기타'),
                                        ('한식'),
                                        ('중식'),
                                        ('일식'),
                                        ('양식'),
                                        ('요리주점'),
                                        ('디저트');


-- matzip 테이블 샘플 데이터
INSERT INTO matzip
(name, kind_id, address, lat, lng, img_url, gu, kakao_map_url)
VALUES
    ('치킨공식', 2, '서울 강남구 역삼로17길 64 역삼동A4스페이스 1층', '37.49882190149589', '127.03662632505247', '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F0C4DB8929DDF4EAFA24A56B9EB3BAB97', '강남구','https://place.map.kakao.com/753087736'),
    ('오늘런치뷔페', 2, '서울 강남구 봉은사로30길 75 2층', '37.50274785411347', '127.03720502093141', '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2F36E80700682E4557A4F7BF32051C03B6', '강남구','https://place.map.kakao.com/727693380'),
    ('누나네', 6, '서울 강남구 삼성로100길 23-15', '37.51101353730648', '127.05679629393407', '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Fplace%2FCBE63A0A25FB4C7F9DD8D9C3240725E3', '강남구', 'https://place.map.kakao.com/1560343065'),
    ('아부라소바', 4, '서울 강남구 학동로43길 8 B동 1층 아부라소바', '37.51659394797542', '127.03750316407037', '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2F42F69920ACCA40168D5B071B9C68049B', '강남구', 'https://place.map.kakao.com/620324880'),
    ('무탄 압구정 본점', 3, '서울 강남구 논현로176길 22 1층', '37.52729767419066', '127.03030491751514', '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.kakaocdn.net%2Fmystore%2FE705A4F2806045B68BE0E2734D3D3F94', '강남구', 'https://place.map.kakao.com/1696571508'),
    ('쇼토 압구정본점', 7, '서울 강남구 압구정로18길 14-6 1층', '37.52384822235571', '127.02430786508214', '//t1.kakaocdn.net/thumb/T800x0.q50/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flocalfiy%2F4D8398A52D7446B78C0CAB2326DAC7D6','강남구', 'https://place.map.kakao.com/1745920539');

-- wish_list 테이블 샘플 데이터
INSERT INTO wish_list (member_id, matzip_id)
VALUES
(1,1)
     ,(1,2)

     ,(2,3)
     ,(2,6)

     ,(3,1)
     ,(3,5)

     ,(4,3)
     ,(4,6);