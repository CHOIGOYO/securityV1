package com.choigoyo.securityV1.config.auth;

import com.choigoyo.securityV1.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * security에서 login을 진행시킬 때 완료되면 security가 갖는 session을 만들어준다.(Security ContextHolder 키값에 session정보를 저장)
 * session에 들어갈 수 있는 obj => Authentication 타입의 객체
 * Authentication 안에 User정보가 있어야됨 => user obj 타입은 UserDetails 타입의 객체이어야만 한다.
 *
 * Security session > Authentication > UserDetails(PrincipalDerails) _상속을 함으로서 가능
 * */
@Data
public class PrincipalDerails implements UserDetails,OAuth2User {

    private User user;
    private Map<String,Object> attributes;
    /**
     * 일반로그인 - 생성자*/
    public PrincipalDerails(User user) {
        this.user = user;
    }
    /**
     * OAuth로그인 - 생성자*/
    public PrincipalDerails(User user,Map<String,Object> attributes) {
        this.user = user;
        // attributes 정보를 같이 받는다.
        this.attributes = attributes;
    }

    @Override // OAuth2User
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    @Override // OAuth2User
    public String getName() {
        return null;
    }

    /**
     * 해당 User의 권한을 반환*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    /**
     * 해당 User의 비밀번호를 반환*/
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 해당 User의 name 반환*/
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 계정 만료여부 */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 Locked 여부*/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정이 1년동안 회원이 로그인을 안하면 비활성화 (휴면계정 하기로 했다면 false 설정)
        return true;
    }


}
