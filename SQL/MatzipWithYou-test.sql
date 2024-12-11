SET SESSION sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''));


SELECT *
FROM member;

select *
from friend;

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
