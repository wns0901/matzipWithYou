package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.FriendDetailsDTO;
import com.lec.spring.member.repository.FriendRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        // 중복 요청 확인
        if (friendRepository.isAlreadyFriend(friend)) { // ?
            return 0;
        }
        return friendRepository.sendFriendRequest(friend); // DB에 저장
    }

    // 친구 요청 수락/거절
    @Override
    public int respondToRequest(Friend friend) {
        int newFriend = friendRepository.acceptFriendRequest(friend);
        if (newFriend == 0) {
            return 0;
        }
        if (newFriend == 1) {
            // 요청 수락
            friend.setIsAccept(true);
            return friendRepository.acceptFriendRequest(friend); // 업데이트된 행 수 반환
        } else {
            // 요청 거절
            return friendRepository.rejectFriendRequest(friend); // 삭제된 행 수 반환
        }
    }


    // 친구 삭제
    @Override
    public int deleteFriend(Friend friend) {
        return friendRepository.rejectFriendRequest(friend);
    }


    // 친구 목록 가져오기
    @Override
    public List<Friend> getFriendsWithDetailsDTO(Long memberId) {
        return friendRepository.findFriendsWithDetailsDTO(memberId);
    }

    // 대기 중인 친구 요청 조회...
    @Override
    public List<Friend> getPendingRequests(Long memberId) {
        // receiverId에 해당하고 isAccept 가 false 인 요청 조회
        return friendRepository.findPendingRequests(memberId);
    }


//    private Long getUserId(FriendDetails friendDetails) {
//        Member member = memberServiceImpl.getId(memberId)
//    } 로그인 유저의 iD 가져오기

}