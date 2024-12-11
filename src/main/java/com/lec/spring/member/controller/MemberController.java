package com.lec.spring.member.controller;

import com.lec.spring.member.domain.EmailMessage;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.domain.MemberValidator;
import com.lec.spring.member.service.MemberService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private MemberValidator validator;
    private WebDataBinder binder;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
        this.validator = new MemberValidator();
        this.binder = new WebDataBinder(new Object(), "member");
    }

    @InitBinder("email")
    public void initBinder(WebDataBinder binder) {
        this.binder = binder;
        // 해당 객체가 Member 타입일 때만 Validator를 설정
        if (binder.getTarget() instanceof Member) {
            binder.setValidator(new MemberValidator());
        } }

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
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("username", member.getUsername());
            redirectAttributes.addFlashAttribute("name", member.getName());
            redirectAttributes.addFlashAttribute("email", member.getEmail());
            redirectAttributes.addFlashAttribute("nickname", member.getNickname());

            List<FieldError> errorList = bindingResult.getFieldErrors();
            for (FieldError error : errorList) {
                redirectAttributes.addFlashAttribute("error", error.getCode());
                break;
            }

            return "redirect:/member/register";
        }

        int cnt = memberService.register(member);
        model.addAttribute("result", cnt);

        System.out.println(member.getUsername());
        return "member/registerOk";
    }


    @PostMapping("/loginError")
    public String loginError() {
        return "member/login";
    }


    @RequestMapping("/rejectAuth")
    public String rejectAuth() {
        return "common/rejectAuth";
    }

    MemberValidator memberValidator;

    @Autowired
    public void setMemberValidator(MemberValidator memberValidator) {
        this.memberValidator = memberValidator;
    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.setValidator(memberValidator);
//    }


    // 이메일 입력받는 창
    @GetMapping("/request-reset")
    public String requestResett() {
        System.out.println("123");
        return "member/request-reset";
    }


    // 이메일 입력창 2
    @PostMapping("/request-reset")
    public String requestReset( @ModelAttribute("email") String email, Model model) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .subject("비밀번호 재설정 요청")
                .build();
        System.out.println("#######emailMessage: " + emailMessage);
        String result = memberService.sendEmail(emailMessage);
        System.out.println("#####이메일이 보내졌나요???: " + result);
        model.addAttribute("result", result);
        return "member/request-reset-ok";


    }

    // 패스워드 리셋
    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("id") Long id, Model model){
        Model member = model.addAttribute("id", id);
        System.out.println("#######id "+member);
        return "member/reset-password";
    }


    @PostMapping("/update-password")
    public String updatePassword(@RequestParam Long id, String newPassword) {
        System.out.println("##########id: " + id);
        System.out.println("#########New Password: " + newPassword);

        boolean isUpdated = memberService.updatePassword(id, newPassword);

        return isUpdated ? "redirect:/member/reset-fail" : "redirect:/member/reset-success";
    }


    @GetMapping("/reset-success")
    public String resetSuccess(){
        return "member/reset-success";
    }

}// end memberController