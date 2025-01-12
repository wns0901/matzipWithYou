SET SESSION sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''));


SELECT *
FROM member;

select *
from friend;

select *
from my_matzip;

select *
from profile_img;


SELECT m.id, m.matzip_id, m.member_id, m.visibility
FROM my_matzip m
WHERE m.visibility = 'HIDDEN';

select m.id, m.matzip_id, m.member_id, m.visibility
from my_matzip m
where m.visibility = 'HIDDEN';

SELECT m.id AS 'myMatzipId',
       m.matzip_id AS 'matzipId',
       m.member_id AS 'memberId',
       m.visibility AS 'visibility',
       f.kindname AS 'kindName',
       mt.tag_id AS 'tagId',
       t.tagname AS 'tagName'
FROM my_matzip m
         LEFT JOIN matzip mz ON m.matzip_id = mz.id
         LEFT JOIN food_kind f ON mz.kind_id = f.id
         LEFT JOIN matzip_tag mt ON m.id = mt.my_matzip_id
         LEFT JOIN tag t ON mt.tag_id = t.id
         JOIN user_matzip_tag_status ut ON m.id = ut.my_matzip_id
WHERE m.visibility = 'HIDDEN'
  and m.id = ut.my_matzip_id
ORDER BY m.regdate;


SELECT
       m.matzip_id AS 'matzipId',
       m.member_id AS 'memberId',
       m.visibility AS 'visibility',
       f.kindname AS 'kindName',
       mt.tag_id AS 'tagId',
       t.tagname AS 'tagName'
FROM my_matzip m
        right JOIN user_matzip_tag_status ut
              ON m.id = ut.my_matzip_id
          JOIN matzip mz ON m.matzip_id = mz.id
          JOIN food_kind f ON mz.kind_id = f.id
          JOIN matzip_tag mt ON m.id = mt.my_matzip_id
          JOIN tag t ON mt.tag_id = t.id
WHERE m.visibility = 'HIDDEN'
ORDER BY m.regdate;


SELECT DISTINCT
    m.matzip_id AS 'matzipId',
    m.member_id AS 'memberId',
    m.visibility AS 'visibility',
    f.kindname AS 'kindName',
    mt.tag_id AS 'tagId',
    t.tagname AS 'tagName'
FROM my_matzip m
         JOIN user_matzip_tag_status ut
              ON m.id = ut.my_matzip_id
         LEFT JOIN matzip mz ON m.matzip_id = mz.id
         LEFT JOIN food_kind f ON mz.kind_id = f.id
         LEFT JOIN matzip_tag mt ON m.id = mt.my_matzip_id
         LEFT JOIN tag t ON mt.tag_id = t.id
WHERE m.visibility = 'HIDDEN';

SELECT DISTINCT
    m.matzip_id AS 'matzipId',
    m.member_id AS 'memberId',
    m.visibility AS 'visibility',
    f.kindname AS 'kindName',
    mt.tag_id AS 'tagId',
    t.tagname AS 'tagName'
FROM my_matzip m
         LEFT JOIN matzip mz ON m.matzip_id = mz.id
         LEFT JOIN food_kind f ON mz.kind_id = f.id
         LEFT JOIN matzip_tag mt ON m.id = mt.my_matzip_id
         LEFT JOIN tag t ON mt.tag_id = t.id
         JOIN user_matzip_tag_status ut
              ON m.id = ut.my_matzip_id
                  AND mt.tag_id = ut.tag_id
WHERE m.visibility = 'HIDDEN';


SELECT DISTINCT
    m.matzip_id AS 'matzipId',
    m.member_id AS 'memberId',
    m.visibility AS 'visibility',
    f.kindname AS 'kindName',
    mt.tag_id AS 'tagId',
    t.tagname AS 'tagName'
FROM my_matzip m
         LEFT JOIN matzip mz ON m.matzip_id = mz.id
         LEFT JOIN food_kind f ON mz.kind_id = f.id
         LEFT JOIN matzip_tag mt ON m.id = mt.my_matzip_id
         LEFT JOIN tag t ON mt.tag_id = t.id
         JOIN user_matzip_tag_status ut
              ON m.id = ut.my_matzip_id
                  AND mt.tag_id = ut.tag_id
                  AND m.member_id = ut.member_id
