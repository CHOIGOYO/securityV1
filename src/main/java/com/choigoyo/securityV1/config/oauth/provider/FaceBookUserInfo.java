package com.choigoyo.securityV1.config.oauth.provider;

import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor // 기본생성자
public class FaceBookUserInfo implements OAuth2UserInfo{
    private Map<String,Object> attributes; // getAttributes()

    public FaceBookUserInfo(Map<String,Object> attributes){ // 생성자
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
