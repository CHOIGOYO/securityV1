package com.choigoyo.securityV1.controller;

import com.choigoyo.securityV1.entity.User;
import com.choigoyo.securityV1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


/**
 *  컨트롤러는 visw page를 반환 */
@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     *  localhost:8081 */
    @GetMapping("/")
    public String index() {
        //  ser/main/resources/templates/...
        return "index";
    }
    /**
     * 유저*/
    @GetMapping("/user")
    public String user(){
        return "user";
    }

    /**
     * 관리자*/
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
    /**
     * 매니저*/
    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    /**
     * 로그인 폼으로이동 */
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }
    /**
     * 로그인 */
    @PostMapping("/login")
    public String login(){
        return "login";
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

}
