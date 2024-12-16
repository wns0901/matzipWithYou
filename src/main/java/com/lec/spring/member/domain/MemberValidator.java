package com.lec.spring.member.domain;

import com.lec.spring.member.service.EmailAuthService;
import com.lec.spring.member.service.MemberService;
import com.lec.spring.member.service.ValidationEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Component
@ControllerAdvice
public class MemberValidator implements Validator {
    MemberService memberService;
    EmailAuthService emailAuthService;
    ValidationEmailService validationEmailService;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
        this.emailAuthService = emailAuthService;
        this.validationEmailService = validationEmailService;
    }



    @Override
    public boolean supports(Class<?> clazz) {
        boolean result = Member.class.isAssignableFrom(clazz);
        return result;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member) target;

        // username 검증 (8~20자)
        String username = member.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.rejectValue("username", "ID는 필수입니다");
        } else if (username.length() < 8 || username.length() > 20) {
            errors.rejectValue("username", "ID는 8~20자 여야 합니다");
        } else if (memberService.isExist(username)) {
            errors.rejectValue("username", "이미 존재하는 아이디입니다");
        }

        // password 검증 (8~16자)
        String password = member.getPassword();
        if (password == null || password.trim().isEmpty()) {
            errors.rejectValue("password", "password는 필수입니다");
        } else if (password.length() < 8 || password.length() > 16) {
            errors.rejectValue("password", "password는 8~16자 여야 합니다");
        }

        // nickname 검증
        String nickname = member.getNickname();
        if (nickname == null || nickname.trim().isEmpty()) {
            errors.rejectValue("nickname", "nickname은 필수입니다");
        } else if (memberService.isExistNickname(nickname)) {
            errors.rejectValue("nickname", "이미 존재하는 닉네임입니다");
        }

        // email 검증
        String email = member.getEmail();
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email == null || email.trim().isEmpty()) {
            errors.rejectValue("email", "email은 필수입니다");
        } else if (!email.matches(emailPattern)) {
            errors.rejectValue("email", "유효하지 않은 이메일 형식입니다");
        } else if (memberService.isExistEmail(email)) {
            errors.rejectValue("email", "이미존재하는 이메일입니다");

        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name은 필수입니다");

        // 비밀번호 확인 검증
        if (!member.getPassword().equals(member.getRe_password())) {
            errors.rejectValue("re_password", "비밀번호 확인란을 다시 입력해주세요");
        }




    // 인증번호 검증
    String authCode = member.getAuthCode(); // Member 객체에서 인증번호를 가져온다고 가정
        if (authCode == null || authCode.trim().isEmpty()) {
        errors.rejectValue("authCode", "인증번호는 필수입니다");
    } else {
        String storedAuthCode = emailAuthService.getAuthCode(email); // 이메일을 통해 인증번호 가져오기
        if (storedAuthCode == null) {
            errors.rejectValue("authCode", "인증번호가 존재하지 않습니다");
        } else if (!storedAuthCode.equals(authCode)) {
            errors.rejectValue("authCode", "인증번호가 일치하지 않습니다");
        }
    }
}






}


