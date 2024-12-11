package com.lec.spring.member.service;

// import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.Authority;
import com.lec.spring.member.domain.EmailMessage;
import com.lec.spring.member.domain.Friend;
import com.lec.spring.member.domain.Member;
//import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.repository.AuthorityRepository;
import com.lec.spring.member.repository.FriendRepository;
import com.lec.spring.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MemberServiceImpl implements MemberService {

    private static final int REFERRAL_POINTS = 1000;    // 추천인 작성 시 포인트
    private static final int REFERRAL_INTIMACY = 10;

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final FriendRepository friendRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final RedisTemplate redisTemplate;
    

    public MemberServiceImpl(SqlSession sqlSession, PasswordEncoder passwordEncoder, JavaMailSender mailSender, SpringTemplateEngine templateEngine, @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.memberRepository = sqlSession.getMapper(MemberRepository.class);
        this.authorityRepository = sqlSession.getMapper(AuthorityRepository.class);
        this.mailSender = mailSender;
        this.friendRepository = sqlSession.getMapper(FriendRepository.class);
        this.passwordEncoder = passwordEncoder;
        this.templateEngine = templateEngine;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public int register(Member member) {
        member.setUsername(member.getUsername().toUpperCase());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        int result = memberRepository.save(member);

        Authority memberAuthority = authorityRepository.findByName("ROLE_MEMBER");
        if (memberAuthority != null) {
            authorityRepository.addAuthority(member.getId(), memberAuthority.getId());
        }

        return result;
    }

    @Override
    public int registerWithReferral(Member member, String referrerNickname) {

        member.setPoint(0);
        int result = register(member);

        // 일반 회원가입 시 추천인 처리
        if (referrerNickname != null && !referrerNickname.isEmpty()) {
            Member referrer = memberRepository.findByNickname(referrerNickname);
            if (referrer != null) {
                handleReferralProcess(member, referrer);
            }
        }

        return result;
    }

    @Override
    public void processReferral(Member member, Member referrer) {
        // OAuth 회원 추가 정보 입력 시 추천인 처리
        handleReferralProcess(member, referrer);
    }

    private void handleReferralProcess(Member member, Member referrer) {
        memberRepository.updatePoint(member.getId(), REFERRAL_POINTS);
        memberRepository.updatePoint(referrer.getId(), REFERRAL_POINTS);

        Friend friendship = Friend.builder()
                .senderId(member.getId())
                .receiverId(referrer.getId())
                .intimacy(REFERRAL_INTIMACY)
                .isAccept(true)
                .build();

        friendRepository.sendFriendRequest(friendship);
        friendRepository.acceptFriendRequest(friendship);
    }

    @Override
    public int updateAdditionalInfo(Long id, String name, String nickname, String email) {
        return memberRepository.updateAdditionalInfo(id, name, nickname, email);
    }


    @Override
    public boolean isExist(String username) {
        Member member = findByUsername(username);
        return (member != null);
    }

    @Override
    public boolean isExistNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname);
        return (member != null);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username.toUpperCase());
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    //이메일이 화원db에 있는지 확인
    @Override
    public String sendEmail(EmailMessage emailMessage) {
        Member member = memberRepository.findByEmail(emailMessage.getTo());
        if (member == null) {
            return "이메일이 등록되지 않았습니다;";
        }
        String uuid = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(uuid, member.getId(), 3, TimeUnit.MINUTES);
        // 비밀번호 재설정 링크 생성
        String resetLink = "http://localhost:8080/member/reset-password?id=" + member.getId() + "&uuid=" + uuid;
//        String emailContet ="비밀번호 재설정을 위해 아래 링크를 클릭하세요 :" + resetLink;

        Context context = new Context();
        context.setVariable("resetLink", resetLink);
        String emailContet = templateEngine.process("email-template", context);


        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailContet, true);
            mailSender.send(mimeMessage);

            return "success";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean updatePassword(Long id, String newPassword) {

        String encodedPassword = passwordEncoder.encode(newPassword);
        System.out.println("######encodPassword : "  + encodedPassword);
        Map<String, String> updatelist = new HashMap<String, String>();
        updatelist.put(String.valueOf(id), "id");
        updatelist.put("newPassword", encodedPassword );
       int result = memberRepository.updatePassword(id, encodedPassword);
       if (result > 0) {
           System.out.println("비밀번호 업데이트 성공");
           System.out.println("변경된 암호화된 비밀번호 :" + encodedPassword);
       } else{
           System.out.println("비밀번호 업데이트 실패");
       }


        return false;
    }

    @Override
    public boolean isExistEmail(String email) {
        memberRepository.findByEmail(email);
        return false;
    }

    @Override
    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    @Override
    public int updateMember(Member member) {
        return memberRepository.update(member);
    }
}