package com.choigoyo.securityV1.config;

import com.choigoyo.securityV1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션과 preauthorize 어노테이션을 활성화
@Configuration
@EnableWebSecurity /*스프링 시큐리티 필터(SecurityConfig)가 스프링 기본 필터체인에 등록*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

/*    *//**
     * password 암호화 설정 내용
     * *//*
    @Bean // 해당 메서드의 리턴되는 obj를 ioc로 등록을 해줌
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    } ------------------------순환참조 에러 삭제  */

    /**
     * page 권한 설정 내용
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf 비활성화
        http.authorizeRequests()
                // 로그인한 회원만 들어갈 수 있음
                .antMatchers("/user/**").authenticated()
                // 매니저이거나 관리자 역할만 access가능
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                // 관리자 역할만 access가능
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                // 위 설정 외 모든권한 허용
                .anyRequest().permitAll()
                /*권한없는 page를 요청했을 때 login페이지 요청*/
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // 주소가 호출이되면 security login을 진행함 = > 컨트롤러에 login이 필요없음
                .usernameParameter("email")
                .defaultSuccessUrl("/") // 로그인 성공하면 index페이지로 또는 인증이필요한 페이지를 반환
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // oauth2 로그인 페이지 설정
                //구글 로그인이 완료된 뒤 후처리가 필요 -> 엑세스토큰 + 사용자의 정보에 접근할 수 있는 권한이 생김
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}
