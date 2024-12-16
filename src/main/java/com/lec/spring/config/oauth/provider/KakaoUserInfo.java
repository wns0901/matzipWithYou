package com.lec.spring.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;
    private Map<String, Object> properties;
    private Map<String, Object> kakaoAccount;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getName() {
        if (properties != null) {
            return (String) properties.get("nickname");
        }
        return null;
    }

    @Override
    public String getProfileImageUrl() {
        if (properties != null) {
            return (String) properties.get("profile_image");
        }
        return null;
    }
}