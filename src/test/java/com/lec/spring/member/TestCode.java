package com.lec.spring.member;

import com.lec.spring.member.domain.EmailMessage;
import com.lec.spring.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCode {

    @Autowired
    MemberService memberService;

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
        emailMessage.setSubject("cooconut0226@gmail.com" );

       memberService.createEmail(emailMessage);
    }

}
