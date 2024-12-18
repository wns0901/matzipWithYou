package com.lec.spring.member.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.member.domain.EmailMessage;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.domain.MemberValidator;
import com.lec.spring.member.service.EmailAuthService;
import com.lec.spring.member.service.MemberService;
import com.lec.spring.member.service.ValidationEmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private MemberValidator validator;
    private WebDataBinder binder;
    private RedisTemplate redisTemplate;
    private final EmailAuthService emailAuthService;
    private final ValidationEmailService validationEmailService;
    MemberValidator memberValidator;

    @Autowired
    public void setMemberValidator(MemberValidator memberValidator) {
        this.memberValidator = memberValidator;
    }

    @InitBinder("member")
    public void initMemberBinder(WebDataBinder binder) {
        binder.setValidator(memberValidator);
    }

    @Autowired
    public MemberController(MemberService memberService, EmailAuthService emailAuthService, ValidationEmailService validationEmailService) {
        this.memberService = memberService;
        this.emailAuthService = emailAuthService;
        this.validationEmailService = validationEmailService;
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
            , EmailMessage emailMessage
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
        return "member/login";


    }

    @GetMapping("/sendEmail")
    @ResponseBody  // 이 어노테이션으로 JSON 형식 응답을 반환
    public ResponseEntity<Map<String, String>> sendEmail(@RequestParam String email) {


        Map<String, String> response = new HashMap<>();

        // 이메일 형식 검증
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email == null || email.trim().isEmpty()) {
            response.put("error", "이메일은 필수입니다.");
            return ResponseEntity.badRequest().body(response);
        } else if (!email.matches(emailPattern)) {
            response.put("error", "유효하지 않은 이메일 형식입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setTo(email);          // 수신자 이메일 설정
            emailMessage.setSubject("이메일 인증");  // 이메일 제목 설정

            memberService.createEmail(emailMessage);  // 이메일 발송
            response.put("message", "인증 이메일이 전송되었습니다.");
            System.out.println("response: " + response);
            return ResponseEntity.ok(response);  // 성공시 JSON 응답
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "이메일 전송에 실패했습니다. 다시 시도해주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // 에러 시 JSON 응답
        }

    }

    @PostMapping("/verify-code")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyAuthCode(@RequestParam String email, @RequestParam String code) {
        // Redis
        boolean isValid = validationEmailService.validateAuthCode(email, code);
        System.out.println("########isValid: " + isValid);

        // 응답을 위한 Map 생성
        Map<String, Object> response = new HashMap<>();
        response.put("success", isValid);  // 인증 성공 여부 추가
        System.out.println("########respose " + response);

        if (isValid) {
            emailAuthService.deleteAuthCode(email);
            System.out.println("인증 성공하셨습니까 : " + isValid);

        }

        return ResponseEntity.ok(response);  // JSON 응답 반환
    }


    @PostMapping("/loginError")
    public String loginError() {
        return "member/login";
    }


    @RequestMapping("/rejectAuth")
    public String rejectAuth() {
        return "common/rejectAuth";
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
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "이메일은 필수입니다");
            return "member/request-reset";
        }

        if (!email.matches(emailPattern)) {
            model.addAttribute("error", "유효하지 않은 이메일 형식입니다");
            return "member/request-reset";
        }

        if (!memberService.isExistEmail(email)) {
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

    @ResponseBody
    @GetMapping("/nicknames")
    public ResponseEntity<Map<String, List<String>>> nicknames(@RequestParam List<Long> memberId) {
        return memberService.findNicknameBymemberIds(memberId);
    }

    @Qualifier("redisTemplate")
    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}// end memberController