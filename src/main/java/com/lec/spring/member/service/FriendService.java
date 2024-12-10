package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;

import java.util.List;

public interface FriendService {

    // 친구 요청 보내기
    int sendFriendRequest(Long senderId, Long receiverId);

    // 친구 요청 수락/거절
    int respondToRequest(Long senderId, Long receiverId, boolean accept);

    // 친구 목록 가져오기
    List<Friend> getFriends(Long userId);

    // 친구 요청 대기중
    List<Friend> getPendingRequests(Long receiverId);
}