WHERE m.visibility = 'HIDDEN';

SELECT
    f.memberId AS memberId,
    m.nickname AS nickname,
    p.filename AS profileImg,
    JSON_ARRAYAGG(
            IF(mmz.visibility = 'PUBLIC', mz.gu, NULL)
    ) AS publicGu,
    JSON_ARRAYAGG(
            IF(mmz.visibility = 'HIDDEN', mz.gu, NULL)
    ) AS hiddenGu
FROM (
         SELECT IF(receiver_id = 1, sender_id, receiver_id) AS memberId
         FROM friend
         WHERE receiver_id = 1 OR sender_id = 1
     ) f
         JOIN my_matzip mmz ON f.memberId = mmz.member_id AND mmz.visibility != 'PRIVATE'
         JOIN member m ON f.memberId = m.id
         JOIN profile_img p ON f.memberId = p.member_id
         JOIN matzip mz ON mmz.matzip_id = mz.id
GROUP BY f.memberId, m.nickname, p.filename;

SELECT
        json_object(
                        'id', mymz.id,
                        'matzipId', mymz.matzip_id,
                        'regdate', mymz.regdate,
                        'visibility', mymz.visibility,
                        'name', mz.name,
                        'imgUrl', mz.img_url
                      ) AS matzipInfo,
        json_object(
                            'starRating', mymz.star_rating,
                            'content', mymz.content,
                            'kindName', k.kindname,
                            'tagList', json_arrayagg(json_object('id', t.id, 'tagName', t.tagname))
                        ) AS matzipReview,
        json_array(
                      json_object(
                            'id', mr.id,
                            'starRating', mr.star_rating,
                            'content', mr.content,
                            'kindName', k.kindname,
                            'regdate', mr.regdate,
                            'tagList', (SELECT JSON_ARRAYAGG( JSON_OBJECT('id', rt.id, 'tagName', rt.tagname) ) FROM tag rt WHERE rt.id = rtl.tag_id)
                    )
                  ) AS review
FROM my_matzip mymz
    JOIN matzip mz ON mymz.matzip_id = mz.id
    JOIN food_kind k ON mz.kind_id = k.id
    JOIN matzip_tag mt ON mymz.id = mt.my_matzip_id
    JOIN tag t ON mt.tag_id = t.id
    JOIN my_review mr ON mymz.matzip_id = mr.matzip_id AND mr.member_id = mymz.member_id
    JOIN review_tag rtl ON mr.id = rtl.my_review_id
WHERE mymz.member_id = 1
GROUP BY mymz.id, mymz.matzip_id, mymz.regdate, mr.id, mr.regdate
ORDER BY mymz.regdate DESC , mr.regdate DESC
;

SELECT *
FROM my_matzip mymz
         JOIN matzip mz ON mymz.matzip_id = mz.id
         JOIN food_kind k ON mz.kind_id = k.id
         JOIN matzip_tag mt ON mymz.id = mt.my_matzip_id
         JOIN tag t ON mt.tag_id = t.id
         JOIN my_review mr ON mymz.matzip_id = mr.matzip_id AND mr.member_id = mymz.member_id
         JOIN review_tag rtl ON mr.id = rtl.my_review_id
WHERE mymz.member_id = 1
;





SELECT
    mymz.id AS id,
    mymz.member_id AS memberId,
    mymz.matzip_id AS matzipId,
    mymz.regdate AS regdate,
    mymz.visibility AS visibility,
    mymz.content AS content,
    mymz.star_rating AS starRating,
    mz.name AS name,
    mz.img_url AS imgUrl,
    k.kindname AS kindName,
    json_arrayagg(t.tagname) AS tagNames
FROM my_matzip mymz
         JOIN matzip mz ON mymz.matzip_id = mz.id
         JOIN food_kind k ON mz.kind_id = k.id
         JOIN matzip_tag mt ON mymz.id = mt.my_matzip_id
         JOIN tag t ON mt.tag_id = t.id

WHERE mymz.member_id = 1
GROUP BY 1,2,3
ORDER BY 4 DESC;


