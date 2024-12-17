package com.lec.spring.config.oauth;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.config.oauth.provider.KakaoUserInfo;
import com.lec.spring.config.oauth.provider.GoogleUserInfo;
import com.lec.spring.config.oauth.provider.NaverUserInfo;
import com.lec.spring.config.oauth.provider.OAuth2UserInfo;
import com.lec.spring.member.domain.Authority;
import com.lec.spring.member.domain.Member;
import com.lec.spring.member.domain.ProfileImg;
import com.lec.spring.member.repository.AuthorityRepository;
import com.lec.spring.member.repository.MemberRepository;
import com.lec.spring.member.repository.ProfileImgRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final ProfileImgRepository profileImgRepository;

    @Autowired // Autowired 이슈 해결
    public PrincipalOauth2UserService(SqlSession sqlSession){
        this.memberRepository = sqlSession.getMapper(MemberRepository.class);
        this.authorityRepository = sqlSession.getMapper(AuthorityRepository.class);
        this.profileImgRepository = sqlSession.getMapper(ProfileImgRepository.class);
    }

    @Value("${app.oauth2.password}")
    private String oauth2Password;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("""
                [loadUser():]
                %s
                %s
                %s
                %s
                """.formatted(userRequest.getClientRegistration()
                , userRequest.getClientRegistration().getRegistrationId()
                , userRequest.getAccessToken().getTokenValue()
                , oAuth2User.getAttributes())
        );

        String provider = userRequest.getClientRegistration().getRegistrationId();


        
        OAuth2UserInfo oAuth2UserInfo = switch (provider.toLowerCase()){
            case "kakao" -> new KakaoUserInfo(oAuth2User.getAttributes());
            case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            case "naver" -> new NaverUserInfo(oAuth2User.getAttributes());
            default -> null;
        };

        String providerId = oAuth2UserInfo.getProviderId();
        String username = "MATDORI_" + providerId;
        String password = oauth2Password;
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();

        if (email == null && provider.equals("kakao")) {
            email = username + "@kakao.user.com";
        }

        String tempNickname = "MATDORI_" + providerId;

        Member member = memberRepository.findByUsername(username);
        if(member == null) {
            Member newMember = Member.builder()
                    .username(username)
                    .name(name)
                    .email(email)
                    .password(password)
                    .provider(provider)
                    .providerId(providerId)
                    .nickname(tempNickname)
                    .build();

            int cnt = memberRepository.save(newMember);

            String profileImageUrl = oAuth2UserInfo.getProfileImageUrl();
            if (profileImageUrl != null) {
                ProfileImg profileImg = ProfileImg.builder()
                        .memberId(newMember.getId())
                        .sourcename(username + "_" + provider + "_profile")
                        .filename(profileImageUrl)
                        .isImage(true)
                        .build();

                profileImgRepository.save(profileImg);
            }

            Authority auth = authorityRepository.findByName("ROLE_MEMBER");
            Long memberId = newMember.getId();
            Long authId = auth.getId();
            authorityRepository.addAuthority(memberId, authId);

            if (cnt > 0) {
                member = memberRepository.findByUsername(username);
            }

        }

        PrincipalDetails principalDetails = new PrincipalDetails(member, oAuth2User.getAttributes());
        principalDetails.setAuthorityRepository(authorityRepository);
        return principalDetails;
    }
}











