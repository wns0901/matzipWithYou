package com.lec.spring.member.domain;

import com.lec.spring.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MemberValidator implements Validator {
    MemberService memberService;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        System.out.println("supports(" + clazz.getName() + ")");
        // ↓ 검증할 객체의 클래스 타입인지 확인
        boolean result = Member.class.isAssignableFrom(clazz);
        System.out.println(result);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member) target;

        String username = member.getUsername();
        if(username == null || username.trim().isEmpty()){
            errors.rejectValue("username", "username 은 필수입니다");
        } else if(memberService.isExist(username)){
            errors.rejectValue("username", "이미 존재하는 아이디(username) 입니다");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name 은 필수입니다");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password 은 필수입니다");

        // email
        // 정규표현식 패턴 체크...
        // TODO

        // 입력 password, re_password 가 동일한지 비교
        if(!member.getPassword().equals(member.getRe_password())){
            errors.rejectValue("re_password", "비밀번호와 비밀번호 확인 입력값은 같아야 합니다.");
        }


    }

}