-- my_matzip에 추가
INSERT INTO my_matzip (matzip_id, member_id, visibility, content, star_rating)
VALUES (15, 5, 'PUBLIC', '분위기도 좋고 안주도 맛있어요! 친구들과 오기 좋은 곳입니다.', 5);

-- 방금 추가된 my_matzip의 id를 사용해서 matzip_tag 추가
INSERT INTO matzip_tag (tag_id, my_matzip_id)
VALUES (8, (SELECT id FROM my_matzip WHERE member_id = 5 AND matzip_id = 15)),  -- 분위기좋다
       (12, (SELECT id FROM my_matzip WHERE member_id = 5 AND matzip_id = 15)), -- 친구와 함께
       (4, (SELECT id FROM my_matzip WHERE member_id = 5 AND matzip_id = 15));  -- 맛있다

-- my_review에 추가
INSERT INTO my_review (member_id, matzip_id, content, star_rating)
VALUES (5, 15, '분위기가 정말 좋고 안주가 맛있어요. 특히 안주가 양이 많아서 좋았습니다!', 5);

-- 방금 추가된 review의 id를 사용해서 review_tag 추가
INSERT INTO review_tag (my_review_id, tag_id)
VALUES ((SELECT id FROM my_review WHERE member_id = 5 AND matzip_id = 15), 8),  -- 분위기좋다
       ((SELECT id FROM my_review WHERE member_id = 5 AND matzip_id = 15), 12), -- 친구와 함께
       ((SELECT id FROM my_review WHERE member_id = 5 AND matzip_id = 15), 7);  -- 양많다

INSERT INTO wish_list (member_id, matzip_id)
VALUES (5, 15);

INSERT INTO user_matzip_tag_status (member_id, my_matzip_id, tag_id)
VALUES (5, (SELECT id FROM my_matzip WHERE member_id = 5 AND matzip_id = 15), 8);

SELECT * FROM my_matzip WHERE member_id = 5;

SELECT * FROM friend
WHERE (sender_id = 5 AND receiver_id = 6)
   OR (sender_id = 6 AND receiver_id = 5);

SELECT * FROM wish_list WHERE member_id = 5;

-- 5번과 6번의 양방향 친구관계 재설정
INSERT INTO friend (sender_id, receiver_id, intimacy, is_accept)
VALUES (5, 6, 10, true);


-- my_matzip 재설정 (기존 데이터 있다면 삭제 후)
DELETE FROM my_matzip WHERE member_id = 5;
INSERT INTO my_matzip (matzip_id, member_id, visibility, content, star_rating)
VALUES (15, 5, 'PUBLIC', '분위기도 좋고 안주도 맛있어요! 친구들과 오기 좋은 곳입니다.', 5);

-- 관련 태그 추가
INSERT INTO matzip_tag (tag_id, my_matzip_id)
SELECT 8, id FROM my_matzip WHERE member_id = 5 AND matzip_id = 15;

SELECT * FROM my_matzip WHERE member_id = 5;
SELECT * FROM friend WHERE sender_id = 5 OR receiver_id = 5;
SELECT * FROM matzip WHERE id = 15;
SELECT * FROM matzip_tag mt
                  JOIN my_matzip mm ON mt.my_matzip_id = mm.id
WHERE mm.member_id = 5;


SELECT * FROM matzip WHERE gu = '광진구';

SELECT
    f.memberid AS 'friendId',
    m.nickname AS 'nickname',
    m.username AS 'username',
    f.intimacy AS 'intimacy',
    p.filename AS 'profileImg'
FROM (SELECT IF(receiver_id = 2, sender_id, receiver_id) AS memberid,
                intimacy
                FROM friend f
                WHERE (receiver_id = 2 OR sender_id = 2)
            AND is_accept = 1) f
JOIN member m ON m.id = f.memberid
JOIN my_matzip mz ON f.memberid = mz.member_id AND mz.matzip_id = 6 AND mz.visibility = 'HIDDEN'
LEFT JOIN profile_img p ON m.id = p.id;


SELECT * FROM matzip_tag
WHERE (tag_id = 7 AND my_matzip_id = 5)
   OR (tag_id = 1 AND my_matzip_id = 4)
   OR (tag_id = 15 AND my_matzip_id = 2);
