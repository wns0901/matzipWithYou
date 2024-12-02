package com.lec.spring.member.controller;

import com.lec.spring.member.domain.Member;
import com.lec.spring.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/register")
    public void register() {}

    @PostMapping("/register")
    public String registerOk(Member member, Model model) {
        model.addAttribute("result", memberService.register(member));
        return "member/registerOk";
    }

    @PostMapping("/loginError")
    public String loginError() {
        return "member/login";
    }
}