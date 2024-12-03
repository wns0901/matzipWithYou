package com.lec.spring.member.controller;

import com.lec.spring.member.domain.Member;
import com.lec.spring.member.domain.MemberValidator;
import com.lec.spring.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    public void register() {
    }

    @PostMapping("/register")
    public String registerOk(@Valid Member member
            , BindingResult bindingResult
            , Model model
            , RedirectAttributes redirectAttributes
    ) {
        // 검증동작 처리
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("username", member.getUsername());
            redirectAttributes.addFlashAttribute("name", member.getName());
            redirectAttributes.addFlashAttribute("email", member.getEmail());

            List<FieldError> errorList = bindingResult.getFieldErrors();
            for(FieldError error : errorList){
                // 가장 처음에 발견된 에러만 담아 보낸다
                redirectAttributes.addFlashAttribute("error", error.getCode());
                break;
            }

            return "redirect:/member/register";
        }

        // 에러가 없었으면 회원등록 진행
        String page = "member/registerOk";

        int cnt = memberService.register(member);
        model.addAttribute("result", cnt);

        return page;
    }


    @PostMapping("/loginError")
    public String loginError() {
        return "member/login";
    }

    // rejectAuth

    MemberValidator memberValidator;

    @Autowired
    public void setMemberValidator(MemberValidator memberValidator) {
        this.memberValidator = memberValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(memberValidator);
    }
}