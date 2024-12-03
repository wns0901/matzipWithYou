package com.lec.spring.member.service;

import com.lec.spring.matzip.repository.MatzipRepository;
import com.lec.spring.member.domain.Member;
//import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {
//    private final MemberRepository memberRepository;
//    private final FriendRepository friendRepository;
//    private final MatzipRepository matzipRepository;
//
//    public MyPageService(MemberRepository memberRepository, FriendRepository friendRepository, MatzipRepository matzipRepository) {
//        this.memberRepository = memberRepository;
//        this.friendRepository = friendRepository;
//        this.matzipRepository = matzipRepository;
//    }
//
//    public int getFriendCount(Long memberId) {
//        return friendRepository.countBySenderIdOrReceiverId(memberId);
//    }
//
//    public int getMatzipCount(Long memberId) {
//        return matzipRepository.;
//    }
//
//    public void updateNickname(Long memberId, String newNickname) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException(""));
//        member.setNickname(newNickname);
//        memberRepository.save(member);
//    }
//
//    public void updateProfileImage(Long memberId, String imagePath) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException(""));
//        member.setProfileImagePath(imagePath);
//        memberRepository.save(member);
//    }
}