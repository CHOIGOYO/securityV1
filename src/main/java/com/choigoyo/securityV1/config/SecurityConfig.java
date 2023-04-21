package com.choigoyo.securityV1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity /*스프링 시큐리티 필터(SecurityConfig)가 스프링 기본 필터체인에 등록*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * password 암호화 설정 내용
     * */
    @Bean // 해당 메서드의 리턴되는 obj를 ioc로 등록을 해줌
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

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
                .loginPage("/loginForm");
    }
}
