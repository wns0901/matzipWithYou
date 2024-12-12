SELECT *
FROM member;

select *
from friend;

SELECT m.id, m.matzip_id, m.member_id, m.visibility
FROM my_matzip m
WHERE m.visibility = 'HIDDEN';

select m.id, m.matzip_id, m.member_id, m.visibility
from my_matzip m
where m.visibility = 'HIDDEN'

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