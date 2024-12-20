
package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.domain.FriendRequestDTO;
import com.lec.spring.member.domain.FriendSearchResponseDTO;

import java.util.List;

public interface FriendService {

    // 친구 요청 보내기
    int sendFriendRequest(Friend friend);

    // 친구 요청 수락/거절
    int respondToRequest(Friend friend);

    // 친구 삭제
    int deleteFriend(Long friendId, Long memberId);

    // 친구 상세 정보
    List<FriendDetailsDTO> getFriendsWithDetailsDTO(Long memberId);

    // 친구 요청 대기중
    List<FriendRequestDTO> getPendingRequests(Long memberId);

    // 친구 검색
    List<FriendSearchResponseDTO> searchPotentialFriends(String searchTerm, Long currentMemberId);
}
