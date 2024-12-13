package com.lec.spring.member.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.member.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {
    @RequestMapping("/")
    public String home(Model model) {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public void home() {
    }

    @RequestMapping("/auth")
    @ResponseBody
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @RequestMapping("/userDetails")
    @ResponseBody
    public PrincipalDetails userDetails(Authentication authentication) {
        if (authentication != null)
            return (PrincipalDetails) authentication.getPrincipal();
        return null;
    }

    @RequestMapping("/member")
    @ResponseBody
    public Member member(@AuthenticationPrincipal PrincipalDetails userDetails) {
        return (userDetails != null) ? userDetails.getMember() : null;
    }


}

