package com.lec.spring.member.repository;

import com.lec.spring.member.domain.Friend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository{

    // 친구 신청시 이미 보낸 신청은 없나 확인용
    boolean isAlreadyFriend(Friend friend);

    // 친구 목록 조회 (수락된 상태만)
    List<Friend> findFriends(Long id);

    // 대기 중인 요청 목록 조회
    List<Friend> findPendingRequests(Long id);

    // 요청 하나 수락/거절 하기
    Friend findPendingRequest(Friend friend);

    // 친구 요청 보내기
    int sendFriendRequest(Long receiverId, Long senderId);

    // 새로운 친구 DB에 저장
    int save(Friend friend);

    // 친구 삭제
    int delete(Friend friend);
}
