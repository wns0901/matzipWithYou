package com.lec.spring.member.service;

import com.lec.spring.member.domain.EmailMessage;
import com.lec.spring.member.domain.Member;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MemberService {

    int register(Member member);

    int registerWithReferral(Member member, String referrerNickname);

    boolean isExist(String username);

    boolean isExistNickname(String nickname);

    Member findByUsername(String username);

    Member findByNickname(String nickname);

    List<Member> findAll();

    int deleteById(Long id);

    Member findById(Long id);

    // 인증코드 확인
    boolean verifyAuthorizationCode(String code, String email);

    //이메일이 화원db에 있는지 확인
    String sendEmail(EmailMessage emailMessage);

    //비밀번호 업데이트하기
    boolean updatePassword(Long id, String newPassword);

    //이메일이 존재하는지 확인
    boolean isExistEmail(String email);

    int updateMember(Member member);

    int updateAdditionalInfo(Long id, String name, String nickname, String email);

    void processReferral(Member member, Member referrer);

    // 랜덤번호 생성
    void createNumber();

    //mail content
    MimeMessage createEmail(EmailMessage emailMessage);


    ResponseEntity<Map<String, List<String>>> findNicknameBymemberIds(List<Long> memberIds);

    int getPoontByMemberId(Long id);

}