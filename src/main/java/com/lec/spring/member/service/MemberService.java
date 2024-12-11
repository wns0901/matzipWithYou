package com.lec.spring.member.service;

import com.lec.spring.member.domain.EmailMessage;
import com.lec.spring.member.domain.Member;

import java.util.List;

public interface MemberService {

    int register(Member member);

    int registerWithReferral(Member member, String referrerNickname);

    boolean isExist(String username);

    boolean isExistNickname(String nickname);

    Member findByUsername(String username);

    List<Member> findAll();



    //이메일이 화원db에 있는지 확인
    String sendEmail(EmailMessage emailMessage);

    //비밀번호 업데이트하기
    boolean updatePassword(Long id, String newPassword);

   //이메일이 존재하는지 확인
    boolean isExistEmail(String email);
}