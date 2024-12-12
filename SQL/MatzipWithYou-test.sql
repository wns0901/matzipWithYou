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