package com.artogether.controller.ian;

import com.artogether.common.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("platform")
public class PlatformController {

    @Autowired
    private MemberService memberService;

    @GetMapping("home")
    public String index() {
        //先確認是否有登入

        return "platform/index";
    }


    //平台後臺管理員登入
    @GetMapping("login")
    public String login(){

        return "platform/login";

    }

    //管理員登入驗證
    @PostMapping("login")
    public String loginVerify(String account, String password){

        boolean test = (account.equals("123") && password.equals("123"));


        return test ? "platform/index" : "platform/login";
    }

    //管理員登出
    @GetMapping("logout")
    public String logout(){

        //刪除session

        return "platform/login";
    }


}
