package com.lec.spring.member.service;

import com.lec.spring.member.domain.Friend;
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
        return friendRepository.save(friend); // DB에 저장
    }

    // 친구 요청 수락/거절
    @Override
    public int respondToRequest(Friend friend) {
        Friend newFriend = friendRepository.findPendingRequest(friend);
        if (newFriend == null) {
            return 0;
        }
        if (newFriend.isAccept()) {
            // 요청 수락
            friend.setAccept(true);
            return friendRepository.save(friend); // 업데이트된 행 수 반환
        } else {
            // 요청 거절
            return friendRepository.delete(friend); // 삭제된 행 수 반환
        }
    }

    // 친구 목록 가져오기
    @Override
    public List<Friend> getFriends(Long id) {
        // senderId 또는 receiverId에 해당하고 isAccept가 true인 친구 목록 조회
        return friendRepository.findFriends(id);
    }

    // 대기 중인 친구 요청
    @Override
    public List<Friend> getPendingRequests(Long id) {
        // receiverId에 해당하고 isAccept가 false인 요청 조회
        return friendRepository.findPendingRequests(id);
    }






}