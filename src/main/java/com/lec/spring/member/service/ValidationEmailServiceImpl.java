package com.lec.spring.member.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationEmailServiceImpl implements ValidationEmailService {

    private final EmailAuthService emailAuthService;

    public ValidationEmailServiceImpl(EmailAuthService emailAuthService) {
        this.emailAuthService = emailAuthService;
    }

    //인증번호 확인
    @Override
    public boolean validateAuthCode(String email, String authCode) {
        String storedAuthCode = emailAuthService.getAuthCode(email);
        System.out.println("#########3storedAuthCode = " + storedAuthCode);
        return storedAuthCode != null && storedAuthCode.equals(authCode);
    }

}
