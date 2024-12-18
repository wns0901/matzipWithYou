package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.repository.FriendRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;

    public FriendServiceImpl(SqlSession sqlSession) {
        this.friendRepository = sqlSession.getMapper(FriendRepository.class);
    }

    // 친구 요청 보내기
    @Override
    public int sendFriendRequest(Friend friend) {
        if (friendRepository.isAlreadyFriend(friend)) {
            return 0;
        }
        return friendRepository.sendFriendRequest(friend);
    }

    // 친구 요청 수락
    @Override
    public int acceptFriendRequest(Friend friend) {
        friend.setIsAccept(true);
        return friendRepository.acceptFriendRequest(friend);
    }

    // 친구 요청 거절
    @Override
    public int rejectFriendRequest(Friend friend) {
        return friendRepository.rejectFriendRequest(friend);
    }

    // 친구 삭제
    @Override
    public int deleteFriend(Long friendId) {
        return friendRepository.deleteFriendById(friendId);
    }

    // 친구 목록(상세정보) 가져오기
    @Override
    public List<FriendDetailsDTO> getFriendsWithDetailsDTO(Long memberId) {
        return friendRepository.findFriendsWithDetailsDTO(memberId);
    }

    // 대기 중인 친구 요청 조회
    @Override
    public List<Friend> getPendingRequests(Long memberId) {
        return friendRepository.findPendingRequests(memberId);
    }
}