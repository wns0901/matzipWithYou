package com.lec.spring.config.oauth.provider;

import java.util.List;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    String getProfileImageUrl();
}
