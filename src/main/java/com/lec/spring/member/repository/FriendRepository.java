package com.lec.spring.member.repository;

import com.lec.spring.member.domain.Friend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository{

    // id 로 신청 받은 유저 리턴
    List<Friend> findByReceiver(Long receiverId);

    // id로 내가 보낸 신청 리턴
    List<Friend> findBySender(Long senderId);

    // 친구 목록 조회 (수락된 상태만)
    List<Friend> findFriends(Long userId);

    // 대기 중인 요청 목록 조회
    List<Friend> findPendingRequests(Long userId);





    // 친구 요청 보내기
    int sendFriendRequest(Long receiverId, Long senderId);

    // 새로운 친구 DB에 저장
    int save(Friend friend);

    // 친구 삭제
    int delete(Friend friend);
}
