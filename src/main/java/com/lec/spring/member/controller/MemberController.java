package com.lec.spring.member.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.domain.MemberValidator;
import com.lec.spring.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    // GET: 추가 정보 입력 폼
    @GetMapping("/additional-info")
    public String additionalInfo(Authentication authentication, Model model) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        model.addAttribute("member", member);
        return "member/additional-info";    // templates/member/additional-info.html
    }

    // POST: 추가 정보 저장
    @PostMapping("/additional-info")
    public String saveAdditionalInfo(
            Authentication authentication,
            @RequestParam String nickname,
            RedirectAttributes redirectAttributes) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();

        // 중복 닉네임 체크
        if (memberService.isExistNickname(nickname)) {
            redirectAttributes.addFlashAttribute("error", "이미 사용 중인 닉네임입니다.");
            return "redirect:/member/additional-info";
        }

        // 닉네임 업데이트
        member.setNickname(nickname);
        memberService.updateMember(member);

        redirectAttributes.addFlashAttribute("message", "추가 정보가 저장되었습니다.");
        return "redirect:/home";  // 정보 저장 후 홈으로 리다이렉트
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(memberValidator);
    }
}