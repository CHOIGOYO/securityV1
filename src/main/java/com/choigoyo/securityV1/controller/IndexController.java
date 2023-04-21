package com.choigoyo.securityV1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller/* 컨트롤러는 visw page를 반환 */
public class IndexController {
    /* localhost:8081 */
    @GetMapping("/")
    public String index() {
        /*
        ser/main/resources/templates/...
        */
        return "index";
    }
    /*유저*/
    @GetMapping("/user")
    public String user(){
        return "user";
    }
    /*관리자*/
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
    /*매니저*/
    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    /*로그인*/
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    /*회원가입*/
    @GetMapping("/join")
    public String join(){
        return "join";
    }
    /*회원가입Proc*/
    @GetMapping("/joinProc")
    public String joinProc(){
        return "회원가입 성공!";
    }
}
