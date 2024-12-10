SELECT *
FROM member;

select *
from friend;

SELECT
    f.memberId AS memberId,
    m.nickname AS nickname,
    p.filename AS profileImg


FROM (SELECT
          CASE
              WHEN receiver_id = 1 THEN sender_id
              ELSE receiver_id
              END AS memberId
      FROM friend
      WHERE receiver_id = 1 or sender_id = 1) f
JOIN my_matzip mz ON f.memberId = mz.member_id
JOIN member m ON f.memberId = m.id
JOIN profile_img p ON f.memberId = p.member_id
;

SELECT
    CASE
        WHEN receiver_id = 1 THEN sender_id
        ELSE receiver_id
    END AS memberId
FROM friend
WHERE receiver_id = 1 or sender_id = 1;
