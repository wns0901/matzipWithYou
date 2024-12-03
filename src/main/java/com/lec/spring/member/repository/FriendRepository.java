package com.lec.spring.member.repository;

import com.lec.spring.member.domain.Friend;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository{

    // 친구 수락 여부
    List<Friend> findByReceiverIdAndIsAccept(Long receiverId, Boolean isAccept);

    // 친구 목록, 요청 목록 등 친구관계 조회
    List<Friend> findBySenderIdOrReceiverIdAndIsAccept(Long senderId, Long receiverId, Boolean isAccept);

    // 신규 친구관계 DB에 저장
    int save(Friend friend);

    // 친구 요청/관계 조회
    Friend findByMembers(Long senderId, Long receiverId);

}
