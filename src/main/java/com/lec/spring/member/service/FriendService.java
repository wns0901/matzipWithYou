
package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;

import java.util.List;

public interface FriendService {

    // 친구 요청 보내기
    int sendFriendRequest(Friend friend);

    // 친구 요청 수락/거절
    int respondToRequest(Friend friend);

    // 친구 삭제
    int deleteFriend(Friend friend);

    // 친구 상세 정보
    List<FriendDetailsDTO> getFriendsWithDetailsDTO(Long memberId);

    // 친구 요청 대기중
    List<Friend> getPendingRequests(Long memberId);

}
