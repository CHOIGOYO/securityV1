package com.choigoyo.securityV1.controller;

import com.choigoyo.securityV1.config.auth.PrincipalDerails;
import com.choigoyo.securityV1.entity.User;
import com.choigoyo.securityV1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *  컨트롤러는 visw page를 반환 */
@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/test/login") // 사용자 정보 가져오기 테스트 페이지
    public @ResponseBody String loginTest(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDerails userDetails ){ // Authentication DI
        System.out.println("/test/login");
        System.out.println("======================");
        PrincipalDerails principalDerails = (PrincipalDerails) authentication.getPrincipal();
        System.out.println("principalDerails :"+principalDerails.getUser());
        System.out.println("======================");
        System.out.println("userDetails - UserName :"+userDetails.getUsername());
        return"세션 정보 확인하기";
    }


    /**
     *  localhost:8081 */
    @GetMapping("/")
    public String index() {
        //  ser/main/resources/templates/...
        return "index";
    }

    /**
     * 유저*/
    // OAuth2 로그인과 일반 로그인 모두 PrincipalDerails 를 가져와 컨트롤러를 분리할 필요 없어졌음
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDerails principalDerails){
        System.out.println("principalDerails : "+principalDerails.getUser());
        return "user";
    }

    /**
     * 관리자*/
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }
    /**
     * 매니저*/
    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    /**
     * 로그인 폼으로이동 */
    @GetMapping("/loginForm")
    public  String loginForm(){
        return "loginForm";
    }


    /**
     * 회원가입 폼으로 이동*/
    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }
    /**
     * 회원가입*/
    @PostMapping("/join")
    public String join(User user){
        System.out.println("회원가입 입력 정보 : "+user);
        user.setRole("ROEL_USER"); // 유저의 역할은 기본 USER로 세팅
        /**
         * userRepository.save(user); => 받아온 정보를 DB에 저장은 가능하나 비밀번호가 그대로 저장되어 보안에 취약하고
         * 비밀번호가 암호화 되어있지 않기 때문에 security로 로그인을 할 수 없다.
         * */
        String rawPassword = user.getPassword(); // 암호화 전 비밀번호를 얻어오기
        String encPassword = bCryptPasswordEncoder.encode(rawPassword); // 비밀번호를 암호화
        user.setPassword(encPassword); // 암호화된 비밀번호를 user에 세팅
        userRepository.save(user);
        return "redirect/loginForm";
    }

    @Secured("ROLE_ADMIN") // 회원의 권한이 admin이면 접근가능하도록 설정
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MANAGER')") // data매서드가 실행되기 직전에 실행된다 data 페이지는 매니저이거나 관리자만 접근할 수 있다.
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "data";
    }

}



