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
        return "member/additional-info";
    }

    // 추가 정보 저장 처리
    @PostMapping("/additional-info")
    public String saveAdditionalInfo(
            @RequestParam String name,
            @RequestParam String nickname,
            @RequestParam String email,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Member member = principalDetails.getMember();

            // Check if nickname is already taken by a different user
            Member existingMember = memberService.findByNickname(nickname);
            if (existingMember != null && !existingMember.getId().equals(member.getId())) {
                redirectAttributes.addFlashAttribute("error", "이미 사용 중인 닉네임입니다.");
                return "redirect:/member/additional-info";
            }

            // Update additional info
            int result = memberService.updateAdditionalInfo(member.getId(), name, nickname, email);

            if (result > 0) {
                // Update the member object in PrincipalDetails
                member.setName(name);
                member.setNickname(nickname);
                member.setEmail(email);

                redirectAttributes.addFlashAttribute("message", "추가 정보가 저장되었습니다.");
                return "redirect:/home";
            } else {
                redirectAttributes.addFlashAttribute("error", "정보 저장에 실패했습니다.");
                return "redirect:/member/additional-info";
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "처리 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/member/additional-info";
        }
    }

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/register")
    public void register() {
    }

    @PostMapping("/register")
    public String registerOk(@Valid Member member
            , @RequestParam(required = false) String referrerNickname
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

        // 추천인 닉네임 검증
        if (referrerNickname != null && !referrerNickname.isEmpty()) {
            Member referrer = memberService.findByNickname(referrerNickname);
            if (referrer == null) {
                redirectAttributes.addFlashAttribute("error", "존재하지 않는 추천인입니다.");
                return "redirect:/member/register";
            }
        }

//        int cnt = memberService.register(member);
//        model.addAttribute("result", cnt);
//
//        System.out.println(member.getUsername());
//        return "member/registerOk";

        int cnt = memberService.registerWithReferral(member, referrerNickname);
        model.addAttribute("result", cnt);

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