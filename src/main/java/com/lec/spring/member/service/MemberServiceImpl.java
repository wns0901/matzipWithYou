package com.lec.spring.member.service;

// import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.Authority;
import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.Member;
//import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.repository.AuthorityRepository;
import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.repository.MemberRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private static final int REFERRAL_POINTS = 1000;    // 추천인 작성 시 포인트
    private static final int REFERRAL_INTIMACY = 10;

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberServiceImpl(SqlSession sqlSession, PasswordEncoder passwordEncoder) {
        this.memberRepository = sqlSession.getMapper(MemberRepository.class);
        this.authorityRepository = sqlSession.getMapper(AuthorityRepository.class);
        this.friendRepository = sqlSession.getMapper(FriendRepository.class);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int register(Member member) {
        member.setUsername(member.getUsername().toUpperCase());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        int result = memberRepository.save(member);

        Authority memberAuthority = authorityRepository.findByName("ROLE_MEMBER");
        if (memberAuthority != null) {
            authorityRepository.addAuthority(member.getId(), memberAuthority.getId());
        }

        return result;
    }

    @Override
    public int registerWithReferral(Member member, String referrerNickname) {

        member.setPoint(0);
        int result = register(member);

        // 일반 회원가입 시 추천인 처리
        if (referrerNickname != null && !referrerNickname.isEmpty()) {
            Member referrer = memberRepository.findByNickname(referrerNickname);
            if (referrer != null) {
                handleReferralProcess(member, referrer);
            }
        }

        return result;
    }

    @Override
    public void processReferral(Member member, Member referrer) {
        // OAuth 회원 추가 정보 입력 시 추천인 처리
        handleReferralProcess(member, referrer);
    }

    private void handleReferralProcess(Member member, Member referrer) {
        memberRepository.updatePoint(member.getId(), REFERRAL_POINTS);
        memberRepository.updatePoint(referrer.getId(), REFERRAL_POINTS);

        Friend friendship = Friend.builder()
                .senderId(member.getId())
                .receiverId(referrer.getId())
                .intimacy(REFERRAL_INTIMACY)
                .isAccept(true)
                .build();

        friendRepository.sendFriendRequest(friendship);
        friendRepository.acceptFriendRequest(friendship);
    }

    @Override
    public int updateAdditionalInfo(Long id, String name, String nickname, String email) {
        return memberRepository.updateAdditionalInfo(id, name, nickname, email);
    }


    @Override
    public boolean isExist(String username) {
        Member member = findByUsername(username);
        return (member != null);
    }

    @Override
    public boolean isExistNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname);
        return (member != null);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username.toUpperCase());
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    @Override
    public int updateMember(Member member) {
        return memberRepository.update(member);
    }
}