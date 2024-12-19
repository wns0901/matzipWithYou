package com.lec.spring.member;

import com.lec.spring.member.domain.EmailMessage;
import com.lec.spring.member.repository.MemberRepository;
import com.lec.spring.member.service.MemberService;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestCode {

    @Autowired
    MemberService memberService;

    @Autowired
    SqlSession sqlSession;

    @Test
    void test1() {
        EmailMessage emailMessage = EmailMessage.builder()
                .to("cooconut0226@gmail.com")
                .subject("hihi")
                .build();

        System.out.println();
        System.out.println("########emailMessage : " + emailMessage);

        System.out.println(memberService.sendEmail(emailMessage));
    }

    @Test
    void test2() {


    }

    @Test
    public void testSendEmail() {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo("cooconut0226@gmail.com");
        emailMessage.setSubject("cooconut0226@gmail.com");

        memberService.createEmail(emailMessage);
    }

    @Test
    public void test3() {
        MemberRepository memberRepository = sqlSession.getMapper(MemberRepository.class);

        memberRepository.findNicknameByMemberIds(List.of(1L)).forEach(System.out::println);
    }
}
