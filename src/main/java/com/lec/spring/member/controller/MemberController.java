package com.lec.spring.member.controller;

import com.lec.spring.member.domain.EmailMessage;
import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.domain.MemberValidator;
import com.lec.spring.member.service.MemberService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
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
    private MemberValidator validator;
    private WebDataBinder binder;
    private RedisTemplate redisTemplate;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
        this.validator = new MemberValidator();
        this.binder = new WebDataBinder(new Object(), "member");
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
            @RequestParam(required = false) String referrerNickname,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Member member = principalDetails.getMember();

            Member existingMember = memberService.findByNickname(nickname);
            if (existingMember != null && !existingMember.getId().equals(member.getId())) {
                redirectAttributes.addFlashAttribute("error", "이미 사용 중인 닉네임입니다.");
                return "redirect:/member/additional-info";
            }

            // 추천인 닉네임 검증
            if (referrerNickname != null && !referrerNickname.isEmpty()) {
                Member referrer = memberService.findByNickname(referrerNickname);
                if (referrer == null) {
                    redirectAttributes.addFlashAttribute("error", "존재하지 않는 추천인입니다.");
                    return "redirect:/member/additional-info";
                }

                // 자기 자신을 추천인으로 등록하는 것 방지
                if (member.getId().equals(referrer.getId())) {
                    redirectAttributes.addFlashAttribute("error", "자기 자신을 추천인으로 등록할 수 없습니다.");
                    return "redirect:/member/additional-info";
                }
            }

            // Update additional info
            int result = memberService.updateAdditionalInfo(member.getId(), name, nickname, email);

            if (result > 0) {
                // Update the member object in PrincipalDetails
                member.setName(name);
                member.setNickname(nickname);
                member.setEmail(email);

                // 추천인 처리
                if (referrerNickname != null && !referrerNickname.isEmpty()) {
                    Member referrer = memberService.findByNickname(referrerNickname);
                    memberService.processReferral(member, referrer);
                }
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

    //email 인증 구현하기
    @PostMapping("/register")
    public String registerOk(@Valid Member member
            , BindingResult bindingResult
            , @RequestParam(required = false) String referrerNickname
            , Model model
            , RedirectAttributes redirectAttributes
                             ,EmailMessage emailMessage
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

        int cnt = memberService.registerWithReferral(member, referrerNickname);
        model.addAttribute("result", cnt);

        // email 인증
        // 이메일 인증 전송
        try {
            emailMessage.setSubject("이메일 인증");
            String result = memberService.authorizationEmail(emailMessage);
            if ("success".equals(result)) {
                model.addAttribute("email", member.getEmail());
                model.addAttribute("message", "이메일 인증 링크가 전송되었습니다.");
                System.out.println("email이 보내졌나요" + member.getEmail());
                System.out.println(result);
                return "이메일 인증 안내 페이지"; // 인증을 위한 안내 페이지
            } else {
                model.addAttribute("error", "이메일 전송 중 오류가 발생했습니다.");
                return "이메일 오류";
            }
        } catch (Exception e) {
            model.addAttribute("error", "이메일 전송 중 오류가 발생했습니다.");
            return "이메일 오류";
        }


    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String email,
                              @RequestParam String verificationCode,
                              Model model) {
        // 인증 코드 검증
        boolean isVerified = memberService.verifyAuthorizationCode(verificationCode, email);

        if (isVerified) {
            model.addAttribute("message", "이메일 인증이 완료되었습니다.");
            return "이메일 인증 성공";
        } else {
            model.addAttribute("message", "잘못된 인증 코드입니다.");
            return "이메일 인증 실패";
        }
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

    @InitBinder("member")
    public void initMemberBinder(WebDataBinder binder) {
        binder.setValidator(memberValidator);
    }

    // 이메일 입력받는 창
    @GetMapping("/request-reset")
    public String requestReset(Model model) {
        model.addAttribute("emailMessage", new EmailMessage());
        return "member/request-reset";
    }

    // 이메일 입력창 처리
    @PostMapping("/request-reset")
    public String requestReset(@ModelAttribute EmailMessage emailMessage, Model model) {
        String email = emailMessage.getTo();
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        // 유효성 검사
        if(email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "이메일은 필수입니다");
            return "member/request-reset";
        }

        if(!email.matches(emailPattern)) {
            model.addAttribute("error", "유효하지 않은 이메일 형식입니다");
            return "member/request-reset";
        }

        if(!memberService.isExistEmail(email)) {
            model.addAttribute("error", "존재하지 않는 이메일입니다");
            return "member/request-reset";
        }
        try {
            emailMessage.setSubject("비밀번호 재설정 요청");
            String result = memberService.sendEmail(emailMessage);
            model.addAttribute("result", result);
            return "member/request-reset-ok";
        } catch (Exception e) {
            model.addAttribute("error", "이메일 전송 중 오류가 발생했습니다");
            return "member/request-reset";
        }
    }

    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("uuid") String uuid, Model model) {
        Long memberId = (Long) redisTemplate.opsForValue().get(uuid);
        if (memberId == null) {
            return "redirect:member/reset-fail";
        }
        Model member = model.addAttribute("id", memberId);
        return "member/reset-password";
    }


    @PostMapping("/update-password")
    public String updatePassword(@RequestParam Long id, String newPassword) {
        boolean isUpdated = memberService.updatePassword(id, newPassword);
        return isUpdated ? "redirect:/member/reset-fail" : "redirect:/member/reset-success";
    }

    @GetMapping("/reset-success")
    public String resetSuccess() {
        return "member/reset-success";
    }

    @Qualifier("redisTemplate")
    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}// end memberController